package com.wo1haitao.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.CustomResponse;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.ResponsePacket;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/14/17.
 */

public class ForgetPasswordActivity extends BaseActivity {

    EditText edt_email;
    ScrollView scrollview;
    TextView tv_message;
    FrameLayout fl_message;
    ProgressDialog pd_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getActionbar(R.string.form_forget_password);
        Button btn_forget_password = (Button) findViewById(R.id.btn_forget_password);
        edt_email = (EditText) findViewById(R.id.et_mail);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        tv_message = (TextView) findViewById(R.id.tv_message);
        fl_message = (FrameLayout) findViewById(R.id.fl_message);

        btn_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_email.getText().toString().equals("")) {
                    tv_message.setText("请输入电子邮件地址!");
                    fl_message.setVisibility(View.VISIBLE);
                    scrollview.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollview.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    return;
                }
                pd_dialog = Utils.createProgressDialog(ForgetPasswordActivity.this);
                pd_dialog.show();
                Utils.hideSoftKeyWhenClick(ForgetPasswordActivity.this);
                ApiServices api = ApiServices.getInstance();
                WebService ws = api.getRetrofit().create(WebService.class);
                ws.actionResetPassword(edt_email.getText().toString()).enqueue(new Callback<ResponseMessage<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ResponseMessage<UserProfile>> call, Response<ResponseMessage<UserProfile>> response) {
                        try {
                            pd_dialog.dismiss();
                            ResponsePacket response_packet = CustomResponse.getResponseMessage(response);
                            ResponseMessage responseMessage;
                            if (response_packet.isSet()) {
                                responseMessage = response_packet.getResponseMessage();
                                if (response_packet.isSuccess()) {
                                    if (responseMessage.isSuccess()) {
                                        Intent i = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                        i.putExtra("screen", "mail-activity");
                                        customStartActivity(i);
                                        fl_message.setVisibility(View.GONE);
                                    }
                                } else {
                                    if (responseMessage.isSuccess()) {
                                        Intent i = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                        i.putExtra("screen", "mail-activity");
                                        customStartActivity(i);
                                    } else {
                                        tv_message.setText("无法发送电子邮件，请检查您的电子邮件地址!");
                                        fl_message.setVisibility(View.VISIBLE);
                                        scrollview.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                scrollview.fullScroll(ScrollView.FOCUS_UP);
                                            }
                                        });
                                    }
                                }
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Utils.crashLog(e);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                        Utils.OnFailException(t);
                    }
                });

            }
        });

    }


}
