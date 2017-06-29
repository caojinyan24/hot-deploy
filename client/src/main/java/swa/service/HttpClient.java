package swa.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.ning.http.client.*;
import swa.obj.ConfigFile;
import swa.zookeeper.ServiceDiscovery;


/**
 * Created by jinyan.cao on 2017/6/7.
 */
public class HttpClient {
    private static String SERVER_URL = "http://127.0.0.1:8081/getData";
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
        String serverIp = ServiceDiscovery.getInstance().getServerAddress();
        System.out.println("get server ip:" + serverIp);
        if (Strings.isNullOrEmpty(serverIp)) {
            throw new RuntimeException("get server address error");
        }
        String url = "http://" + serverIp + "/getData";
        AsyncHttpClient.BoundRequestBuilder requestBuilder = client.preparePost(url);
        requestBuilder.addQueryParam("fileName", fileName);
        requestBuilder.setMethod("GET");
        Request request = requestBuilder.build();
        System.out.println("request param:" + request);
        ListenableFuture<Response> response = client.executeRequest(request);
        return processResponse(response);


    }

    private ConfigFile processResponse(ListenableFuture<Response> response) {
        try {
            Response resp = response.get();
            String respStr = resp.getResponseBody();
            System.out.println("processResponse-end:" + respStr);
            if (Strings.isNullOrEmpty(respStr)) {
                return null;
            }
            return JSONObject.parseObject(respStr, ConfigFile.class);
        } catch (Exception e) {
            System.out.println("processResponse error" + e);
            return null;
        }
    }

}
