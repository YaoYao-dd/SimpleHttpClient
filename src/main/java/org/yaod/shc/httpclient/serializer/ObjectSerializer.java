package org.yaod.shc.httpclient.serializer;

/**
 * the only built-in implementation is based on Gson, any serializer mechanism is easily plugged-in.
 * @author Yaod
 **/
public interface ObjectSerializer {

    public <T> T deSerialize(String jsonString, Class<T> clazz);

    public String serialize(Object object);
}
