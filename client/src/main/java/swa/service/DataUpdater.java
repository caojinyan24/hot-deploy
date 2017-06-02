package swa.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;
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
        System.out.println("DataUpdater.setData:" + fileName);
        for (ListenerConfig config : listenerConfigList) {
            if (config.getFileName().equals(fileName)) {
                config.getLoader().loadData(JSONObject.parseObject(RedisUtil.get(fileName), Map.class));
            }
        }
    }

}
