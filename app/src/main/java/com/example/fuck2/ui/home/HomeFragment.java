package com.example.fuck2.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.fuck2.GoodsDetail;
import com.example.fuck2.R;
import com.example.fuck2.config.Config;
import com.example.fuck2.ui.PreViewGoods;
import com.example.fuck2.utils.ApiThread;
import com.example.fuck2.utils.Utils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private View root;
    private Banner banner;
    private List<PreViewGoods> hot = new ArrayList<>();
    private List<PreViewGoods> newest = new ArrayList<>();
    private HomeFragment.MHandler mHandler;

    static private class MHandler extends Handler {
        private final WeakReference<HomeFragment> homeFragmentWeakReference;

        public MHandler(HomeFragment homeFragment) {
            homeFragmentWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            if (code == 0) {
                String body = msg.obj.toString();
                JSONArray hotSubGoods = JSONObject.parseObject(body).getJSONArray("data");
                for (int i = 0; i < hotSubGoods.size(); i++) {
                    homeFragmentWeakReference.get().hot.get(i).setTitle(hotSubGoods.getJSONObject(i).getString("title"));
                    homeFragmentWeakReference.get().hot.get(i).setPrice(hotSubGoods.getJSONObject(i).getFloat("price"));
                    homeFragmentWeakReference.get().hot.get(i).setImageUrl(hotSubGoods.getJSONObject(i).getString("img"));
                }
            }

        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        banner = root.findViewById(R.id.banner);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(getContext(), GoodsDetail.class);
                startActivity(intent);
            }
        });
        mHandler = new MHandler(this);
        this.LoadBannerImg();
        this.LoadHot();
        return root;
    }

    private void LoadHot() {
        hot.clear();
        hot.add((PreViewGoods) root.findViewById(R.id.hot_1));
        hot.add((PreViewGoods) root.findViewById(R.id.hot_2));
        hot.add((PreViewGoods) root.findViewById(R.id.hot_3));
        hot.add((PreViewGoods) root.findViewById(R.id.hot_4));
        hot.add((PreViewGoods) root.findViewById(R.id.hot_5));
        hot.add((PreViewGoods) root.findViewById(R.id.hot_6));
        new ApiThread(0, mHandler, "get", Config.getServerAddress() + "/v1/home/hot", Utils.EmptyString).start();
    }

    private void LoadBannerImg() {
        ArrayList<Integer> img = new ArrayList<>();
        img.add(R.drawable.goods_1);
        img.add(R.drawable.goods_2);
        img.add(R.drawable.goods_3);

        ArrayList<String> title = new ArrayList<>();
        title.add("奥利奥");
        title.add("趣多多");
        title.add("好多鱼");

        banner.setImages(img);
        banner.setImageLoader(new ImageLoadBanner());
        banner.setBannerTitles(title);
        banner.setDelayTime(5500);
        banner.isAutoPlay(true);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setBannerAnimation(Transformer.Accordion);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.start();
    }

    static class ImageLoadBanner extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setImageResource(Integer.parseInt(path.toString()));
        }
    }
}