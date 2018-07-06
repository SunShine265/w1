package com.wo1haitao.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wo1haitao.R;
import com.wo1haitao.models.ViewAddress;

import java.util.List;

/**
 * Created by user on 5/17/2017.
 */

public class CustomListAddress extends LinearLayout {
    Context context;
    FrameLayout fl_default_inlist;
    ImageView iv_newest;

    public CustomListAddress(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomListAddress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public void init(){
        setOrientation(VERTICAL);
    }

    public void showView(List<ViewAddress> list){
       for(int i = 0 ; i < list.size() ; i++){
           ViewAddress address = list.get(i);
           View v = inflate(context, R.layout.item_adderss_view,null);
           TextView tv_adr = (TextView) v.findViewById(R.id.tv_adress);
           FrameLayout fl_default = (FrameLayout) v.findViewById(R.id.fl_default);
           FrameLayout fl_setdefault = (FrameLayout) v.findViewById(R.id.fl_setdefault);
           tv_adr.setText(address.getFull_address());
           if(address.is_default()){
               fl_default.setVisibility(VISIBLE);
               fl_default_inlist = fl_default;
           }
           else {
               fl_setdefault.setVisibility(GONE);
           }
           if(i == list.size()-1){
               ImageView iv_add = (ImageView) v.findViewById(R.id.iv_add);
               iv_add.setVisibility(VISIBLE);
               iv_newest = iv_add;
           }
           addView(v);
       }
    }

    public void addAddress(ViewAddress address){
            View v = inflate(context, R.layout.item_adderss_view,null);
            TextView tv_adr = (TextView) v.findViewById(R.id.tv_adress);
            FrameLayout fl_default = (FrameLayout) v.findViewById(R.id.fl_default);
            FrameLayout fl_setdefault = (FrameLayout) v.findViewById(R.id.fl_setdefault);
            tv_adr.setText(address.getFull_address());
            fl_default.setVisibility(GONE);
            fl_setdefault.setVisibility(VISIBLE);
            iv_newest.setVisibility(GONE);
            ImageView iv_add = (ImageView) v.findViewById(R.id.iv_add);
            iv_add.setVisibility(VISIBLE);
            iv_newest = iv_add;
            addView(v);
    }
}
