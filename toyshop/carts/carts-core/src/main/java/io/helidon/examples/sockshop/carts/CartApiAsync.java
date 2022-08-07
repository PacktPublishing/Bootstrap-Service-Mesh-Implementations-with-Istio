/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.carts;

import java.util.concurrent.CompletionStage;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Async REST API for {@code /carts} service.
 */
public interface CartApiAsync {
    @GET
    @Path("{customerId}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return customer's shopping cart")
    CompletionStage<Cart> getCart(
            @Parameter(name = "customerId", description = "Customer identifier")
            @PathParam("customerId") String customerId);

    @DELETE
    @Path("{customerId}")
    @Operation(summary = "Delete customer's shopping cart")
    @APIResponses({
        @APIResponse(responseCode = "202", description = "if the shopping cart was successfully deleted"),
        @APIResponse(responseCode = "404", description = "if the shopping cart doesn't exist")
    })
    CompletionStage<Response> deleteCart(
            @Parameter(name = "customerId", description = "Customer identifier")
            @PathParam("customerId") String customerId);

    @GET
    @Path("{customerId}/merge")
    @Operation(summary = "Merge one shopping cart into another",
               description = "Customer can add products to a shopping cart anonymously, "
                       + "but when she logs in the anonymous shopping cart needs to be merged "
                       + "into the customer's own shopping cart")
    @APIResponses({
        @APIResponse(responseCode = "202", description = "if the shopping carts were successfully merged"),
        @APIResponse(responseCode = "404", description = "if the session shopping cart doesn't exist")
    })
    CompletionStage<Response> mergeCarts(
            @Parameter(name = "customerId", description = "Customer identifier")
            @PathParam("customerId") String customerId,
            @Parameter(name = "sessionId", required = true, description = "Anonymous session identifier")
            @QueryParam("sessionId") String sessionId);

    @Path("{customerId}/items")
    @Operation
    ItemsApiAsync getItems(
            @Parameter(name = "customerId", description = "Customer identifier")
            @PathParam("customerId") String customerId);
}
