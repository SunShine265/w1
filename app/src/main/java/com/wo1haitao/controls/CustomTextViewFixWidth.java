package com.wo1haitao.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;

import static com.wo1haitao.utils.Utils.SMALL_SIZE_SCREEN_DP;

/**
 * Created by goodproductssoft on 11/7/17.
 */

public class CustomTextViewFixWidth extends CustomTextViewFixSize {
    public CustomTextViewFixWidth(Context context) {
        super(context);
    }

    public CustomTextViewFixWidth(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextViewFixWidth(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



}
