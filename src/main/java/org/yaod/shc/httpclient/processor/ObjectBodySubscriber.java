package org.yaod.shc.httpclient.processor;

import org.yaod.shc.httpclient.exception.ShcException;
import org.yaod.shc.httpclient.serializer.ObjectSerializer;

import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;

/**
 * @author Yaod
 **/
public class ObjectBodySubscriber<T> implements HttpResponse.BodySubscriber<T> {

    private final int httpcode;
    private final HttpResponse.BodySubscriber<String> stringBodySubscriber;

    private final ObjectSerializer objectSerializer;
    Class<T> clazz;

    public ObjectBodySubscriber(int httpcode, HttpResponse.BodySubscriber<String> stringBodySubscriber,
                                Class<T> clazz,
                                ObjectSerializer serializer) {
        this.httpcode=httpcode;
        this.stringBodySubscriber = stringBodySubscriber;
        this.objectSerializer= serializer;
        this.clazz = clazz;

    }

    @Override
    public CompletionStage<T> getBody() {
        return stringBodySubscriber.getBody().thenApply(this::deSerializer);
    }

    private T deSerializer(String content) {
        var potentialError=ShcException.parseHttpCode(this.httpcode, content);
        if(Objects.nonNull(potentialError)){
            throw potentialError;
        }
        if(this.clazz.isAssignableFrom(String.class)){
            return (T)content;
        }
        return this.objectSerializer.deSerialize(content, this.clazz);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        stringBodySubscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(List<ByteBuffer> item) {
        stringBodySubscriber.onNext(item);
    }

    @Override
    public void onError(Throwable throwable) {
        stringBodySubscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        stringBodySubscriber.onComplete();
    }
}
