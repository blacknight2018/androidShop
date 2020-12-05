package com.example.fuck2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.fuck2.R;

public class preViewGoods extends LinearLayout {
    public preViewGoods(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.preview_goods, this, true);
    }

    public preViewGoods(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.preview_goods, this, true);
    }

    public preViewGoods(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.preview_goods, this, true);
    }

    public preViewGoods(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.preview_goods, this, true);
    }
}
