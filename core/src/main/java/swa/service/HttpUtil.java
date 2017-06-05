package swa.service;


import com.alibaba.fastjson.JSONObject;
import com.ning.http.client.*;
import com.ning.http.client.multipart.StringPart;

import java.util.Map;

/**
 * Created by jinyan.cao on 2017/6/5.
 */

public class HttpUtil  {
    private static String CLIENT_URL = "http:127.0.0.1:8080";
    private static String SERVER_URL = "http:127.0.0.1:8081";
    private static final AsyncHttpClient client;

    static {
        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        builder.setConnectTimeout(1000);
        builder.setRequestTimeout(2000);
        builder.setAllowPoolingConnections(true);
        builder.setCompressionEnforced(true);
        builder.setPooledConnectionIdleTimeout(3 * 60 * 1000);
        client = new AsyncHttpClient(builder.build());
    }

    public  void post(Map<String, Map<String, String>> value) {
        AsyncHttpClient.BoundRequestBuilder requestBuilder = client.preparePost(CLIENT_URL);
        requestBuilder.addBodyPart(new StringPart("content", JSONObject.toJSONString(value)));
        Request request = requestBuilder.build();
        ListenableFuture<Response> response = client.executeRequest(request);
        processResponse(response);
    }

    private void processResponse(ListenableFuture<Response> response) {
        // TODO: 2017/6/5
    }
}
