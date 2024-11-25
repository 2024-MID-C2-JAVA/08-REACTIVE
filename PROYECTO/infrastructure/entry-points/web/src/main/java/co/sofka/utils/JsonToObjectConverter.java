package co.sofka.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToObjectConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T convertJsonToObject(String jsonString, Class<T> clazz) throws Exception {
        return objectMapper.readValue(jsonString, clazz);
    }
}
