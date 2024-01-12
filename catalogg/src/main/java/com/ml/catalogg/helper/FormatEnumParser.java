package com.ml.catalogg.helper;

import com.ml.catalogg.enums.FormatEnum;

public class FormatEnumParser {
    public static FormatEnum parse(String value) {
        FormatEnum formatEnum = FormatEnum.OTHER;
        if (value == null) {
            return formatEnum;
        }
        value = value.trim();
        value = value.toLowerCase();
        if (value.contains("sacd") || value.contains("hqcd") || value.contains("cd") && (
                value.contains("blu-spec") || value.contains("shm"))) {
            formatEnum = FormatEnum.SACD;
        } else if (value.contains("vinyl") || value.contains("flexi-disc")) {
            formatEnum = FormatEnum.VINYL;
        } else if (value.contains("cassette")) {
            formatEnum = FormatEnum.CASSETTE;
        } else if (value.contains("digital")) {
            formatEnum = FormatEnum.DIGITAL_MEDIA;
        } else if (value.contains("minidisc")){
            formatEnum = FormatEnum.MINIDISC;
        } else if (value.contains("shellac")) {
            formatEnum = FormatEnum.SHELLAC;
        } else if (value.contains("cd")) {
            formatEnum = FormatEnum.CD;
        } else if (value.contains("reel")) {
            formatEnum = FormatEnum.REEL_TO_REEL;
        }
        return formatEnum;
    }
}
