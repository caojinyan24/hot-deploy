package swa.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swa.obj.ConfigFile;

/**
 * 提供文件的更新和获取服务
 * Created by jinyan on 5/26/17.
 */
public class DataStorerService {
    private static final Logger logger = LoggerFactory.getLogger(DataStorerService.class);

    public static ConfigFile getValue(String fileName) {
        String fileStr = RedisUtil.get(fileName);
        if (Strings.isNullOrEmpty(fileStr)) {
            logger.error("fileNotExist");
            return new ConfigFile(1, null, fileName);
        } else {
            return JSON.parseObject(RedisUtil.get(fileName), ConfigFile.class);
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
        RedisUtil.save(fileName, JSON.toJSONString(file));
    }


}
