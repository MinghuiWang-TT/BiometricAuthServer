package com.alex.auth;

import com.alex.auth.model.User;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class AuthServiceImpl implements AuthService {
    @Override public Response crateUser(User user) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @Override public Response validate(@NotNull String emailAddress, HttpHeaders headers) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @Override public Response getChallenge(@NotNull String emailAddress) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @Override public Response responseToChallenge(@NotNull String emailAddress) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }
}
