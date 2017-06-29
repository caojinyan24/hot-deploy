package swa.zookeeper;

/**
 * Created by jinyan on 6/20/17.
 */
public class Constant {
    public static Integer ZK_SESSION_TIMEOUT = 5000;
    public static final String ZK_REGISTRY_PATH = "/registry";
    public static String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
    public static final String MONITOR_FILE="C:\\Users\\hp\\Documents\\app\\zookeeper-3.4.10\\data\\2181\\file";

    public static final String SERVER_LIST="127.0.0.1:8081";//提供服务的服务器地址，zookeeper数据节点
    public static String SERVER_REGISTRY_ADDRESS="127.0.0.1:2181";
}
