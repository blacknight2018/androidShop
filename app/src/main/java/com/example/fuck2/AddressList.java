package com.example.fuck2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.fuck2.config.Config;
import com.example.fuck2.ui.PreViewAddress;
import com.example.fuck2.ui.ScrollBottomScrollView;
import com.example.fuck2.utils.ApiThread;
import com.example.fuck2.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressList extends AppCompatActivity {
    private TextView addBtn;
    private MHandler mHandler;
    private List<PreViewAddress> preViewAddressList;
    private ScrollBottomScrollView scrollBottomScrollView;
    private int limit = 10, offset = 0;


    private class MHandler extends Handler {
        private WeakReference<AddressList> weakReference;

        public MHandler(AddressList addressList) {
            weakReference = new WeakReference<AddressList>(addressList);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (0 == msg.what) {
                String responseBody = (String) msg.obj;
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String phone = jsonArray.getJSONObject(i).getString("phone");
                        String nick_name = jsonArray.getJSONObject(i).getString("nick_name");
                        String detail = jsonArray.getJSONObject(i).getString("detail");
                        int addressId = jsonArray.getJSONObject(i).getIntValue("id");
                        addOneAddress(addressId, phone, nick_name, detail);
                    }
                    offset += jsonArray.size();
                }
            }
        }
    }

    private void addOneAddress(int addressId, String phone, String nick_name, String detail) {
        LinearLayout linearLayout = findViewById(R.id.address_set);
        PreViewAddress preViewAddress = new PreViewAddress(getApplicationContext()) {
            @Override
            public void itemClick(String nick_name, String phone, String detail) {
                super.itemClick(nick_name, phone, detail);
                Intent intent = new Intent();
                intent.putExtra("nick_name", nick_name);
                intent.putExtra("phone", phone);
                intent.putExtra("detail", detail);
                intent.putExtra("address_id", getAddressId());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void editClick(int addressId) {
                super.editClick(addressId);
                Intent intent = new Intent(AddressList.this, AddAddress.class);
                intent.putExtra("address_id", addressId);
                startActivity(intent);

            }
        };
        preViewAddress.setAddressId(addressId);
        preViewAddress.setDetail(detail);
        preViewAddress.setNickName(nick_name);
        preViewAddress.setPhone(phone);
        preViewAddressList.add(preViewAddress);
        linearLayout.addView(preViewAddress);
    }

    private void clear() {
        LinearLayout linearLayout = findViewById(R.id.address_set);
        linearLayout.removeAllViews();
        offset = 0;
        limit = 10;
        preViewAddressList.clear();
    }

    private void loadAddress() {
        HashMap<String, String> param = new HashMap<>();
        param.put("limit", String.valueOf(limit));
        param.put("offset", String.valueOf(offset));
        new ApiThread(0, mHandler, "get-c", Config.getServerAddress() + "/v1/address", Utils.MapToHttpParam(param), Config.getCookie()).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        clear();
        loadAddress();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        scrollBottomScrollView = findViewById(R.id.scroll);
        addBtn = findViewById(R.id.add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressList.this, AddAddress.class);
                startActivity(intent);
            }
        });
        scrollBottomScrollView.registerOnScrollViewScrollToBottom(new ScrollBottomScrollView.OnScrollBottomListener() {
            @Override
            public void scrollToBottom() {
                loadAddress();
            }
        });
        mHandler = new MHandler(AddressList.this);
        preViewAddressList = new ArrayList<>();

    }
}
