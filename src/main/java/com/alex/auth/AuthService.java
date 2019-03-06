package com.alex.auth;

import com.alex.auth.model.ChallengeResponse;
import com.alex.auth.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.WILDCARD) @Produces(MediaType.APPLICATION_JSON) @Tag(name = "auth")
@Path("/auth") public interface AuthService {

    @GET @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @Path("/user/{userName}") Response getUser(@NotNull @PathParam("userName") String userName);

    @POST @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON) @Path("/user")
    Response createUser(User user);

    @PUT @Path("/user/{userName}/key/{publicKey}") @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) Response updatePublicKey(
        @NotNull @PathParam("userName") String userName,
        @NotNull @PathParam("publicKey") String publicKey);

    @POST @Path("/challenge/{userName}") @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) Response createChallenge(
        @NotNull @PathParam("userName") String userName);

    @PUT @Path("/challenge/{userName}") @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) Response responseToChallenge(
        @NotNull @PathParam("userName") String userName, @NotNull ChallengeResponse challengeId);
}
