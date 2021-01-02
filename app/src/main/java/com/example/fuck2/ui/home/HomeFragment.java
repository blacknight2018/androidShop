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
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.fuck2.GoodsDetail;
import com.example.fuck2.R;
import com.example.fuck2.SearchActivity;
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
    private SearchView searchView;
    private List<String> bannerUrl = new ArrayList<>();
    private List<Integer> bannerSubGoodsId = new ArrayList<>();


    static private class MHandler extends Handler {
        private final WeakReference<HomeFragment> homeFragmentWeakReference;

        public MHandler(HomeFragment homeFragment) {
            homeFragmentWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            String body = msg.obj.toString();

            if (code == 0 || code == 1) {
                List<PreViewGoods> obj = null;
                JSONObject bodyObject = JSONObject.parseObject(body);
                if (bodyObject != null) {
                    JSONArray data = bodyObject.getJSONArray("data");
                    if (code == 0) {
                        obj = homeFragmentWeakReference.get().hot;
                    } else {
                        obj = homeFragmentWeakReference.get().newest;
                    }
                    for (int i = 0; data != null && i < obj.size(); i++) {
                        obj.get(i).setTitle(data.getJSONObject(i).getString("title"));
                        obj.get(i).setPrice(data.getJSONObject(i).getFloat("price"));
                        obj.get(i).setImageUrl(data.getJSONObject(i).getString("img"));
                        obj.get(i).setSubGoodsId(data.getJSONObject(i).getIntValue("id"));
                    }
                }
            } else if (code == 2) {
                JSONObject bodyObject = JSONObject.parseObject(body);
                if (bodyObject != null) {
                    JSONArray arrayData = bodyObject.getJSONArray("data");
                    homeFragmentWeakReference.get().bannerUrl.clear();
                    homeFragmentWeakReference.get().bannerSubGoodsId.clear();
                    for (int i = 0; arrayData != null && i < arrayData.size(); i++) {
                        String img = arrayData.getJSONObject(i).getString("img");
                        Integer subGoodsId = arrayData.getJSONObject(i).getIntValue("sub_goods_id");
                        homeFragmentWeakReference.get().bannerUrl.add(img);
                        homeFragmentWeakReference.get().bannerSubGoodsId.add(subGoodsId);
                        homeFragmentWeakReference.get().LoadBannerImg();
                    }
                }
            } else if (code == 3) {
                JSONObject bodyObject = JSONObject.parseObject(body);
                if (bodyObject != null) {
                    int userId = bodyObject.getIntValue("data");
                    if (userId == 0) {
                        Utils.WritePreferences(homeFragmentWeakReference.get().getContext(), "Cookie", Utils.EmptyString);
                        homeFragmentWeakReference.get().getActivity().finish();
                    } else {
                        homeFragmentWeakReference.get().loadView();
                    }
                }
            }

        }
    }

    /**
     * 用于校验Token后加载图片,防止加载时由于Token无效销毁Activity后还在利用Context加载图片
     */
    void loadView() {
        new ApiThread(2, mHandler, "get", Config.getServerAddress() + "/v1/home/banner", Utils.EmptyString).start();
        this.LoadBannerImg();
        this.LoadHot();
        this.LoadNewest();
    }

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        banner = root.findViewById(R.id.banner);
        searchView = root.findViewById(R.id.search);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                if (position <= bannerSubGoodsId.size() - 1) {
                    Intent intent = new Intent(getContext(), GoodsDetail.class);
                    intent.putExtra("sub_goods_id", bannerSubGoodsId.get(position));
                    startActivity(intent);
                }

            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        searchView.onActionViewCollapsed();
        mHandler = new MHandler(this);
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
        for (int i = 0; i < hot.size(); i++) {
            final int finalI = i;
            hot.get(i).setClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), GoodsDetail.class);
                    intent.putExtra("sub_goods_id", hot.get(finalI).getSubGoodsId());
                    startActivity(intent);
                }
            });
        }

        new ApiThread(0, mHandler, "get", Config.getServerAddress() + "/v1/home/hot", Utils.EmptyString).start();
    }

    private void LoadNewest() {
        newest.clear();
        newest.add((PreViewGoods) root.findViewById(R.id.newest_1));
        newest.add((PreViewGoods) root.findViewById(R.id.newest_2));
        newest.add((PreViewGoods) root.findViewById(R.id.newest_3));
        newest.add((PreViewGoods) root.findViewById(R.id.newest_4));
        newest.add((PreViewGoods) root.findViewById(R.id.newest_5));
        newest.add((PreViewGoods) root.findViewById(R.id.newest_6));
        for (int i = 0; i < newest.size(); i++) {
            final int finalI = i;
            newest.get(i).setClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), GoodsDetail.class);
                    intent.putExtra("sub_goods_id", newest.get(finalI).getSubGoodsId());
                    startActivity(intent);
                }
            });
        }
        new ApiThread(1, mHandler, "get", Config.getServerAddress() + "/v1/home/newest", Utils.EmptyString).start();
    }

    private void LoadBannerImg() {
        ArrayList<String> title = new ArrayList<>();
        for (int i = 0; i < bannerUrl.size(); i++) {
            title.add(Utils.EmptyString);
        }
        if (bannerUrl.isEmpty()) {
            bannerUrl.add(Utils.EmptyString);
            title.add(Utils.EmptyString);
        }
        banner.setImages(bannerUrl);
        banner.setImageLoader(new ImageLoadBanner());
        banner.setBannerTitles(title);
        banner.setDelayTime(5500);
        banner.isAutoPlay(true);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setBannerAnimation(Transformer.Accordion);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.start();
    }

    static class ImageLoadBanner extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            if (path.toString().length() == 0) {
                imageView.setImageResource(R.drawable.logo);
                return;
            }
            Glide.with(context).load(path).into(imageView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        new ApiThread(3, mHandler, "get-c", Config.getServerAddress() + "/v1/user/valid", Utils.EmptyString, Config.getCookie()).start();
    }
}