package com.reststar.api.jikan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reststar.api.jikan.model.*;
import com.reststar.model.anime.Type;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JikanAPI {

    @Value("${api.jikan.api.base.url}")
    private String baseUrl;

    @Value("${api.jikan.api.anime.url}")
    private String animeUrl;

    public com.reststar.model.anime.Anime findAnimeById(Long id) {

        com.reststar.model.anime.Anime anime;
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            URIBuilder uriBuilder = new URIBuilder(baseUrl + animeUrl + "/" + id + "/full");
            HttpGet httpGet = new HttpGet(uriBuilder.build());

            System.out.println(uriBuilder.build());


            CloseableHttpResponse response = client.execute(httpGet);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            JikanAnimeByIdResponse jikanResponse = objectMapper.readValue(response.getEntity().getContent(), JikanAnimeByIdResponse.class);

            anime = mapJikanAnime(jikanResponse.getData());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Service Unavailable.");
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return anime;
    }

    private com.reststar.model.anime.Anime mapJikanAnime(Anime anime) {
        com.reststar.model.anime.Anime animeModel = new com.reststar.model.anime.Anime();
        if (anime == null) {
            return animeModel;
        }

        animeModel.setId(anime.getMalId());
        animeModel.setEpisodes(anime.getEpisodes());
        animeModel.setSynopsis(anime.getSynopsis());

        mapTitle(animeModel, anime);
        mapType(animeModel, anime);
        mapAired(animeModel, anime);
        mapSeason(animeModel, anime);
        mapStatus(animeModel, anime);
        mapGenres(animeModel, anime);

        return animeModel;
    }

    private void mapGenres(com.reststar.model.anime.Anime animeModel, Anime anime) {
        List<com.reststar.model.anime.Genre> genres = new ArrayList<>();

        if(anime.getGenres() != null && anime.getGenres().size() > 0) {
            for(Genre modelGenre: anime.getGenres()) {
                com.reststar.model.anime.Genre genre = new com.reststar.model.anime.Genre();
                genre.setId(modelGenre.getMalId());
                genre.setName(modelGenre.getName());

                genres.add(genre);
            }
        }

        if(anime.getExplicitGenres() != null && anime.getExplicitGenres().size() > 0) {
            for(Genre modelGenre: anime.getExplicitGenres()) {
                com.reststar.model.anime.Genre genre = new com.reststar.model.anime.Genre();
                genre.setName(modelGenre.getName());
                genre.setId(modelGenre.getMalId());

                genres.add(genre);
            }
        }

        animeModel.setGenres(genres);
    }

    private void mapStatus(com.reststar.model.anime.Anime animeModel, Anime anime) {
        if(anime.getStatus() == null) {
            return;
        }

        switch (anime.getStatus()) {
            case CURRENTLY_AIRING -> animeModel.setStatus(com.reststar.model.anime.Status.AIRING);
            case NOT_AIRED -> animeModel.setStatus(com.reststar.model.anime.Status.NOT_AIRED);
            case FINISHED_AIRING -> animeModel.setStatus(com.reststar.model.anime.Status.FINISHED_AIRING);
        }
    }

    private void mapSeason(com.reststar.model.anime.Anime animeModel, Anime anime) {
        if(anime.getSeason() == null) {
            return;
        }
        switch (anime.getSeason()) {
            case FALL -> animeModel.setSeason(com.reststar.model.anime.Season.FALL);
            case SPRING -> animeModel.setSeason(com.reststar.model.anime.Season.SPRING);
            case SUMMER -> animeModel.setSeason(com.reststar.model.anime.Season.SUMMER);
            case WINTER -> animeModel.setSeason(com.reststar.model.anime.Season.WINTER);
        }
    }

    private void mapAired(com.reststar.model.anime.Anime animeModel, Anime anime) {
        Aired aired = anime.getAired();
        if(aired == null) {
            return;
        }

        animeModel.setAiredForm(anime.getAired().getFrom());
        animeModel.setAiredTo(anime.getAired().getTo());
    }

    private void mapType(com.reststar.model.anime.Anime animeModel, Anime anime) {
        if(anime.getType() == null) {
            return;
        }

        switch (anime.getType()) {
            case TV -> animeModel.setType(Type.TV);
            case ONA -> animeModel.setType(Type.ONA);
            case OVA -> animeModel.setType(Type.OVA);
            case MOVIE -> animeModel.setType(Type.MOVIE);
            case MUSIC -> animeModel.setType(Type.MUSIC);
            case SPECIAL -> animeModel.setType(Type.SPECIAL);
        }
    }

    public void mapTitle(com.reststar.model.anime.Anime animeModel, com.reststar.api.jikan.model.Anime anime) {
        List<Title> titles = anime.getTitles();
        for (Title title : titles) {
            if (StringUtils.equals(title.getType(), "Default")) {
                animeModel.setTitle(title.getTitle());
            } else if (StringUtils.equals(title.getType(), "English")) {
                animeModel.setEnglishTitle(title.getTitle());
            }
        }
    }
}
