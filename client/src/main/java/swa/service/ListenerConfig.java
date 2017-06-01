package swa.service;

import java.lang.reflect.Field;

/**
 * Created by jinyan on 5/26/17.
 */
public class ListenerConfig {
    String fileName;
    CallBackLoader loader;

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
}
