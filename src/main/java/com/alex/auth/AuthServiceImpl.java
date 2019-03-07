package com.alex.auth;

import com.alex.auth.db.DataBase;
import com.alex.auth.model.*;
import com.google.common.base.Charsets;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

public class AuthServiceImpl implements AuthService {
    private static final String SALT = "GouDan";
    @Autowired DataBase dataBase;

    @Override public Response getUser(@NotNull String userName) {
        User user = dataBase.getUser(userName);
        if (user == null) {
            throw new NotFoundException("No user found by user name:" + userName);
        }

        return Response.ok().entity(user).build();
    }

    @Override public Response createUser(User user) {
        dataBase.saveUser(user);
        return Response.ok().entity(user).build();
    }

    @Override public Response createChallenge(@NotNull String userName) {
        User user = dataBase.getUser(userName);
        if (user == null) {
            throw new NotFoundException("No user found by userName:" + userName);
        }
        Challenge challenge = new Challenge();
        challenge.setUserName(userName);
        challenge.setNonce(new SecureRandom().nextLong());
        challenge.setChallenge(UUID.randomUUID().toString());
        dataBase.createChallenge(challenge);
        return Response.created(URI.create("/challenge/" + userName + "/" + challenge.getId()))
            .entity(challenge).build();
    }

    @Override public Response responseToChallenge(@NotNull String userName,
        @NotNull ChallengeResponse response) {
        Challenge challenge = dataBase.getChallenge(response.getChallengeId());
        if (!userName.equals(challenge.getUserName())) {
            throw new IllegalArgumentException(
                "Challenge:" + challenge.getId() + " is not for user:" + userName);
        }

        User user = dataBase.getUser(userName);

        try {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            if (verifySignature(response.getPayload(), user.getPublicKey(),
                challenge.getNonce().toString() + challenge.getChallenge() + SALT)) {
                authenticationResponse.setStatus(AuthStatus.SUCCESS.name());
                authenticationResponse.setSecret(user.getSecret());
            } else {
                authenticationResponse.setSecret("");
                authenticationResponse.setStatus(AuthStatus.FAIL.name());
            }
            dataBase.deleteChallenge(response.getChallengeId());
            return Response.ok().entity(authenticationResponse).build();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static boolean verifySignature(String encodedSignature, String encodedPublicKey,
        String rawString)
        throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException,
        SignatureException {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = factory
            .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(encodedPublicKey)));
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(rawString.getBytes(Charsets.UTF_8));
        return signature.verify(Base64.getDecoder().decode(encodedSignature));
    }
}
