package com.example.fuck2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.fuck2.MyOrderActivity;
import com.example.fuck2.R;
import com.example.fuck2.config.Config;
import com.example.fuck2.result.Result;
import com.google.android.flexbox.FlexboxLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class OrderItem extends LinearLayout {
    private TextView titleView;
    private TextView amountView;
    private TextView timeView;
    private TextView priceView;
    private Button payNow;
    private Button confirmDelivery;
    private String title;
    private int amount;
    private int orderId;
    private String time;
    private FlexboxLayout btnGroup;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        if (status == Result.Status.UnPay.ordinal()) {
            payNow.setVisibility(VISIBLE);
        } else if (status == Result.Status.Delivery.ordinal()) {
            confirmDelivery.setVisibility(VISIBLE);
        }
    }

    private int status;

    private Context mContext;

    public ArrayList<String> getImg() {
        return img;
    }

    public void setImg(ArrayList<String> img) {
        this.img = img;
        RoundedImageView img1 = findViewById(R.id.img1);
        if (img.size() >= 1) {
            Glide.with(mContext).load(img.get(0)).into(img1);
        } else {
            img1.setBackgroundResource(0);
        }
        RoundedImageView img2 = findViewById(R.id.img2);
        if (img.size() >= 2) {
            Glide.with(mContext).load(img.get(1)).into(img2);
        } else {
            img1.setBackgroundResource(0);
        }
        RoundedImageView img3 = findViewById(R.id.img3);
        if (img.size() >= 3) {
            Glide.with(mContext).load(img.get(2)).into(img3);
        } else {
            img1.setBackgroundResource(0);
        }
    }

    private float price;
    private ArrayList<String> img;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        titleView.setText(title);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        amountView.setText("共计" + amount + "件商品");
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        timeView.setText(time);
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
        priceView.setText(price + "¥");
    }

    public void loadLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.activity_order_item, this, true);
        titleView = findViewById(R.id.title);
        amountView = findViewById(R.id.amount);
        timeView = findViewById(R.id.time);
        priceView = findViewById(R.id.price);
        payNow = findViewById(R.id.btn_pay);
        confirmDelivery = findViewById(R.id.btn_confirm);
        btnGroup = findViewById(R.id.btn_group);

        payNow.setVisibility(GONE);
        confirmDelivery.setVisibility(GONE);

        payNow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPay();
            }
        });

        confirmDelivery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirm();
            }
        });

        mContext = context;
    }

    public void onPay() {

    }

    public void onConfirm() {

    }

    public OrderItem(@NonNull Context context) {
        super(context);
        loadLayout(context);
    }

    public OrderItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadLayout(context);
    }

    public OrderItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadLayout(context);
    }

    public OrderItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadLayout(context);
    }
}
