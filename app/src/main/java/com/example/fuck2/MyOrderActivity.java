package com.example.fuck2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.fuck2.config.Config;
import com.example.fuck2.result.Result;
import com.example.fuck2.ui.OrderItem;
import com.example.fuck2.ui.ScrollBottomScrollView;
import com.example.fuck2.utils.ApiThread;
import com.example.fuck2.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class MyOrderActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private final int maxLimit = 5;
    private int limit = maxLimit, offset;
    private int status;
    private LinearLayout orderSet;
    private ScrollBottomScrollView scrollView;
    private ProgressDialog waitDialog;
    private ArrayList<OrderItem> orderItemList;
    private MHandler mHandler;


    private class MHandler extends Handler {
        private WeakReference<MyOrderActivity> weakReference;

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (0 == msg.what) {
                String responseData = msg.obj.toString();
                JSONObject jsonObject = JSONObject.parseObject(responseData);
                if (jsonObject != null) {
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    for (int i = 0; dataArray != null && i < dataArray.size(); i++) {
                        JSONObject itemObject = dataArray.getJSONObject(i);
                        String CreateTime = itemObject.getString("create_time");
                        float price = itemObject.getFloatValue("total_price");
                        JSONArray subGoodsArray = itemObject.getJSONArray("sub_goods");
                        JSONArray imgArray = itemObject.getJSONArray("img");
                        int status = itemObject.getIntValue("status");
                        int orderId = itemObject.getIntValue("id");
                        ArrayList<String> imgList = new ArrayList<>();
                        for (int j = 0; imgArray != null && !imgArray.isEmpty() && j < imgArray.size(); j++) {
                            imgList.add(imgArray.getString(j));
                        }
                        weakReference.get().offset++;
                        weakReference.get().addOrder(orderId, CreateTime, subGoodsArray.size(), price, imgList, status);
                    }
                }
            } else if (1 == msg.what) {
                String responseData = msg.obj.toString();
                JSONObject jsonObject = JSONObject.parseObject(responseData);
                if (jsonObject != null && jsonObject.getIntValue("code") == 0) {
                    clear();
                    request();
                }
            }
            waitDialog.cancel();
        }

        public MHandler(MyOrderActivity myOrderActivity) {
            weakReference = new WeakReference<>(myOrderActivity);
        }
    }

    private void clear() {
        offset = 0;
        limit = maxLimit;
        orderItemList.clear();
        orderSet.removeAllViews();
    }

    private void addOrder(final int orderId, String time, int amount, float price, ArrayList<String> img, int status) {
        OrderItem orderItem = new OrderItem(MyOrderActivity.this) {
            @Override
            public void onPay() {
                super.onPay();
                HashMap<String, String> param = new HashMap<>();
                param.put("order_id", String.valueOf(orderId));
                new ApiThread(1, mHandler, "post-c", Config.getServerAddress() + "/v1/order/pay", Utils.MapToHttpParam(param), Config.getCookie()).start();

            }

            @Override
            public void onConfirm() {
                super.onConfirm();

            }
        };
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 10);
        orderItem.setLayoutParams(params);
        orderItem.setTime(time);
        orderItem.setAmount(amount);
        orderItem.setPrice(price);
        orderItem.setImg(img);
        orderItem.setStatus(status);
        orderItem.setOrderId(orderId);
        orderItemList.add(orderItem);
        orderSet.addView(orderItem);
    }

    private void request() {

        waitDialog.setTitle("加载中");
        waitDialog.setMessage("加载订单中");
        waitDialog.create();
        waitDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put("limit", String.valueOf(limit));
        param.put("offset", String.valueOf(offset));
        param.put("status", String.valueOf(status));
        new ApiThread(0, mHandler, "get-c", Config.getServerAddress() + "/v1/order", Utils.MapToHttpParam(param), Config.getCookie()).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        scrollView = findViewById(R.id.scroll);
        orderSet = findViewById(R.id.order_set);

        RadioButton radioButton1 = findViewById(R.id.btn_1);
        RadioButton radioButton2 = findViewById(R.id.btn_2);
        RadioButton radioButton3 = findViewById(R.id.btn_3);
        RadioButton radioButton4 = findViewById(R.id.btn_4);
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = Result.Status.All.ordinal();
                clear();
                request();
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = Result.Status.UnPay.ordinal();
                clear();
                request();
            }
        });
        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = Result.Status.Pay.ordinal();
                clear();
                request();
            }
        });
        radioButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = Result.Status.Delivery.ordinal();
                clear();
                request();
            }
        });

        scrollView.registerOnScrollViewScrollToBottom(new ScrollBottomScrollView.OnScrollBottomListener() {
            @Override
            public void scrollToBottom() {
                request();
            }
        });
        orderItemList = new ArrayList<>();
        waitDialog = new ProgressDialog(MyOrderActivity.this);
        mHandler = new MHandler(MyOrderActivity.this);

        status = Result.Status.All.ordinal();
        clear();
        request();
    }
}