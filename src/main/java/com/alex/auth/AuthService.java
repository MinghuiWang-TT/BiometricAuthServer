package com.alex.auth;

import com.alex.auth.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.WILDCARD) @Produces(MediaType.APPLICATION_JSON) @Tag(name = "auth")
@Path("/auth") public interface AuthService {

    @POST @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    Response crateUser(User user);

    @PUT @Path("/validate/{email}") @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) Response validate(
        @NotNull @PathParam("email") String emailAddress, @Context HttpHeaders headers);

    @GET @Path("/challenge/{email}") @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) Response getChallenge(
        @NotNull @PathParam("email") String emailAddress);

    @PUT @Path("/challenge/{email}") @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) Response responseToChallenge(
        @NotNull @PathParam("email") String emailAddress);
}
