package com.wo1haitao.controls;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by user on 9/27/2017.
 */

public class CustomSquareFrameLayout extends FrameLayout {

    public CustomSquareFrameLayout(Context context) {
        super(context);
    }

    public CustomSquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSquareFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int h = this.getMeasuredHeight();
        int w = this.getMeasuredWidth();
        int curSquareDim = Math.min(w, h);
        setMeasuredDimension(curSquareDim, curSquareDim);

    }
}
