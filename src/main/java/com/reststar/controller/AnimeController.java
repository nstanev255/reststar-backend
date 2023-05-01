package com.reststar.controller;

import com.reststar.constants.Constants;
import com.reststar.model.anime.Anime;
import com.reststar.model.anime.PaginationResponse;
import com.reststar.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("anime")
public class AnimeController {
    @Autowired
    private AnimeService animeService;

    @GetMapping("{id}")
    public Anime getAnimeById(@PathVariable("id") String id) {
        return animeService.findAnimeById(Long.valueOf(id));
    }

    @GetMapping("/search")
    public PaginationResponse search(@RequestParam Map<String, Object> requestParams) {
        return animeService.searchAnime(requestParams);
    }
}
