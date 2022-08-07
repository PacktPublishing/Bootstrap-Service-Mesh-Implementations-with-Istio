/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts;

import javax.enterprise.inject.spi.CDI;

import io.helidon.microprofile.server.Server;

import io.restassured.RestAssured;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for {@link io.helidon.examples.sockshop.carts.CartResource}.
 */
public class CartResourceIT {
    protected static Server SERVER;

    private CartRepository carts;

    /**
     * This will start the application on ephemeral port to avoid port conflicts.
     * We can discover the actual port by calling {@link io.helidon.microprofile.server.Server#port()} method afterwards.
     */
    @BeforeAll
    static void startServer() {
        // disable global tracing so we can start server in multiple test suites
        System.setProperty("tracing.global", "false");
        SERVER = Server.builder().port(0).build().start();
    }

    /**
     * Stop the server, as we cannot have multiple servers started at the same time.
     */
    @AfterAll
    static void stopServer() {
        SERVER.stop();
    }

    @BeforeEach
    void setup() {
        // Configure RestAssured to run tests against our application
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = SERVER.port();
        RestAssured.basePath = getBasePath();

        carts = getCartsRepository();
        carts.deleteCart("C1");
        carts.deleteCart("C2");
    }

    protected String getBasePath() {
        return "/carts";
    }

    protected CartRepository getCartsRepository() {
        return CDI.current().select(CartRepository.class).get();
    }

    @Test
    void testGetCart() {
        when().
                get("/{cartId}", "C1").
        then().
                statusCode(200).
                body("customerId", equalTo("C1"),
                     "items", nullValue());
    }

    @Test
    void testDeleteCart() {
        when().
                delete("/{cartId}", "C1").
        then().
                statusCode(ACCEPTED.getStatusCode());
    }

    @Test
    void testMergeNonExistentCarts() {
        given().
                queryParam("sessionId", "FOO").
        when().
                get("/{cartId}/merge", "C1").
        then().
                statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    void testMergeCarts() {
        // let's add some data to the repo
        carts.addItem("C1", new Item("X1", 5, 10f));
        carts.addItem("C2", new Item("X1", 5, 10f));
        carts.addItem("C2", new Item("X2", 5, 10f));

        // it should succeed the first time
        given().
                queryParam("sessionId", "C2").
        when().
                get("/{cartId}/merge", "C1").
        then().
                statusCode(ACCEPTED.getStatusCode());

        assertThat(carts.getItems("C1").size(), is(2));
        assertThat(carts.getItem("C1", "X1").getQuantity(), is(10));
        
        // now that it has been merged, it should fail
        given().
                queryParam("sessionId", "C2").
        when().
                get("/{cartId}/merge", "C1").
        then().
                statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    void testGetItems() {
        // should be empty to start
        when().
                get("/{cartId}/items", "C1").
        then().
                statusCode(OK.getStatusCode()).
                body("$", empty());

        // let's add some data to the repo
        carts.addItem("C1", new Item("X1", 5, 10f));
        carts.addItem("C1", new Item("X2", 5, 10f));

        // now it should return two items
        when().
                get("/{cartId}/items", "C1").
        then().
                statusCode(OK.getStatusCode()).
                body("itemId", hasItems("X1", "X2"));
    }

    @Test
    void testAddItem() {
        given().
                contentType(JSON).
                body(new Item("X1", 0, 10f)).
        when().
                post("/{cartId}/items", "C1").
        then().
                statusCode(CREATED.getStatusCode()).
                body("itemId", is("X1"),
                     "quantity", is(1),
                     "unitPrice", is(10f));

        // if we do it again the quantity should increase
        given().
                contentType(JSON).
                body(new Item("X1", 3, 10f)).
        when().
                post("/{cartId}/items", "C1").
        then().
                statusCode(CREATED.getStatusCode()).
                body("itemId", is("X1"),
                     "quantity", is(4),
                     "unitPrice", is(10f));
    }

    @Test
    void testUpdateItem() {
        given().
                contentType(JSON).
                body(new Item("X1", 5, 10f)).
        when().
                patch("/{cartId}/items", "C1").
        then().
                statusCode(ACCEPTED.getStatusCode());

        assertThat(carts.getItem("C1", "X1").getQuantity(), is(5));

        // if we do it again the quantity should be overwritten
        given().
                contentType(JSON).
                body(new Item("X1", 3, 10f)).
        when().
                patch("/{cartId}/items", "C1").
        then().
                statusCode(ACCEPTED.getStatusCode());

        assertThat(carts.getItem("C1", "X1").getQuantity(), is(3));
    }

    @Test
    void testGetItem() {
        // let's add some data to the repo
        carts.addItem("C1", new Item("X1", 5, 10f));
        carts.addItem("C1", new Item("X2", 3, 17f));

        when().
                get("/{cartId}/items/{itemId}", "C1", "X1").
        then().
                statusCode(OK.getStatusCode()).
                body("itemId", is("X1"),
                     "quantity", is(5),
                     "unitPrice", is(10f));
    }

    @Test
    void testDeleteItem() {
        // let's add some data to the repo
        carts.addItem("C1", new Item("X1", 5, 10f));
        carts.addItem("C1", new Item("X2", 3, 17f));

        when().
                delete("/{cartId}/items/{itemId}", "C1", "X1").
        then().
                statusCode(ACCEPTED.getStatusCode());

        assertThat(carts.getItems("C1").size(), is(1));
        assertThat(carts.getItem("C1", "X1"), nullValue());
    }
}
