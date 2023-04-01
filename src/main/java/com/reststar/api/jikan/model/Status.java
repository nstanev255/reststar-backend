package com.reststar.api.jikan.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    FINISHED_AIRING("Finished Airing"),
    CURRENTLY_AIRING("Currently Airing"),
    NOT_AIRED("Not yet aired");

    private String status;

    Status(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
