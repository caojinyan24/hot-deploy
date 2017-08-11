package swa.zookeeper;

import com.google.common.base.Strings;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by jinyan on 6/30/17.
 */
public class ServerWatcher implements Watcher, Runnable {
    private static final CountDownLatch latch = new CountDownLatch(1);
    private static final Logger logger = LoggerFactory.getLogger(ServerWatcher.class);
    private ZooKeeper zk;
    private Boolean isClosed = false;


    ServerWatcher() {
        try {
            zk = new ZooKeeper(ZKParam.SERVER_REGISTRY_ADDRESS, ZKParam.ZK_SESSION_TIMEOUT, new Watcher() {
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
            zk.exists(ZKParam.ZK_REGISTRY_PATH, this);
            List<String> nodeList = zk.getChildren(ZKParam.ZK_REGISTRY_PATH, this);
            for (String node : nodeList) {
                zk.exists(ZKParam.ZK_REGISTRY_PATH + '/' + node, this);
            }
        } catch (Exception e) {
            logger.error("init server watcher error:", e);
        }
    }


    public void process(WatchedEvent event) {//event关联了变动节点对应的client。当client的session过期，event会报session过期相关异常
        try {
            logger.info("process event:{}", event);
            if (Strings.isNullOrEmpty(event.getPath())) {
                return;
            }
            switch (event.getType()) {
                case NodeDeleted:
                    break;
                case NodeDataChanged:
                    break;
                case None:
                    switch (event.getState()) {
                        case SyncConnected:
                            break;
                        case Expired:
                            isClosed = true;
                            synchronized (this) {
                                notifyAll();
                            }
                            break;
                    }
                    break;
                case NodeChildrenChanged:
                    break;
            }
        } catch (Exception e) {
            logger.error("process watcher error", e);
        }
    }

    public synchronized void run() {
        while (!isClosed) {
            try {
                wait();
            } catch (Exception e) {
                logger.error("run error:", e);
            }
        }
    }
}
