package com.wo1haitao.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.dialogs.DialogPermission;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyActivity extends AppCompatActivity {
    ActionBarProject my_action_bar;
    ImageView iv_ID;
    TextView tv_top, tv_bottom;
    static String STATE_VERIFY_OPEN = "opened";
    static String STATE_VERIFY_REJECT = "rejected";
    static String STATE_VERIFY_CLOSE = "closed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        initControl();

    }

    private void initControl() {
        my_action_bar = (ActionBarProject) findViewById(R.id.my_action_bar);
//        btn_to_main = (Button) findViewById(R.id.btn_to_main);
        my_action_bar.showTitle(R.string.title_verify);
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //my_action_bar.changeButtonBack();
                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
//        ImageView verifies_user, id_verifies;
//        TextView tv_name_us;
//        RoundedImageView roundedImageUser;
//
//        verifies_user = (ImageView) findViewById(R.id.verifies_user);
//        id_verifies = (ImageView) findViewById(R.id.id_verifies);
//        tv_name_us = (TextView) findViewById(R.id.tv_name_us);
//        roundedImageUser = (RoundedImageView) findViewById(R.id.roundedImageUser);

//        btn_to_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//
//            }
//        });
        tv_top = (TextView) findViewById(R.id.tv_top);
        tv_bottom = (TextView) findViewById(R.id.tv_bottom);
        iv_ID = (ImageView) findViewById(R.id.iv_ID);
        GetUserApi();
        iv_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result;
                String mPermissions[] = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                List<String> listPermissionsNeeded = new ArrayList<>();

                for (String p : mPermissions) {
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(VerifyActivity.this, p);
                    if(ContextCompat.checkSelfPermission(VerifyActivity.this, p) == PackageManager.PERMISSION_DENIED){
                        if (ContextCompat.checkSelfPermission(VerifyActivity.this, p) == PackageManager.PERMISSION_DENIED && !showRationale) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    DialogPermission dialogPermission = new DialogPermission(VerifyActivity.this);
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(dialogPermission.getWindow().getAttributes());
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                    dialogPermission.show();
                                }
                            });
                            return;
                        } else {
                            result = ContextCompat.checkSelfPermission(VerifyActivity.this, p);
                            if (result != PackageManager.PERMISSION_GRANTED) {
                                listPermissionsNeeded.add(p);
                            }
                        }
                    }
                }

                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions(VerifyActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1003);
                }
                else {
                    Intent takePhotoIntent = Utils.getTakePictureIntent(VerifyActivity.this);
                    startActivityForResult(takePhotoIntent, Utils.RQ_CODE_GET_IMAGE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.RQ_CODE_GET_IMAGE) {
            if (resultCode == RESULT_OK) {
                Utils.getImageView(iv_ID, data, VerifyActivity.this);
//                btn_to_main.setEnabled(true);
                PostVerification();
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
        final ProgressDialog progressDialog = Utils.createProgressDialog(VerifyActivity.this);
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
                       // Toast.makeText(VerifyActivity.this, "恭喜您已成功上传您的身份证", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } catch (Exception e) {
                        Utils.crashLog(e);
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
     * Get user me
     *
     * @params:
     * @return:
     */
    public void GetUserApi() {
        final ProgressDialog progressDialogGetData = Utils.createProgressDialog(VerifyActivity.this);
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        ws.actionGetUser().enqueue(new Callback<ResponseMessage<UserProfile>>() {
            @Override
            public void onResponse(Call<ResponseMessage<UserProfile>> call, Response<ResponseMessage<UserProfile>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        UserProfile userProfile = response.body().getData();
                        if (userProfile.getVerification_state().equals(STATE_VERIFY_OPEN)) {
                            //iv_ID.setImageDrawable(null);

                            if(userProfile.getIdentity_image().getUrl().isEmpty() == false) {
                                tv_top.setText("申请已提交，正在等待我要海淘网的审核");
                                tv_bottom.setVisibility(View.VISIBLE);
                                tv_bottom.setText("验证过程需要一到三日来完成。");
                                String urlVerify = ApiServices.BASE_URI + userProfile.getIdentity_image().getUrl();
                                DiskCacheUtils.removeFromCache(urlVerify, ImageLoader.getInstance().getDiskCache());
                                MemoryCacheUtils.removeFromCache(urlVerify, ImageLoader.getInstance().getMemoryCache());
                                ImageLoader il = ImageLoader.getInstance();
                                il.displayImage(urlVerify, iv_ID);
                                iv_ID.setEnabled(false);
//                                btn_to_main.setVisibility(View.VISIBLE);
                            }
                            else {
                                tv_top.setText("请用户拍摄手持本人身份证身份证的照片");
//                                tv_bottom.setText("请参照示范");
                                iv_ID.setEnabled(true);
//                                btn_to_main.setVisibility(View.GONE);
                            }
//                            btn_to_main.setEnabled(true);
                        }
                        else if(userProfile.getVerification_state().equals(STATE_VERIFY_REJECT)){
                            tv_top.setText("请用户拍摄手持本人身份证身份证的照片");
//                                tv_bottom.setText("请参照示范");
                            iv_ID.setEnabled(true);
                            //iv_ID.setImageDrawable(null);

                            //iv_ID.setBackgroundResource(R.drawable.upload_id_here);
//                            btn_to_main.setEnabled(false);
                        }
                        else if(userProfile.getVerification_state().equals(STATE_VERIFY_CLOSE)){
                            iv_ID.setEnabled(false);
                            iv_ID.setImageDrawable(null);
//                            tv_bottom.setVisibility(View.INVISIBLE);
                            tv_top.setText("恭喜您已通过实名认证");
                            String urlVerify = ApiServices.BASE_URI + userProfile.getIdentity_image().getUrl();
                            DiskCacheUtils.removeFromCache(urlVerify, ImageLoader.getInstance().getDiskCache());
                            MemoryCacheUtils.removeFromCache(urlVerify, ImageLoader.getInstance().getMemoryCache());
                            ImageLoader il = ImageLoader.getInstance();
                            il.displayImage(urlVerify, iv_ID);
//                            btn_to_main.setEnabled(true);
                        }
                        progressDialogGetData.dismiss();
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(VerifyActivity.this, "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Crashlytics.logException(e);
                            Toast.makeText(VerifyActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                        if(progressDialogGetData != null){
                            progressDialogGetData.dismiss();
                        }
                    } else {
                        if(progressDialogGetData != null){
                            progressDialogGetData.dismiss();
                        }
                        Toast.makeText(VerifyActivity.this, R.string.no_advesting, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    if(progressDialogGetData != null){
                        progressDialogGetData.dismiss();
                    }
                    Crashlytics.logException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                if(progressDialogGetData != null){
                    progressDialogGetData.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }
}
