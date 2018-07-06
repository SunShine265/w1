package com.wo1haitao.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.R;
import com.wo1haitao.activities.BaseActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends android.app.Fragment {
    boolean on_click_edt = false;
    ProgressDialog pd_dialog;
    ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);

        return textView;
    }

    public boolean onBackPressed(){
        return false;
    }

    public final void backPress(){
        if(getActivity()!=null){
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(getActivity() instanceof BaseActivity) {
            BaseActivity baseActivity = (com.wo1haitao.activities.BaseActivity) getActivity();
            baseActivity.onActivityResultFragment(requestCode, resultCode, data);
        }

    }

    protected void eventOnEndAnimaor() {

    }


}
