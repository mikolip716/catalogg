package com.ml.musiclist.helper;

import com.ml.musiclist.enums.FormatEnum;

public class FormatEnumParser {
    public static FormatEnum parse(String value) {
        value = value.trim();
        value = value.toLowerCase();
        FormatEnum formatEnum = FormatEnum.OTHER;
        if (value.contains("cd")) {
            formatEnum = FormatEnum.CD;
        } else if (value.contains("vinyl")) {
            formatEnum = FormatEnum.VINYL;
        } else if (value.contains("cassette")) {
            formatEnum = FormatEnum.CASSETTE;
        } else if (value.contains("digital")) {
            formatEnum = FormatEnum.DIGITAL_MEDIA;
        } else if (value.contains("minidisc")){
            formatEnum = FormatEnum.MINIDISC;
        } else if (value.contains("shellac")) {
            formatEnum = FormatEnum.SHELLAC;
        } else if (value.contains("sacd") || value.contains("hqcd")) {
            formatEnum = FormatEnum.SACD;
        } else if (value.contains("reel")) {
            formatEnum = FormatEnum.REEL_TO_REEL;
        }
        return formatEnum;
    }
}
