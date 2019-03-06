package com.alex.auth.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type") @SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true) public class ChallengeResponse implements Serializable {
    private String changeId;
    private String response;

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
