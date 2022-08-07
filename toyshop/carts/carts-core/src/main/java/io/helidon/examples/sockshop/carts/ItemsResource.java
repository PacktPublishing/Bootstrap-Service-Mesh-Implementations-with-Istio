/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Implementation of Items sub-resource REST API.
 */
public class ItemsResource implements ItemsApi {

    private final CartRepository carts;
    private final String cartId;

    ItemsResource(CartRepository carts, String cartId) {
        this.carts = carts;
        this.cartId = cartId;
    }

    @Override
    public List<Item> getItems() {
        return carts.getItems(cartId);
    }

    @Override
    public Response addItem(Item item) {
        if (item.getQuantity() == 0) {
            item.setQuantity(1);
        }
        Item result = carts.addItem(cartId, item);
        return Response
                .status(Status.CREATED)
                .entity(result)
                .build();
    }

    @Override
    public Response getItem(String itemId) {
        Item item = carts.getItem(cartId, itemId);
        return item == null
                ? Response.status(Status.NOT_FOUND).build()
                : Response.ok(item).build();
    }

    @Override
    public Response deleteItem(String itemId) {
        carts.deleteItem(cartId, itemId);
        return Response.accepted().build();
    }

    @Override
    public Response updateItem(Item item) {
        carts.updateItem(cartId, item);
        return Response.accepted().build();
    }
}
