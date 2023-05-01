package com.reststar.api.jikan.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reststar.api.jikan.model.Anime;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class JikanSearchResponse {
    private List<Anime> data;
    private Pagination pagination;
}
