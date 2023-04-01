package com.reststar.api.jikan;

import com.reststar.model.anime.Anime;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.beans.beancontext.BeanContext;

@Component
public class JikanAPI {
    public Anime findAnimeById(Long id) {
        Anime anime = new Anime();

        // TODO: Implement this
        return anime;
    }
}
