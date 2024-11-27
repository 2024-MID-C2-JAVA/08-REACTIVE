package co.sofka.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectToJsonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertObjectToJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }
}
