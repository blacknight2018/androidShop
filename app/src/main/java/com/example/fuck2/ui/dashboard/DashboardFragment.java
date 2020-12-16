package com.example.fuck2.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.fuck2.R;
import com.example.fuck2.config.Config;
import com.example.fuck2.ui.CartItem;
import com.example.fuck2.ui.ScrollBottomScrollView;
import com.example.fuck2.utils.ApiThread;
import com.example.fuck2.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardFragment extends Fragment {

    private View root;
    private List<CartItem> cartItemList = new ArrayList<>();
    private LinearLayout linearLayout;
    private ScrollBottomScrollView scrollView;
    private MHandler mHandler;
    private int limit = 6, offset = 0;
    private AppCompatCheckBox allChecked;
    private View.OnClickListener cartStatusChange;


    static private class MHandler extends Handler {
        private final WeakReference<DashboardFragment> weakReference;

        public MHandler(DashboardFragment dashboardFragment) {
            weakReference = new WeakReference<>(dashboardFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            if (0 == code) {
                String body = msg.obj.toString();
                JSONObject jsonObject = JSONObject.parseObject(body);
                if (jsonObject != null) {
                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            String title = item.getString("title");
                            String desc = item.getString("desc");
                            int amount = item.getInteger("amount");
                            String imageUrl = item.getJSONObject("sub_goods").getString("img");
                            weakReference.get().addOneCart(imageUrl, false, desc, title, amount);
                        }
                        weakReference.get().offset += data.size();

                    }
                }
            }
        }
    }

    public void getCart() {
        HashMap<String, String> param = new HashMap<>();
        param.put("limit", String.valueOf(limit));
        param.put("offset", String.valueOf(offset));
        mHandler = new MHandler(DashboardFragment.this);
        new ApiThread(0, mHandler, "get-c", Config.getServerAddress() + "/v1/cart", Utils.MapToHttpParam(param), Config.getCookie()).start();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        linearLayout = root.findViewById(R.id.cart_set);
        allChecked = root.findViewById(R.id.allChecked);
        scrollView = root.findViewById(R.id.scroll);
        scrollView.registerOnScrollViewScrollToBottom(new ScrollBottomScrollView.OnScrollBottomListener() {
            @Override
            public void scrollToBottom() {
                System.out.println("Fuck");
                getCart();
            }
        });
        allChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < cartItemList.size(); i++) {
                    cartItemList.get(i).setChecked(allChecked.isChecked());
                }
            }
        });
        cartStatusChange = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatCheckBox checkBox = (AppCompatCheckBox) v;
                System.out.println(checkBox.isChecked());
                if (!checkBox.isChecked()) {
                    allChecked.setChecked(false);
                }
            }
        };
        getCart();
        return root;
    }

    private void addOneCart(String imageUrl, boolean checked, String desc, String title, int amount) {
        CartItem cartItem = new CartItem(getContext());
        cartItem.setImageUrl(imageUrl);
        cartItem.setChecked(checked);
        cartItem.setDesc(desc);
        cartItem.setTitle(title);
        cartItem.setAmount(amount);
        cartItem.setStatusChangeListener(cartStatusChange);
        cartItemList.add(cartItem);
        linearLayout.addView(cartItem);
    }
}