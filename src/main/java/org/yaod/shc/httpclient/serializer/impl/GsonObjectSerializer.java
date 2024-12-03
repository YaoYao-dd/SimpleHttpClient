package org.yaod.shc.httpclient.serializer.impl;

import com.google.gson.Gson;
import org.yaod.shc.httpclient.serializer.ObjectSerializer;

/**
 * @author Yaod
 **/
public class GsonObjectSerializer implements ObjectSerializer {
    private static final Gson gson =new Gson();

    @Override
    public synchronized <T> T deSerialize(String jsonString, Class<T> clazz) {
        if(jsonString==null) {
            return null;
        }
        if(clazz.isAssignableFrom(String.class)){
            return (T)jsonString;
        }
        return gson.fromJson(jsonString, clazz);
    }

    @Override
    public synchronized String serialize(Object object) {
        if(object==null) {
            return null;
        }
        if(object instanceof String){
            return (String)object;
        }
        return gson.toJson(object);
    }
}
