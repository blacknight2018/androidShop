package com.example.fuck2.result;

import java.util.HashMap;

public class Result {
    public enum ErrCode {
        Ok,
        UserNotExit,
        UserExit,
        PassWordNotRight,
        UnKnow,
        GoodsNotFound
    }

    public static HashMap<Integer, String> Msg = new HashMap<>();

    public static String getMsg(int code) {
        Msg.put(ErrCode.Ok.ordinal(), " 成功");
        Msg.put(ErrCode.UserNotExit.ordinal(), "用户不存在");
        Msg.put(ErrCode.UserExit.ordinal(), "用户已存在");
        Msg.put(ErrCode.PassWordNotRight.ordinal(), "密码不正确");
        Msg.put(ErrCode.UnKnow.ordinal(), "未知错误");
        Msg.put(ErrCode.GoodsNotFound.ordinal(), "找不到该商品");
        return Msg.get(code);
    }
}
