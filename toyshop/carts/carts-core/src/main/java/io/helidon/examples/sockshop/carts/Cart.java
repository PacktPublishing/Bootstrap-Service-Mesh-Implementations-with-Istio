/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Shopping cart data model.
 */
@Data
@Entity
@Schema(description = "Customer's shopping cart")
public class Cart implements Serializable {
    /**
     * The ID of the customer this cart belongs to.
     */
    @Id
    @Schema(description = "Customer identifier")
    private String customerId;

    /**
     * The items in the cart.
     */
    @JsonbTransient
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    /**
     * Default constructor.
     */
    public Cart() {
    }

    /**
     * Construct Cart instance.
     *
     * @param customerId the ID of the customer this cart belongs to
     */
    public Cart(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Return the item with the specified ID.
     *
     * @param itemId the item ID
     *
     * @return the item with the specified ID if present, {@code null} otherwise
     */
    public Item getItem(String itemId) {
        return items.stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Set the items list.
     *
     * @param items the list of items
     */
    public void setItems(List<Item> items) {
        items.stream().map(item -> item.setCart(this)).forEach(this.items::add);
    }
    
    /**
     * Add specified item to this cart, or update quantity if present.
     * <p/>
     * If the item with the same ID already exists in this cart, the quantity
     * will be incremented by the quantity specified in the specified item.
     * Otherwise, the item will be added to the cart as-is.
     *
     * @param item the item to add
     *
     * @return the added or updated item
     */
    public Item add(Item item) {
        Item existing = getItem(item.getItemId());
        if (existing != null) {
            return existing.incrementQuantity(item.getQuantity());
        }
        else {
            items.add(item.setCart(this));
            return item;
        }
    }

    /**
     * Replace specified item, or add it if it's not already present in the cart.
     *
     * @param item the item to add to the cart or replace the existing item with
     *
     * @return added or updated item
     */
    public Item update(Item item) {
        Item existing = getItem(item.getItemId());
        if (existing != null) {
            return existing.setQuantity(item.getQuantity());
        }
        else {
            items.add(item.setCart(this));
            return item;
        }
    }

    /**
     * Remove specified item from the cart.
     *
     * @param itemId the item ID
     *
     * @return this cart, to enable fluent API
     */
    public Cart remove(String itemId) {
        Item item = getItem(itemId);
        if (item != null) {
            items.remove(item);
        }
        return this;
    }

    /**
     * Merges another cart into this one, by adding items from it that are not
     * already present, and incrementing quantity for the ones that are present
     * in both carts.
     *
     * @param other the cart to merge into this one
     *
     * @return this cart after the merge
     */
    public Cart merge(Cart other) {
        other.getItems().forEach(item -> add(new Item(item)));
        return this;
    }
}
