package com.wo1haitao.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by user on 5/15/2017.
 */

public class CustomLayoutMain extends LinearLayout {
    public CustomLayoutMain(Context context) {
        super(context);
    }

    public CustomLayoutMain(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public float getXFraction() {
        return getX() / getWidth();
    }

    public void setXFraction(float xFraction) {
        final int width = getWidth();
        setX((width > 0) ? (xFraction * width) : -9999f);
    }
}
