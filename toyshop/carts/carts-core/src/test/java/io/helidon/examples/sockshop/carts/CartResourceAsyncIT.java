/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts;

import javax.enterprise.inject.spi.CDI;

/**
 * Integration tests for {@link io.helidon.examples.sockshop.carts.CartResourceAsync}.
 */
public class CartResourceAsyncIT extends CartResourceIT {
    protected String getBasePath() {
        return "/carts-async";
    }

    protected CartRepository getCartsRepository() {
        return new SyncCartRepository(CDI.current().select(CartRepositoryAsync.class).get());
    }
}
