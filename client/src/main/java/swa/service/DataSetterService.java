package swa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swa.obj.ConfigFile;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 推送&更新数据
 * Created by jinyan on 5/25/17.
 */
public final class DataSetterService {
    private static final Logger logger = LoggerFactory.getLogger(DataSetterService.class);
    private static final List<ListenerConfig> listenerConfigList = new CopyOnWriteArrayList<ListenerConfig>();
    private static final ScheduledExecutorService scheduledExecutorService;
    private static final Integer TIME_SLOT = 60;//取值时间间隔

    static {//初始化时执行定时调度
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        requestDataOnSchedule();
    }

    private DataSetterService() {
    }


    public static void addListener(ListenerConfig listener) {
        listenerConfigList.add(listener);
    }


    /**
     * 接收http请求并触发setData方法
     */
    public static void requestDataOnSchedule() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    for (ListenerConfig config : listenerConfigList) {
                        ConfigFile file = HttpClientService.requestFile(config.getFileName());
                        if (file == null) {
                            logger.error("requestDataOnSchedule get file empty");
                            return;
                        }
                        if (config.getConfigFile() == null || file.isNewer(config.getConfigFile())) {
                            logger.debug("updateDate:{}", config.getConfigFile());
                            config.getLoader().loadData(file);
                        }
                    }
                } catch (Exception e) {
                    logger.error("requestDataOnSchedule error:", e);
                }
            }
        }, 0, TIME_SLOT, TimeUnit.SECONDS);

    }
}
