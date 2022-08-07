/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts;

/**
 * Tests for default in memory repository implementation.
 */
class DefaultCartRepositoryTest extends CartRepositoryTest {
    @Override
    protected CartRepository getCartRepository() {
        return new DefaultCartRepository();
    }
}
