package com.example.fuck2.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.fuck2.CreateOrder;
import com.example.fuck2.R;
import com.example.fuck2.config.Config;
import com.example.fuck2.result.Result;
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
    private TextView totalPriceTextView;
    private TextView submitView;
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
                            int cartId = item.getInteger("id");
                            int amount = item.getInteger("amount");
                            float price = item.getJSONObject("sub_goods").getFloat("price");
                            String imageUrl = item.getJSONObject("sub_goods").getString("img");

                            weakReference.get().addOneCart(cartId, imageUrl, false, desc, title, amount, price);
                        }
                        weakReference.get().offset += data.size();

                    }
                }
            } else if (1 == msg.what) {
                String body = msg.obj.toString();
                JSONObject jsonObject = JSONObject.parseObject(body);
                if (jsonObject != null && Result.ErrCode.Ok.ordinal() == jsonObject.getInteger("code")) {
                    Toast.makeText(weakReference.get().getContext(), "移除成功", Toast.LENGTH_SHORT).show();
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

    public void calcTotalPrice() {
        float total = 0;
        for (int i = 0; i < cartItemList.size(); i++) {
            if (cartItemList.get(i).isChecked()) {
                total += (cartItemList.get(i).getAmount() * cartItemList.get(i).getPrice());
            }
        }
        totalPriceTextView.setText("总价" + total + "¥");
    }

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        linearLayout = root.findViewById(R.id.cart_set);
        allChecked = root.findViewById(R.id.allChecked);
        scrollView = root.findViewById(R.id.scroll);
        submitView = root.findViewById(R.id.submit);
        totalPriceTextView = root.findViewById(R.id.total_price);
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
                calcTotalPrice();

            }
        });
        submitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Integer> arrayList = new ArrayList<>();
                for (int i = 0; i < cartItemList.size(); i++) {
                    if (cartItemList.get(i).isChecked()) {
                        arrayList.add(cartItemList.get(i).getCartId());
                    }
                }
                int[] cartSet = new int[arrayList.size()];
                for (int i = 0; i < cartSet.length; i++) {
                    cartSet[i] = arrayList.get(i);
                }

//              HashMap<String, String> param = new HashMap<>();
//              System.out.println(JSONArray.toJSONString(param));
//              param.put("cart_id", JSONArray.toJSONString(param));

                Intent intent = new Intent(getContext(), CreateOrder.class);
                intent.putExtra("cart_id", JSONArray.toJSONString(cartSet));
                startActivity(intent);
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
                calcTotalPrice();
            }
        };

        getCart();
        return root;
    }

    private void removeCartViewByCartId(int cartId) {
        for (int i = 0; i < cartItemList.size(); i++) {
            if (cartItemList.get(i).getCartId() == cartId) {
                linearLayout.removeViewAt(i);
                cartItemList.remove(i);
            }
        }
    }

    private void addOneCart(final int cartId, String imageUrl, boolean checked, String desc, String title, int amount, float price) {
        CartItem cartItem = new CartItem(getContext()) {
            @Override
            public void plusAmount() {
                super.plusAmount();
                setAmount(getAmount() + 1);
            }

            @Override
            public void minusAmount() {
                super.minusAmount();
                if (getAmount() == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("提示");
                    builder.setMessage("确定要删除该商品吗?");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("Mother Fuck");
                            HashMap<String, String> param = new HashMap<>();
                            param.put("cart_id", String.valueOf(getCartId()));
                            removeCartViewByCartId(cartId);
                            new ApiThread(1, mHandler, "delete-c", Config.getServerAddress() + "/v1/cart", Utils.MapToHttpParam(param), Config.getCookie()).start();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                } else {
                    setAmount(getAmount() - 1);
                }
            }
        };
        cartItem.setCartId(cartId);
        cartItem.setImageUrl(imageUrl);
        cartItem.setChecked(checked);
        cartItem.setDesc(desc);
        cartItem.setTitle(title);
        cartItem.setAmount(amount);
        cartItem.setStatusChangeListener(cartStatusChange);
        cartItem.setPrice(price);
        cartItemList.add(cartItem);
        linearLayout.addView(cartItem);
    }
}