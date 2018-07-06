package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.controls.CustomEditextSoftkey;
import com.wo1haitao.controls.DecimalDigitsInputFilter;
import com.wo1haitao.fragments.ProductDetailFragment;
import com.wo1haitao.models.CounterOfferModel;
import com.wo1haitao.models.OfferMeModel;
import com.wo1haitao.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class DialogOffer extends Dialog {

    public Activity c;
    public Fragment f;
    public DialogCountoffer countoffer;
    ImageView iv_back_action;
    TextView tv_offer;
    CustomEditextSoftkey counterOfferPrice, remarkCounterOffer;
    long offer_id;
    ArrayList<OfferMeModel> arrayOfOffer;

    public DialogOffer(Activity a, DialogCountoffer countoffer, long offer_id) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.countoffer = countoffer;
        this.offer_id = offer_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_offer);


        iv_back_action = (ImageView) findViewById(R.id.ib_back_action);
        counterOfferPrice = (CustomEditextSoftkey) findViewById(R.id.couter_offer_price);
        remarkCounterOffer = (CustomEditextSoftkey) findViewById(R.id.remark_counter_offer);
        iv_back_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                countoffer.show();
            }
        });

        counterOfferPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        tv_offer = (TextView) findViewById(R.id.tv_offer);
        tv_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strCouterOfferPrice = "", strRemarkCouterOffer = "";
                counterOfferPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
                if(counterOfferPrice.getText().toString().trim().length() != 0) {
                    strCouterOfferPrice = counterOfferPrice.getText().toString();
                    final double CouterOfferPrice = Double.parseDouble(strCouterOfferPrice);
                }
                if(remarkCounterOffer.getText().toString().trim().length() != 0) {
                    strRemarkCouterOffer = remarkCounterOffer.getText().toString();
                }


                //post counter offer
                Call<ResponseMessage<CounterOfferModel>> callCouterOffer;
                if(!strCouterOfferPrice.equals("")) {
                    final ProgressDialog progressDialog = Utils.createProgressDialog(c);
                    ApiServices api = ApiServices.getInstance();
                    final WebService ws = api.getRetrofit().create(WebService.class);
                    callCouterOffer = ws.actionPostCounterOffer(offer_id, strCouterOfferPrice, strRemarkCouterOffer);
                    callCouterOffer.enqueue(new Callback<ResponseMessage<CounterOfferModel>>() {
                        @Override
                        public void onResponse(Call<ResponseMessage<CounterOfferModel>> call, Response<ResponseMessage<CounterOfferModel>> response) {
                            try {
                                if (response.body() != null) {
                                    if (response.body().isSuccess()) {
//                                        Toast.makeText(getContext(), "成功还价", Toast.LENGTH_SHORT).show();
                                        if (f != null && f instanceof ProductDetailFragment) {
                                            ProductDetailFragment fragment = (ProductDetailFragment) f;
                                            fragment.setView(fragment.getProduct_id());
                                        }
                                        progressDialog.dismiss();
                                        countoffer.dismiss();
                                        dismiss();
                                    }
                                } else if (response.errorBody() != null) {
                                    try {
                                        ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                                        ErrorMessage error = responseMessage.getErrors();
                                        Toast.makeText(CustomApp.getInstance(), "Can't post data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(CustomApp.getInstance(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                    }
                                    if(progressDialog != null){
                                        progressDialog.dismiss();
                                    }
                                } else {
                                    if(progressDialog != null){
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(CustomApp.getInstance(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                if(progressDialog != null){
                                    progressDialog.dismiss();
                                }
                                Utils.crashLog(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseMessage<CounterOfferModel>> call, Throwable t) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            Utils.OnFailException(t);
                        }
                    });
                }


            }
        });


    }


}
