package org.badmus.settlement.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;
import java.util.UUID;

public class GeneralUtils {

    public static String generateString() {
        return UUID.randomUUID().toString();
    }


    public static boolean stringIsNullOrEmpty(String value) {
        return Objects.isNull(value) || value.isEmpty();
    }

    public static <T> T convertStringToObject(String jsonString, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error while converting string to object", e);
        }
    }
}
