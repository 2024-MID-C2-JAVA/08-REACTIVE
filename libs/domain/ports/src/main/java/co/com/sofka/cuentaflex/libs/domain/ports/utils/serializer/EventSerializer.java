package co.com.sofka.cuentaflex.libs.domain.ports.utils.serializer;

public interface EventSerializer {
    String serialize(Object event) throws SerializeException;
    Object deserialize(String json, Class<?> clazz) throws DeserializeException;
}
