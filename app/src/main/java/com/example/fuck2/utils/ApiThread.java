package com.example.fuck2.utils;

import android.os.Handler;
import android.os.Message;

public class ApiThread extends Thread {
    private int code;
    private Handler handler;
    private String method;
    private String url;
    private String param;
    private String cookie;

    public ApiThread(int code, Handler handler, String method, String url, String param, String cookie) {
        this.code = code;
        this.handler = handler;
        this.method = method;
        this.url = url;
        this.param = param;
        this.cookie = cookie;
    }


    public ApiThread(int code, Handler handler, String method, String url, String param) {
        this.code = code;
        this.handler = handler;
        this.method = method;
        this.url = url;
        this.param = param;
    }

    @Override
    public void run() {
        super.run();
        String ret = null;
        Message message = new Message();
        message.what = code;
        if (method.toLowerCase().equals("get")) {
            ret = HttpRequest.sendGet(url, param);
        } else if (method.toLowerCase().equals("post")) {
            ret = HttpRequest.sendPost(url, param);
        } else if (method.toLowerCase().equals("get-c")) {
            ret = HttpRequest.sendGetWithCookie(url, param, cookie);
        } else if (method.toLowerCase().equals("post-c")) {
            ret = HttpRequest.sendPostWithCookie(url, param, cookie);
        } else if (method.toLowerCase().equals("post2")) {
            message.obj = HttpRequest.sendPostWithMultiRes(url, param);
            handler.sendMessage(message);
            return;
        } else if (method.toLowerCase().equals("delete-c")) {
            message.obj = HttpRequest.doDelete(url, param, cookie);
            handler.sendMessage(message);
            return;
        } else if (method.toLowerCase().equals("put-c")) {
            message.obj = HttpRequest.sendPut(url, param, cookie);
            handler.sendMessage(message);
            return;
        }
        message.obj = ret;
        handler.sendMessage(message);

    }


}
