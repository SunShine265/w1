package com.wo1haitao.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.R;
import com.wo1haitao.views.ActionBarProject;


/**
 * A simple {@link Fragment} subclass.
 */
public class WhenPurchaseFragment extends BaseFragment {
    ActionBarProject my_action_bar;
    MyCallback getListener(){
        if (this.getActivity() instanceof MyCallback) {
            return (MyCallback) this.getActivity();
        }
        return null;
    };
    LinearLayout ll_to_search;


    public WhenPurchaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment_view = inflater.inflate(R.layout.fragment_when_purchase, container, false);
        ImageView iv_purchase = (ImageView) fragment_view.findViewById(R.id.iv_purchase);
        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.bg_when_purchase, iv_purchase);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        my_action_bar.DowSizeHeightActionbar(48);
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        my_action_bar.showTitle(R.string.txt_purchase_homepage);
        ll_to_search = (LinearLayout) fragment_view.findViewById(R.id.ll_to_search);
        ll_to_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                listener.changeSearchCountry();
                getListener().changeSearchCountry();
            }
        });

        return fragment_view;

    }

    public interface MyCallback{
        void changeSearchCountry();
        void changeProductList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



}
