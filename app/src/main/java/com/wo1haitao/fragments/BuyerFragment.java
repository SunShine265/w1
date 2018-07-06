package com.wo1haitao.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.R;
import com.wo1haitao.views.ActionBarProject;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class BuyerFragment extends BaseFragment {
    ChangeFragment getToSearchListener() {
        if (this.getActivity() instanceof ChangeFragment) {
            return (ChangeFragment) this.getActivity();
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_buyer, container, false);

        ImageView iv_purchase = (ImageView) view.findViewById(R.id.iv_purchase);
        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.bg_when_buyer, iv_purchase);

        LinearLayout btn_search = (LinearLayout) view.findViewById(R.id.btn_search);
        ActionBarProject my_action_bar;
        my_action_bar = (ActionBarProject) view.findViewById(R.id.my_action_bar);
        my_action_bar.showTitle(R.string.txt_buyer_homepage);
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BuyerFragment.this.getActivity() != null) {
                    getToSearchListener().changeRequestPurchase();
                }
            }
        });
        InputMethodManager imm = (InputMethodManager) BuyerFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        return view;
    }

    public interface ChangeFragment {
        void changeRequestPurchase();
    }
}
