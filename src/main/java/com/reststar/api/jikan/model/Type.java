package com.reststar.api.jikan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Type {
    TV("TV"),
    OVA("OVA"),
    MOVIE("Movie"),
    SPECIAL("Special"),
    ONA("ONA"),
    MUSIC("Music"),
    HELLOWROLD("asd");

    private String type;

    private Type(String type) {
        this.type = type;
    }
    @JsonValue
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
