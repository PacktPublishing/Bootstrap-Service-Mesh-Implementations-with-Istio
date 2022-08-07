/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import org.eclipse.microprofile.opentracing.Traced;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 * Simple in-memory implementation of {@link io.helidon.examples.sockshop.carts.CartRepository}
 * that can be used for demos and testing.
 * <p/>
 * This implementation is obviously not suitable for production use, because it is not
 * persistent and it doesn't scale, but it is trivial to write and very useful for the
 * API testing and quick demos.
 */
@ApplicationScoped
@Traced
@Priority(APPLICATION - 10)
public class DefaultCartRepository implements CartRepository {
    protected Map<String, Cart> carts;

    /**
     * Construct {@code DefaultCartRepository} with empty storage map.
     */
    public DefaultCartRepository() {
        this(new ConcurrentHashMap<>());
    }

    /**
     * Construct {@code DefaultCartRepository} with the specified storage map.
     *
     * @param carts the storage map to use
     */
    protected DefaultCartRepository(Map<String, Cart> carts) {
        this.carts = carts;
    }
    
    @Override
    public Cart getOrCreateCart(String customerId) {
        return carts.computeIfAbsent(customerId, Cart::new);
    }

    @Override
    public void deleteCart(String customerId) {
        carts.remove(customerId);
    }

    @Override
    public boolean mergeCarts(String targetId, String sourceId) {
        Cart source = carts.remove(sourceId);
        if (source == null) {
            return false;
        }

        Cart target = getOrCreateCart(targetId);
        saveCart(target.merge(source));

        return true;
    }

    @Override
    public Item getItem(String cartId, String itemId) {
        return getOrCreateCart(cartId).getItem(itemId);
    }

    @Override
    public List<Item> getItems(String cartId) {
        return getOrCreateCart(cartId).getItems();
    }

    @Override
    public Item addItem(String cartId, Item item) {
        Cart cart = getOrCreateCart(cartId);
        Item result = cart.add(item);
        saveCart(cart);
        return result;
    }

    @Override
    public Item updateItem(String cartId, Item item) {
        Cart cart = getOrCreateCart(cartId);
        Item result = cart.update(item);
        saveCart(cart);
        return result;
    }

    @Override
    public void deleteItem(String cartId, String itemId) {
        Cart cart = getOrCreateCart(cartId);
        cart.remove(itemId);
        saveCart(cart);
    }

    // ---- helpers ---------------------------------------------------------

    private void saveCart(Cart cart) {
        carts.put(cart.getCustomerId(), cart);
    }
}
