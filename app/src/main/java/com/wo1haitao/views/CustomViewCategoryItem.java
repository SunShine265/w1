package com.wo1haitao.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;

/**
 * Created by goodproductssoft on 8/19/17.
 */

public class CustomViewCategoryItem extends FrameLayout {
    FrameLayout fl_button;
    TextView tv_button;
    String textContent;
    Boolean isChecked = false;
    String id_category;

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public CustomViewCategoryItem(@NonNull Context context) {
        super(context);
        initView();
    }

    private void initView() {
        LayoutParams layout = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layout);
        View view = inflate(getContext(), R.layout.view_custom_provider_item_free_style, null);
        addView(view);
        fl_button = (FrameLayout) view.findViewById(R.id.fl_button);
        tv_button = (TextView) view.findViewById(R.id.tv_button);

    }

    public void setViewCheck(){
        if(isChecked == true){
            tv_button.setTextColor(ContextCompat.getColor(CustomApp.getInstance(),R.color.white_color));
            fl_button.setBackgroundResource(R.drawable.button_custom_item_search);
        }
        else{
            tv_button.setTextColor(ContextCompat.getColor(CustomApp.getInstance(),R.color.black_color));
            fl_button.setBackgroundResource(R.drawable.button_custom_item_search_provider);
        }
    }

    public void setTextContent(String text){
        textContent = text;
        tv_button.setText(text);
    }
    public void setTextColor(int color){
        tv_button.setTextColor(color);
    }
    public void setFontSize(int fontSize){tv_button.setTextSize(fontSize);}
    public void setBackground(int background){
        fl_button.setBackgroundResource(background);
    }
    public  void fixEms(int length){
        tv_button.setEms(length);
    }
    public void setClickBt(OnClickListener bt_click){
        fl_button.setOnClickListener(bt_click);
    }
}
