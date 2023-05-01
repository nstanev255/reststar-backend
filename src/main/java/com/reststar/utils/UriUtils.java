package com.reststar.utils;

import org.apache.http.client.utils.URIBuilder;

import java.util.Map;

public class UriUtils {
    /**
     * This helper method is used for helping to add params to a URIBuilder.
     *
     * @param uriBuilder The uriBuilder to add the params to.
     * @param params     The params.
     */
    public static void mapUriParams(URIBuilder uriBuilder, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return;
        }

        for (String key : params.keySet()) {
            Object param = params.get(key);
            uriBuilder.addParameter(key, String.valueOf(param));
        }

    }
}
