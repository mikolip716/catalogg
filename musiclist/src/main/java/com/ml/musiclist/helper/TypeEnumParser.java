package com.ml.musiclist.helper;

import com.ml.musiclist.enums.TypeEnum;

public class TypeEnumParser {
    public static TypeEnum parse(String type) {
        TypeEnum typeEnum = TypeEnum.OTHER;
        type = type.trim();
        type = type.toLowerCase();
        if (type.contains("album")) {
            typeEnum = TypeEnum.ALBUM;
        } else if (type.contains("ep")) {
            typeEnum = TypeEnum.EP;
        } else if (type.contains("single")) {
            typeEnum = TypeEnum.SINGLE;
        } else if (type.contains("demo")) {
            typeEnum = TypeEnum.DEMO;
        } else if (type.contains("mixtape")) {
            typeEnum = TypeEnum.MIXTAPE;
        } else if (type.contains("compilation")) {
            typeEnum = TypeEnum.COMPILATION;
        } else if (type.contains("soundtrack")) {
            typeEnum = TypeEnum.SOUNDTRACK;
        } else if (type.contains("live")) {
            typeEnum = TypeEnum.LIVE;
        }
        return typeEnum;
    }
}