package com.reststar.api.jikan.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Season {
    SUMMER("summer"),
    WINTER("winter"),
    SPRING("spring"),
    FALL("fall");

    private String season;
    Season(String season) {
        this.season = season;
    }

    @JsonValue
    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
