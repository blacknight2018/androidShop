package com.example.fuck2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.fuck2.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class PreViewGoods extends LinearLayout {
    private float price;
    private String title;
    private String imageUrl;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
        TextView textView = findViewById(R.id.price);
        textView.setText(String.valueOf(price) + "¥");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        TextView textView = findViewById(R.id.title);
        textView.setText(title);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置商品预览图
     *
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        RoundedImageView imageView = findViewById(R.id.img);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        Glide.with(getContext()).load(imageUrl).into(imageView);
    }

    public PreViewGoods(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.preview_goods, this, true);
    }

    public PreViewGoods(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.preview_goods, this, true);
    }

    public PreViewGoods(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.preview_goods, this, true);
    }

    public PreViewGoods(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.preview_goods, this, true);
    }

}
