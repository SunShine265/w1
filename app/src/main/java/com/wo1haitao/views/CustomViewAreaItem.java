package com.wo1haitao.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.wo1haitao.R;

/**
 * Created by user on 4/26/2017.
 */

public class CustomViewAreaItem extends FrameLayout {
    Button btContent;
    String textContent;

    public CustomViewAreaItem(@NonNull Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.view_custom_provider_item_fix_style, null);
        addView(view);
        btContent = (Button) view.findViewById(R.id.button);

    }
    public void setTextContent(String text){
        btContent.setText(text);
    }
    public void setTextColor(int color){
        btContent.setTextColor(color);
    }
    public void setFontSize(int fontSize){btContent.setTextSize(fontSize);}
    public void setBackground(int background){
        btContent.setBackgroundResource(background);
    }
    public  void fixEms(int length){
        btContent.setEms(length);
    }
    public void setClickBt(OnClickListener bt_click){
        btContent.setOnClickListener(bt_click);
    }
}
