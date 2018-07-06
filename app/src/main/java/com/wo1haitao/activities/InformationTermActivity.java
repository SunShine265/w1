package com.wo1haitao.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsInformationTermsModel;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 8/25/2017.
 */

public class InformationTermActivity extends AppCompatActivity {
    final static String TERM = "服务条款";
    final static String CONDITION = "隐私政策";
    final static String FLAG_TERM = "Terms";
    final static String FLAG_CONDITION = "Conditions";
    WebView wv_information;
    ActionBarProject my_action_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_information);
        initControl();
    }

    private void initControl() {
        wv_information = (WebView) findViewById(R.id.wv_information);
        my_action_bar = (ActionBarProject) findViewById(R.id.my_action_bar);
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (getIntent().getStringExtra("Term") != null) {
            GetTermsInformation(TERM);
        } else if (getIntent().getStringExtra("Condition") != null) {
            GetTermsInformation(CONDITION);
        }
    }

    /**
     * Get terms information
     *
     * @params:
     * @return:
     */
    public void GetTermsInformation(String flag) {

        String paramID = "";
        try {
            paramID = URLEncoder.encode(flag, "utf-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<RsInformationTermsModel> call = ws.actionGetInformationTerms(paramID);
        call.enqueue(new Callback<RsInformationTermsModel>() {
            @Override
            public void onResponse(Call<RsInformationTermsModel> call, Response<RsInformationTermsModel> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        RsInformationTermsModel rsInformationTermsModel = response.body();
                        my_action_bar.showTitle(rsInformationTermsModel.getName());
                        wv_information.loadData(rsInformationTermsModel.getContent(), "text/html; charset=utf-8", "UTF-8");
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(InformationTermActivity.this, "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(InformationTermActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(InformationTermActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<RsInformationTermsModel> call, Throwable t) {
                Utils.OnFailException(t);
            }
        });
    }
}
