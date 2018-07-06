package com.wo1haitao.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wo1haitao.R;

/**
 * Created by user on 4/26/2017.
 */

public class CustomViewThreeImage extends LinearLayout {
    ImageView iv_1,iv_2,iv_3;

    public CustomViewThreeImage(Context context) {
        super(context);
        initView();
    }
    public CustomViewThreeImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setOnClickListener(OnClickListener image_click){
        iv_1.setOnClickListener(image_click);
        iv_2.setOnClickListener(image_click);
        iv_3.setOnClickListener(image_click);
    }

    private void initView() {
        inflate(getContext(), R.layout.view_custom_three_image,this);
        iv_1 = (ImageView) findViewById(R.id.imageview_picker1);
        iv_2 = (ImageView) findViewById(R.id.imageview_picker2);
        iv_3 = (ImageView) findViewById(R.id.imageview_picker3);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        iv_1.getLayoutParams().width = w/3;
        iv_1.getLayoutParams().height = w/3;
        iv_2.getLayoutParams().width = w/3;
        iv_2.getLayoutParams().height = w/3;
        iv_3.getLayoutParams().width = w/3;
        iv_3.getLayoutParams().height = w/3;
    }
}
