package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wo1haitao.R;

/**
 * Created by user on 9/6/2017.
 */

public class DialogConfirmEdit extends Dialog {
    Activity activity;
    String message = "";

    public DialogConfirmEdit(Activity activity, String message) {
        super(activity);
        this.activity = activity;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);
        TextView tv_cancel, tv_ok, tv_content, tvTitle;
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.title_alert_warning_edit);
        tv_content.setVisibility(View.VISIBLE);
        tv_content.setText(message);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_cancel.setVisibility(View.GONE);
        tv_ok.setVisibility(View.VISIBLE);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
