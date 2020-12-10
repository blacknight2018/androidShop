package com.example.fuck2.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fuck2.R;
import com.example.fuck2.config.Config;
import com.example.fuck2.utils.ApiThread;
import com.example.fuck2.utils.Utils;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {


    private MHandler mHandler = new MHandler();

    private class MHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String response = msg.obj.toString();
            if (0 == msg.what) {
                System.out.println(response);
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        final EditText phoneEditText = findViewById(R.id.phone);
        final EditText passWordEditText = findViewById(R.id.password);
        Button loginRegister = findViewById(R.id.login);
        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("phone", phoneEditText.getText().toString());
                hashMap.put("pass_word", passWordEditText.getText().toString());
                new ApiThread(0, mHandler, "post", Config.getServerAddress() + "/v1/login", Utils.MapToHttpParam(hashMap)).start();
            }
        });

    }

}
