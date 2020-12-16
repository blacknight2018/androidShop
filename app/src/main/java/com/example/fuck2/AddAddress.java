package com.example.fuck2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
            }
        }

        public MHandler(AddAddress addAddress) {
            weakReference = new WeakReference<AddAddress>(addAddress);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        nameView = findViewById(R.id.nick_name);
        menRadio = findViewById(R.id.men);
        femaleRadio = findViewById(R.id.female);
        phoneEdit = findViewById(R.id.phone);
        detailEdit = findViewById(R.id.detail);
        submitBtn = findViewById(R.id.submit);
        deleteBtn = findViewById(R.id.delete);
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
        mHandler = new MHandler(this);


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
