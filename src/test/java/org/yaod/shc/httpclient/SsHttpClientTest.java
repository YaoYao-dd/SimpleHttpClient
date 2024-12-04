package org.yaod.shc.httpclient;


import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
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


    @Test
    public void test_massive_async_get() throws IOException, InterruptedException, ExecutionException {
        //when using asynchronous mode, it will utilize virtual thread to conduct http call concurrently.
        //it means very small number of physical thread, no much resource occupied.
        //still get very good performance(less total latency) because of IO multiplexing.
        //of course, you can switch back traditional thread model using builder.executor() method.

        long start = System.currentTimeMillis();
        List<CompletableFuture<String>> taskList=new ArrayList<>();
        for(int idx=0;idx<100;idx++){
         taskList.add(ssHttpClient.getAsync("http://localhost:8081/accounts/get", String.class).thenApply(x->{
             System.out.println(x);return x;
         }));
        }

        var allCf=CompletableFuture.allOf(taskList.toArray(new  CompletableFuture[]{}));
        allCf.get();
        long duration=(System.currentTimeMillis()-start)/1000;
        System.out.println(duration);
    }

}