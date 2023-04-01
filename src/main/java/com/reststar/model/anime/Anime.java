package com.reststar.model.anime;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Anime {
    private Long id;
    private String title;
    private String englishTitle;
    private Type type;
    private Integer episodes;
    private Status status;
    private LocalDate airedForm;
    private LocalDate airedTo;
    private String synopsis;
    private Season season;
    private List<Genre> genres;

}
