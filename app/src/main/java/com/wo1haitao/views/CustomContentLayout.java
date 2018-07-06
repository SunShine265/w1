package com.wo1haitao.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by user on 5/9/2017.
 */

public abstract class CustomContentLayout extends LinearLayout {
    public CustomContentLayout(Context context) {
        super(context);
    }

    public CustomContentLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
        final int actualHeight = getHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (actualHeight > proposedheight){

            setActionSHB(true);
            return;

        } else {

            setActionSHB(false);
            return;
        }
    }

    abstract void setActionSHB(boolean is_show);
}
