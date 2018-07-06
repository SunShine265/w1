package com.wo1haitao.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.wo1haitao.CustomApp;
import com.wo1haitao.R;

import static com.wo1haitao.utils.Utils.SMALL_SIZE_SCREEN_DP;

/**
 * Created by goodproductssoft on 9/26/17.
 */

public class CustomTextViewFixSize extends android.support.v7.widget.AppCompatTextView {
    int newTextsize = 0;
    boolean is_scale_custom = true;
    public CustomTextViewFixSize(Context context) {
        super(context);

    }

    public CustomTextViewFixSize(Context context, AttributeSet attrs) {
        super(context, attrs);
       initView(attrs);
    }

    public CustomTextViewFixSize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
    }



    private void initView(AttributeSet attrs){
        WindowManager manager =  (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int long_dimention = point.x < point.y ? point.y : point.x;
        int short_dimention = point.x > point.y ? point.y : point.x;
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.com_wo1haitao_controls_CustomSizeText);
        is_scale_custom = a.getBoolean(R.styleable.com_wo1haitao_controls_CustomSizeText_is_scale_text_custom, true);
        float scale_by_screen = (float) 0.925;
        if(is_scale_custom == true){
            if(short_dimention < 1200){
                scale_by_screen = (float) 0.9;
            }
            else if (short_dimention < 900){
                scale_by_screen = (float) 0.85;
            }
        }
        float scale_by_screen_size = 1;
        if(CustomApp.getInstance().getInch() >= 9){
            scale_by_screen_size = (float) 0.8;
        }
        else if(CustomApp.getInstance().getInch() >= 6){
            scale_by_screen_size = (float) 0.9;
        }
        float current_size = getTextSize();
        float new_size = scale_by_screen_size*scale_by_screen*long_dimention*current_size/SMALL_SIZE_SCREEN_DP;

        this.setTextSize(TypedValue.COMPLEX_UNIT_PX,new_size);
        // get width and height
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}
