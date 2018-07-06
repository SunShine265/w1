package com.wo1haitao.views;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wo1haitao.R;

/**
 * Created by user on 8/15/2017.
 */

public class BottomBarProject extends FrameLayout {
    Context context;
    LinearLayout ln_bottom_bar_project, btn_to_home, btn_to_notification,
            btn_to_chat, btn_to_profile;
    TextView tv_num_notification, tv_num_chat;
    FrameLayout tab_home, tab_notification, tab_chat, tab_profile;
    ImageView img_home, img_notification, img_chat, img_profile;
    FrameLayout ln_notification, ln_chat;

    public BottomBarProject(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.bottom_bar_project, this, true);
        ln_notification = (FrameLayout) findViewById(R.id.ln_notification);
        tv_num_notification = (TextView) findViewById(R.id.tv_num_notification);
        ln_chat = (FrameLayout) findViewById(R.id.ln_chat);
        tv_num_chat = (TextView) findViewById(R.id.tv_num_chat);
        ln_bottom_bar_project = (LinearLayout) findViewById(R.id.ln_bottom_bar_project);
        tab_home = (FrameLayout) findViewById(R.id.tab_home);
        tab_notification = (FrameLayout) findViewById(R.id.tab_notification);
        tab_chat = (FrameLayout) findViewById(R.id.tab_chat);
        tab_profile = (FrameLayout) findViewById(R.id.tab_profile);
        img_home = (ImageView) findViewById(R.id.img_home);
        img_notification = (ImageView) findViewById(R.id.img_notification);
        img_chat = (ImageView) findViewById(R.id.img_chat);
        img_profile = (ImageView) findViewById(R.id.ln_profile);
        btn_to_home = (LinearLayout) findViewById(R.id.btn_to_home);
        btn_to_notification = (LinearLayout) findViewById(R.id.btn_to_notification);
        btn_to_chat = (LinearLayout) findViewById(R.id.btn_to_chat);
        btn_to_profile = (LinearLayout) findViewById(R.id.btn_to_profile);
    }

    public void HideBottomBar() {
        ln_bottom_bar_project.setVisibility(GONE);
    }

    public void ShowBottomBar() {
        ln_bottom_bar_project.setVisibility(VISIBLE);
    }

    public void CheckOnClickTabHome(OnClickListener clickListener) {
        btn_to_home.setOnClickListener(clickListener);
    }

    public void CheckOnClickTabNotification(OnClickListener clickListener) {
        btn_to_notification.setOnClickListener(clickListener);
    }

    public void CheckOnClickTabChat(OnClickListener clickListener) {
        btn_to_chat.setOnClickListener(clickListener);
    }

    public void CheckOnClickTabProfile(OnClickListener clickListener) {
        btn_to_profile.setOnClickListener(clickListener);
    }

    public void ChangeBackgroundButtonHome(boolean isPressedHome){
        img_home.setBackground(null);
        if (!isPressedHome) {
            img_home.setBackgroundResource(R.drawable.home);
        }
        else {
            img_home.setBackgroundResource(R.drawable.home_downstate);
            img_notification.setBackgroundResource(R.drawable.notification);
            img_chat.setBackgroundResource(R.drawable.chat);
            img_profile.setBackgroundResource(R.drawable.profile);
        }
    }

    public void ChangeBackgroundButtonNotification(boolean isPressedNotification){
        img_notification.setBackground(null);
        if (!isPressedNotification) {
            img_notification.setBackgroundResource(R.drawable.notification);
        }
        else {
            img_home.setBackgroundResource(R.drawable.home);
            img_notification.setBackgroundResource(R.drawable.notification_downstate);
            img_chat.setBackgroundResource(R.drawable.chat);
            img_profile.setBackgroundResource(R.drawable.profile);
        }
    }

    public void ChangeBackgroundButtonChat(boolean isPressedNotification){
        img_chat.setBackground(null);
        if (!isPressedNotification) {
            img_chat.setBackgroundResource(R.drawable.chat);
        }
        else {
            img_home.setBackgroundResource(R.drawable.home);
            img_notification.setBackgroundResource(R.drawable.notification);
            img_chat.setBackgroundResource(R.drawable.chat_downstate);
            img_profile.setBackgroundResource(R.drawable.profile);
        }
    }

    public void ChangeBackgroundButtonProfile(boolean isPressedNotification){
        img_profile.setBackground(null);
        if (!isPressedNotification) {
            img_profile.setBackgroundResource(R.drawable.profile_downstate);
        }
        else {
            img_home.setBackgroundResource(R.drawable.home);
            img_notification.setBackgroundResource(R.drawable.notification);
            img_chat.setBackgroundResource(R.drawable.chat);
            img_profile.setBackgroundResource(R.drawable.profile_downstate);
        }
    }

    public void SetNumberNotification(int num){
        if(num == 0){
            ln_notification.setVisibility(GONE);
        }
        else {
            ln_notification.setVisibility(VISIBLE);
            tv_num_notification.setText(String.valueOf(num));
        }
    }

    public void SetNumberChat(int num){
        if(num == 0){
            ln_chat.setVisibility(GONE);
        }
        else {
            ln_chat.setVisibility(VISIBLE);
            tv_num_chat.setText(String.valueOf(num));
        }
    }
}
