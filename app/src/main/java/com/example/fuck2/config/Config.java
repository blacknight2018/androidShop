package com.example.fuck2.config;

public class Config {
    public static String Cookie;

    public static String getCookie() {
        return Cookie;
    }

    public static void setCookie(String cookie) {
        Cookie = cookie;
    }

    public static String getServerAddress() {
        return "http://10.51.184.211:7777";
    }
}
