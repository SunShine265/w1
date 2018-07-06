package com.wo1haitao.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
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
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.ResponsePacket;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;

import me.pushy.sdk.Pushy;
import me.pushy.sdk.util.exceptions.PushyException;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/14/17.
 */

public class LoginActivity extends BaseActivity {
    EditText et_mail, et_password;
    ScrollView scrollview;
    TextView tv_message;
    FrameLayout fl_message;
    SharedPreferences token_sha;
    ProgressDialog pd_dialog;
    String notifyScopeName="";

    UserProfile user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActionbar(R.string.title_login);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        tv_message = (TextView) findViewById(R.id.tv_message);
        fl_message = (FrameLayout) findViewById(R.id.fl_message);
        Button btn_to_register = (Button) findViewById(R.id.btn_to_register);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        TextView txt_send_mail = (TextView) findViewById(R.id.txt_send_mail);
        TextView txt_resend_mail = (TextView) findViewById(R.id.txt_resend_mail);
        TextView txt_forget_password = (TextView) findViewById(R.id.txt_forget_password);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        et_mail = (EditText) findViewById(R.id.et_mail);
        et_password = (EditText) findViewById(R.id.et_password);
        if(getIntent().getStringExtra("screen") != null){
            tv_message.setText("您将收到一封确认邮件，请注意查收您的电子邮箱。");
            fl_message.setVisibility(View.VISIBLE);
            txt_send_mail.setVisibility(View.GONE);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd_dialog = Utils.createProgressDialog(LoginActivity.this);
                String mail = et_mail.getText().toString();
                String pass = et_password.getText().toString();
                Utils.hideSoftKeyWhenClick(LoginActivity.this);
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
                                        fl_message.setVisibility(View.GONE);
                                        user = (UserProfile) responseMessage.getData();
                                        notifyScopeName = user.getNotify_scope_name();
//                                        SubcribePusher(notifyScopeName);
                                        GetRegisterDevice();
                                        Headers header = response.headers();
                                        MyPreferences.setID(user.getId());
                                        MyPreferences.setUID(header.get(MyPreferences.UID_FIELD));
                                        MyPreferences.setToken(header.get(MyPreferences.TOKEN_FIELD));
                                        MyPreferences.setClient(header.get(MyPreferences.CLIENT_FIELD));
                                        MyPreferences.setExpiryField(header.get(MyPreferences.EXPIRY_FIELD));

                                        Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                        if(user.getSetup_account().getSetup_category()) {
                                            it.putExtra("setup-category", "setup");
                                        }
                                        customStartActivity(it);
                                        LoginActivity.this.finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    try {
                                        ErrorMessage error = responseMessage.getErrors();
                                        if (error.getStringError().contains("translation missing")) {
                                            Intent it = new Intent(LoginActivity.this, MailActiveActivity.class);
                                            it.putExtra("email", et_mail.getText().toString());
                                            it.putExtra("password", et_password.getText().toString());
                                            customStartActivity(it);
                                            return;
                                        }
                                        if(et_mail.getText().toString().isEmpty() == false || et_password.getText().toString().isEmpty() == false ){

                                            tv_message.setText("邮箱或密码错误");
                                            fl_message.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            tv_message.setText(error.getStringError());
                                            fl_message.setVisibility(View.VISIBLE);
                                        }

                                        scrollview.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                scrollview.fullScroll(ScrollView.FOCUS_UP);
                                            }
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Utils.crashLog(e);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                        pd_dialog.dismiss();
                        Utils.OnFailException(t);
//                        Toast.makeText(LoginActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                });

            }


        });


        btn_to_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(getIntent().getStringExtra("register") != null){
                    onBackPressed();
                }
                else {
                    Utils.hideSoftKeyWhenClick(LoginActivity.this);
                    Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                    i.putExtra("login", "true");
                    customStartActivity(i);
                }

            }
        });

        txt_resend_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, MailResendActivity.class);
                customStartActivity(i);
            }
        });

        txt_resend_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, MailResendActivity.class);
                customStartActivity(i);
            }
        });

        txt_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                customStartActivity(i);

            }
        });


        Bundle bundle = getIntent().getExtras();

//        if (bundle != null) {
//            String screen = bundle.getString("screen");
//            if (screen.equals("mail-activity")) {
//                txt_send_mail.setText(getResources().getString(R.string.txt_wait_send_mail));
//            }
//        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    /**
     * Subribe pusher
     * @param:
     *  notify_scope_name
     * @return:
     * */
    /*public void SubcribePusher(String notify_scope_name){
        final String strNotifyScopeName = "notify@" + notify_scope_name;

        if(MyPreferences.getKeySubscribeNative().isEmpty() == false){
            CustomApp.getInstance().getPusher().nativePusher().unsubscribe(MyPreferences.getKeySubscribeNative());
        }



        CustomApp.getInstance().getPusher().connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange connectionStateChange) {
                if(connectionStateChange.getCurrentState() == ConnectionState.CONNECTED){
                    try {
                        CustomApp.getInstance().getPusher().nativePusher().registerFCM(CustomApp.getInstance(), new PushNotificationRegistrationListener() {
                            @Override
                            public void onSuccessfulRegistration() {
                                CustomApp.getInstance().getPusher().nativePusher().subscribe(strNotifyScopeName, new InterestSubscriptionChangeListener() {
                                    @Override
                                    public void onSubscriptionChangeSucceeded() {
                                        MyPreferences.setKeySubscribeNative(strNotifyScopeName);
                                    }

                                    @Override
                                    public void onSubscriptionChangeFailed(int statusCode, String response) {
                                        String s3 = "1";
                                    }
                                });
                            }

                            @Override
                            public void onFailedRegistration(int statusCode, String response) {
                                String s3 = "1";
                            }
                        });
                    } catch (ManifestValidator.InvalidManifestException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(String s, String s1, Exception e) {

                String s3 = "1";
            }
        });
    }*/

    private void GetRegisterDevice(){
        final Handler UIHandler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String deviceToken = Pushy.register(getApplicationContext());

                    UIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            final ApiServices api = ApiServices.getInstance();
                            WebService ws = api.getRetrofit().create(WebService.class);
                            MyPreferences.setDeviceToken(deviceToken);
                            Call<ResponseMessage> call = ws.actionRegisterDevice(deviceToken);
                            call.enqueue(new Callback<ResponseMessage>() {
                                @Override
                                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                    if(response.code() != 200){
                                        Toast.makeText(LoginActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                                    Utils.OnFailException(t);
                                }
                            });
                        }
                    });
                } catch (PushyException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
