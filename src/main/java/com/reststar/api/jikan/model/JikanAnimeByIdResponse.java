package com.reststar.api.jikan.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JikanAnimeByIdResponse {
    private Anime data;
}
