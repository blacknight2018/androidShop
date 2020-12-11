package com.example.fuck2;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.example.fuck2.config.Config;
import com.example.fuck2.ui.PopWinSelectSpecItem;
import com.example.fuck2.utils.ApiThread;
import com.example.fuck2.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectSpecPopWin extends PopupWindow {
    private Context mContext;

    private View view;
    private int subGoodsId = 3;
    private MHandler mHandler = new MHandler();
    private Button btn_take_photo, btn_pick_photo, btn_cancel;

    private class MHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            if (0 == code) {
                String body = msg.obj.toString();
            }
        }
    }

    private void addOneSpec(String title, List<String> values) {
        LinearLayout linearLayout = view.findViewById(R.id.spec_set);
        PopWinSelectSpecItem popWinSelectSpecItem = new PopWinSelectSpecItem(mContext);
        popWinSelectSpecItem.setTitle(title);
        popWinSelectSpecItem.setValues(values);
        linearLayout.addView(popWinSelectSpecItem);
    }

    public SelectSpecPopWin(Context mContext, View.OnClickListener itemsOnClick) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.popwin_select_spec, null);
        this.mContext = mContext;

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        this.setContentView(this.view);
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.select_spec_anim);

        HashMap<String, String> param = new HashMap<>();
        param.put("sub_goods_id", String.valueOf(subGoodsId));
        new ApiThread(0, mHandler, "get", Config.getServerAddress() + "/v1/goods", Utils.MapToHttpParam(param)).start();

        List<String> clothSizes = new ArrayList<>();
        clothSizes.add("1");
        clothSizes.add("2");
        clothSizes.add("3");
        addOneSpec("大小", clothSizes);

        List<String> clothColor = new ArrayList<>();
        clothColor.add("蓝色");
        clothColor.add("黑色");
        clothColor.add("绿色");
        addOneSpec("颜色", clothColor);

    }
}
