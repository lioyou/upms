package com.leecp.upms.common.util;

import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.joda.time.DateTime;

/**
 * 资源文件属性读取工具
 * @author LeeCP
 *
 */
public class PropertiesFileUtil {

    // 当打开多个资源文件时，缓存资源文件
    private static HashMap<String, PropertiesFileUtil> configMap = new HashMap<String, PropertiesFileUtil>();
    // 打开文件时间，判断超时使用
    private DateTime loadTime = null;
    // 资源文件
    private ResourceBundle resourceBundle = null;
    // 默认资源文件名称
    private static final String NAME = "config";
    // 缓存时间
    private static final Integer TIME_OUT = 60 * 1000;

    // 私有构造方法，创建单例
    private PropertiesFileUtil(String name) {
        this.loadTime = new DateTime();
        this.resourceBundle = ResourceBundle.getBundle(name);
    }

    public static synchronized PropertiesFileUtil getInstance() {
        return getInstance(NAME);
    }

    public static synchronized PropertiesFileUtil getInstance(String name) {
        PropertiesFileUtil file = configMap.get(name);
        if (null == file) {
            file = new PropertiesFileUtil(name);
            configMap.put(name, file);
        }
        // 判断是否打开的资源文件是否超时1分钟
        if (new DateTime().isAfter(file.loadTime.plusMillis(TIME_OUT))) {
            file = new PropertiesFileUtil(name);
            configMap.put(name, file);
        }
        return file;
    }

    // 根据key读取value
    public String get(String key) {
        try {
            String value = resourceBundle.getString(key);
            return value;
        }catch (MissingResourceException e) {
            return "";
        }
    }

    // 根据key读取value(整型)
    public Integer getInt(String key) {
        try {
            String value = resourceBundle.getString(key);
            return Integer.parseInt(value);
        }catch (MissingResourceException e) {
            return null;
        }
    }

    // 根据key读取value(布尔)
    public boolean getBool(String key) {
        try {
            String value = resourceBundle.getString(key);
            if ("true".equals(value)) {
                return true;
            }
            return false;
        }catch (MissingResourceException e) {
            return false;
        }
    }

    public DateTime getLoadTime() {
        return loadTime;
    }
}
