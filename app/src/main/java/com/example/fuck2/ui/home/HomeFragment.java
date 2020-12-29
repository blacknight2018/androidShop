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

    static private class MHandler extends Handler {
        private final WeakReference<HomeFragment> homeFragmentWeakReference;

        public MHandler(HomeFragment homeFragment) {
            homeFragmentWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            if (code == 0 || code == 1) {
                String body = msg.obj.toString();
                JSONArray data = JSONObject.parseObject(body).getJSONArray("data");
                List<PreViewGoods> obj = null;
                if (code == 0) {
                    obj = homeFragmentWeakReference.get().hot;
                } else {
                    obj = homeFragmentWeakReference.get().newest;
                }
                for (int i = 0; i < obj.size(); i++) {
                    obj.get(i).setTitle(data.getJSONObject(i).getString("title"));
                    obj.get(i).setPrice(data.getJSONObject(i).getFloat("price"));
                    obj.get(i).setImageUrl(data.getJSONObject(i).getString("img"));
                    obj.get(i).setSubGoodsId(data.getJSONObject(i).getIntValue("id"));
                }
            }

        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        banner = root.findViewById(R.id.banner);
        searchView = root.findViewById(R.id.search);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(getContext(), GoodsDetail.class);
                startActivity(intent);
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
        this.LoadBannerImg();
        this.LoadHot();
        this.LoadNewest();
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
        ArrayList<Integer> img = new ArrayList<>();
        img.add(R.drawable.goods_1);
        img.add(R.drawable.goods_2);
        img.add(R.drawable.goods_3);

        ArrayList<String> title = new ArrayList<>();
        title.add("");
        title.add("");
        title.add("");

        banner.setImages(img);
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
            imageView.setImageResource(Integer.parseInt(path.toString()));
        }
    }
}