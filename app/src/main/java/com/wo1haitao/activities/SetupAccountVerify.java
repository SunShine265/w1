package com.wo1haitao.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.CustomResponse;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.ResponsePacket;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 8/21/2017.
 */

public class SetupAccountVerify extends AppCompatActivity {
    ActionBarProject my_action_bar;
    ImageView iv_ID;
    Button btn_to_main;
    TextView btn_skip_verify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_account_verify);
        initControl();
    }

    private void initControl() {
        my_action_bar = (ActionBarProject) findViewById(R.id.my_action_bar);
        btn_to_main = (Button) findViewById(R.id.btn_to_main);
        iv_ID = (ImageView) findViewById(R.id.iv_ID);
        btn_skip_verify = (TextView) findViewById(R.id.btn_skip_verify);

        my_action_bar.showTitle(R.string.title_verify);
        iv_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePhotoIntent = Utils.getTakePictureIntent(SetupAccountVerify.this);
                startActivityForResult(takePhotoIntent, Utils.RQ_CODE_GET_IMAGE);
            }
        });

        GetUser();
        btn_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostVerification();
            }
        });
        btn_skip_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SetupAccountVerify.this, MainActivity.class);
                it.putExtra("finish_setup_account", "finish");
                it.putExtra("setup-category", "setup");
                PackageManager packageManager = getPackageManager();
                if (it.resolveActivity(packageManager) != null) {
                    startActivity(it);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.RQ_CODE_GET_IMAGE) {
            if (resultCode == RESULT_OK) {
                Utils.getImageView(iv_ID, data, SetupAccountVerify.this);
                btn_to_main.setEnabled(true);
            }
        }
    }

    /**
     * Post verification
     *
     * @params:
     * @return:
     */
    public void PostVerification() {
        final ProgressDialog progressDialog = Utils.createProgressDialog(SetupAccountVerify.this);
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        if (iv_ID.getDrawable() != null) {
            MultipartBody.Part imageInvoice = Utils.createImagePart("identity_image", "identity_image" + ".png", iv_ID);
            Call<ResponseMessage> call = ws.actionPostVerification(imageInvoice);
            call.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    try {
                        progressDialog.dismiss();
                        Intent it = new Intent(SetupAccountVerify.this, MainActivity.class);
                        it.putExtra("finish_setup_account", "finish");
                        it.putExtra("setup-category", "setup");
                        PackageManager packageManager = getPackageManager();
                        if (it.resolveActivity(packageManager) != null) {
                            startActivity(it);
                        }
                        Toast.makeText(SetupAccountVerify.this, "恭喜您已成功上传您的身份证", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Utils.crashLog(e);
                        Crashlytics.logException(e);
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Utils.OnFailException(t);
                }
            });
        }
    }

    /**
     * Get User
     * @params:
     * @return:
     * */
    protected void GetUser() {
        final ProgressDialog progressLoading = Utils.createProgressDialog(SetupAccountVerify.this);
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        ws.actionGetUser().enqueue(new Callback<ResponseMessage<UserProfile>>() {
            @Override
            public void onResponse(Call<ResponseMessage<UserProfile>> call, Response<ResponseMessage<UserProfile>> response) {
                try {
                    ResponsePacket response_packet = CustomResponse.getResponseMessage(response);
                    ResponseMessage responseMessage;
                    if (response_packet.isSet()) {
                        responseMessage = response_packet.getResponseMessage();
                        if (response_packet.isSuccess()) {
                            if (responseMessage.isSuccess()) {
                                UserProfile user = (UserProfile) responseMessage.getData();
                                if(user.getSetup_account().getSetup_verification() == true){
                                    Intent it = new Intent(SetupAccountVerify.this, MainActivity.class);
                                    it.putExtra("finish_setup_account", "finish");
                                    it.putExtra("setup-category", "setup");
                                    PackageManager packageManager = getPackageManager();
                                    if (it.resolveActivity(packageManager) != null) {
                                        startActivity(it);
                                    }
                                }
                                progressLoading.dismiss();
                            }
                        } else {
                            try {
                                ErrorMessage error = responseMessage.getErrors();
                                Toast.makeText(SetupAccountVerify.this, error.getStringError(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(progressLoading != null){
                                progressLoading.dismiss();
                            }
                        }
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                    Crashlytics.logException(e);
                    if(progressLoading != null){
                        progressLoading.dismiss();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                if(progressLoading != null){
                    progressLoading.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }
}
