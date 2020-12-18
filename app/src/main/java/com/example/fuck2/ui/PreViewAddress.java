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
    private ImageView editView;
    private TextView phoneView;
    private TextView detailView;
    private TextView nameView;
    private TextView tipsView;

    public void hiddenEditIcon() {

        editView.setVisibility(GONE);
        LinearLayout linearLayout = findViewById(R.id.info_bar);
        linearLayout.removeView(editView);
    }

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
        nameView.setText(nick_name);
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
        detailView.setText(detail);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        phoneView.setText(phone);
        this.phone = phone;
    }

    public void itemClick(String nick_name, String phone, String detail) {

    }

    public void editClick(int addressId) {

    }

    /**
     * 改变成选择提示
     */
    public void changeToSelectMode(boolean status) {
        if (status) {
            phoneView.setVisibility(INVISIBLE);
            nameView.setVisibility(INVISIBLE);
            detailView.setVisibility(INVISIBLE);
            editView.setVisibility(INVISIBLE);
            tipsView.setVisibility(VISIBLE);

        } else {
            phoneView.setVisibility(VISIBLE);
            nameView.setVisibility(VISIBLE);
            detailView.setVisibility(VISIBLE);
            editView.setVisibility(VISIBLE);
            tipsView.setVisibility(INVISIBLE);
        }
    }

    public void loadLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.preview_address, this, true);
        editView = findViewById(R.id.edit);
        phoneView = findViewById(R.id.phone);
        detailView = findViewById(R.id.detail);
        nameView = findViewById(R.id.nick_name);
        tipsView = findViewById(R.id.tips);
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
        changeToSelectMode(false);
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
