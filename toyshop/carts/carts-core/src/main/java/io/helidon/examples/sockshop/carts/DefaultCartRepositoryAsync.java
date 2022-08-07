/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.opentracing.Traced;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 * Simple in-memory implementation of {@link CartRepositoryAsync}
 * that can be used for demos and testing.
 * <p/>
 * This implementation is obviously not suitable for production use, because it is not
 * persistent and it doesn't scale, but it is trivial to write and very useful for the
 * API testing and quick demos.
 */
@ApplicationScoped
@Traced
@Priority(APPLICATION - 10)
public class DefaultCartRepositoryAsync implements CartRepositoryAsync {
    private CartRepository delegate = new DefaultCartRepository();

    @Override
    public CompletionStage<Cart> getOrCreateCart(String customerId) {
        return CompletableFuture.completedFuture(delegate.getOrCreateCart(customerId));
    }

    @Override
    public CompletionStage<Void> deleteCart(String customerId) {
        return CompletableFuture.runAsync(() -> delegate.deleteCart(customerId));
    }

    @Override
    public CompletionStage<Boolean> mergeCarts(String targetId, String sourceId) {
        return CompletableFuture.completedFuture(delegate.mergeCarts(targetId, sourceId));
    }

    @Override
    public CompletionStage<Item> getItem(String cartId, String itemId) {
        return CompletableFuture.completedFuture(delegate.getItem(cartId, itemId));
    }

    @Override
    public CompletionStage<List<Item>> getItems(String cartId) {
        return CompletableFuture.completedFuture(delegate.getItems(cartId));
    }

    @Override
    public CompletionStage<Item> addItem(String cartId, Item item) {
        return CompletableFuture.completedFuture(delegate.addItem(cartId, item));
    }

    @Override
    public CompletionStage<Item> updateItem(String cartId, Item item) {
        return CompletableFuture.completedFuture(delegate.updateItem(cartId, item));
    }

    @Override
    public CompletionStage<Void> deleteItem(String cartId, String itemId) {
        return CompletableFuture.runAsync(() -> delegate.deleteItem(cartId, itemId));
    }
}
