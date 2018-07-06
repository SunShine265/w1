package com.wo1haitao.fragments;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wo1haitao.R;
import com.wo1haitao.activities.BaseActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseProgressFragment extends android.app.Fragment {
    boolean on_click_edt = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);


        return textView;
    }

    protected void customOnBack(){
        if(getActivity()!=null){
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(getActivity() instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) getActivity();
            baseActivity.onActivityResultFragment(requestCode, resultCode, data);
        }

    }

    protected void eventOnEndAnimaor() {

    }

    @Override
    public Animator onCreateAnimator(int transit, final boolean enter, int nextAnim) {
        Animator animator;
        if (enter) {
            animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.up_new_fragment);
        } else {
            animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.down_old_fragment);
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (enter) {
                    eventOnEndAnimaor();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        return animator;
    }

}
