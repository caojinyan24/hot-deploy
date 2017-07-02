package swa.zookeeper;

import com.google.common.collect.Maps;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jinyan on 6/19/17.
 */
public class ServiceDiscovery {
    private static final CountDownLatch latch = new CountDownLatch(1);
    private static Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);
    private static ServiceDiscovery instance;
    private static AtomicBoolean isInited = new AtomicBoolean(Boolean.FALSE);
    private static Map<String, String> appServerMap = Maps.newConcurrentMap();

    private ServiceDiscovery() {
        try {
            new ZooKeeper(Constant.SERVER_REGISTRY_ADDRESS, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                public void process(WatchedEvent event) {
                    //zookeeper处于同步连通的状态时
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }

            });
            latch.await();//
        } catch (Exception e) {
            logger.error("ServiceDiscovery init error", e);
        }
    }

    public static ServiceDiscovery getInstance() {
        if (!isInited.get()) {
            if (instance == null) {
                instance = new ServiceDiscovery();
                System.out.printf("ServerMap:" + appServerMap);
            }
            isInited.compareAndSet(Boolean.FALSE, Boolean.TRUE);
            System.out.printf("ServerMap1:" + appServerMap);
        }
        return instance;

    }

    public Map<String, String> getAppServerMap() {
        return appServerMap;
    }
}
// TODO: 6/19/17 有没有不用static的