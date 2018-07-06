package com.wo1haitao.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.controls.CustomEditextSoftkey;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 9/22/2017.
 */

public class ChangePasswordFragment extends BaseFragment {
    View viewFragment;
    ActionBarProject actionBar;
    CustomEditextSoftkey et_current_password, et_new_password, et_confirm_password, et_id_alipay, et_id_alipay_confirm, et_alipay_name;
    FrameLayout fl_message, fl_error_mes_alipay;
    TextView tv_message, tv_bottom, tv_error_alipay;
    Button btn_to_main, bt_save_alipay;
    ImageView img_alipay_empty;
    public String alipay_name = "", alipay_id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewFragment = inflater.inflate(R.layout.fragment_change_password, container, false);
        actionBar = (ActionBarProject) viewFragment.findViewById(R.id.my_action_bar);
        actionBar.showTitle(R.string.txt_change_password);
        actionBar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        fl_message = (FrameLayout) viewFragment.findViewById(R.id.fl_message);
        tv_message = (TextView) viewFragment.findViewById(R.id.tv_message);
        btn_to_main = (Button) viewFragment.findViewById(R.id.btn_to_main);
        tv_bottom = (TextView) viewFragment.findViewById(R.id.tv_bottom);
        et_id_alipay = (CustomEditextSoftkey) viewFragment.findViewById(R.id.edt_id);
        et_alipay_name = (CustomEditextSoftkey) viewFragment.findViewById(R.id.edt_name_alipay);
        et_id_alipay_confirm = (CustomEditextSoftkey) viewFragment.findViewById(R.id.edt_id_confirm);
        et_current_password = (CustomEditextSoftkey) viewFragment.findViewById(R.id.et_current_password);
        et_new_password = (CustomEditextSoftkey) viewFragment.findViewById(R.id.et_new_password);
        et_confirm_password = (CustomEditextSoftkey) viewFragment.findViewById(R.id.et_confirm_password);
        fl_error_mes_alipay = (FrameLayout) viewFragment.findViewById(R.id.fl_message_alipay);
        tv_error_alipay = (TextView) viewFragment.findViewById(R.id.tv_message_alipay);
        bt_save_alipay = (Button) viewFragment.findViewById(R.id.btn_save_alipay);
        img_alipay_empty = (ImageView) viewFragment.findViewById(R.id.img_empty_alipay);
        btn_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) ChangePasswordFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(viewFragment.getWindowToken(), 0);
                UpdatePassword();
            }
        });
        if(alipay_id.isEmpty() == true || alipay_name.isEmpty() == true){
            img_alipay_empty.setVisibility(View.VISIBLE);
        }
        else{
            img_alipay_empty.setVisibility(View.INVISIBLE);
        }
        et_alipay_name.setText(alipay_name);
        et_id_alipay.setText(alipay_id);
        bt_save_alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) ChangePasswordFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(viewFragment.getWindowToken(), 0);
                UpdateAlipay(true,et_id_alipay.getText().toString(),et_alipay_name.getText().toString(), et_id_alipay_confirm.getText().toString());
            }
        });

        return viewFragment;
    }

    private void UpdatePassword() {
        final ApiServices apiServices = ApiServices.getInstance();
        final ProgressDialog progressDialog = Utils.createProgressDialog(ChangePasswordFragment.this.getActivity());
        WebService webService = apiServices.getRetrofit().create(WebService.class);
        Call<ResponseMessage> call = webService.actionUpdatePassword(et_new_password.getText().toString(), et_confirm_password.getText().toString(), et_current_password.getText().toString());
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    tv_bottom.setVisibility(View.VISIBLE);
                    fl_message.setVisibility(View.GONE);
                    et_current_password.setText("");
                    et_new_password.setText("");
                    et_confirm_password.setText("");
                    viewFragment.clearFocus();
                    viewFragment.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            backPress();
                        }
                    }, 1000);
//                   backPress();
                } else {
                    try {
                        ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                        ArrayList<String> list_error = responseMessage.getError_messages();
                        String strError = "";
                        if (list_error != null) {
                            for (String error_item : list_error) {
                                if (strError.isEmpty() == true) {
                                    strError = "\u2022 " + error_item;
                                } else {
                                    strError += "\n\u2022 " + error_item;
                                }
                            }
                        }
                        if (!strError.equals("")) {
                            tv_message.setText(strError);
                            fl_message.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                progressDialog.dismiss();
                tv_message.setText(R.string.something_wrong);
                fl_message.setVisibility(View.VISIBLE);
            }
        });
    }

    public void UpdateAlipay(boolean checkDetails, String alipayID, String alipayName, String alipayIDConfirm) {
        final ProgressDialog progressDialog = Utils.createProgressDialog(ChangePasswordFragment.this.getActivity());
        ApiServices services = ApiServices.getInstance();
        WebService api = services.getRetrofit().create(WebService.class);
        Call<ResponseMessage> callUpdateAlipay = api.actionUpdateAlipay(checkDetails, alipayID, alipayName, alipayIDConfirm);
        callUpdateAlipay.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                progressDialog.dismiss();
                try {
                    if (response.code() == 200) {
                        fl_error_mes_alipay.setVisibility(View.GONE);
                        viewFragment.clearFocus();
                        viewFragment.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                backPress();
                            }
                        }, 1000);
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            //ErrorMessage error = responseMessage.getErrors();
                            ArrayList<String> list_error = responseMessage.getError_messages();
                            String strError = "";
                            if (list_error != null) {
                                for (String error_item : list_error) {
                                    if (strError.isEmpty() == true) {
                                        strError = "\u2022 " + error_item;
                                    } else {
                                        strError += "\n\u2022 " + error_item;
                                    }
                                }
                            }
                            if (!strError.equals("")) {
                                tv_error_alipay.setText(strError);
                                fl_error_mes_alipay.setVisibility(View.VISIBLE);
                            }
                            //Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                fl_error_mes_alipay.setVisibility(View.GONE);
                Utils.OnFailException(t);
                progressDialog.dismiss();
            }
        });
    }
}
