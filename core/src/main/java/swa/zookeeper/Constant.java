package swa.zookeeper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * Created by jinyan on 6/20/17.
 */
public class Constant {
    public static final String ZK_REGISTRY_PATH = "/registry";
    public static final String SERVER_LIST = "127.0.0.1:8081";//提供服务的服务器地址，zookeeper数据节点
    public static Integer ZK_SESSION_TIMEOUT = 5000000;
    public static String SERVER_REGISTRY_ADDRESS = "127.0.0.1:2181";

    // TODO: 6/30/17 可做一个单独的项目用来做应用管理
    public static List<String> hotDeployAppNames = Lists.newArrayList("/app1", "/app2");//区分同一个应用的不同机器部署的服务
    public static Map<String, String> ipAppMap = ImmutableMap.of("127.0.1.1", "/app1", "0.0.0.0", "/app2", "127.0.0.1", "/app3");//根据本地ip获取本地服务的应用名，用于node注册
    public static Map<String, String> ipAddrassMap = ImmutableMap.of("/app1", "127.0.0.1:8081", "/app2", "0.0.0.0:1111", "/app3", "127.0.0.1:8081");//通过应用名获得服务调用方请求的server地址
}
