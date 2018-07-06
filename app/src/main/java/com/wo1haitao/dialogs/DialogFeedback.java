package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsReport;
import com.wo1haitao.api.response.RsReportResponse;
import com.wo1haitao.fragments.ProductDetailFragment;
import com.wo1haitao.utils.Utils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class DialogFeedback extends Dialog {

    public Activity c;
    TextView tv_send_report, tv_content, tvTitle;
    MyCallback listener;
    long productListingID;
    CheckBox checkBox1 ,checkBox2 ,checkBox3 ,checkBox4 ,checkBox5;
    String strReport = "", title = "", nameBtn = "", content = "";
    ProductDetailFragment f;
    boolean isCheck = false;
    LinearLayout ln_list_item;

    public DialogFeedback(Activity a, long id, ProductDetailFragment f,
                          String title, String nameBtn, String content) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.productListingID = id;
        if (a instanceof DialogFeedback.MyCallback) {
            listener = (DialogFeedback.MyCallback) a;
        }
        this.f = f;
        this.title = title;
        this.nameBtn = nameBtn;
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_feedback);
        tv_send_report = (TextView) findViewById(R.id.tv_send_report);
        tv_content = (TextView) findViewById(R.id.tv_content);
        ln_list_item = (LinearLayout) findViewById(R.id.ln_list_item);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        tvTitle.setText(title);
        tv_send_report.setText(nameBtn);
        if(!DialogFeedback.this.content.equals("")){
            ln_list_item.setVisibility(View.GONE);
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(content);
            tv_send_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        else {
            checkBox1 = (CheckBox) findViewById(R.id.checkBox);
            checkBox2 = (CheckBox) findViewById(R.id.checkBox1);
            checkBox3 = (CheckBox) findViewById(R.id.checkBox2);
            checkBox4 = (CheckBox) findViewById(R.id.checkBox3);
            checkBox5 = (CheckBox) findViewById(R.id.checkBox4);
            checkBox1.setOnCheckedChangeListener(checkboxListioner);
            checkBox2.setOnCheckedChangeListener(checkboxListioner);
            checkBox3.setOnCheckedChangeListener(checkboxListioner);
            checkBox4.setOnCheckedChangeListener(checkboxListioner);
            checkBox5.setOnCheckedChangeListener(checkboxListioner);
            tv_send_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null && isCheck == true) {
                        PostReport(strReport, productListingID);
                    }
                }
            });
        }
    }

    CheckBox.OnCheckedChangeListener checkboxListioner = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean b) {
            if(b == true ){
                isCheck = true;
                if (buttonView.getId() == R.id.checkBox) {
                    strReport = checkBox1.getText().toString();
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox4.setChecked(false);
                    checkBox5.setChecked(false);
                }
                if (buttonView.getId() == R.id.checkBox1) {
                    strReport = checkBox2.getText().toString();
                    checkBox1.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox4.setChecked(false);
                    checkBox5.setChecked(false);
                }
                if (buttonView.getId() == R.id.checkBox2) {
                    strReport = checkBox3.getText().toString();
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                    checkBox4.setChecked(false);
                    checkBox5.setChecked(false);
                }
                if (buttonView.getId() == R.id.checkBox3) {
                    strReport = checkBox4.getText().toString();
                    checkBox1.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox2.setChecked(false);
                    checkBox5.setChecked(false);
                }
                if (buttonView.getId() == R.id.checkBox4) {
                    strReport = checkBox5.getText().toString();
                    checkBox1.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox4.setChecked(false);
                    checkBox2.setChecked(false);
                }
            }
        }
    };

    public interface MyCallback {

        void changeFBack();
    }

    public void PostReport(String reasonReport, long productListingID){
        final ProgressDialog progressDialogLoading = Utils.createProgressDialog(c);
        final ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);

        Call<RsReportResponse> call = ws.actionPostReport(productListingID, reasonReport);
        call.enqueue(new Callback<RsReportResponse>() {
            @Override
            public void onResponse(Call<RsReportResponse> call, Response<RsReportResponse> response) {
                try {
                    if (response.body() != null)
                    {
                        RsReport rsReport = response.body();
                        progressDialogLoading.dismiss();
                        f.backPress();
                        dismiss();
                    }
                    else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialogLoading != null) {
                            progressDialogLoading.dismiss();
                        }
                    }
                    else {
                        if (progressDialogLoading != null) {
                            progressDialogLoading.dismiss();
                        }
                        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Utils.crashLog(e);
                    if (progressDialogLoading != null) {
                        progressDialogLoading.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<RsReportResponse> call, Throwable t) {
                if (progressDialogLoading != null) {
                    progressDialogLoading.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }
}
