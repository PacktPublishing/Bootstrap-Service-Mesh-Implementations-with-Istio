/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Abstract base class containing tests for all
 * {@link io.helidon.examples.sockshop.carts.CartRepository} implementations.
 */
public abstract class CartRepositoryTest {

    private CartRepository carts = getCartRepository();

    protected abstract CartRepository getCartRepository();

    @BeforeEach
    void setup() {
        carts.deleteCart("C1");
        carts.deleteCart("C2");
    }

    @Test
    void testCartCreation() {
        // explicit
        Cart c1 = carts.getOrCreateCart("C1");
        assertThat(c1.getCustomerId(), is("C1"));
        assertThat(carts.getOrCreateCart("C1"), is(c1));

        // implicit, when the item is added
        Item x2 = new Item("X2", 3, 5.99f);
        carts.addItem("C2", x2);
        assertThat(carts.getItem("C2", "X2"), is(x2));
    }

    @Test
    void testCartDeletion() {
        carts.addItem("C1", new Item("X1", 5, 10f));
        carts.addItem("C2", new Item("X2", 3, 5.99f));

        carts.deleteCart("C2");

        // it should fail because the cart C2 has been removed from the repo
        assertThat(carts.mergeCarts("C1", "C2"), is(false));
    }

    @Test
    void testCartMerge() {
        carts.addItem("C1", new Item("X1", 5, 10f));
        carts.addItem("C1", new Item("X2", 3, 5.99f));
        carts.addItem("C2", new Item("X2", 3, 5.99f));
        carts.addItem("C2", new Item("X3", 1, 20f));

        // it should succeed on first attempt
        assertThat(carts.mergeCarts("C1", "C2"), is(true));

        // if we try again it shouldn't succeed, as the source cart should've been deleted
        // from the repo
        assertThat(carts.mergeCarts("C1", "C2"), is(false));

        // and it should fail with source that never existed
        assertThat(carts.mergeCarts("C1", "FOO"), is(false));

        Cart cart = carts.getOrCreateCart("C1");
        assertThat(cart.getItems().size(), is(3));
        assertThat(cart.getItem("X2").getQuantity(), is(6));
    }

    @Test
    void testItemAccess() {
        carts.addItem("C1", new Item("X1", 5, 10f));
        carts.addItem("C1", new Item("X2", 3, 5.99f));

        assertThat(carts.getItems("C1").size(), is(2));
        assertThat(carts.getItem("C1", "X1").getQuantity(), is(5));
        assertThat(carts.getItem("C1", "X2").getQuantity(), is(3));
        assertThat(carts.getItem("C1", "X2").getUnitPrice(), is(5.99f));

        assertThat(carts.addItem("C1", new Item("X2", 4, 5.99f)).getQuantity(), is(7));
        assertThat(carts.updateItem("C1", new Item("X1", 3, 10f)).getQuantity(), is(3));
        
        carts.deleteItem("C1", "X2");
        assertThat(carts.getItems("C1").size(), is(1));
    }
}
