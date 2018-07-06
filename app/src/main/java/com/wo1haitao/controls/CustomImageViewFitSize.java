package com.wo1haitao.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wo1haitao.R;

import static com.wo1haitao.utils.Utils.SMALL_SIZE_SCREEN_DP;

/**
 * Created by goodproductssoft on 9/26/17.
 */


public class CustomImageViewFitSize extends ImageView {
    private int mPixelHeight = 0;
    private int mPixelWidth = 0;
    private  boolean is_scale_custom = true;

    public CustomImageViewFitSize(Context context) {
        super(context);
        initView(null);

    }

    public CustomImageViewFitSize(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public CustomImageViewFitSize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }


    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if(mPixelHeight != 0 && mPixelWidth != 0){
            params.height = mPixelHeight;
            params.width = mPixelWidth;
        }
        super.setLayoutParams(params);
    }

    private void initView(AttributeSet myAttributeSet){
        if(myAttributeSet != null){
            WindowManager manager =  (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int short_dimention = point.x > point.y ? point.y : point.x;
            int long_dimention = point.x < point.y ? point.y : point.x;

            TypedArray a = getContext().obtainStyledAttributes(myAttributeSet, R.styleable.com_wo1haitao_controls_CustomSizeImage);
            a.getString(R.styleable.com_wo1haitao_controls_CustomSizeImage_fit_height);
            a.getString(R.styleable.com_wo1haitao_controls_CustomSizeImage_fit_width);
            int current_height = a.getInteger(R.styleable.com_wo1haitao_controls_CustomSizeImage_fit_height, 0);
            int current_width =  a.getInteger(R.styleable.com_wo1haitao_controls_CustomSizeImage_fit_width, 0);
            float ratio = (float) long_dimention/SMALL_SIZE_SCREEN_DP;
            is_scale_custom = a.getBoolean(R.styleable.com_wo1haitao_controls_CustomSizeImage_is_scale_image_custom, true);
            int new_height = (int) (ratio*current_height);
            int new_width = (int) (ratio*current_width);
            float scale_by_screen = (float) 0.925;
            if(is_scale_custom == true){
                if(short_dimention < 900){
                    scale_by_screen = (float) 0.85;
                }
                else if(short_dimention < 1200){
                    scale_by_screen = (float) 0.9;
                }
            }
            float scale_by_screen_size = 1;
            if(is_scale_custom == true){
                if(short_dimention < 1200){
                    scale_by_screen_size = (float) 0.95;
                }
                else if (short_dimention < 900){
                    scale_by_screen_size = (float) 0.9;
                }
            }

            mPixelHeight = (int) (scale_by_screen_size*new_height*scale_by_screen);
            mPixelWidth = (int) (scale_by_screen_size*new_width*scale_by_screen);
            a.recycle();
        }
   }
}
