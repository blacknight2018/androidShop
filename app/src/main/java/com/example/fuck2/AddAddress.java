package com.example.fuck2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.example.fuck2.config.Config;
import com.example.fuck2.result.Result;
import com.example.fuck2.utils.ApiThread;
import com.example.fuck2.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class AddAddress extends AppCompatActivity {
    private TextView nameView;
    private RadioButton menRadio, femaleRadio;
    private EditText phoneEdit, detailEdit;
    private MHandler mHandler;
    private Button submitBtn, deleteBtn;
    private int addressId;

    private class MHandler extends Handler {
        private WeakReference<AddAddress> weakReference;

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String responseBody = (String) msg.obj;
                JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBody);
                if (jsonObject != null) {
                    int code = jsonObject.getInteger("code");
                    if (code == Result.ErrCode.Ok.ordinal()) {
                        Toast.makeText(AddAddress.this, "添加成功", Toast.LENGTH_SHORT).show();
                        weakReference.get().finish();
                    }
                }
            } else if (msg.what == 1) {
                String responseBody = (String) msg.obj;
                JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBody);
                if (jsonObject != null) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data != null) {
                        int id = data.getIntValue("id");
                        String nickName = data.getString("nick_name");
                        String sex = data.getString("sex");
                        String phone = data.getString("phone");
                        String detail = data.getString("detail");
                        weakReference.get().addressId = id;
                        weakReference.get().nameView.setText(nickName);
                        weakReference.get().phoneEdit.setText(phone);
                        weakReference.get().detailEdit.setText(detail);
                        if (sex.toUpperCase().equals("F")) {
                            weakReference.get().femaleRadio.setChecked(true);
                        } else if (sex.toUpperCase().equals("M")) {
                            weakReference.get().menRadio.setChecked(true);
                        }
                    }
                }
            } else if (2 == msg.what) {
                String responseBody = (String) msg.obj;
                JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(responseBody);
                if (jsonObject != null) {
                    Integer integer = jsonObject.getInteger("code");
                    if (integer != null && integer == Result.ErrCode.Ok.ordinal()) {
                        Toast.makeText(AddAddress.this, "删除成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }

        public MHandler(AddAddress addAddress) {
            weakReference = new WeakReference<>(addAddress);
        }
    }

    private void loadAddress(int address_id) {
        HashMap<String, String> param = new HashMap<>();
        param.put("address_id", String.valueOf(address_id));
        new ApiThread(1, mHandler, "get-c", Config.getServerAddress() + "/v1/address", Utils.MapToHttpParam(param), Config.getCookie()).start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        mHandler = new MHandler(this);
        Intent param = getIntent();
        final int address_id = param.getIntExtra("address_id", 0);

        nameView = findViewById(R.id.nick_name);
        menRadio = findViewById(R.id.men);
        femaleRadio = findViewById(R.id.female);
        phoneEdit = findViewById(R.id.phone);
        detailEdit = findViewById(R.id.detail);
        submitBtn = findViewById(R.id.submit);


        deleteBtn = findViewById(R.id.delete);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddAddress.this);
                builder.setTitle("提示");
                builder.setMessage("确定删除吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("address_id", String.valueOf(address_id));
                        new ApiThread(2, mHandler, "delete-c", Config.getServerAddress() + "/v1/address", Utils.MapToHttpParam(param), Config.getCookie()).start();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> param = new HashMap<>();
                param.put("phone", phoneEdit.getText().toString());
                param.put("name", nameView.getText().toString());
                if (menRadio.isChecked() && !femaleRadio.isChecked()) {
                    param.put("sex", "M");
                } else if (!menRadio.isChecked() && femaleRadio.isChecked()) {
                    param.put("sex", "F");
                } else {
                    Toast.makeText(AddAddress.this, "请选择性别", Toast.LENGTH_SHORT).show();
                    return;
                }
                param.put("detail", detailEdit.getText().toString());
                new ApiThread(0, mHandler, "post-c", Config.getServerAddress() + "/v1/address", Utils.MapToHttpParam(param), Config.getCookie()).start();
            }
        });
        if (0 != address_id) {
            loadAddress(address_id);

        } else {
            deleteBtn.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
