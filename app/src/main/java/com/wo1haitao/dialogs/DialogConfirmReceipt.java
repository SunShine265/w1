package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.fragments.MyPostFragment;
import com.wo1haitao.models.OrderModel;
import com.wo1haitao.utils.Utils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 9/6/2017.
 */

public class DialogConfirmReceipt extends Dialog {
    Activity activity;
    MyPostFragment f;
    int idOrder;
    static final String BUYER_RECEIVE_STATUS_RECEIVED = "item_received";
    String title = "";

    public DialogConfirmReceipt(Activity activity, int idOrderAccept, MyPostFragment f, String title) {
        super(activity);
        this.activity = activity;
        this.idOrder = idOrderAccept;
        this.f = f;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);
        TextView tv_cancel, tv_ok, tvTitle;
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tvTitle.setText(title);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetConfirmReceive(idOrder);
            }
        });
    }

    /**
     * Get confirm receive
     *
     * @params: id: identify order
     * @return:
     */
    public void GetConfirmReceive(int idConfirmReceive) {
        final ProgressDialog progressLoading = Utils.createProgressDialog(activity);
        ApiServices api = ApiServices.getInstance();
        final WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<OrderModel>> call = ws.actionGetConfirmReceive(idConfirmReceive);
        call.enqueue(new Callback<ResponseMessage<OrderModel>>() {
            @Override
            public void onResponse(Call<ResponseMessage<OrderModel>> call, Response<ResponseMessage<OrderModel>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        OrderModel orderModel = response.body().getData();
                        if (orderModel.getBuyer_receive_status().equals(BUYER_RECEIVE_STATUS_RECEIVED)) {
                            if (f instanceof MyPostFragment) {
                                MyPostFragment myPostFragment = (MyPostFragment) f;
                                myPostFragment.GetDataMyPost(1);
                                dismiss();
                            }
                        }
                        progressLoading.dismiss();
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }

                        if (progressLoading != null) {
                            progressLoading.dismiss();
                        }
                    } else {
                        if (progressLoading != null) {
                            progressLoading.dismiss();
                        }
                        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    if(progressLoading != null){
                        progressLoading.dismiss();
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<OrderModel>> call, Throwable t) {
                if(progressLoading != null){
                    progressLoading.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }
}
