package swa.zookeeper;

import com.google.common.base.Strings;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import java.util.concurrent.CountDownLatch;

/**
 * 做本地服务的zk注册
 * Created by jinyan on 6/17/17.
 */
public final class RegistryService {
    private static final CountDownLatch latch = new CountDownLatch(1);


    public static void setUp(){//注册完成之后，创建一个新的zk节点    //注册到zk中，其中data为服务端的 ip:port
        String data = Constant.SERVER_LIST;
        if (!Strings.isNullOrEmpty(data)) {
            ZooKeeper zk = connectServer();//获取一个client
            if (zk != null) {
                addRootNode(zk);
                createNode(zk, data);//创建节点
            }
        }
    }

    private static void addRootNode(ZooKeeper zk) {
        Stat s = null;
        try {
            s = zk.exists(Constant.ZK_REGISTRY_PATH, false);
            System.out.println("addRootNode:" + s);
            if (s == null) {
                String actualPath = zk.create(Constant.ZK_REGISTRY_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                System.out.println("addRootNode end:" + actualPath);
            }
        } catch (Exception e) {
            System.out.println("addRootNode error:" + e);
        }
    }

    private static ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(Constant.SERVER_REGISTRY_ADDRESS, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });//当zookeeper连接成功后，会发送event消息，接收到连接成功的消息之后，latch减1，否则阻塞线程。也就是说，在连接未成功之前，会一直阻塞线程，不会执行后续操作。
            latch.await();
        } catch (Exception e) {
            System.out.println("connectServer error:" + e);
        }
        return zk;
    }

    private static void createNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();//加入节点的数据
            String path = zk.create(Constant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("create zookeeper node path:" + path + " data:" + data);
        } catch (Exception e) {
            System.out.println("createNode error:" + e);
        }
    }
//ZooDefs.Ids.OPEN_ACL_UNSAFE：可做任何操作（读，写，添加，删除，管理），无权限控制
    //CreateMode.EPHEMERAL_SEQUENTIAL：当连接中断时，这个数据会被删除，同时这个节点的名称后会加一个递增的数字（保证节点数据的唯一性）

}
