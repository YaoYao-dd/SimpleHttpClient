package org.yaod.shc.httpclient.processor;

import org.yaod.shc.httpclient.exception.ShcException;
import org.yaod.shc.httpclient.serializer.ObjectSerializer;

import java.net.http.HttpResponse;

/**
 * @author Yaod
 **/
public class ObjectBodyHandler<T> implements HttpResponse.BodyHandler<T> {

    private final Class<T> responseClazz;
    private final HttpResponse.BodyHandler<String> stringHandler;

    private final ObjectSerializer serializer;

    private ObjectBodyHandler(Class<T> aResponseClazz, ObjectSerializer serializer) {
        this.stringHandler = HttpResponse.BodyHandlers.ofString() ;
        this.responseClazz=aResponseClazz;
        this.serializer=serializer;
    }

    public static <Z> ObjectBodyHandler<Z> of(Class<Z> clazz, ObjectSerializer serializer){
        return new ObjectBodyHandler<>(clazz, serializer);
    }

    @Override
    public HttpResponse.BodySubscriber<T> apply(HttpResponse.ResponseInfo responseInfo) {

        HttpResponse.BodySubscriber<String> bodySubscriber= stringHandler.apply(responseInfo);
        return new ObjectBodySubscriber<T>(responseInfo.statusCode(),bodySubscriber, this.responseClazz, serializer);
    }
}
