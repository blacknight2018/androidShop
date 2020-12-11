package com.example.fuck2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.fuck2.R;

public class PreViewAddress extends LinearLayout {
    public PreViewAddress(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.preview_address, this, true);
    }

    public PreViewAddress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.preview_address, this, true);
    }

    public PreViewAddress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.preview_address, this, true);
    }

    public PreViewAddress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.preview_address, this, true);
    }
}
