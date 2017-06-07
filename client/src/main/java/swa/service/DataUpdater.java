package swa.service;

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
public class DataUpdater {
    private static List<ListenerConfig> listenerConfigList = new CopyOnWriteArrayList<ListenerConfig>();

    public static void addListener(ListenerConfig listener) {
        listenerConfigList.add(listener);
    }

    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * 接收http请求并触发setData方法
     */
    public static void requestDataOnSchedule() {
        System.out.println("DataUpdater.setData:");
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                                                         public void run() {
                                                             for (ListenerConfig config : listenerConfigList) {
                                                                 ConfigFile file = new HttpClient().request(config.getFileName());
                                                                 if (config.getConfigFile() == null || file.isNewer(config.getConfigFile())) {
                                                                     config.getLoader().loadData(file.parseFile());

                                                                 }
                                                             }
                                                         }

                                                     }

                , 60 * 1000, 60 * 1000, TimeUnit.MILLISECONDS);

    }


}
