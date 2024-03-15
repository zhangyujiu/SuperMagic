package com.supermagic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NameUtils {

    private static final String DIC_RESOURCE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIC_RESOURCE_NUM = "0123456789abcdefghijklmnopqrstuvwxyz_";

    private static final String DIC_CLASS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIC_CLASS_NUM = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";

    private static final List<String> keep_name = Arrays.asList("if", "do", "while", "R", "Class", "final", "int", "bool", "String", "enum", "else");


    public static String encodeAsset(String name, String packageName, Map<String, String> dic, List<String> preloadList,String suffix) {
        if (dic.containsKey(name)) {
            return dic.get(name);
        }
        int count = 0;
        do {
            String encodedName = encodeResource(name, packageName, count++);
            encodedName += suffix;
            if (!dic.values().contains(encodedName) && !preloadList.contains(encodedName)) {
                dic.put(name, encodedName);
                return encodedName;
            }
        } while (true);
    }

    public static String encodeResource(String name, String packageName, Map<String, String> dic, List<String> existKeys) {
        if (dic.containsKey(name)) {
            return dic.get(name);
        }
        int count = 0;
        do {
            String encodedName = encodeResource(name, packageName, count++);
            if (!dic.values().contains(encodedName) && (existKeys == null || !existKeys.contains(encodedName))) {
                dic.put(name, encodedName);
                return encodedName;
            }
        } while (true);
    }

    public static String encodeViewId(String name, String packageName, Map<String, String> dic, List<String> existKeys) {
        if (dic.containsKey(name)) {
            return dic.get(name);
        }
        do {
            String encodedName = encodeResource(name, packageName, 4);
            if (!dic.values().contains(encodedName) && (existKeys == null || !existKeys.contains(encodedName))) {
                dic.put(name, encodedName);
                return encodedName;
            }
        } while (true);
    }

    private static String encodeResource(String name, String packageName, int step) {
        int hashCode = Math.abs((name + packageName).hashCode());
        int length = hashCode % DIC_RESOURCE.length();
        StringBuilder sb = new StringBuilder();
        sb.append(DIC_RESOURCE.charAt(length));

        for (int i = 1; i < step; i++) {
            switch (i) {
                case 1:
                    sb.append(DIC_RESOURCE_NUM.charAt(hashCode % DIC_RESOURCE_NUM.length()));
                    break;
                case 2:
                    int hashCode2 = Math.abs((packageName + name).hashCode());
                    int length2 = hashCode2 % DIC_RESOURCE_NUM.length();
                    sb.append(DIC_RESOURCE_NUM.charAt(length2));
                    break;
                default:
                    int random = Random.random(DIC_RESOURCE_NUM.length());
                    sb.append(DIC_RESOURCE_NUM.charAt(random));
                    break;
            }
        }

        String s = sb.toString();
        if (keep_name.contains(s)) {
            return encodeResource(name, packageName, ++step);
        }
        return s;
    }

    public static String encodeClass(String name, String packageName, Map<String, String> dic) {
        if (dic.containsKey(name)) {
            System.out.println("get " + name + "   > " + dic.get(name));
            return dic.get(name);
        }
        int count = 0;
        do {
            String encodedName = encodeClass(name, packageName, count++);
            if (!dic.values().contains(encodedName.toLowerCase()) && !dic.values().contains(encodedName.toUpperCase())) {
                dic.put(name, encodedName);
                return encodedName;
            }
        } while (true);
    }

    private static String encodeClass(String name, String packageName, int step) {
        int hashCode = Math.abs((name + packageName).hashCode());
        int length = hashCode % DIC_CLASS.length();
        StringBuilder sb = new StringBuilder();
        sb.append(DIC_CLASS.charAt(length));

        for (int i = 1; i < step; i++) {
            switch (i) {
                case 1:
                    sb.append(DIC_CLASS_NUM.charAt(hashCode % DIC_CLASS_NUM.length()));
                    break;
                case 2:
                    int hashCode2 = Math.abs((packageName + name).hashCode());
                    int length2 = hashCode2 % DIC_CLASS_NUM.length();
                    sb.append(DIC_CLASS_NUM.charAt(length2));
                    break;
                default:
                    int random = Random.random(DIC_CLASS_NUM.length());
                    sb.append(DIC_CLASS_NUM.charAt(random));
                    break;
            }
        }

        String s = sb.toString();
        if (keep_name.contains(s)) {
            return encodeClass(name, packageName, ++step);
        }
        return s;
    }


}
