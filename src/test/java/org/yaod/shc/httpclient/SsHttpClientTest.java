package org.yaod.shc.httpclient;


import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * @author Yaod
 **/


public class SsHttpClientTest {

    private String baseUrl="http://localhost:3000/users";
    SsHttpClient ssHttpClient;
    @Before
    public void setup(){
        ssHttpClient=SsHttpClient.newBuilder()
                .build();
    }

    @Test
    public void test_sync_get() throws IOException, InterruptedException {
        User user1= ssHttpClient.get(baseUrl+"/1", User.class);
        assertEquals(25, user1.getAge());
    }

    @Test
    public void test_sync_getAsync() throws IOException, InterruptedException, ExecutionException {
        var cf= ssHttpClient.getAsync(baseUrl+"/1", User.class);
        var user1= cf.get();
        assertEquals(25, user1.getAge());
    }
    @Test
    public void test_sync_post_and_delete() throws IOException, InterruptedException {
        User user=new User();
        user.setId("999");
        user.setName("James Green");
        user.setAge(34);
        User result =ssHttpClient.post(user,baseUrl, User.class);
        assertNotNull(result);
        var deleteResult = ssHttpClient.delete(baseUrl + "/999", String.class);
        assertNotNull(deleteResult);
    }

    @Test
    public void test_sync_put_and_get() throws IOException, InterruptedException {
        User originalUser = ssHttpClient.get(baseUrl + "/1", User.class);
        int originalAge= originalUser.getAge();

        User user=new User();
        user.setId("1");
        user.setName("James Smith");
        user.setAge(4);
        User result =ssHttpClient.put(user,baseUrl+"/1", User.class);

        User updateResult = ssHttpClient.get(baseUrl + "/1", User.class);
        assertEquals(updateResult.getAge(), 4);

        user=new User();
        user.setId("1");
        user.setName("James Smith");
        user.setAge(originalAge);
        User finalResult =ssHttpClient.put(user,baseUrl+"/1", User.class);
    }


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

}