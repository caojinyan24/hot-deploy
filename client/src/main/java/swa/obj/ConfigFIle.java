package swa.obj;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by jinyan.cao on 2017/6/7.
 */
public class ConfigFile implements Serializable {
    private static final long serialVersionUID = 8762761008727084587L;
    private Integer version;
    private String content;

    public Map<String, String> parseFile() {
        return (Map<String, String>) JSONObject.parseObject(content, Map.class);
    }

    public Boolean isNewer(ConfigFile configFile) {
        return this.version > configFile.version;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
