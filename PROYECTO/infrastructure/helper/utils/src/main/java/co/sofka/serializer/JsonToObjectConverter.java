package co.sofka.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

public class JsonToObjectConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T convertJsonToObject(String jsonString, Class<T> clazz) throws Exception {
        return objectMapper.readValue(jsonString, clazz);
    }


    public static String convertObjectToJson(Object clazz) throws Exception {
        return objectMapper.writeValueAsString(clazz);
    }
}
