package com.wo1haitao.controls;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by user on 4/25/2017.
 */

public class SquareHeightImageView extends AppCompatImageView {
    public SquareHeightImageView(Context context) {
        super(context);
    }

    public SquareHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareHeightImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);

//        int h = this.getMeasuredHeight();
//        int w = this.getMeasuredWidth();
        //int curSquareDim = Math.min(w, h);
        setMeasuredDimension(heightMeasureSpec, heightMeasureSpec);

    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        if (this.getLayoutParams().width != h)
//        {
//            if (this.getDrawable() != null)
//            {
//                this.getLayoutParams().width = h * this.getDrawable().getIntrinsicWidth() /  this.getDrawable().getIntrinsicHeight();
////                this.setVisibility(INVISIBLE);
//                this.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        SquareHeightImageView.this.requestLayout();
////                        SquareHeightImageView.this.setVisibility(VISIBLE);
//                    }
//                });
//            }
//        }
//    }
}
