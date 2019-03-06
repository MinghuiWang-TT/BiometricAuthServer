package com.alex.auth.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type") @SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true) public class ChallengeResponse implements Serializable {
    private String challengeId;
    private String payload;

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
