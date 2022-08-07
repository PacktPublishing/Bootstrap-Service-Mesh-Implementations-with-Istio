/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts.mongo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import io.helidon.examples.sockshop.carts.Cart;
import io.helidon.examples.sockshop.carts.CartRepositoryAsync;
import io.helidon.examples.sockshop.carts.Item;

import com.mongodb.async.client.MongoCollection;

import org.eclipse.microprofile.opentracing.Traced;

import static com.mongodb.client.model.Filters.eq;
import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 * An implementation of {@link io.helidon.examples.sockshop.carts.CartRepositoryAsync}
 * that that uses MongoDB as a backend data store.
 */
@SuppressWarnings("deprecation")
@ApplicationScoped
@Alternative
@Priority(APPLICATION)
@Traced
public class MongoCartRepositoryAsync implements CartRepositoryAsync {

    private MongoCollection<Cart> carts;

    @Inject
    MongoCartRepositoryAsync(MongoCollection<Cart> carts) {
        this.carts = carts;
    }

    @Override
    public CompletionStage<Cart> getOrCreateCart(String customerId) {
        CompletableFuture<Cart> future = new CompletableFuture<>();

        carts.find(eq("customerId", customerId))
                .first((cart, t1) -> {
                    if (t1 != null) future.completeExceptionally(t1);
                    else if (cart == null) {
                        cart = new Cart(customerId);
                        final Cart aCart = cart;
                        carts.insertOne(cart, (aVoid, t2) -> {
                            if (t2 != null) future.completeExceptionally(t2);
                            else future.complete(aCart);
                        });
                    }
                    else future.complete(cart);
                });

        return future;
    }

    @Override
    public CompletionStage<Void> deleteCart(String customerId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        carts.deleteOne(eq("customerId", customerId), (deleteResult, throwable) -> {
            if (throwable != null) future.completeExceptionally(throwable);
            else future.complete(null);
        });

        return future;
    }

    @Override
    public CompletionStage<Boolean> mergeCarts(String targetId, String sourceId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        carts.findOneAndDelete(eq("customerId", sourceId), (source, t1) -> {
            if (t1 != null) future.completeExceptionally(t1);
            else if (source != null) {
                getOrCreateCart(targetId).whenComplete((target, t2) -> {
                    if (t2 != null) future.completeExceptionally(t2);
                    else {
                        target.merge(source);
                        carts.replaceOne(eq("customerId", targetId), target, (updateResult, t3) -> {
                            if (t3 != null) future.completeExceptionally(t3);
                            else future.complete(true);
                        });
                    }
                });
            }
            else future.complete(false);
        });

        return future;
    }

    @Override
    public CompletionStage<List<Item>> getItems(String cartId) {
        return getOrCreateCart(cartId).thenApply(Cart::getItems);
    }

    @Override
    public CompletionStage<Item> getItem(String cartId, String itemId) {
        return getOrCreateCart(cartId).thenApply(cart -> cart.getItem(itemId));
    }

    @Override
    public CompletionStage<Item> addItem(String cartId, Item item) {
        CompletableFuture<Item> future = new CompletableFuture<>();

        getOrCreateCart(cartId).whenComplete((cart, t1) -> {
            if (t1 != null) future.completeExceptionally(t1);
            else {
                Item result = cart.add(item);
                carts.replaceOne(eq("customerId", cartId), cart, (updateResult, t2) -> {
                    if (t2 != null) future.completeExceptionally(t2);
                    else future.complete(result);
                });
            }
        });

        return future;
    }

    @Override
    public CompletionStage<Item> updateItem(String cartId, Item item) {
        CompletableFuture<Item> future = new CompletableFuture<>();

        getOrCreateCart(cartId).whenComplete((cart, t1) -> {
            if (t1 != null) future.completeExceptionally(t1);
            else {
                Item result = cart.update(item);
                carts.replaceOne(eq("customerId", cartId), cart, (updateResult, t2) -> {
                    if (t2 != null) future.completeExceptionally(t2);
                    else future.complete(result);
                });
            }
        });

        return future;
    }

    @Override
    public CompletionStage<Void> deleteItem(String cartId, String itemId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        getOrCreateCart(cartId).whenComplete((cart, t1) -> {
            if (t1 != null) future.completeExceptionally(t1);
            else {
                cart.remove(itemId);
                carts.replaceOne(eq("customerId", cartId), cart, (updateResult, t2) -> {
                    if (t2 != null) future.completeExceptionally(t2);
                    else future.complete(null);
                });
            }
        });

        return future;
    }
}
