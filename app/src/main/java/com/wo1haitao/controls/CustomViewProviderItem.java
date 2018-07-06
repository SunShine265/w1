package com.wo1haitao.controls;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.wo1haitao.R;

/**
 * Created by user on 4/26/2017.
 */

public class CustomViewProviderItem extends FrameLayout {
    Button btContent;
    String textContent;
    public CustomViewProviderItem(@NonNull Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.view_custom_provider_item_free_style, null);
        addView(view);
        btContent = (Button) view.findViewById(R.id.button);

    }
    public void setTextContent(String text){
        btContent.setText(text);
    }
    public void setTextColor(int color){
        btContent.setTextColor(color);
    }
    public void setBackground(int background){
        btContent.setBackgroundResource(background);
    }
    public  void fixEms(int length){
        btContent.setEms(length);
    }
    public void setClickEvent(OnClickListener click){
        btContent.setOnClickListener(click);
    }
}
