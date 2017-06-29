package swa.zookeeper;

/**
 * Created by jinyan on 6/20/17.
 */
public class Constant {
    public static Integer ZK_SESSION_TIMEOUT = 5000;
    public static String ZK_REGISTRY_PATH = "/registry";
    public static String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
    public static String SERVER_ADDRESS="127.0.0.1:8081";
    public static String REGISTRY_ADDRESS="127.0.0.1:2181";
}
