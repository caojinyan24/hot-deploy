package swa.service;

import swa.obj.ConfigFile;


/**
 * Created by jinyan on 5/26/17.
 */
public class ListenerConfig {
    ConfigFile configFile;
    CallBackLoader loader;
    String fileName;

    public ListenerConfig(String fileName, CallBackLoader loader) {
        this.fileName = fileName;
        this.loader = loader;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public CallBackLoader getLoader() {
        return loader;
    }

    public void setLoader(CallBackLoader loader) {
        this.loader = loader;
    }

    public ConfigFile getConfigFile() {
        return configFile;
    }

    public void setConfigFile(ConfigFile configFile) {
        this.configFile = configFile;
    }
}
