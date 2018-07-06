package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.models.OrderModel;
import com.wo1haitao.models.ProductTenders;
import com.wo1haitao.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class DialogShipMethod extends Dialog {

    public Activity c;
    OrderModel orderModel;
    MyCallback listener;
    ProductTenders productTenders;
    Button checkout, cancel;
    int positionShipping = 0;

    public DialogShipMethod(Activity a, ProductTenders productTenders) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.productTenders = productTenders;
        if (a instanceof MyCallback) {
            listener = (MyCallback) a;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_shipping_method);
        ImageView iv_back;
        Spinner spinnerShippingMethod;
        iv_back = (ImageView) findViewById(R.id.iv_back);
        checkout = (Button) findViewById(R.id.btn_to_checkout);
        cancel = (Button) findViewById(R.id.btn_cancel_offer);
        spinnerShippingMethod = (Spinner) findViewById(R.id.sp_shipping_method);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        List<String> spListShippingMehtod = new ArrayList<>();
        if (productTenders.getProduct_tender_shippings().size() > 0) {
            for (int i = 0; i < productTenders.getProduct_tender_shippings().size(); i++) {
                float fShippingCost = Float.parseFloat(productTenders.getProduct_tender_shippings().get(i).getShipping_cost().getFractional()) / 100;
                spListShippingMehtod.add("快速邮递, ¥" + String.valueOf(fShippingCost)
                        + ", 预计送达时间: " + productTenders.getProduct_tender_shippings().get(i).expected_arrival_days + "天");
            }
        }

        spinnerShippingMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionShipping = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostOrderProduct(productTenders.getProduct_tender_shippings().get(positionShipping).getId()
                        , productTenders.getProduct_offers().get(productTenders.getProduct_offers().size() - 1).getId()
                        , productTenders.getProduct_tender_shippings().get(0).getProduct_tender_id());
            }
        });
//        spListShippingMehtod.add("快速邮递, ¥ 3.00, 预计送达时间: 2天");
        ArrayAdapter<String> adapterShippingMethod = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, spListShippingMehtod) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.dialog_text_hint));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerShippingMethod.setAdapter(adapterShippingMethod);
    }

    public interface MyCallback {
        void changeAcceptOffer(OrderModel orderModel, String flagAccept);
    }

    public void PostOrderProduct(long product_tender_shipping_id, long offerIDlast, long tender_id) {
        ApiServices api = ApiServices.getInstance();
        final ProgressDialog progressDialogLoading = Utils.createProgressDialog(c);
        final WebService ws = api.getRetrofit().create(WebService.class);
        //get offer_id from want to buy / id_product
        Call<ResponseMessage<OrderModel>> acceptOffer = ws.actionAccept(product_tender_shipping_id, offerIDlast, tender_id);
        acceptOffer.enqueue(new Callback<ResponseMessage<OrderModel>>() {
            @Override
            public void onResponse(Call<ResponseMessage<OrderModel>> call, Response<ResponseMessage<OrderModel>> response) {
                try {
                    if (response.body() != null) {
                        if (response.body().isSuccess()) {
                            orderModel = response.body().getData();
                            if (listener != null) {
                                dismiss();
                                String flagAccept = "AcceptBid";
                                listener.changeAcceptOffer(DialogShipMethod.this.orderModel, flagAccept);
                            }
                            progressDialogLoading.dismiss();
                        }
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialogLoading != null) {
                            progressDialogLoading.dismiss();
                        }
                    } else {
                        if (progressDialogLoading != null) {
                            progressDialogLoading.dismiss();
                        }
                        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    if (progressDialogLoading != null) {
                        progressDialogLoading.dismiss();
                    }
                    Utils.crashLog(e);
                }
            }
            @Override
            public void onFailure(Call<ResponseMessage<OrderModel>> call, Throwable t) {
                if (progressDialogLoading != null) {
                    progressDialogLoading.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }
}
