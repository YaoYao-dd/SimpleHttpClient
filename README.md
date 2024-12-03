# Why this SimpleHttpClient?

Prior to JDK 11, the built-in http client URLConnection is useless at all.
then the opensource offers many convenient tools, like apache httpclient, spring restTemplate,
OkHttp, and even openFeign(declarative http client), but none of them support synchronous and asynchrous at the same time,
SpringWebFlux offers a Webclient looks cool, but it is highly coupled with webflux, netty offers good library for
asynchronous client, but still not convenient.

NOW, along with JDK11, Java HttpClient API implements the client-side of the most recent HTTP standards. It supports HTTP/1.1 and HTTP/2, both synchronous and asynchronous programming models.

But it does NOT support POJO conversion automatically, this is main point I created this Simple library.

1. It supports automatically POJO conversion(use Gson as default serialization engine, easy to extend to other engines).
2. Very friendly use, compare to official JDK http client.
3. Similar symantic for sync and async http calls.
4. Supports default read timeout at client level.
5. Throw Exceptional when httpcode is not 2xx, most applications favorite this behavior.

# how to use?
```java
@Test
    public void test_async_put_and_async_get() throws IOException, InterruptedException, ExecutionException {
        User originalUser = ssHttpClient.get(baseUrl + "/1", User.class);
        int originalAge= originalUser.getAge();

        User user=new User();
        user.setId("1");
        user.setName("James Smith");
        user.setAge(4);
        CompletableFuture<User> cfresult =ssHttpClient.putAsync(user,baseUrl+"/1", User.class);
        var upldatedUser=cfresult.get();

        User updateResult = ssHttpClient.getAsync(baseUrl + "/1", User.class).get();
        assertEquals(updateResult.getAge(), 4);

        user=new User();
        user.setId("1");
        user.setName("James Smith");
        user.setAge(originalAge);
        User finalResult =ssHttpClient.putAsync(user,baseUrl+"/1", User.class).get();
    }

```
See [Sample in TestCase](src/test/java/org/yaod/shc/httpclient/SsHttpClientTest.java)

This repo offers [json](src/test/resources/jsonserver.json) for testing, use https://www.npmjs.com/package/json-server to start a mock webserver if you like.

Thanks

