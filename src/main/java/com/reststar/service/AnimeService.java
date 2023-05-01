package com.reststar.service;

import com.reststar.api.jikan.JikanAPI;
import com.reststar.constants.Constants;
import com.reststar.model.anime.Anime;
import com.reststar.model.anime.PaginationResponse;
import com.reststar.model.anime.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AnimeService {
    @Autowired
    private JikanAPI jikanAPI;

    public Anime findAnimeById(Long id) {
        return jikanAPI.findAnimeById(id);
    }

    private void validateDefaultSearchParams(Map<String, Object> requestParam) {
        Integer page = (Integer) requestParam.get("page");
        if (page == null) {
            requestParam.put("page", Constants.PAGE);
        } else if (page <= 0) {
            throw new IllegalArgumentException("Page cannot be 0, or lower than 0.");
        }

        Integer limit = (Integer) requestParam.get("limit");
        if (limit == null) {
            requestParam.put("limit", Constants.LIMIT);
        }

        String typeParm = (String) requestParam.get("type");
        if (typeParm != null) {
            Type type = Type.typeOf(typeParm);

            if (type == null) {
                throw new IllegalArgumentException("Illegal type value");
            }
        }
    }

    public PaginationResponse searchAnime(Map<String, Object> requestParams) {
        this.validateDefaultSearchParams(requestParams);
        return jikanAPI.search(requestParams);
    }
}
