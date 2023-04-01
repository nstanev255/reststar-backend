package com.reststar.service;

import com.reststar.api.jikan.JikanAPI;
import com.reststar.model.anime.Anime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimeService {
    @Autowired
    private JikanAPI jikanAPI;

    public Anime findAnimeById(Long id) {
        return jikanAPI.findAnimeById(id);
    }
}
