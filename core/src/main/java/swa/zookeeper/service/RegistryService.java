package swa.zookeeper.service;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import swa.zookeeper.Constant;

import java.util.concurrent.CountDownLatch;

/**
 * 做本地服务的zk注册
 * Created by jinyan on 6/17/17.
 */
public class RegistryService {
    private static final CountDownLatch latch = new CountDownLatch(1);


    //注册到zk中，其中data为服务端的 ip:port
    public static void register(String data) {//注册完成之后，创建一个新的zk节点
        if (data != null) {
            ZooKeeper zk = connectServer();//获取一个client
            if (zk != null) {
                addRootNode(zk);
                createNode(zk, data);//创建节点
            }
        }
    }

    //todo  自动在程序里添加节点
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
            zk = new ZooKeeper(Constant.ZK_REGISTRY_PATH, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (Exception e) {
            System.out.println("connectServer error:" + e);
        }
        return zk;
    }

    private static void createNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();
            String path = zk.create(Constant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("create zookeeper node path:" + path + " data:" + data);
        } catch (Exception e) {
            System.out.println("createNode error:" + e);
        }
    }


}
