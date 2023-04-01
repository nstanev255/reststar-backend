package com.reststar.api.jikan.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class JikanSearchResponse {
    private List<Anime> data;
}
