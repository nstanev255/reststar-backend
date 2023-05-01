package com.reststar.api.jikan.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Pagination {
    @JsonProperty("last_visible_page")
    private int lastVisiblePage;
    @JsonProperty("has_next_page")
    private boolean hasNextPage;
    @JsonProperty("current_page")
    private int currentPage;
    private PaginationItems items;
}
