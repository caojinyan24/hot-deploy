package swa.service;

import swa.obj.ConfigFile;

import java.util.List;
import java.util.concurrent.*;

/**
 * 推送&更新数据
 * Created by jinyan on 5/25/17.
 */
public class DataUpdater {
    private static List<ListenerConfig> listenerConfigList = new CopyOnWriteArrayList<ListenerConfig>();
    private static ScheduledExecutorService scheduledExecutorService;

    static {//初始化时执行定时调度
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        requestDataOnSchedule();
    }


    public static void addListener(ListenerConfig listener) {
        listenerConfigList.add(listener);
        System.out.println("addListener:" + listenerConfigList);
    }


    /**
     * 接收http请求并触发setData方法
     */
    public static void requestDataOnSchedule() {
        System.out.println("DataUpdater.setData:" + listenerConfigList);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    for (ListenerConfig config : listenerConfigList) {
                        System.out.println("loadDataOnSchedule-begin:" + config);
                        ConfigFile file = new HttpClient().request(config.getFileName());
                        System.out.println("loadDataOnSchedule-end:" + file);
                        if (config.getConfigFile() == null || file.isNewer(config.getConfigFile())) {
                            config.getLoader().loadData(file);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("requestDataOnSchedule error:" + e);
                }
            }
        }, 0, 3, TimeUnit.SECONDS);

    }


}
