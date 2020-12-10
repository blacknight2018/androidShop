package com.example.fuck2.utils;

import android.os.Handler;
import android.os.Message;

public class ApiThread extends Thread {
    private int code;
    private Handler handler;
    private String method;
    private String url;
    private String param;

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
        }
        message.obj = ret;
        handler.sendMessage(message);

    }


}
