package com.reststar.api.jikan.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaginationItems {
    private int count;
    private int total;
    @JsonProperty("per_page")
    private int perPage;
}
