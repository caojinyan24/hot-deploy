package swa.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jinyan on 5/25/17.
 */
public class DataUpdater {
    private static List<ListenerConfig> listenerConfigList = new CopyOnWriteArrayList<ListenerConfig>();

    public static void addListener(ListenerConfig listener) {
        listenerConfigList.add(listener);
    }

    public static void setData(String fileName) {
        for (ListenerConfig config : listenerConfigList) {
            if (config.getFileName().equals(fileName)) {
                config.getLoader().loadData();
            }
        }
    }
}
