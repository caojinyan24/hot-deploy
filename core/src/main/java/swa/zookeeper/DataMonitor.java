package swa.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Arrays;


/**
 * Created by jinyan on 6/17/17.
 */
public class DataMonitor implements Watcher, AsyncCallback.StatCallback {
    ZooKeeper zk;
    String znode;
    Watcher chainedWatcher;
    boolean dead;
    DataMonitorLisener listener;
    byte prevData[];

//The call to ZooKeeper.exists() checks for the existence of the znode, sets a watch,
// and passes a reference to itself (this) as the completion callback object. In this sense,
// it kicks things off, since the real processing happens when the watch is triggered.
    public DataMonitor(ZooKeeper zk, String znode, Watcher chainedWatcher, DataMonitorLisener listener) {
        this.zk = zk;
        this.znode = znode;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;
        zk.exists(znode, true, this, null);//异步检查当前节点是否存在，若不存在抛出异常
    }

    public void processResult(int rc, String path, Object ctx, Stat stat) {
        boolean exists;
        switch (KeeperException.Code.get(rc)) {
            case OK:
                exists = true;
                break;
            case NONODE:
                exists = false;
                break;
            case SESSIONEXPIRED:
            case NOAUTH:
                dead = true;
                listener.closing(rc);
                return;
            default:
                zk.exists(znode, true, this, null);
                return;
        }
        byte b[] = null;
        if (exists) {
            try {
                b = zk.getData(znode, false, null);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if ((b != null && b != prevData) || (b != null && !Arrays.equals(prevData, b))) {
            listener.exists(b);
            prevData = b;

        }
    }


    public void process(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Event.EventType.None) {
            switch (event.getState()) {
                case SyncConnected:
                    break;
                case Expired:
                    dead = true;
                    listener.closing(KeeperException.Code.SESSIONEXPIRED.intValue());
                    break;
            }
        } else {
            if (path != null && path.equals(znode)) {//一个Monitor对应一个节点
                zk.exists(znode, true, this, null);
            }
        }
        if (chainedWatcher != null) {
            chainedWatcher.process(event);//处理event
        }
    }

    public interface DataMonitorLisener {
        void exists(byte data[]);

        void closing(int rc);
    }
}
