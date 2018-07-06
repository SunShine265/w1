package com.wo1haitao.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import com.crashlytics.android.Crashlytics;
import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.CustomResponse;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.ResponsePacket;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/12/17.
 */

public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        ImageLoader.getInstance().displayImage("drawable://" + "splash_1_2x", (ImageView) findViewById(R.id.logo), OPTION_DISPLAY_IMG);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        else {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    if (MyPreferences.checkToken()) {
                        GetUserApi();
                    }
                    else {
            /* Create an Intent that will start the Menu-Activity. */
                       changeIntroActivity();
                    }
                }
            }, SPLASH_DISPLAY_LENGTH);
        }



    }


    private void changeIntroActivity(){
        Intent mainIntent = new Intent(SplashActivity.this, IntroActivity.class);
        startActivity(mainIntent);
        finish();
    }

//    public void SubcribePusher(String notify_scope_name) {
//        final String strNotifyScopeName = "notify@" + notify_scope_name;
//
//        if(MyPreferences.getKeySubscribeNative().isEmpty() == false){
//            CustomApp.getInstance().getPusher().nativePusher().unsubscribe(MyPreferences.getKeySubscribeNative());
//        }
//
//
//
//        CustomApp.getInstance().getPusher().connect(new ConnectionEventListener() {
//            @Override
//            public void onConnectionStateChange(ConnectionStateChange connectionStateChange) {
//                if(connectionStateChange.getCurrentState() == ConnectionState.CONNECTED){
//                    if(connectionStateChange.getCurrentState() == ConnectionState.CONNECTED){
//                        try {
//                            CustomApp.getInstance().getPusher().nativePusher().registerFCM(CustomApp.getInstance(), new PushNotificationRegistrationListener() {
//                                @Override
//                                public void onSuccessfulRegistration() {
//                                    CustomApp.getInstance().getPusher().nativePusher().subscribe(strNotifyScopeName, new InterestSubscriptionChangeListener() {
//                                        @Override
//                                        public void onSubscriptionChangeSucceeded() {
//                                            MyPreferences.setKeySubscribeNative(strNotifyScopeName);
//                                        }
//
//                                        @Override
//                                        public void onSubscriptionChangeFailed(int statusCode, String response) {
//                                            String s3 = "1";
//                                        }
//                                    });
//                                }
//
//                                @Override
//                                public void onFailedRegistration(int statusCode, String response) {
//                                    String s3 = "1";
//                                }
//                            });
//                        } catch (ManifestValidator.InvalidManifestException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//            }
//
//            @Override
//            public void onError(String s, String s1, Exception e) {
//                String s3 = "1";
//            }
//        });
//    }

    public void GetUserApi() {

        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        ws.actionGetUser().enqueue(new Callback<ResponseMessage<UserProfile>>() {
            @Override
            public void onResponse(Call<ResponseMessage<UserProfile>> call, Response<ResponseMessage<UserProfile>> response) {
                boolean isLoginSuccess = false;

                try {

                    ResponsePacket response_packet = CustomResponse.getResponseMessage(response);
                    ResponseMessage responseMessage;

                    if (response_packet.isSet()) {
                        responseMessage = response_packet.getResponseMessage();
                        if (response_packet.isSuccess()) {
                            if (responseMessage.isSuccess()) {
                                UserProfile userProfile = response.body().getData();
//                                SubcribePusher(userProfile.getNotify_scope_name());
                                Intent it = new Intent(SplashActivity.this, MainActivity.class);
                                if(userProfile.getSetup_account().getSetup_category()) {
                                    it.putExtra("setup-category", "setup");
                                }
                                isLoginSuccess = true;
                                PackageManager packageManager = getPackageManager();
                                if (it.resolveActivity(packageManager) != null) {
                                    startActivity(it);
                                    overridePendingTransition(R.anim.new_activity, R.anim.empty_ani);
                                }
                                finish();


                            }
                        }
                    }
                } catch (Exception e) {
                    Crashlytics.logException(e);
                }

                if (!isLoginSuccess) {
                    Intent it = new Intent(SplashActivity.this, IntroActivity.class);

                    PackageManager packageManager = getPackageManager();
                    if (it.resolveActivity(packageManager) != null) {
                        startActivity(it);
                        overridePendingTransition(R.anim.new_activity, R.anim.empty_ani);
                    }
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                Intent it = new Intent(SplashActivity.this, IntroActivity.class);
                PackageManager packageManager = getPackageManager();
                if (it.resolveActivity(packageManager) != null) {
                    startActivity(it);
                    overridePendingTransition(R.anim.new_activity, R.anim.empty_ani);
                }
                finish();
                //No internet connection or can not connect server!

//                            Intent it = new Intent(IntroActivity.this,LoginActivity.class);
//                            startActivity(it);

//                if (t instanceof java.net.UnknownHostException) {
//                    Toast.makeText(SplashActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
//                } else {
//                    Crashlytics.logException(t);
//                }
                Utils.OnFailException(t);
            }
        });
    }
}
