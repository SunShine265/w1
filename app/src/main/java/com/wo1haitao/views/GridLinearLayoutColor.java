package com.wo1haitao.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;

/**
 * Created by goodproductssoft on 6/19/17.
 */

public class GridLinearLayoutColor extends LinearLayout {
    private int Lines;

    public GridLinearLayoutColor(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public void setLines(int lines) {
        Lines = lines;
        invalidate();
    }

    public GridLinearLayoutColor(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public GridLinearLayoutColor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        try {
            Paint paint = new Paint();

            Rect r = new Rect(0, 0, canvas.getWidth(), canvas.getHeight() / 3);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.dark_color_dialog));
            canvas.drawRect(r, paint);

            r = new Rect(0,  canvas.getHeight() / 3, canvas.getWidth(), canvas.getHeight() * 2 / 3);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(ContextCompat.getColor(CustomApp.getInstance(), android.R.color.transparent));
            canvas.drawRect(r, paint);

            r = new Rect(0,  canvas.getHeight() * 2 / 3, canvas.getWidth(), canvas.getHeight());
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.dark_color_dialog));
            canvas.drawRect(r, paint);
        }
        catch (Exception ex) {
        }

    }
}
