package com.duobiao.mainframedart.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duobiao.mainframedart.R;
import com.duobiao.mainframedart.util.ObjectUtils;

/**
 * Author:Admin
 * Time:2019/7/23 15:27
 * 描述：
 */
public class MainItemView extends LinearLayout {
    private TextView main_item_name;
    private ImageView main_item_icon;
    public MainItemView(Context context) {
        super(context);
    }

    public MainItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.main_item_view,this);
        main_item_name = findViewById(R.id.main_item_name);
        main_item_icon = findViewById(R.id.main_item_icon);


    }

    public void setMain_item_name(String name){
        if (ObjectUtils.isStringEmpty(name)) return;
        main_item_name.setText(name);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int with = getWidth();
//        main_item_icon.setLayoutParams(new LayoutParams(getWidth(),getWidth()));
    }
}
