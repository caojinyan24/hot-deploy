package swa.service;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by jinyan on 5/26/17.
 */
public class DataStorer {
    public static Map<String, Map<String, String>> fileValues = Maps.newConcurrentMap();

    public static Map<String, String> getValue(String fileName) {
        System.out.println("stored data:" + fileValues);
        return fileValues.get(fileName);
    }

    public static void setValue(String fileName, Map<String, String> value) {
        fileValues.put(fileName, value);
    }
}
