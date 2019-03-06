package com.alex.auth;

import com.alex.auth.db.DataBase;
import com.alex.auth.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Base64;
import java.util.Date;

public class AuthServiceImpl implements AuthService {
    @Autowired DataBase dataBase;


    @Override public Response getUser(@NotNull String userId) {
        User user = dataBase.getUser(userId);
        if (user == null) {
            throw new NotFoundException("No user found by userId:" + userId);
        }

        return Response.ok().entity(user).build();
    }

    @Override public Response crateUser(User user) {
        dataBase.createUser(user);
        return Response.ok().entity(user).build();
    }

    @Override public Response updatePublicKey(@NotNull String userId, @NotNull String publicKey) {
        //TODO:Authentication needed
        User user = dataBase.getUser(userId);
        if (user == null) {
            throw new NotFoundException("No user found by userId:" + userId);
        }
        user.setPublicKey(publicKey);
        dataBase.updateUser(user);
        return Response.ok().entity(user).build();
    }

    @Override public Response createChallenge(@NotNull String userId) {
        User user = dataBase.getUser(userId);
        if (user == null) {
            throw new NotFoundException("No user found by userId:" + userId);
        }
        Challenge challenge = new Challenge();
        challenge.setUserId(userId);
        challenge.setNonce(generateNonce());
        challenge.setPayload("Alex");
        dataBase.createChallenge(challenge);

        return Response.created(URI.create("/challenge/" + userId + "/" + challenge.getId()))
            .entity(challenge).build();
    }

    @Override public Response responseToChallenge(@NotNull String userId,
        @NotNull ChallengeResponse response) {
        Challenge challenge = dataBase.getChallenge(response.getChallengeId());
        if (!userId.equals(challenge.getUserId())) {
            throw new IllegalArgumentException(
                "Challenge:" + challenge.getId() + " is not for user:" + userId);
        }

        User user = dataBase.getUser(userId);

        boolean verified = decrypt(response.getPayload(), user.getPublicKey());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse
            .setStatus(verified ? AuthStatus.SUCCESS.name() : AuthStatus.FAIL.name());
        dataBase.deleteChallenge(response.getChallengeId());
        return Response.ok().entity(authenticationResponse).build();
    }

    private static String generateNonce() {
        String dateTimeString = Long.toString(new Date().getTime());
        byte[] nonceByte = dateTimeString.getBytes();
        return Base64.getEncoder().encodeToString(nonceByte);
    }

    private static boolean decrypt(String payLoad, String pubKey) {
        return true;
    }
}
