package com.example.fuck2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.fuck2.config.Config;
import com.example.fuck2.ui.CreateOrderCart;
import com.example.fuck2.ui.PreViewAddress;
import com.example.fuck2.utils.ApiThread;
import com.example.fuck2.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class CreateOrder extends AppCompatActivity {
    private LinearLayout linearLayout, addressLinearLayout;
    private PreViewAddress preViewAddress;
    private CreateOrderCart createOrderCart;
    private TextView totalPriceView;
    private MHandler mHandler;
    private String cartIdJson;

    private class MHandler extends Handler {
        private WeakReference<CreateOrder> weakReference;

        public MHandler(CreateOrder createOrder) {
            weakReference = new WeakReference<CreateOrder>(createOrder);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (0 == msg.what) {
                String response = (String) msg.obj;
                System.out.println(response);
                JSONObject jsonObject = JSONObject.parseObject(response);
                if (jsonObject != null) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    float totalPrice = data.getFloatValue("total_price");
                    setTotalPrice(totalPrice);
                    JSONArray cartArray = data.getJSONArray("cart");
                    for (int i = 0; i < cartArray.size(); i++) {
                        String imgUrl = cartArray.getJSONObject(i).getString("img");
                        String title = cartArray.getJSONObject(i).getString("title");
                        float price = cartArray.getJSONObject(i).getFloatValue("price");
                        int amount = cartArray.getJSONObject(i).getIntValue("amount");
                        int subGoodsId = cartArray.getJSONObject(i).getIntValue("sub_goods_id");
                        addOneCart(imgUrl, title, price, amount, subGoodsId);

                    }
                }
            }
        }
    }

    private void addOneCart(String imgUrl, String title, float price, int amount, int subGoodsId) {
        createOrderCart = new CreateOrderCart(CreateOrder.this);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) createOrderCart.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        layoutParams.setMargins(0, 0, 0, 10);
        createOrderCart.setLayoutParams(layoutParams);
        createOrderCart.setAmount(amount);
        createOrderCart.setImageUrl(imgUrl);
        createOrderCart.setPrice(price);
        createOrderCart.setSubGoodsId(subGoodsId);
        createOrderCart.setTitle(title);
        linearLayout.addView(createOrderCart);

    }

    void setTotalPrice(float totalPrice) {
        totalPriceView.setText(String.valueOf(totalPrice) + "¥");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(data.getStringExtra("nick_name"));
        if (requestCode == 0 && resultCode == RESULT_OK) {
            int addressId = data.getIntExtra("address_id", 0);
            String nickName = data.getStringExtra("nick_name");
            String detail = data.getStringExtra("detail");
            String phone = data.getStringExtra("phone");
            preViewAddress.changeToSelectMode(false);
            preViewAddress.setPhone(phone);
            preViewAddress.setDetail(detail);
            preViewAddress.setNickName(nickName);
            preViewAddress.hiddenEditIcon();
            preViewAddress.setAddressId(addressId);
            queryOrder(addressId);
        }
    }

    private void queryOrder(int addressId) {
        HashMap<String, String> param = new HashMap<>();
        param.put("address_id", String.valueOf(addressId));
        param.put("cart_id", cartIdJson);
        new ApiThread(0, mHandler, "get-c", Config.getServerAddress() + "/v1/order/query", Utils.MapToHttpParam(param), Config.getCookie()).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        linearLayout = findViewById(R.id.cart_set);
        addressLinearLayout = findViewById(R.id.address);
        preViewAddress = new PreViewAddress(CreateOrder.this) {
            @Override
            public void itemClick(String nick_name, String phone, String detail) {
                super.itemClick(nick_name, phone, detail);
                Intent intent = new Intent(CreateOrder.this, AddressList.class);
                startActivityForResult(intent, 0);
            }
        };
        totalPriceView = findViewById(R.id.total_price);
        preViewAddress.changeToSelectMode(true);

        addressLinearLayout.addView(preViewAddress);

        mHandler = new MHandler(CreateOrder.this);
        String cartId = getIntent().getStringExtra("cart_id");
//        addOneCart(null, 0, 0);
//        addOneCart(null, 0, 0);
        setTotalPrice(0);
        System.out.println(cartId);
        cartIdJson = cartId;
    }
}