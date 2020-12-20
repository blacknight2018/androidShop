package com.example.fuck2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.fuck2.config.Config;
import com.example.fuck2.ui.ScrollBottomScrollView;
import com.example.fuck2.ui.SearchGoodsView;
import com.example.fuck2.utils.ApiThread;
import com.example.fuck2.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private ProgressDialog waitDialog;
    private MHandler mHandler;
    private final int maxLimit = 7;
    private int limit = maxLimit, offset = 0;
    private LinearLayout resultLayout;
    private List<SearchGoodsView> searchGoodsViewList;
    private TextView foundTipView;
    private ScrollBottomScrollView scrollBottomScrollView;

    private class MHandler extends Handler {
        private WeakReference<SearchActivity> weakReference;

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String responseBody = msg.obj.toString();
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                if (jsonObject != null) {
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    for (int i = 0; null != dataArray && i < dataArray.size(); i++) {
                        JSONObject goods = dataArray.getJSONObject(i);
                        String title = goods.getString("title");
                        String desc = goods.getString("desc");
                        JSONArray bannerJsonArray = goods.getJSONArray("banner");
                        JSONArray subGoodsJsonArray = goods.getJSONArray("sub_goods");
                        ArrayList<Integer> subGoodsArray = new ArrayList<>();
                        for (int j = 0; subGoodsJsonArray != null && j < subGoodsJsonArray.size(); j++) {
                            subGoodsArray.add(subGoodsJsonArray.getIntValue(j));
                        }
                        String imgUrl = Utils.EmptyString;
                        if (bannerJsonArray.size() > 0) {
                            imgUrl = bannerJsonArray.getString(0);
                        }
                        weakReference.get().addGoods(title, desc, imgUrl, subGoodsArray);
                    }
                    if (dataArray != null) {
                        offset += dataArray.size();
                    }
                }
            }
            if (weakReference.get().searchGoodsViewList.size() == 0) {
                weakReference.get().showNotFound(true);
            } else {
                weakReference.get().showNotFound(false);
            }
            waitDialog.cancel();
        }

        public MHandler(SearchActivity searchActivity) {
            weakReference = new WeakReference<>(searchActivity);
        }
    }

    public void addGoods(String title, String desc, String imgUrl, ArrayList<Integer> subGoodsArray) {
        SearchGoodsView searchGoodsView = new SearchGoodsView(SearchActivity.this) {
            @Override
            public void itemClick(ArrayList<Integer> subGoodsArray) {
                super.itemClick(subGoodsArray);
                if (subGoodsArray.size() > 0) {
                    Intent intent = new Intent(SearchActivity.this, GoodsDetail.class);
                    intent.putExtra("sub_goods_id", subGoodsArray.get(0));
                    startActivity(intent);
                }

            }
        };
        searchGoodsView.setDesc(desc);
        searchGoodsView.setTitle(title);
        searchGoodsView.setImgUrl(imgUrl);
        searchGoodsView.setSubGoodsArray(subGoodsArray);
        searchGoodsViewList.add(searchGoodsView);
        resultLayout.addView(searchGoodsView);

    }

    private void showNotFound(boolean display) {
        if (display)
            foundTipView.setVisibility(View.VISIBLE);
        else
            foundTipView.setVisibility(View.GONE);
    }

    private void clear() {
        resultLayout.removeAllViews();
        searchGoodsViewList.clear();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mHandler = new MHandler(SearchActivity.this);
        waitDialog = new ProgressDialog(this);
        searchGoodsViewList = new ArrayList<>();
        searchView = findViewById(R.id.search);
        resultLayout = findViewById(R.id.result);
        foundTipView = findViewById(R.id.found_tips);
        scrollBottomScrollView = findViewById(R.id.scroll);
        searchView.setSubmitButtonEnabled(true);
        searchView.setActivated(true);
        searchView.setIconified(false);
        scrollBottomScrollView.registerOnScrollViewScrollToBottom(new ScrollBottomScrollView.OnScrollBottomListener() {
            @Override
            public void scrollToBottom() {
                HashMap<String, String> param = new HashMap<>();
                param.put("key", searchView.getQuery().toString());
                param.put("limit", String.valueOf(limit));
                param.put("offset", String.valueOf(offset));
                new ApiThread(0, mHandler, "get", Config.getServerAddress() + "/v1/goods/search", Utils.MapToHttpParam(param)).start();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                waitDialog.setTitle("加载中");
                waitDialog.setMessage("搜索中");
                waitDialog.create();
                waitDialog.show();
                limit = maxLimit;
                offset = 0;
                HashMap<String, String> param = new HashMap<>();
                param.put("key", query);
                param.put("limit", String.valueOf(limit));
                param.put("offset", String.valueOf(offset));
                clear();
                new ApiThread(0, mHandler, "get", Config.getServerAddress() + "/v1/goods/search", Utils.MapToHttpParam(param)).start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


}