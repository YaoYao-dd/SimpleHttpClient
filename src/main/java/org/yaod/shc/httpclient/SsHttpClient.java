package org.yaod.shc.httpclient;

import org.yaod.shc.httpclient.exception.ShcException;
import org.yaod.shc.httpclient.processor.ObjectBodyHandler;
import org.yaod.shc.httpclient.serializer.ObjectSerializer;
import org.yaod.shc.httpclient.serializer.impl.GsonObjectSerializer;

import java.io.IOException;
import java.net.Authenticator;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Yaod
 **/
public class SsHttpClient implements AutoCloseable{
    private final Duration defaultReadTimeout;
    private final HttpClient httpClientInternal;
    private final ObjectSerializer serializer;

    private

    SsHttpClient(HttpClient httpClientInternal, ObjectSerializer serializer,  Duration readTimeout) {
        this.defaultReadTimeout = readTimeout;
        this.serializer=serializer;
        this.httpClientInternal = httpClientInternal;
        
    }

    public <T> T send(HttpRequest request, Class<T> responseClazz) throws IOException, InterruptedException {
        var result= this.httpClientInternal.send(request, ObjectBodyHandler.of(responseClazz, this.serializer));
        return result.body();
    }

    public <T> CompletableFuture<T> sendAsync(HttpRequest request, Class<T> responseClazz) throws IOException, InterruptedException {
        var result= this.httpClientInternal.sendAsync(request, ObjectBodyHandler.of(responseClazz, this.serializer));
        return result.thenApply(HttpResponse::body);
    }

    public <T, R> T post(R requestBody,String url, Class<T> responseClazz, Duration readTimeout) throws IOException, InterruptedException {
        HttpRequest request = buildRequest(HttpVerb.POST, requestBody, url, readTimeout);
        var result= this.httpClientInternal.send(request, ObjectBodyHandler.of(responseClazz, this.serializer));
        return result.body();
    }
    public <T, R> T post(R requestBody,String url, Class<T> responseClazz) throws IOException, InterruptedException {
        return this.post(requestBody,url,responseClazz,null);
    }
    public <T, R> CompletableFuture<T> postAsync(R requestBody, String url, Class<T> responseClazz,Duration readTimeout) throws IOException, InterruptedException {
        HttpRequest request = buildRequest(HttpVerb.POST, requestBody, url, readTimeout);
        var result= this.httpClientInternal.sendAsync(request, ObjectBodyHandler.of(responseClazz, this.serializer));
        return result.thenApply(HttpResponse::body);
    }
    public <T, R> CompletableFuture<T> postAsync(R requestBody, String url, Class<T> responseClazz) throws IOException, InterruptedException {
        return this.postAsync(requestBody,url,responseClazz,null);
    }

    public <T, R> T put(R requestBody,String url, Class<T> responseClazz,Duration readTimeout) throws IOException, InterruptedException {
        HttpRequest request = buildRequest(HttpVerb.PUT, requestBody, url, readTimeout);
        var result= this.httpClientInternal.send(request, ObjectBodyHandler.of(responseClazz, this.serializer));
        return result.body();
    }
    public <T, R> T put(R requestBody,String url, Class<T> responseClazz) throws IOException, InterruptedException {
        return this.put(requestBody,url,responseClazz, null);
    }
    public <T, R> CompletableFuture<T> putAsync(R requestBody, String url, Class<T> responseClazz,Duration readTimeout) throws IOException, InterruptedException {
        HttpRequest request = buildRequest(HttpVerb.PUT, requestBody, url, readTimeout);
        var result= this.httpClientInternal.sendAsync(request, ObjectBodyHandler.of(responseClazz, this.serializer));
        return result.thenApply(HttpResponse::body);
    }

    public <T, R> CompletableFuture<T> putAsync(R requestBody, String url, Class<T> responseClazz) throws IOException, InterruptedException {
        return this.putAsync(requestBody,url,responseClazz, null);
    }

    public <T> T get(String url, Class<T> responseClazz,Duration readTimeout) throws IOException, InterruptedException {
        HttpRequest request = buildRequest(HttpVerb.GET, null, url, readTimeout);
        var result= this.httpClientInternal.send(request, ObjectBodyHandler.of(responseClazz, this.serializer));
        return result.body();
    }

    public <T> T get(String url, Class<T> responseClazz) throws IOException, InterruptedException {
        return this.get(url,responseClazz, null);
    }
    public <T> CompletableFuture<T> getAsync(String url, Class<T> responseClazz,Duration readTimeout) throws IOException, InterruptedException {
        HttpRequest request = buildRequest(HttpVerb.GET, null, url, readTimeout);
        var result= this.httpClientInternal.sendAsync(request, ObjectBodyHandler.of(responseClazz, this.serializer));
        return result.thenApply(HttpResponse::body);
    }
    public <T> CompletableFuture<T> getAsync(String url, Class<T> responseClazz) throws IOException, InterruptedException {
        return this.getAsync(url,responseClazz, null);
    }

