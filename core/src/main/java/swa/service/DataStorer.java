package swa.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swa.obj.ConfigFile;
import swa.util.RedisUtil;

import java.util.List;
import java.util.Random;

/**
 * 保存/取用数据
 * Created by jinyan on 5/26/17.
 */
public class DataStorer {
    private static final Logger logger = LoggerFactory.getLogger(DataStorer.class);

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

    public static String getServerAddress() {
        String serverStr = RedisUtil.get("servers");
        if (Strings.isNullOrEmpty(serverStr)) {
            return null;
        }
        List<String> servers = JSON.parseArray(serverStr, String.class);

        return servers.get(new Random(System.currentTimeMillis()).nextInt(servers.size()));
    }

    public static void setServerAddressList(List<String> servers) {
        RedisUtil.save("servers", JSON.toJSONString(servers));
        logger.info("set serverList:{}", servers);
    }
}
