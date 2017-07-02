package swa.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.ning.http.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swa.obj.ConfigFile;
import swa.zookeeper.ServiceDiscovery;


/**
 * Created by jinyan.cao on 2017/6/7.
 */
public final class HttpClient {
    private static final AsyncHttpClient client;
    private static final ServiceDiscovery instance = ServiceDiscovery.getInstance();
    private static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    static {
        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        builder.setConnectTimeout(1000);
        builder.setRequestTimeout(2000);
        builder.setAllowPoolingConnections(true);
        builder.setCompressionEnforced(true);
        builder.setPooledConnectionIdleTimeout(3 * 60 * 1000);
        client = new AsyncHttpClient(builder.build());
    }

    private HttpClient() {
    }

    public static ConfigFile request(String fileName) {
        String serverIp = DataStorer.getServerAddress();
        logger.info("get server ip:{}", serverIp);
        if (Strings.isNullOrEmpty(serverIp)) {
            throw new RuntimeException("get server address error");
        }
        String url = "http://" + serverIp + "/getData";
        AsyncHttpClient.BoundRequestBuilder requestBuilder = client.preparePost(url);
        requestBuilder.addQueryParam("fileName", fileName);
        requestBuilder.setMethod("GET");
        requestBuilder.setRequestTimeout(1000);
        Request request = requestBuilder.build();
        logger.debug("request param:{}", request);
        ListenableFuture<Response> response = client.executeRequest(request);
        return processResponse(response);


    }

    private static ConfigFile processResponse(ListenableFuture<Response> response) {
        try {
            Response resp = response.get();
            String respStr = resp.getResponseBody();
            if (Strings.isNullOrEmpty(respStr)) {
                return null;
            }
            return JSON.parseObject(respStr, ConfigFile.class);
        } catch (Exception e) {
            logger.error("processResponse error", e);
            return null;
        }
    }

}
