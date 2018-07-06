package com.wo1haitao.activities;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.request.RequestUser;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/14/17.
 */

public class RegisterActivity extends BaseActivity {

    EditText et_password, et_name, et_mail, et_confirm_password;
    FrameLayout fl_message;
    TextView tv_message, tv_term, tv_conditon;
    ScrollView scrollview;
    ProgressDialog pd_dialog;
    FragmentTransaction frag_trans;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getActionbar(R.string.register);
//        TextView txt_terms = (TextView) findViewById(R.id.txt_terms);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        et_name = (EditText) findViewById(R.id.et_name);
        et_mail = (EditText) findViewById(R.id.et_mail);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        et_password = (EditText) findViewById(R.id.et_password);
        fl_message = (FrameLayout) findViewById(R.id.fl_message);
        tv_message = (TextView) findViewById(R.id.tv_message);
        pd_dialog = Utils.createProgressDialog(this);
        tv_term = (TextView) findViewById(R.id.tv_term);
        tv_conditon = (TextView) findViewById(R.id.tv_condition);
        pd_dialog.dismiss();
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Button btn_to_login = (Button) findViewById(R.id.btn_to_login);
        btn_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getStringExtra("login") != null){
                    onBackPressed();
                }
                else {
                    Intent it = new Intent(RegisterActivity.this, LoginActivity.class);
                    it.putExtra("register", "true");
                    PackageManager packageManager = getPackageManager();
                    if (it.resolveActivity(packageManager) != null) {
                        startActivity(it);
                    }
                }
            }
        });
        Button btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyWhenClick(RegisterActivity.this);
                pd_dialog.show();
                RequestUser requestUser = new RequestUser(et_name.getText().toString(), et_mail.getText().toString(), et_password.getText().toString(), et_confirm_password.getText().toString());
                ApiServices webService = ApiServices.getInstance();
                WebService api = webService.getRetrofit().create(WebService.class);
                api.actionRegister(requestUser.getNickname(), requestUser.getEmail(), requestUser.getPassword(), requestUser.getPassword_confirmation()).enqueue(new Callback<ResponseMessage<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ResponseMessage<UserProfile>> call, Response<ResponseMessage<UserProfile>> response) {
                        try {
                            pd_dialog.dismiss();
                            Response<ResponseMessage<UserProfile>> response1 = response;
                            response1.message();
                            if (response.body() != null) {
                                ResponseMessage responseMessage = response.body();
                                if (responseMessage.isSuccess()) {
                                    UserProfile user = (UserProfile) responseMessage.getData();
                                    user.getId();
                                    fl_message.setVisibility(View.GONE);
                                    Intent i = new Intent(RegisterActivity.this, MailActiveActivity.class);
                                    i.putExtra("email", et_mail.getText().toString());
                                    i.putExtra("password", et_password.getText().toString());
                                    customStartActivity(i);


                                }
                            } else if (response.errorBody() != null) {
                                try {
                                    ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);

                                    String strError = "";
                                    ArrayList<String> list_error = responseMessage.getError_messages();
                                    if(list_error != null){
                                        for(String error_item :list_error){
                                            if(strError.isEmpty() == true){
                                                strError = error_item;
                                            }
                                            else{
                                                strError += "\n"+error_item;
                                            }
                                        }
                                    }
                                    if(!strError.equals("")) {
                                        tv_message.setText(strError);
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
                            } else {
                                Toast.makeText(RegisterActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Utils.crashLog(e);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                        pd_dialog.dismiss();
                        Utils.OnFailException(t);
                    }
                });
            }
        });

        tv_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(RegisterActivity.this, InformationTermActivity.class);
                it.putExtra("Term", "term");
                PackageManager packageManager = getPackageManager();
                if (it.resolveActivity(packageManager) != null) {
                    startActivity(it);
                }
//                InformationTermFragment fragment = new InformationTermFragment();
//                fragment.setFlag("Terms");
//                changeFragment(fragment, true);
            }
        });
        tv_conditon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(RegisterActivity.this, InformationTermActivity.class);
                PackageManager packageManager = getPackageManager();
                if (it.resolveActivity(packageManager) != null) {
                    it.putExtra("Condition", "condition");
                    startActivity(it);
                }
            }
        });
//        if (Build.VERSION.SDK_INT >= 24) {
//            txt_terms.setText(fromHtml(getString(R.string.form_check_terms), Html.FROM_HTML_MODE_LEGACY));
//        } else {
//            txt_terms.setText(fromHtml(getString(R.string.form_check_terms)));
//        }
//        txt_terms.setMovementMethod(CustomLinkMovementMethod.getInstance(this));

    }


    public void pushToLogin(View view) {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        PackageManager packageManager = getPackageManager();
        if (i.resolveActivity(packageManager) != null) {
            startActivity(i);
        }
    }
}
