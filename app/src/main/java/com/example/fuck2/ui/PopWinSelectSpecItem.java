package com.example.fuck2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.fuck2.R;

import java.util.List;


public class PopWinSelectSpecItem extends LinearLayout {
    private String title;
    private List<String> values;


    public int getCurIdx() {
        return curIdx;
    }

    public void setCurIdx(int curIdx) {
        this.curIdx = curIdx;
    }

    private int curIdx;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        TextView titleTextView = findViewById(R.id.title);
        titleTextView.setText(title);
    }

    public List<String> getValues() {
        return values;
    }

    public void onChange(String title, int idx) {
        RadioGroup radioGroup = findViewById(R.id.spec_set);
        if (curIdx >= 0 && idx != curIdx) {
            ((RadioButton) radioGroup.getChildAt(curIdx)).setChecked(false);
            ((RadioButton) radioGroup.getChildAt(idx)).setChecked(true);
            curIdx = idx;
        }
    }

    public void setValues(List<String> values) {
        this.values = values;
        RadioGroup radioGroup = findViewById(R.id.spec_set);
        radioGroup.removeAllViews();
        for (int i = 0; i < values.size(); i++) {
            final RadioButton radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.spec_btn, null);
            radioButton.setText(values.get(i));
            radioButton.getLayoutParams();
            final int finalI = i;
            radioButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onChange(radioButton.getText().toString(), finalI);
                }
            });
            if (i == curIdx) {
                radioButton.setChecked(true);
            }
            radioGroup.addView(radioButton);
        }


    }


    public void loadLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.popwin_select_spec_item, this, true);
    }

    public PopWinSelectSpecItem(Context context) {
        super(context);
        loadLayout(context);
    }

    public PopWinSelectSpecItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadLayout(context);
    }

    public PopWinSelectSpecItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadLayout(context);
    }

    public PopWinSelectSpecItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadLayout(context);
    }
}
