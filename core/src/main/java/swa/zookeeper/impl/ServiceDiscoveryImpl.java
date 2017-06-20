package swa.zookeeper.impl;

import com.google.common.collect.Lists;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.util.CollectionUtils;
import swa.zookeeper.Constant;
import swa.zookeeper.ServiceDiscovery;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by jinyan on 6/19/17.
 */
public class ServiceDiscoveryImpl {
    private static ZooKeeper zk;
    private static final CountDownLatch latch = new CountDownLatch(1);
    private List<String> serverList = Lists.newArrayList();//记得初始化--空指针
    private static ServiceDiscoveryImpl instance = null;//todo 做同步

    public static ServiceDiscoveryImpl getInstance() {
        if (instance == null) {
            instance = new ServiceDiscoveryImpl("127.0.0.1:8081");
        }
        return instance;
    }

    private ServiceDiscoveryImpl(String data) {
        try {
            RegistryServiceImpl.register(data);//todo：
            zk = new ZooKeeper(Constant.ZK_REGISTRY_PATH, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                public void process(WatchedEvent event) {
                    //zookeeper处于同步连通的状态时
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }

            });
            latch.await();//
            if (zk != null) {
                watchNode(zk);
            }
            System.out.println("serviceDiscovery init:" + zk);
        } catch (Exception e) {
            System.out.println("ServiceDiscovery init error" + e);
        }
    }


    public String getServerAddress() {
        System.out.println("get server list:" + serverList);
        if (CollectionUtils.isEmpty(serverList)) {
            return null;
        }
        String serverAddress = serverList.get(new Random(System.currentTimeMillis()).nextInt(serverList.size()));
        System.out.println("final address:" + serverAddress);
        return serverAddress;

    }

    public void watchNode(final ZooKeeper zk) {
        try {
            List<String> nodeLIst = zk.getChildren(Constant.ZK_REGISTRY_PATH, new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        watchNode(zk);
                    }
                }
            });
            System.out.println("watchNode-getNode:" + nodeLIst);
            for (String node : nodeLIst) {
                byte[] bytes = zk.getData(Constant.ZK_REGISTRY_PATH + '/' + node, false, null);
                System.out.println("bytes:" + bytes);
                serverList.add(new String(bytes));
            }
            System.out.println("watchNode-serverList:" + serverList);
            // TODO: 6/20/17  updateConnection???
        } catch (Exception e) {
            System.out.println("watchNode error:" + e);
        }
    }
}
// TODO: 6/19/17 有没有不用static的