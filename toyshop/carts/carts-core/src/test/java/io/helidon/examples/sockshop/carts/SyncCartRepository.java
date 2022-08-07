/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts;

import java.util.List;

/**
 * Helper class that blocks on async repository calls, allowing
 * us to reuse the tests for both sync and async repository implementations.
 */
public class SyncCartRepository implements CartRepository {
    private CartRepositoryAsync carts;

    public SyncCartRepository(CartRepositoryAsync carts) {
        this.carts = carts;
    }

    @Override
    public Cart getOrCreateCart(String customerId) {
        return carts.getOrCreateCart(customerId).toCompletableFuture().join();
    }

    @Override
    public void deleteCart(String customerId) {
        carts.deleteCart(customerId).toCompletableFuture().join();
    }

    @Override
    public boolean mergeCarts(String targetId, String sourceId) {
        return carts.mergeCarts(targetId, sourceId).toCompletableFuture().join();
    }

    @Override
    public Item getItem(String cartId, String itemId) {
        return carts.getItem(cartId, itemId).toCompletableFuture().join();
    }

    @Override
    public List<Item> getItems(String cartId) {
        return carts.getItems(cartId).toCompletableFuture().join();
    }

    @Override
    public Item addItem(String cartId, Item item) {
        return carts.addItem(cartId, item).toCompletableFuture().join();
    }

    @Override
    public Item updateItem(String cartId, Item item) {
        return carts.updateItem(cartId, item).toCompletableFuture().join();
    }

    @Override
    public void deleteItem(String cartId, String itemId) {
        carts.deleteItem(cartId, itemId).toCompletableFuture().join();
    }
}
