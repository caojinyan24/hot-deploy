package swa.service;

import com.alibaba.fastjson.JSONObject;
import swa.service.RedisUtil;

import java.util.Map;

/**
 * Created by jinyan on 5/26/17.
 */
public class DataStorer {

    public static Map getValue(String fileName) {
        Map map = JSONObject.parseObject(RedisUtil.get(fileName), Map.class);
        System.out.println("get data:fileName:" + fileName + "#value:" + map);
        return map;

    }

    public static void setValue(String fileName, Map<String, String> value) {
        System.out.println("stored data:fileName:" + fileName + "#value:" + value);
        RedisUtil.save(fileName, JSONObject.toJSONString(value));
    }
}
