package com.wo1haitao.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wo1haitao.R;

/**
 * Created by user on 4/25/2017.
 */

public class CustomEditextCountry extends LinearLayout {
    public EditText editext_country;
    public ImageView imageview;
    public Boolean isEnable;
    public int position;
    public CustomEditextCountry(Context context) {
        super(context);
        isEnable=true;
        initView();
    }
    public CustomEditextCountry(Context context, AttributeSet attrs) {
        super(context, attrs);
        isEnable=true;
        initView();
    }


    private void initView() {
        View view = inflate(getContext(), R.layout.view_custom_country, null);
        addView(view);
        editext_country = (EditText) view.findViewById(R.id.edt_country);
        imageview = (ImageView) view.findViewById(R.id.imageView);
    }


}
