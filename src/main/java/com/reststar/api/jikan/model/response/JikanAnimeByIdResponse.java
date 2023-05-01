package com.reststar.api.jikan.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reststar.api.jikan.model.Anime;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JikanAnimeByIdResponse {
    private Anime data;
}
