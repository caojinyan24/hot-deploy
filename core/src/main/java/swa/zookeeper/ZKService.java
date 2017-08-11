package swa.zookeeper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * zk节点数据管理服务：数据注册和数据获取
 * Created by jinyan on 6/17/17.
 */
public class ZKService {
    private static final Logger logger = LoggerFactory.getLogger(ZKService.class);

    private static final CountDownLatch latch = new CountDownLatch(1);

    public static Map<String, String> ipAddrassMap = ImmutableMap.of("0.0.0.1", "127.0.0.1:8081", "127.0.0.1", "127.0.0.1:8081", "127.0.1.1", "127.0.0.1:8081");//// TODO: 8/10/17 想办法获得服务器的ip地址和host
    public static Boolean isInited = Boolean.FALSE;
    private static ZooKeeper zk;

    public static void setUp() {
        logger.debug("ZKService init");
        zk = connectServer();//获取一个client
        logger.debug("ZKService init：{}", zk);

        if (zk != null) {
            addRootNode();
            createNode();//创建节点
        }
        isInited = true;

    }


    private static void addRootNode() {
        Stat s = null;
        try {
            s = zk.exists(ZKParam.ZK_REGISTRY_PATH, new ServerWatcher());//启动监听
            logger.debug("ZKService init：s:{}", s);

            if (s == null) {
                zk.create(ZKParam.ZK_REGISTRY_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.debug("addRootNode");

            }
        } catch (Exception e) {
            logger.error("addRootNode error:", e);
        }
    }

    private static ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(ZKParam.SERVER_REGISTRY_ADDRESS, ZKParam.ZK_SESSION_TIMEOUT, new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }

                }
            });//当zookeeper连接成功后，会发送event消息，接收到连接成功的消息之后，latch减1，否则阻塞线程。也就是说，在连接未成功之前，会一直阻塞线程，不会执行后续操作。
            latch.await();
        } catch (Exception e) {
            logger.error("connectServer error:", e);
        }
        return zk;
    }


    /**
     * 服务启动后，将本地ip地址加入到zookeeper节点中
     */
    private static void createNode() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            String addr = ipAddrassMap.get(ip);//// TODO: 8/10/17 待优化，怎么获取到本地web服务的host和port
            Stat s = zk.exists(ZKParam.ZK_REGISTRY_PATH + '/' + ip, new ServerWatcher());
            if (s == null) {
                zk.create(ZKParam.ZK_REGISTRY_PATH + '/' + ip, addr.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);//当client断开连接后，节点会自动被删除
            }
            logger.info("create zookeeper node path:{},data:{}", ZKParam.ZK_REGISTRY_PATH + '/' + ip, ipAddrassMap.get(ip).getBytes());
        } catch (Exception e) {
            logger.error("createNode error:", e);
        }
    }

    /**
     * 获取zk节点数据
     *
     * @return 当前可用的服务器地址
     */
    public static String getServer() throws KeeperException, InterruptedException {
        List<String> servers = getValidServers();
        if (CollectionUtils.isEmpty(servers)) {
            throw new RuntimeException("no server found");
        }
        return getValidServers().get(new Random(System.currentTimeMillis()).nextInt(servers.size()));
    }


    /**
     * 获取zk节点数据
     *
     * @return 当前可用的服务器地址
     */
    private static List<String> getValidServers() throws KeeperException, InterruptedException {
        List<String> servers = Lists.newLinkedList();
        if (!isInited) {
            setUp();//// TODO: 8/11/17 初始化方式有待优化 
        }
        List<String> ch = zk.getChildren(ZKParam.ZK_REGISTRY_PATH, Boolean.TRUE, null);
        for (String s : ch) {
            servers.add(new String(zk.getData(ZKParam.ZK_REGISTRY_PATH + '/' + s, Boolean.TRUE, null)));
        }
        return servers;
    }


}
