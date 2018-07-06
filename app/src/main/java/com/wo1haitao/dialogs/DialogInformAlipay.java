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

public class DialogInformAlipay extends Dialog {
    Activity activity;
    String content = "";

    public DialogInformAlipay(Activity activity, String content) {
        super(activity);
        this.activity = activity;
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_inform_alipay);
        TextView tv_cancel, tv_content;
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content.setText(content);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
