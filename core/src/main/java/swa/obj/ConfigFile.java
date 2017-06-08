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
    private Map<String, String> content;

    public ConfigFile() {
    }

    public ConfigFile(Integer version, Map<String, String> content) {
        this.version = version;
        this.content = content;
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

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ConfigFile{" +
                "version=" + version +
                ", content=" + content +
                '}';
    }
}
