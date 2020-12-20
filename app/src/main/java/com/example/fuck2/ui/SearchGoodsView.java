package com.example.fuck2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.fuck2.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class SearchGoodsView extends ConstraintLayout {
    private String title;
    private String desc;
    private String imgUrl;
    private ArrayList<Integer> subGoodsArray;

    public ArrayList<Integer> getSubGoodsArray() {
        return subGoodsArray;
    }

    public void setSubGoodsArray(ArrayList<Integer> subGoodsArray) {
        this.subGoodsArray = subGoodsArray;
    }

    public void itemClick(ArrayList<Integer> subGoodsArray) {

    }

    private TextView titleView;
    private TextView descView;
    private ConstraintLayout mainView;
    private RoundedImageView roundedImageView;

    private Context mContext;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        titleView.setText(title);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
        descView.setText(desc);
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        Glide.with(mContext).load(imgUrl).into(roundedImageView);
    }

    public void loadLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.search_goods_view, this, true);
        titleView = findViewById(R.id.title);
        descView = findViewById(R.id.desc);
        roundedImageView = findViewById(R.id.img);
        mainView = findViewById(R.id.main_view);
        mainView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick(getSubGoodsArray());
            }
        });
        mContext = context;

    }

    public SearchGoodsView(@NonNull Context context) {
        super(context);
        loadLayout(context);
    }

    public SearchGoodsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadLayout(context);
    }

    public SearchGoodsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadLayout(context);
    }

    public SearchGoodsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadLayout(context);
    }
}
