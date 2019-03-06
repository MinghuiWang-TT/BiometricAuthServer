package com.alex.auth;

import com.alex.auth.db.DataBase;
import com.alex.auth.model.*;
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

    @Override public Response updatePublicKey(@NotNull String userId, @NotNull String publicKey) {
        User user = dataBase.getUser(userId);
        if (user == null) {
            throw new NotFoundException("No user found by userId:" + userId);
        }
        user.setPublicKey(publicKey);
        dataBase.saveUser(user);
        return Response.ok().entity(user).build();
    }

    @Override public Response createChallenge(@NotNull String userId) {
        User user = dataBase.getUser(userId);
        if (user == null) {
            throw new NotFoundException("No user found by userId:" + userId);
        }
        Challenge challenge = new Challenge();
        challenge.setUserId(userId);
        challenge.setNonce(new SecureRandom().nextLong());
        challenge.setChallenge("UUID.randomUUID().toString()");
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

        try {
            String decryptString = decrypt(response.getPayload(), user.getPublicKey());
            boolean verified = decryptString
                .equals(Long.toString(challenge.getNonce()) + challenge.getNonce() + SALT);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse
                .setStatus(verified ? AuthStatus.SUCCESS.name() : AuthStatus.FAIL.name());
            dataBase.deleteChallenge(response.getChallengeId());
            return Response.ok().entity(authenticationResponse).build();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static String decrypt(String payLoad, String pubKey)
        throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException,
        BadPaddingException, IllegalBlockSizeException, NoSuchProviderException,
        NoSuchPaddingException, UnsupportedEncodingException {
        Cipher asymmetricCipher = Cipher.getInstance("RSA/NONE/NoPadding", "BC");
        X509EncodedKeySpec publicKeySpec =
            new X509EncodedKeySpec(Base64.getDecoder().decode(pubKey));
        KeyFactory keyFactory;
        keyFactory = KeyFactory.getInstance(publicKeySpec.getFormat());
        Key key = keyFactory.generatePublic(publicKeySpec);
        asymmetricCipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainBytes = asymmetricCipher.doFinal(Base64.getDecoder().decode(payLoad));
        return new String(plainBytes, "UTF-8");
    }
}
