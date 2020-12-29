package com.example.fuck2.ui.notifications;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.fuck2.AddressList;
import com.example.fuck2.MainActivity;
import com.example.fuck2.MyOrderActivity;
import com.example.fuck2.R;
import com.example.fuck2.config.Config;
import com.example.fuck2.utils.ApiThread;
import com.example.fuck2.utils.Utils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.lang.ref.WeakReference;

public class NotificationsFragment extends Fragment {
    private RoundedImageView avatarImgView;
    private TextView nickNameTextView;
    private TextView phoneTextView;
    private AlertDialog waitDialog;

    static private class MHandler extends Handler {
        private final WeakReference<NotificationsFragment> notificationsFragmentWeakReference;

        public MHandler(NotificationsFragment notificationsFragment) {
            notificationsFragmentWeakReference = new WeakReference<>(notificationsFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int code = msg.what;

            if (code == 0) {
                String body = msg.obj.toString();
                System.out.println(body);
                String avatarUrl = JSONObject.parseObject(body).getJSONObject("data").getString("avatar");
                String nickName = JSONObject.parseObject(body).getJSONObject("data").getString("nick_name");
                String phone = JSONObject.parseObject(body).getJSONObject("data").getString("phone");
                Context context = notificationsFragmentWeakReference.get().getContext();
                if (null != context) {
                    Glide.with(context).load(avatarUrl).into(notificationsFragmentWeakReference.get().avatarImgView);
                }
                notificationsFragmentWeakReference.get().nickNameTextView.setText(nickName);
                notificationsFragmentWeakReference.get().phoneTextView.setText(phone);

            }
            notificationsFragmentWeakReference.get().waitDialog.cancel();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final MainActivity mainActivity = (MainActivity) getContext();
        TextView addressItemView = root.findViewById(R.id.item_address);
        addressItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddressList.class);
                startActivity(intent);
            }
        });
        TextView orderItemView = root.findViewById(R.id.item_order);
        orderItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyOrderActivity.class);
                startActivity(intent);
            }
        });
        TextView exitItemView = root.findViewById(R.id.item_exit);
        exitItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear Cookie Token
                Utils.WritePreferences(mainActivity, "Cookie", Utils.EmptyString);
                mainActivity.finish();

            }
        });

        avatarImgView = root.findViewById(R.id.avatar);
        nickNameTextView = root.findViewById(R.id.nick_name);
        phoneTextView = root.findViewById(R.id.phone);
        MHandler mHandler = new MHandler(this);
        waitDialog = new ProgressDialog(getContext());
        waitDialog.setTitle("加载中");
        waitDialog.setMessage("加载个人信息中");
        waitDialog.create();
        waitDialog.show();
        new ApiThread(0, mHandler, "get-c", Config.getServerAddress() + "/v1/user", "", Config.getCookie()).start();
        return root;
    }
}