package br.com.ecommerce.util;

import java.util.ResourceBundle;

public class ConfigLoader {
    private static ResourceBundle bundle = ResourceBundle.getBundle("config");

    public static String getProperty(String key) {
        return bundle.getString(key);
    }
}
