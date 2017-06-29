package swa.util;

import com.google.common.collect.Lists;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.List;

/**
 * redis服务，暂时将文件数据保存在redis中
 * Created by jinyan.cao on 2017/6/2.
 */
public class RedisUtil {
    public static ShardedJedis jedis;

    static {
        List<JedisShardInfo> shardInfos = Lists.newArrayList(new JedisShardInfo("127.0.0.1", "6379"));
        jedis = new ShardedJedisPool(new JedisPoolConfig(), Lists.newCopyOnWriteArrayList(shardInfos)).getResource();
    }

    public static void save(String key, String value) {
        jedis.set(key, value);
    }

    public static String get(String key) {
        return jedis.get(key);
    }

    public static void main(String[] args) {
        save("fileName.properties", "");
        System.out.println(get("fileName.properties"));
    }
}
