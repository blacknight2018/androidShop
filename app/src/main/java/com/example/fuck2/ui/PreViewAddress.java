package com.example.fuck2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.fuck2.R;

public class PreViewAddress extends LinearLayout {
    private int addressId;
    private String nick_name, detail, phone;

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getNickName() {
        return nick_name;
    }

    public void setNickName(String nick_name) {
        this.nick_name = nick_name;
        TextView nameView = findViewById(R.id.nick_name);
        nameView.setText(nick_name);
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
        TextView detailView = findViewById(R.id.detail);
        detailView.setText(detail);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        TextView phoneView = findViewById(R.id.phone);
        phoneView.setText(phone);
        this.phone = phone;
    }

    public void itemClick(String nick_name, String phone, String detail) {

    }

    public void editClick(int addressId) {

    }

    public void loadLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.preview_address, this, true);
        ImageView editView = findViewById(R.id.edit);
        LinearLayout linearLayout = findViewById(R.id.item);
        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick(nick_name, phone, detail);
            }
        });
        editView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editClick(addressId);
            }
        });
    }

    public PreViewAddress(Context context) {
        super(context);
        loadLayout(context);
    }

    public PreViewAddress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadLayout(context);
    }

    public PreViewAddress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadLayout(context);
    }

    public PreViewAddress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadLayout(context);
    }
}
