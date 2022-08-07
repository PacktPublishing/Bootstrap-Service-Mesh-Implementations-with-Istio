/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * A repository interface that should be implemented by
 * the various async data store integrations.
 */
public interface CartRepositoryAsync {
    /**
     * Get an existing or create a new cart.
     *
     * @param customerId the customer to get or create the cart for
     *
     * @return an existing or a newly created cart for the specified customer
     */
    CompletionStage<Cart> getOrCreateCart(String customerId);

    /**
     * Delete cart associated with the specified customer.
     *
     * @param customerId the customer to delete the cart for
     */
    CompletionStage<Void> deleteCart(String customerId);

    /**
     * Merge the source cart into the target cart, and remove the source cart.
     *
     * @param targetId the target cart/customer ID
     * @param sourceId the source cart/customer ID
     *
     * @return {@code true} if the carts were merged successfully, {@code false} otherwise
     */
    CompletionStage<Boolean> mergeCarts(String targetId, String sourceId);

    /**
     * Return the item with the specified ID from the specified cart, if present.
     *
     * @param cartId the cart to get the item from
     * @param itemId the item ID to get
     *
     * @return the item with the specified ID, or {@code null} if absent
     */
    CompletionStage<Item> getItem(String cartId, String itemId);

    /**
     * Return the list of items from the specified cart.
     *
     * @param cartId the cart ID to get the items from
     *
     * @return the list of items, which could be empty, but never {@code null}
     */
    CompletionStage<List<Item>> getItems(String cartId);

    /**
     * Add specified item to the cart.
     * <p/>
     * If the item with the same ID already exists in the cart, its quantity should be
     * incremented by the quantity of the specified item. Otherwise, the item should be
     * added to the cart as-is.
     *
     * @param cartId the cart to add the item to
     * @param item   the item to add
     *
     * @return the added or updated item
     */
    CompletionStage<Item> addItem(String cartId, Item item);

    /**
     * Update specified item in the cart.
     * <p/>
     * If the item with the same ID already exists in the cart, its quantity should be
     * updated to the quantity of the specified item. Otherwise, the item should be
     * added to the cart as-is.
     *
     * @param cartId the cart to update the item in
     * @param item   the item to update or add
     *
     * @return the added or updated item
     */
    CompletionStage<Item> updateItem(String cartId, Item item);

    /**
     * Remove specified item from the cart.
     *
     * @param cartId the cart to remove the item from
     * @param itemId the item to remove
     */
    CompletionStage<Void> deleteItem(String cartId, String itemId);
}
