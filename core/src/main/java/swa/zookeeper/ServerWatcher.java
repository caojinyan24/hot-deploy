package swa.zookeeper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swa.service.DataStorer;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by jinyan on 6/30/17.
 */
public class ServerWatcher implements Watcher, Runnable {
    private static final CountDownLatch latch = new CountDownLatch(1);
    private static Logger logger = LoggerFactory.getLogger(ServerWatcher.class);
    private ZooKeeper zk;
    private Boolean isClosed = false;


    ServerWatcher() {
        try {
            zk = new ZooKeeper(Constant.SERVER_REGISTRY_ADDRESS, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });//注册本类为watcher
            latch.await();

            //  The call to ZooKeeper.exists() checks for the existence of the znode, sets a watch,
            // and passes a reference to itself (this) as the completion callback object. In this sense,
            // it kicks things off, since the real processing happens when the watch is triggered.
            zk.exists(Constant.ZK_REGISTRY_PATH, this);
            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH, this);
            for (String node : nodeList) {
                zk.exists(Constant.ZK_REGISTRY_PATH + '/' + node, this);
            }
        } catch (Exception e) {
            logger.error("init server watcher error:", e);
        }
    }

    public static void main(String[] args) {
        RegistryService.setUp();
        new ServerWatcher().run();
    }

    public void process(WatchedEvent event) {//event关联了变动节点对应的client。当client的session过期，event会报session过期相关异常
        try {
            logger.info("process event:{}", event);
            if (Strings.isNullOrEmpty(event.getPath())) {
                return;
            }
            switch (event.getType()) {// TODO: 6/30/17
                case NodeDeleted:
                    ServiceDiscovery.getInstance().getAppServerMap().clear();
                    break;
                case NodeDataChanged:
                    byte[] bytes = zk.getData(event.getPath(), Boolean.TRUE, null);
                    ServiceDiscovery.getInstance().getAppServerMap().put(event.getPath(), new String(bytes));
                    DataStorer.setServerAddressList(Lists.newArrayList(ServiceDiscovery.getInstance().getAppServerMap().values()));
                    break;
                case None:
                    switch (event.getState()) {
                        case SyncConnected:
                            break;
                        case Expired:
                            ServiceDiscovery.getInstance().getAppServerMap().remove(event.getPath());
                            isClosed = true;
                            synchronized (this) {
                                notifyAll();
                            }
                            DataStorer.setServerAddressList(Lists.newArrayList(ServiceDiscovery.getInstance().getAppServerMap().values()));
                            break;
                    }
                    break;
                case NodeChildrenChanged:
                    List<String> nodeList = zk.getChildren(event.getPath(), this, null);
                    for (String node : nodeList) {
                        byte[] bytes1 = zk.getData(event.getPath() + '/' + node, Boolean.TRUE, null);
                        ServiceDiscovery.getInstance().getAppServerMap().put(event.getPath() + '/' + node, new String(bytes1));
                        DataStorer.setServerAddressList(Lists.newArrayList(ServiceDiscovery.getInstance().getAppServerMap().values()));
                    }
                    break;
            }
        } catch (Exception e) {
            logger.error("process watcher error", e);
        }
    }

    public void run() {
        synchronized (this) {//获取对象锁
            while (!isClosed) {
                try {
                    wait();
                } catch (Exception e) {
                    logger.error("run error:", e);
                }
            }
        }
    }
}
