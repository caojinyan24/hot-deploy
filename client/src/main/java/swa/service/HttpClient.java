package swa.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ning.http.client.*;
import com.ning.http.client.multipart.StringPart;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import swa.obj.ConfigFile;

import java.util.Map;

/**
 * Created by jinyan.cao on 2017/6/7.
 */
public class HttpClient {
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

    public ConfigFile request(String fileName) {
        AsyncHttpClient.BoundRequestBuilder requestBuilder = client.preparePost(CLIENT_URL);
        requestBuilder.addBodyPart(new StringPart("fileName", fileName));
        Request request = requestBuilder.build();
        ListenableFuture<Response> response = client.executeRequest(request);
        return processResponse(response);


    }

    private ConfigFile processResponse(ListenableFuture<Response> response) {
        // TODO: 2017/6/5
        return null;
    }

}
