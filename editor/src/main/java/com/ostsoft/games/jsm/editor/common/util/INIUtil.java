package com.ostsoft.games.jsm.editor.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class INIUtil {
    public static synchronized Properties loadINI(InputStream stream) throws IOException {
        Properties properties = new Properties();
        properties.load(stream);
        stream.close();

        return properties;
    }

    public static synchronized void saveINI(OutputStream stream, Properties properties) throws IOException {
        properties.store(stream, null);
        stream.close();
    }

    public static synchronized Map<String, String> loadINItoMap(InputStream stream) throws IOException {
        Properties ini = new Properties();
        ini.load(stream);
        stream.close();

        Enumeration e = ini.propertyNames();
        Map<String, String> map = new HashMap<>();

        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            map.put(key, ini.getProperty(key));
        }
        return map;
    }

    public static synchronized void saveINIfromMap(OutputStream stream, Map<String, String> map) throws IOException {
        Properties ini = new Properties();
        Set<String> set = map.keySet();

        for (String key : set) {
            String value = map.get(key);
            ini.setProperty(key, value);
        }

        ini.store(stream, null);
        stream.close();
    }
}
