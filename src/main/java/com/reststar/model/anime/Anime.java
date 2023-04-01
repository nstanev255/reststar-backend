package com.reststar.model.anime;

import com.reststar.api.jikan.model.Title;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
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
    private Instant airedForm;
    private Instant airedTo;
    private String synopsis;
    private Season season;
    private List<Genre> genres;

}
