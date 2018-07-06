package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.fragments.ProductDetailFragment;
import com.wo1haitao.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class DialogCancelOffer extends Dialog {

    public Activity c;
    public DialogCountoffer countoffer;
    ImageView iv_back_action, iv_cancel;
    TextView tv_destroy_offer, tv_cancel_oselect;
    Long product_id, tender_id, offer_id, idProduct;
    String state = "rejected";
    Fragment f;

    public DialogCancelOffer(Activity a, DialogCountoffer countoffer, Long offer_id, long tender_id, long idProduct, ProductDetailFragment f) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.countoffer = countoffer;
        this.tender_id = tender_id;
        this.offer_id = offer_id;
        this.idProduct = idProduct;
        this.f = f;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_cancel_countoffer);
        iv_back_action = (ImageView) findViewById(R.id.ib_back_action);
        iv_back_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                countoffer.show();
            }
        });
        tv_destroy_offer = (TextView) findViewById(R.id.tv_destroy_offer);
        tv_destroy_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiServices api = ApiServices.getInstance();
                final WebService ws = api.getRetrofit().create(WebService.class);
                final ProgressDialog progressDialogLoading = Utils.createProgressDialog(c);
                //get offer_id from want to buy / id_product
                Call<ResponseMessage> callCancelOffer = ws.actionReject(DialogCancelOffer.this.offer_id, state, DialogCancelOffer.this.offer_id, DialogCancelOffer.this.tender_id);
                callCancelOffer.enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                        try {
                            Response<ResponseMessage> response1 = response;
//                            Toast.makeText(getContext(), "成功拒绝", Toast.LENGTH_SHORT).show();
                            if (f != null && f instanceof ProductDetailFragment) {
                                ProductDetailFragment fragment = (ProductDetailFragment) f;
                                fragment.setView(fragment.getProduct_id());
                            }
                            progressDialogLoading.dismiss();
                            dismiss();
                            countoffer.dismiss();
                        } catch (Exception e) {
                            Utils.crashLog(e);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {
                        if(progressDialogLoading != null){
                            progressDialogLoading.dismiss();
                        }
                        Utils.OnFailException(t);
                    }
                });
            }
        });
        tv_cancel_oselect = (TextView) findViewById(R.id.tv_cancel_select);
        tv_cancel_oselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                countoffer.dismiss();
            }
        });


    }
}
