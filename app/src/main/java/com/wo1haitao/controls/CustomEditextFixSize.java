package com.wo1haitao.controls;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.wo1haitao.CustomApp;

import static com.wo1haitao.utils.Utils.SMALL_SIZE_SCREEN_DP;

/**
 * Created by goodproductssoft on 11/7/17.
 */

public class CustomEditextFixSize extends AppCompatEditText {
    protected boolean is_scale_custom = true;
    public CustomEditextFixSize(Context context) {
        super(context);
        initView();
    }

    public CustomEditextFixSize(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomEditextFixSize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        WindowManager manager =  (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int long_dimention = point.x < point.y ? point.y : point.x;
        int short_dimention = point.x > point.y ? point.y : point.x;
        float scale_by_screen = (float) 0.925;
        if(short_dimention < 1200){
            scale_by_screen = (float) 0.9;
        }
        float scale_by_screen_size = 1;
        if(is_scale_custom == true){
            if(short_dimention < 1200){
                scale_by_screen_size = (float) 0.9;
            }
            else if (short_dimention < 900){
                scale_by_screen_size = (float) 0.85;
            }
        }
        float scale_by_screen_size_screen = 1;
        if(CustomApp.getInstance().getInch() >= 9){
            scale_by_screen_size_screen = (float) 0.8;
        }
        else if(CustomApp.getInstance().getInch() >= 6){
            scale_by_screen_size_screen = (float) 0.9;
        }
        float current_size = getTextSize();
        float new_size = scale_by_screen_size_screen*scale_by_screen_size*scale_by_screen*long_dimention*current_size/SMALL_SIZE_SCREEN_DP;
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX,new_size);
        // get width and height
    }
}
