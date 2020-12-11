package com.example.fuck2.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.fuck2.AddressList;
import com.example.fuck2.R;
import com.example.fuck2.config.Config;
import com.example.fuck2.utils.ApiThread;
import com.makeramen.roundedimageview.RoundedImageView;

public class NotificationsFragment extends Fragment {
    private RoundedImageView avatarImgView;
    private TextView nickNameTextView;
    private NotificationsViewModel notificationsViewModel;
    private NotificationsFragment.MHandler mHandler = new NotificationsFragment.MHandler();

    private class MHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            if (code == 0) {
                String body = msg.obj.toString();
                System.out.println(body);
                String avatarUrl = JSONObject.parseObject(body).getJSONObject("data").getString("avatar");
                String nickName = JSONObject.parseObject(body).getJSONObject("data").getString("nick_name");
                //avatarImgView.setImageURI(Uri.parse(avatarUrl));
                Glide.with(getContext()).load(avatarUrl).into(avatarImgView);
                nickNameTextView.setText(nickName);
            }

        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        TextView view = root.findViewById(R.id.item_address);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddressList.class);
                startActivity(intent);
            }
        });

        avatarImgView = root.findViewById(R.id.avatar);
        nickNameTextView = root.findViewById(R.id.nick_name);
        new ApiThread(0, mHandler, "get-c", Config.getServerAddress() + "/v1/user", "", Config.getCookie()).start();


        return root;
    }
}