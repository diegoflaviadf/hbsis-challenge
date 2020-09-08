package br.com.hbsis.challenge.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultMapper extends ObjectMapper {

    public static ObjectMapper INSTANCE = new DefaultMapper();

    private DefaultMapper() {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}
