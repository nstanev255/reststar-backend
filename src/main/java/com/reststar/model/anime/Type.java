package com.reststar.model.anime;

import org.apache.commons.lang3.StringUtils;

public enum Type {
    TV("tv"), OVA("ova"), MOVIE("movie"), SPECIAL("special"), ONA("ona"), MUSIC("music");

    final String type;

    Type(String type) {
        this.type = type;
    }

    public static Type typeOf(String type) {
        Type enumType = null;
        for (Type type_ : Type.values()) {
            if (StringUtils.equals(type_.type, type)) {
                enumType = type_;
                break;
            }
        }

        return enumType;
    }
}
