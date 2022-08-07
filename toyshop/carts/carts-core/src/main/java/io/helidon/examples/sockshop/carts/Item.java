/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Representation of a single item in a shopping cart.
 */
@Data
@Entity
@IdClass(ItemId.class)
@Schema(description = "Shopping cart item")
public class Item implements Serializable {
    /**
     * The item identifier.
     */
    @Id
    @Schema(description = "The item identifier")
    private String itemId;

    /**
     * The item quantity.
     */
    @Schema(description = "The item quantity")
    private int quantity;

    /**
     * The item's price per unit.
     */
    @Schema(description = "The item's price per unit")
    private float unitPrice;

    /**
     * The cart this item belongs to, purely for JPA optimization.
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonbTransient
    private Cart cart;

    /**
     * Default constructor.
     */
    public Item() {
    }

    /**
     * Construct an Item instance.
     *
     * @param itemId    the item identifier
     * @param quantity  the item quantity
     * @param unitPrice the item's price per unit
     */
    public Item(String itemId, int quantity, float unitPrice) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    /**
     * Copy constructor.
     * <p/>
     * This constructor intentionally does not copy {@code carts} field,
     * in order to detach the entity from the persistence context when
     * merging carts.
     * 
     * @param item the item to initialize this item from
     */
    @SuppressWarnings("CopyConstructorMissesField")
    public Item(Item item) {
        this (item.itemId, item.quantity, item.unitPrice);
    }

    /**
     * Return the cart this item belongs to.
     *
     * @return the cart this item belongs to
     */
    Cart getCart() {
        return cart;
    }

    /**
     * Set the cart this item belongs to.
     *
     * @param cart the cart to set
     *
     * @return this item
     */
    Item setCart(Cart cart) {
        this.cart = cart;
        return this;
    }

    /**
     * Set the item quantity.
     *
     * @param quantity the new quantity
     *
     * @return this item
     */
    public Item setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * Increment quantity in the cart by the specified number of items.
     *
     * @param count the number of items to increase quantity by
     *
     * @return this item
     */
    public Item incrementQuantity(int count) {
        this.quantity += count;
        return this;
    }
}
