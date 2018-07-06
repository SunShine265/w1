package com.wo1haitao.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ScrollView;

import com.wo1haitao.R;

public class DialogActivityBidShop extends Activity {
    View view_bottom;
    ScrollView sv_product;
    int long_dimention, scroll_height;
    boolean isSetHeightSv = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_bid_shop);
        view_bottom  = findViewById(R.id.view_bottom);
        sv_product = (ScrollView) findViewById(R.id.sv_product);
        WindowManager manager =  (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        long_dimention = point.y;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.setFinishOnTouchOutside(false);
        final View contentView = this.findViewById(android.R.id.content);
        this.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    view_bottom.setVisibility(View.GONE);
//                    ViewGroup.LayoutParams layoutParams = sv_product.getLayoutParams();
//                    layoutParams.height = scroll_height - keypadHeight;
//                    sv_product.setLayoutParams(layoutParams);
                }
                else {
                    view_bottom.setVisibility(View.VISIBLE);
//                    ViewGroup.LayoutParams layoutParams = sv_product.getLayoutParams();
//                    layoutParams.height = scroll_height;
//                    sv_product.setLayoutParams(layoutParams);
                }
            }
        });
    }
}
