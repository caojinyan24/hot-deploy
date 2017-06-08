package swa.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import swa.obj.ConfigFile;

/**
 * 保存/取用数据
 * Created by jinyan on 5/26/17.
 */
public class DataStorer {

    public static ConfigFile getValue(String fileName) {
        String fileStr = RedisUtil.get(fileName);
        if (Strings.isNullOrEmpty(fileStr)) {
            System.out.println("fileNotExist");
            return null;
        } else {
            return JSONObject.parseObject(RedisUtil.get(fileName), ConfigFile.class);
        }

    }


    /**
     * 把更新后的数据推送给服务调用端
     * 触发远程的DataUpdater
     *
     * @param fileName
     * @param file
     */
    public static void setValue(String fileName, ConfigFile file) {
        System.out.println("stored data:fileName:" + fileName + "#value:" + file);
        RedisUtil.save(fileName, JSONObject.toJSONString(file));
    }
}
