package com.example.fuck2;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class GoodsDetail extends AppCompatActivity {
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        banner = findViewById(R.id.banner);
        LoadBannerImg();


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
    }

    private void LoadBannerImg() {
        List<String> imgList = new ArrayList<>();
        imgList.add("https://gd4.alicdn.com/imgextra/i2/921553090/O1CN01L2OtH71YhGQu8fcBD_!!921553090.jpg");
        imgList.add("https://gd4.alicdn.com/imgextra/i2/921553090/O1CN01L2OtH71YhGQu8fcBD_!!921553090.jpg");
        imgList.add("https://gd4.alicdn.com/imgextra/i2/921553090/O1CN01L2OtH71YhGQu8fcBD_!!921553090.jpg");

        banner.setImages(imgList);
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
            //imageView.setImageResource(Integer.parseInt(path.toString()));
            //imageView.setImageURI(Uri.parse(path.toString()));
            Glide.with(context).load(path.toString()).into(imageView);
        }
    }
}
