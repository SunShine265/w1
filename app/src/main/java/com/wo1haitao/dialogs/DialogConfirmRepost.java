package com.wo1haitao.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wo1haitao.R;
import com.wo1haitao.adapters.ProductsMyPostAdapter;

/**
 * Created by user on 9/19/2017.
 */

public class DialogConfirmRepost extends Dialog {

    String msg;
    boolean isWarning;
    ProductsMyPostAdapter a;
    int id;

    public DialogConfirmRepost(@NonNull Context context, String msg, boolean isWarning, ProductsMyPostAdapter a, int id) {
        super(context);
        this.msg = msg;
        this.isWarning = isWarning;
        this.a = a;
        this.id = id;
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

        String btnCancel = DialogConfirmRepost.this.getContext().getResources().getString(R.string.btn_alert_negative_dispute);
        String btnAccept = DialogConfirmRepost.this.getContext().getResources().getString(R.string.btn_alert_positive_dispute);
        tv_cancel.setText(btnCancel);
        tv_ok.setText(btnAccept);
        if(!msg.equals("")){
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(DialogConfirmRepost.this.msg);
        }
        tvTitle.setText(R.string.title_alert_dispute);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        if(isWarning){
            tv_ok.setVisibility(View.GONE);
        }
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a instanceof ProductsMyPostAdapter){
                    a.RepostProduct(DialogConfirmRepost.this.id);
                    dismiss();
                }
            }
        });
    }
}
