package com.example.fuck2.utils;

import java.util.HashMap;

public class Utils {
    public static String EmptyString = "";

    public static String MapToHttpParam(HashMap<String, String> param) {
        String ret = EmptyString;
        Object[] keys = param.keySet().toArray();
        int len = keys.length;
        for (int i = 0; i < len; i++) {
            System.out.println(keys[i]);
            if (i > 0)
                ret = ret + "&";
            ret = ret + keys[i];
            ret = ret + "=";
            ret = ret + param.get(keys[i]);
        }
        return ret;
    }

    public static void main(String[] args) {
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("a", "1");
//        hashMap.put("b", "2");
//        System.out.println(MapToHttpParam(hashMap));
    }

}
