package com.reststar.api.jikan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Genre {
    @JsonProperty("mal_id")
    private Long malId;
    private String type;
    private String name;
    private String url;
}
