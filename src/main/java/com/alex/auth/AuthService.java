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
    @Path("/user/{userId}") Response getUser(@NotNull @PathParam("userId") String userId);

    @POST @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON) @Path("/user")
    Response crateUser(User user);

    @PUT @Path("/user/{userId}/key/{publicKey}") @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) Response updatePublicKey(
        @NotNull @PathParam("userId") String userId,
        @NotNull @PathParam("publicKey") String publicKey);

    @POST @Path("/challenge/{userId}") @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) Response createChallenge(
        @NotNull @PathParam("userId") String userId);

    @PUT @Path("/challenge/{userId}") @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) Response responseToChallenge(
        @NotNull @PathParam("userId") String userId, @NotNull ChallengeResponse challengeId);
}
