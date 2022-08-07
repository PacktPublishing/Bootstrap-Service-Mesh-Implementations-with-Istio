/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts;

import java.util.List;
import java.util.concurrent.CompletionStage;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Implementation of Items sub-resource REST API.
 */
public class ItemsResourceAsync implements ItemsApiAsync {

    private final CartRepositoryAsync carts;
    private final String cartId;

    ItemsResourceAsync(CartRepositoryAsync carts, String cartId) {
        this.carts = carts;
        this.cartId = cartId;
    }

    @Override
    public CompletionStage<List<Item>> getItems() {
        return carts.getItems(cartId);
    }

    @Override
    public CompletionStage<Response> addItem(Item item) {
        if (item.getQuantity() == 0) {
            item.setQuantity(1);
        }

        return carts.addItem(cartId, item)
                    .thenApply(result -> Response.status(Status.CREATED).entity(result).build());
    }

    @Override
    public CompletionStage<Response> getItem(String itemId) {
        return carts.getItem(cartId, itemId)
                    .thenApply(item ->
                            item == null
                            ? Response.status(Status.NOT_FOUND).build()
                            : Response.ok(item).build());
    }

    @Override
    public CompletionStage<Response> deleteItem(String itemId) {
        return carts.deleteItem(cartId, itemId)
                .thenApply(ignore -> Response.accepted().build());
    }

    @Override
    public CompletionStage<Response> updateItem(Item item) {
        return carts.updateItem(cartId, item)
                .thenApply(ignore -> Response.accepted().build());
    }
}
