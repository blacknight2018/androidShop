package com.example.fuck2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.fuck2.R;

public class CartItem extends ConstraintLayout {

    private int cartId;
    private String title;
    private String desc;
    private String imageUrl;
    private float price;
    private ImageView plusImageView, minusImageView;

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    private int amount;

    public int getAmount() {
        return amount;
    }

    public boolean isChecked() {
        CheckBox checkBox = findViewById(R.id.select);
        return checkBox.isChecked();
    }

    public void setAmount(int amount) {
        this.amount = amount;
        TextView textView = findViewById(R.id.amount);
        textView.setText(String.valueOf(amount));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        TextView textView = findViewById(R.id.title);
        textView.setText(title);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
        TextView descView = findViewById(R.id.desc);
        descView.setText(desc);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        Glide.with(getContext()).load(imageUrl).into(imageView);
    }

    public void plusAmount() {

    }

    public void minusAmount() {

    }

    private void loadLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.cart_item, this, true);
        plusImageView = findViewById(R.id.plus);
        minusImageView = findViewById(R.id.minus);
        plusImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                plusAmount();

            }
        });
        minusImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                minusAmount();
            }
        });
    }

    public void setStatusChangeListener(OnClickListener listener) {
        CheckBox checkBox = findViewById(R.id.select);
        checkBox.setOnClickListener(listener);
    }

    public void setChecked(boolean checked) {
        CheckBox checkBox = findViewById(R.id.select);
        checkBox.setChecked(checked);

    }

    public CartItem(Context context) {
        super(context);
        loadLayout(context);
    }

    public CartItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadLayout(context);
    }

    public CartItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadLayout(context);
    }

    public CartItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadLayout(context);
    }
}
