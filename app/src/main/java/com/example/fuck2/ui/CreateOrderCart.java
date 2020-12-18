package com.example.fuck2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.fuck2.R;

public class CreateOrderCart extends ConstraintLayout {

    private ImageView imageView;
    private TextView priceView;
    private TextView amountView;
    private TextView titleView;
    private float price;
    private int amount;
    private String imageUrl;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        titleView.setText(title);
    }

    public int getSubGoodsId() {
        return subGoodsId;
    }

    public void setSubGoodsId(int subGoodsId) {
        this.subGoodsId = subGoodsId;
    }

    private int subGoodsId;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
        priceView.setText(String.valueOf(price) + "¥" );
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        amountView.setText(" * " + String.valueOf(amount));
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        Glide.with(getContext()).load(imageUrl).into(imageView);
    }

    public void loadLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.create_order_cart, this, true);
        imageView = findViewById(R.id.img);
        priceView = findViewById(R.id.price);
        amountView = findViewById(R.id.amount);
        titleView = findViewById(R.id.title);
    }

    public CreateOrderCart(@NonNull Context context) {
        super(context);
        loadLayout(context);
    }

    public CreateOrderCart(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadLayout(context);
    }

    public CreateOrderCart(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadLayout(context);
    }

    public CreateOrderCart(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadLayout(context);
    }
}
