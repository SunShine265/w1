package com.wo1haitao.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;

/**
 * Created by goodproductssoft on 10/9/17.
 */

public class ButtonTender extends FrameLayout {
    FrameLayout fl_backgroundbutton;
    TextView tv_textbt;
    ImageView img_background;
    LinearLayout ll_layout_background;
    Drawable bg_default;

    public Drawable getBg_default() {
        return bg_default;
    }

    public void setBg_default(Drawable bg_default) {
        this.bg_default = bg_default;

    }

    public void setTextTender(String text) {
        if (tv_textbt != null) {
            tv_textbt.setText(text);
        }
    }

    public ButtonTender(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ButtonTender(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ButtonTender(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = inflate(context, R.layout.layout_button_tender, null);
        fl_backgroundbutton = (FrameLayout) view.findViewById(R.id.fl_button);
        tv_textbt = (TextView) view.findViewById(R.id.tv_button);
        img_background = (ImageView) view.findViewById(R.id.img_background);
        //ll_layout_background = (LinearLayout) view.findViewById(R.id.ll_layout_background);
        tv_textbt.setText("jgjjhgjhggyhjgjgjhgjjgjh");
        bg_default = ContextCompat.getDrawable(CustomApp.getInstance(), R.drawable.button_drable_dark);
        addView(view);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (bg_default != null) {
            fl_backgroundbutton.setBackground(bg_default);
        }
        super.onSizeChanged(w, h, oldw, oldh);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
