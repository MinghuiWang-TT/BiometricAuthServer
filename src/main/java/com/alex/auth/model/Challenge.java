package com.alex.auth.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type") @SuppressWarnings("serial")
public class Challenge implements Serializable {

    private String id;
    private String userId;
    private String challenge;
    private Long nonce;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }
}
