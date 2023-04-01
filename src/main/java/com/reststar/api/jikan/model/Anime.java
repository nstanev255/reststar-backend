package com.reststar.api.jikan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Anime {
    @JsonProperty("mal_id")
    private Long malId;
    private String url;
    private Images images;
    private boolean approved;
    private List<Title> titles;
    private Type type;
    private String source;
    private Integer episodes;
    private Status status;
    private boolean airing;
    private Aired aired;
    private String duration;
    private String rating;
    private Double score;
    @JsonProperty("scored_by")
    private Integer scoredBy;
    private Integer rank;
    private Integer popularity;
    private Integer members;
    private Integer favorites;
    private String synopsis;
    private String background;
    private Season season;
    private Integer year;
    private List<Genre> genres;
    @JsonProperty("explicit_genres")
    private List<Genre> explicitGenres;
}