    public <T> T delete(String url, Class<T> responseClazz,Duration readTimeout) throws IOException, InterruptedException {
        HttpRequest request = buildRequest(HttpVerb.DELETE, null, url, readTimeout);
        var result= this.httpClientInternal.send(request, ObjectBodyHandler.of(responseClazz, this.serializer));
        return result.body();
    }

    public <T> T delete(String url, Class<T> responseClazz) throws IOException, InterruptedException {
        return this.delete(url,responseClazz, null);
    }
    public <T> CompletableFuture<T> deleteAsync(String url, Class<T> responseClazz,Duration readTimeout) throws IOException, InterruptedException {
        HttpRequest request = buildRequest(HttpVerb.DELETE, null, url, readTimeout);
        var result= this.httpClientInternal.sendAsync(request, ObjectBodyHandler.of(responseClazz, this.serializer));
        return result.thenApply(HttpResponse::body);
    }

    public <T> CompletableFuture<T> deleteAsync(String url, Class<T> responseClazz) throws IOException, InterruptedException {
        return this.deleteAsync(url,responseClazz, null);
    }

    private <R> HttpRequest buildRequest(HttpVerb verb, R request, String url, Duration readTimeout) {
        String requestString = this.serializer.serialize(request);
        var reqBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json");
        Duration finalTimeout= Objects.nonNull(readTimeout)?readTimeout:this.defaultReadTimeout;
        if(Objects.nonNull(finalTimeout)) {
            reqBuilder = reqBuilder.timeout(finalTimeout);
        }
        if (verb == HttpVerb.POST)    {
            reqBuilder=reqBuilder.POST(HttpRequest.BodyPublishers.ofString(requestString));
        }else if (verb == HttpVerb.GET)    {
            reqBuilder=reqBuilder.GET();
        }else if (verb == HttpVerb.PUT)    {
            reqBuilder=reqBuilder.PUT(HttpRequest.BodyPublishers.ofString(requestString));
        }else if (verb == HttpVerb.DELETE)    {
            reqBuilder=reqBuilder.DELETE();
        }
        else if (verb == HttpVerb.HEAD)    {
            reqBuilder=reqBuilder.HEAD();
        }else{
            throw new ShcException(0, String.format("http verb not supported for %s.", verb), null);
        }
        return reqBuilder.build();
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    @Override
    public void close() throws Exception {
        //this.httpClientInternal.close();
    }

    public static class Builder{
        private final HttpClient.Builder builderInternal;

        private HttpClient.Version version;
        private HttpClient.Redirect followRedirections;
        private Duration connectTimeout;
        private Duration readTimeout;
        private ProxySelector proxy;
        private Authenticator authenticator;
        private ObjectSerializer serializer;
        private Executor executor;


        Builder(){
            builderInternal = HttpClient.newBuilder();
        }

        public HttpClient.Builder internalBuilder(){
            return this.builderInternal;
        }
        public SsHttpClient build(){

            if(this.version!=null){
                builderInternal.version(this.version);
            }
            if(this.followRedirections!=null){
                builderInternal.followRedirects(this.followRedirections);
            }
            if(this.connectTimeout!=null){
                builderInternal.connectTimeout(connectTimeout);
            }
            if(this.proxy!=null){
                builderInternal.proxy(proxy);
            }
            if(this.authenticator!=null){
                builderInternal.authenticator(this.authenticator);
            }
            if(this.serializer==null){
                this.serializer=new GsonObjectSerializer();
            }
            if(this.executor==null){
                this.executor= Executors.newVirtualThreadPerTaskExecutor();
            }
            builderInternal.executor(this.executor);

            var httpClientInternal= builderInternal.build();
            return new SsHttpClient(httpClientInternal,this.serializer,  this.readTimeout);
        }

        public Builder version(HttpClient.Version version) {
            this.version = version;
            return this;
        }
        public Builder followRedirections(HttpClient.Redirect followRedirections) {
            this.followRedirections = followRedirections;
            return this;
        }
        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }
        public Builder readTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }
        public Builder proxy(ProxySelector proxy) {
            this.proxy = proxy;
            return this;
        }
        public Builder authenticator(Authenticator authenticator) {
            this.authenticator = authenticator;
            return this;
        }
        public Builder setSerializer(ObjectSerializer serializer) {
            this.serializer = serializer;
            return this;
        }
    }
}
