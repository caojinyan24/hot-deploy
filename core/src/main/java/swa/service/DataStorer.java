package swa.service;

import com.alibaba.fastjson.JSONObject;
import swa.service.RedisUtil;

import java.util.Map;

/**
 * Created by jinyan on 5/26/17.
 */
public class DataStorer {

    public static Map getValue(String fileName) {
        System.out.println("stored data:" + fileName);
        return JSONObject.parseObject(RedisUtil.get(fileName), Map.class);
    }

    public static void setValue(String fileName, Map<String, String> value) {
        RedisUtil.save(fileName, JSONObject.toJSONString(value));
    }
}
