package com.wo1haitao.controls;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;

/**
 * Created by user on 4/27/2017.
 */

public class CustomSwitch extends SwitchCompat {
    public CustomSwitch(Context context) {
        super(context);
    }
    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        changeColor(checked);
    }

    private void changeColor(boolean isChecked) {
        int thumbColor;
        int trackColor;

        if(isChecked) {
            //getContext().getResources().getColor(R.color.white);
            thumbColor =  ContextCompat.getColor(CustomApp.getInstance(), R.color.white);
            trackColor = ContextCompat.getColor(CustomApp.getInstance(), R.color.colorPrimary);
        } else {
            thumbColor = ContextCompat.getColor(CustomApp.getInstance(), R.color.white);
            trackColor = ContextCompat.getColor(CustomApp.getInstance(), R.color.track_disable);
        }

        try {
            getThumbDrawable().setColorFilter(thumbColor, PorterDuff.Mode.MULTIPLY);
            getTrackDrawable().setColorFilter(trackColor, PorterDuff.Mode.MULTIPLY);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
