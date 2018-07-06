package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wo1haitao.R;
import com.wo1haitao.adapters.ProductsMyPostAdapter;

/**
 * Created by user on 9/6/2017.
 */

public class DialogConfirmDelete extends Dialog {
    Activity activity;
    ProductsMyPostAdapter a;
    String message = "";
    int id;
    boolean isWarning = false;

    public DialogConfirmDelete(Activity activity, String message, ProductsMyPostAdapter a, int id, boolean isWarning) {
        super(activity);
        this.activity = activity;
        this.message = message;
        this.a = a;
        this.id = id;
        this.isWarning = isWarning;
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

        tvTitle.setText(R.string.title_alert_delete);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        if(!DialogConfirmDelete.this.message.equals("")){
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(DialogConfirmDelete.this.message);
        }
        if(isWarning){
            tv_ok.setVisibility(View.GONE);
        }
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a instanceof ProductsMyPostAdapter){
                    a.DeleteProduct(id);
                    dismiss();
                }
            }
        });
    }
}
