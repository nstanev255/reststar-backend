package com.reststar.api.jikan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reststar.api.jikan.model.*;
import com.reststar.api.jikan.model.response.JikanAnimeByIdResponse;
import com.reststar.api.jikan.model.response.JikanSearchResponse;
import com.reststar.api.jikan.model.response.Pagination;
import com.reststar.model.anime.PaginationResponse;
import com.reststar.model.anime.Type;
import com.reststar.utils.HttpMethod;
import com.reststar.utils.UriUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
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

    private final ObjectMapper mapper;

    public JikanAPI() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    /**
     * This method is used for calling the jikan api.
     * @param additionalUrl
     *   If there is something additional needed to be added to the url.
     *   For example, if the url has a path param, or some additions to the url.
     *
     *   If not, use null.
     * @param queryParams
     *   The GET Query params, if any.
     *
     *   If not, use null.
     * @param httpMethod
     *   The HttpMethod to do request. This is required.
     *
     * @return
     *   Returns the HttpEntity, which can be used to extract all the needed inforamtion.
     */
    private HttpEntity doRequest(String additionalUrl, Map<String, Object> queryParams, HttpMethod httpMethod) {

        CloseableHttpClient client = HttpClients.createDefault();

        try {
            String baseAnimeUrl = baseUrl + animeUrl;
            if (!StringUtils.isEmpty(additionalUrl)) {
                baseAnimeUrl += additionalUrl;
            }

            URIBuilder uriBuilder = new URIBuilder(baseAnimeUrl);
            UriUtils.mapUriParams(uriBuilder, queryParams);

            HttpRequestBase request = null;
            switch (httpMethod) {
                case GET -> request = new HttpGet(uriBuilder.build());
                case POST -> request = new HttpPost(uriBuilder.build());
                case PUT -> request = new HttpPut(uriBuilder.build());
            }

            return client.execute(request).getEntity();

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.toString());
        } finally {
            try {
                client.close();
            } catch (Exception exception) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.toString());
            }
        }
    }

    /**
     * Hit the Jikan /search api endpoint.
     * @param params
     *   The query params.
     * @return
     *   The response form the search, paginated.
     */
    public PaginationResponse search(Map<String, Object> params) {
        PaginationResponse paginationResponse;

        try {
            HttpEntity entity = doRequest(null, params, HttpMethod.GET);
            JikanSearchResponse jikanSearchResponse = this.mapper.readValue(entity.getContent(), JikanSearchResponse.class);
            paginationResponse = mapPaginationResponse(jikanSearchResponse);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.toString());
        }

        return paginationResponse;
    }


    private PaginationResponse mapPaginationResponse(JikanSearchResponse jikanSearchResponse) {
        PaginationResponse response = new PaginationResponse();

        List<com.reststar.model.anime.Anime> data = mapSearchData(jikanSearchResponse.getData());
        Pagination jikanPagination = jikanSearchResponse.getPagination();

        response.setData(data);
        response.setTotal(jikanPagination.getItems().getTotal());
        response.setLastPage(jikanPagination.getLastVisiblePage());
        response.setItemsPerPage(jikanPagination.getItems().getPerPage());
        response.setItems(jikanPagination.getItems().getCount());

        return response;
    }

    private List<com.reststar.model.anime.Anime> mapSearchData(List<Anime> data) {
        List<com.reststar.model.anime.Anime> mappedData = new ArrayList<>();

        if (data != null && !data.isEmpty()) {
            for (Anime anime : data) {
                com.reststar.model.anime.Anime mappedAnime = mapJikanAnime(anime);
                mappedData.add(mappedAnime);
            }
        }

        return mappedData;
    }

    /**
     * Hit the getById anime endpoint.
     * @param id
     *   The id of the anime to search for.
     * @return
     *   The Found Anime. If not found an 404 exception is thrown.
     */
    public com.reststar.model.anime.Anime findAnimeById(Long id) {

        com.reststar.model.anime.Anime anime;
        try {
            HttpEntity httpEntity = doRequest("/" + id + "/full", null, HttpMethod.GET);
            JikanAnimeByIdResponse jikanResponse = this.mapper.readValue(httpEntity.getContent(), JikanAnimeByIdResponse.class);

            if (jikanResponse.getData() == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
            }

            anime = mapJikanAnime(jikanResponse.getData());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.toString());
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

        if (anime.getGenres() != null && anime.getGenres().size() > 0) {
            for (Genre modelGenre : anime.getGenres()) {
                com.reststar.model.anime.Genre genre = new com.reststar.model.anime.Genre();
                genre.setId(modelGenre.getMalId());
                genre.setName(modelGenre.getName());

                genres.add(genre);
            }
        }

        if (anime.getExplicitGenres() != null && anime.getExplicitGenres().size() > 0) {
            for (Genre modelGenre : anime.getExplicitGenres()) {
                com.reststar.model.anime.Genre genre = new com.reststar.model.anime.Genre();
                genre.setName(modelGenre.getName());
                genre.setId(modelGenre.getMalId());

                genres.add(genre);
            }
        }

        animeModel.setGenres(genres);
    }

    private void mapStatus(com.reststar.model.anime.Anime animeModel, Anime anime) {
        if (anime.getStatus() == null) {
            return;
        }

        switch (anime.getStatus()) {
            case CURRENTLY_AIRING -> animeModel.setStatus(com.reststar.model.anime.Status.AIRING);
            case NOT_AIRED -> animeModel.setStatus(com.reststar.model.anime.Status.NOT_AIRED);
            case FINISHED_AIRING -> animeModel.setStatus(com.reststar.model.anime.Status.FINISHED_AIRING);
        }
    }

    private void mapSeason(com.reststar.model.anime.Anime animeModel, Anime anime) {
        if (anime.getSeason() == null) {
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
        if (aired == null) {
            return;
        }

        animeModel.setAiredForm(anime.getAired().getFrom());
        animeModel.setAiredTo(anime.getAired().getTo());
    }

    private void mapType(com.reststar.model.anime.Anime animeModel, Anime anime) {
        if (anime.getType() == null) {
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
