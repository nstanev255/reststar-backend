package com.reststar.api.jikan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Urls {
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("small_image_url")
    private String smallImageUrl;

    @JsonProperty("large_image_url")
    private String largeImageUrl;
}
