package com.reststar.model.anime;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResponse {
    private List<Anime> data;
    private int lastPage;
    private int items;
    private int total;
    private int itemsPerPage;
}
