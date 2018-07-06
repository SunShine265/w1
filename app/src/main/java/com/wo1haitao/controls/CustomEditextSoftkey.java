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
 * Created by user on 5/10/2017.
 */

public class CustomEditextSoftkey extends CustomEditextFixSize {
    Context context;
    ListenShowKey listener;
    static Boolean on_click;

    public static Boolean getOn_click() {
        return on_click;
    }

    public void setOn_click(Boolean on_click) {
        this.on_click = on_click;
    }

    public CustomEditextSoftkey(Context context) {
        super(context);
        this.context = context;
        if(context instanceof ListenShowKey){
            listener = (ListenShowKey) context;
        }

    }

    public CustomEditextSoftkey(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if(context instanceof ListenShowKey){
            listener = (ListenShowKey) context;
        }
    }


//
//    @Override
//    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
//        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//        if(!focused){
//            hideKeyboard();
//        }
//
//    }
//
//    private void hideKeyboard() {
//        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
//    }
//
    public interface ListenShowKey {
        void keyShow(boolean is_show);
    }
//
//    @Override
//    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
//        return super.dispatchKeyEvent(event);
//    }
}
