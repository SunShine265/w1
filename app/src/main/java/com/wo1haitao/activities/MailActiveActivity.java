package com.wo1haitao.activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.CustomResponse;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.ResponsePacket;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/17/17.
 */

public class MailActiveActivity extends BaseActivity {

    ProgressDialog pd_dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_active);
        Button btn_to_login = (Button) findViewById(R.id.btn_to_login);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_custom_logo_title);


        btn_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = getIntent().getExtras().getString("email");
                String pass = getIntent().getExtras().getString("password");
                pd_dialog = Utils.createProgressDialog(MailActiveActivity.this);
                pd_dialog.show();
                ApiServices api = ApiServices.getInstance();
                WebService ws = api.getRetrofit().create(WebService.class);
                ws.actionLogin(mail, pass).enqueue(new Callback<ResponseMessage<UserProfile>>() {
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
                                        Headers header = response.headers();
                                        MyPreferences.setUID(header.get(MyPreferences.UID_FIELD));
                                        MyPreferences.setToken(header.get(MyPreferences.TOKEN_FIELD));
                                        MyPreferences.setClient(header.get(MyPreferences.CLIENT_FIELD));
                                        MyPreferences.setExpiryField(header.get(MyPreferences.EXPIRY_FIELD));
                                        Intent i = new Intent(MailActiveActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        i.putExtra("screen", "mail-activity");

                                        PackageManager packageManager = getPackageManager();
                                        if (i.resolveActivity(packageManager) != null) {
                                            startActivity(i);
                                        }
                                        finish();
                                    }
                                } else {
                                    try {
                                        ErrorMessage error = responseMessage.getErrors();
                                        Toast.makeText(MailActiveActivity.this, "检查你的电子邮件!", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                                        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
//                                        startActivity(intent);
                                        Intent i = new Intent(MailActiveActivity.this, LoginActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        i.putExtra("screen", "mail-activity");

                                        PackageManager packageManager = getPackageManager();
                                        if (i.resolveActivity(packageManager) != null) {
                                            startActivity(i);
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                Toast.makeText(MailActiveActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Utils.crashLog(e);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                        Utils.OnFailException(t);
                        pd_dialog.dismiss();
                    }
                });
            }
        });
    }


}
