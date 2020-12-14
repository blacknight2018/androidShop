package com.example.fuck2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.fuck2.config.Config;
import com.example.fuck2.result.Result;
import com.example.fuck2.ui.PopWinSelectSpecItem;
import com.example.fuck2.utils.ApiThread;
import com.example.fuck2.utils.Utils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectSpecPopWin extends PopupWindow {
    private final Context mContext;
    private final View view;
    private int curGoodsId;
    private final RoundedImageView preview;
    private final TextView titleTextView;
    private final TextView descTextView;
    private final TextView priceTextView;
    private final MHandler mHandler;
    private final List<PopWinSelectSpecItem> popWinSelectSpecItemArrayList = new ArrayList<>();

    static private class MHandler extends Handler {
        private final WeakReference<SelectSpecPopWin> winWeakReference;

        public MHandler(SelectSpecPopWin winWeakReference) {
            this.winWeakReference = new WeakReference<SelectSpecPopWin>(winWeakReference);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            //Clear
            Glide.with(winWeakReference.get().mContext).load("").into(winWeakReference.get().preview);
            winWeakReference.get().titleTextView.setText("");
            winWeakReference.get().descTextView.setText("");
            winWeakReference.get().priceTextView.setText(String.valueOf(0) + "¥");
            if (0 == code) {
                String body = msg.obj.toString();
                JSONObject jsonObject = JSONObject.parseObject(body);
                if (jsonObject != null) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    String title = data.getString("title");
                    String desc = data.getString("desc");
                    String img = data.getJSONObject("sub_goods").getString("img");
                    String subGoodsTemplate = data.getJSONObject("sub_goods").getString("template");
                    String template = data.getString("template");
                    winWeakReference.get().curGoodsId = data.getInteger("id");
                    winWeakReference.get().parseTemplate(template, subGoodsTemplate);
                    float price = data.getJSONObject("sub_goods").getFloat("price");
                    winWeakReference.get().titleTextView.setText(title);
                    winWeakReference.get().descTextView.setText(desc);
                    winWeakReference.get().priceTextView.setText(String.valueOf(price) + "¥");
                    winWeakReference.get().preview.setScaleType(ImageView.ScaleType.CENTER);
                    Glide.with(winWeakReference.get().mContext).load(img).into(winWeakReference.get().preview);
                }
            } else if (code == 1) {
                String body = msg.obj.toString();
                JSONObject jsonObject = JSONObject.parseObject(body);
                if (jsonObject != null) {
                    int retCode = jsonObject.getInteger("code");
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data != null && retCode == 0) {
                        int subGoodsId = data.getInteger("id");
                        HashMap<String, String> param = new HashMap<>();
                        param.put("sub_goods_id", String.valueOf(subGoodsId));
                        new ApiThread(0, winWeakReference.get().mHandler, "get", Config.getServerAddress() + "/v1/goods", Utils.MapToHttpParam(param)).start();
                    } else if (retCode != 0) {
                        Toast.makeText(winWeakReference.get().mContext, Result.getMsg(retCode), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }

    }


    /**
     * 解析goods的template参数,并且添加到界面
     *
     * @param templateString server返回的json格式数据
     */
    private void parseTemplate(String templateString, String subGoodsTemplate) {
        JSONArray jsonArray = JSONArray.parseArray(templateString);
        JSONArray jsonArray1 = JSONArray.parseArray(subGoodsTemplate);
        popWinSelectSpecItemArrayList.clear();
        LinearLayout linearLayout = view.findViewById(R.id.spec_set);
        linearLayout.removeAllViews();
        for (int i = 0; i < jsonArray.size(); i++) {
            String title = jsonArray.getJSONObject(i).getString("title");
            List<String> value = new ArrayList<>();
            JSONArray valuesArray = jsonArray.getJSONObject(i).getJSONArray("values");
            for (int j = 0; j < valuesArray.size(); j++) {
                value.add(String.valueOf(valuesArray.getString(j)));
            }
            addOneSpec(title, value, jsonArray1.getInteger(i));
        }
    }

    /**
     * 添加一个商品规格到界面
     *
     * @param title  规格的名称
     * @param values 可选的值
     */
    private void addOneSpec(String title, List<String> values, int curIdx) {

        LinearLayout linearLayout = view.findViewById(R.id.spec_set);
        PopWinSelectSpecItem popWinSelectSpecItem = new PopWinSelectSpecItem(mContext) {
            @Override
            public void onChange(String title, int idx) {
                super.onChange(title, idx);
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < popWinSelectSpecItemArrayList.size(); i++) {
                    int t = popWinSelectSpecItemArrayList.get(i).getCurIdx();
                    jsonArray.add(t);
                }
                System.out.println(jsonArray.toString());
                HashMap<String, String> param = new HashMap<>();
                param.put("goods_id", Integer.toString(curGoodsId));
                param.put("template_index", jsonArray.toString());
                new ApiThread(1, mHandler, "get", Config.getServerAddress() + "/v1/goods/query", Utils.MapToHttpParam(param)).start();

            }
        };
        popWinSelectSpecItem.setCurIdx(curIdx);
        popWinSelectSpecItem.setTitle(title);
        popWinSelectSpecItem.setValues(values);

        popWinSelectSpecItemArrayList.add(popWinSelectSpecItem);
        linearLayout.addView(popWinSelectSpecItem);
    }


    @SuppressLint("InflateParams")
    public SelectSpecPopWin(Context mContext, View.OnClickListener itemsOnClick, int subGoodsId) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.popwin_select_spec, null);
        this.mContext = mContext;
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
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

        preview = view.findViewById(R.id.preview);
        titleTextView = view.findViewById(R.id.title);
        descTextView = view.findViewById(R.id.desc);
        priceTextView = view.findViewById(R.id.price);

        HashMap<String, String> param = new HashMap<>();
        param.put("sub_goods_id", String.valueOf(subGoodsId));
        mHandler = new MHandler(this);
        new ApiThread(0, mHandler, "get", Config.getServerAddress() + "/v1/goods", Utils.MapToHttpParam(param)).start();


//        List<String> clothSizes = new ArrayList<>();
//        clothSizes.add("1");
//        clothSizes.add("2");
//        clothSizes.add("3");
//        addOneSpec("大小", clothSizes);
//
//        List<String> clothColor = new ArrayList<>();
//        clothColor.add("蓝色");
//        clothColor.add("黑色");
//        clothColor.add("绿色");
//        addOneSpec("颜色", clothColor);
//
//        List<String> weightColor = new ArrayList<>();
//        weightColor.add("10kg");
//        weightColor.add("20kg");
//        weightColor.add("30kg");
//        addOneSpec("重量", weightColor);

    }
}
