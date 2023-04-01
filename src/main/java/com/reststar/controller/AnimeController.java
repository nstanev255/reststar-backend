package com.reststar.controller;

import com.reststar.model.anime.Anime;
import com.reststar.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("anime")
public class AnimeController {
    @Autowired
    private AnimeService animeService;

    @GetMapping("{id}")
    public Anime getAnimeById(@PathVariable("id") String id) {
        return animeService.findAnimeById(Long.valueOf(id));
    }
}
