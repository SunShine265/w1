package com.wo1haitao.controls;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.wo1haitao.views.ActionBarProject;

/**
 * Created by user on 5/11/2017.
 */

public class CustomToolBar extends Toolbar {
    Context context;
    ActionBarProject view_actionbar;
    public CustomToolBar(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CustomToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView(){
        view_actionbar = new ActionBarProject(context);
        addView(view_actionbar);
    }
}
