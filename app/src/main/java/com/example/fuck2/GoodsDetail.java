package com.example.fuck2;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.fuck2.config.Config;
import com.example.fuck2.utils.ApiThread;
import com.example.fuck2.utils.Utils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.HashMap;
import java.util.List;

public class GoodsDetail extends AppCompatActivity {
    private Banner banner;
    private int subGoodsId = 3;
    private GoodsDetail.MHandler mHandler = new GoodsDetail.MHandler();
    private TextView titleTextView, descTextView;

    private class MHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            if (code == 0) {
                String body = msg.obj.toString();
                String title = JSONObject.parseObject(body).getJSONObject("data").getString("title");
                String desc = JSONObject.parseObject(body).getJSONObject("data").getString("desc");
                String imgs = JSONObject.parseObject(body).getJSONObject("data").getString("banner");
                String detailImgs = JSONObject.parseObject(body).getJSONObject("data").getString("detail_img");
                titleTextView.setText(title);
                descTextView.setText(desc);
                LoadBannerImg(Utils.ParseJSONString(imgs));
                LoadDetailImg(Utils.ParseJSONString(detailImgs));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        banner = findViewById(R.id.banner);


        //LoadBannerImg();
        titleTextView = findViewById(R.id.title);
        descTextView = findViewById(R.id.desc);

        RelativeLayout textView = findViewById(R.id.select_spec);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectSpecPopWin selectSpecPopWin = new SelectSpecPopWin(GoodsDetail.this, onClickListener);
                selectSpecPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.CENTER, 0, 0);
            }
        });


        //
        HashMap<String, String> param = new HashMap<>();
        param.put("sub_goods_id", String.valueOf(subGoodsId));
        new ApiThread(0, mHandler, "get", Config.getServerAddress() + "/v1/goods", Utils.MapToHttpParam(param)).start();
    }

    private void LoadDetailImg(List<String> imgPathList) {
        LinearLayout imgSet = findViewById(R.id.img_set);

        for (int i = 0; i < imgPathList.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            ViewGroup.LayoutParams imgParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Glide.with(getApplicationContext()).load(imgPathList.get(i)).into(imageView);
            imageView.setLayoutParams(imgParam);
            imgSet.addView(imageView);
        }

        //LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imgSet.getLayoutParams();
        //imageView.setLayoutParams(layoutParams);
    }

    private void LoadBannerImg(List<String> imgPathList) {
//        List<String> imgList = new ArrayList<>();
//        imgList.add("https://gd4.alicdn.com/imgextra/i2/921553090/O1CN01L2OtH71YhGQu8fcBD_!!921553090.jpg");
//        imgList.add("https://gd4.alicdn.com/imgextra/i2/921553090/O1CN01L2OtH71YhGQu8fcBD_!!921553090.jpg");
//        imgList.add("https://gd4.alicdn.com/imgextra/i2/921553090/O1CN01L2OtH71YhGQu8fcBD_!!921553090.jpg");

        banner.setImages(imgPathList);
        banner.setImageLoader(new GoodsDetail.ImageLoadBanner());
        banner.setDelayTime(5500);
        banner.isAutoPlay(true);

        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setBannerAnimation(Transformer.Accordion);
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
    }

    class ImageLoadBanner extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path.toString()).into(imageView);
        }
    }
}
