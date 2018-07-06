package com.wo1haitao.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.R;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.controls.CustomTextViewFixSize;
import com.wo1haitao.models.OrderModel;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 8/25/2017.
 */

public class DetailOrderFragment extends BaseFragment {
    ActionBarProject my_action_bar;
    int idOrder;
    TextView tv_name_product, id_offer_price, id_shipping_cost, id_total_amount, tv_method_tranfer
            , tv_trade_number, tv_date_of_transaction, tv_payment_method, tv_total_mount
            , tv_bank_account_number, name_product, offer_price, name_category, name_country
            , bidder_name, delivery_method, shipping_fee, estimated_delivery, tv_shipping_address
            , tv_billing_address, tv_bank_account_information, tvCommisionChargesPercent, id_service_charge
            , tv_charge_fee_seller, tv_price_charge_seller, tv_total_amount_seller;
    ImageView imv_product;
    String flag = "";
    View fragment_view;
    LinearLayout ll_charge_fee, ll_charge_fee_seller, ll_total_seller;
    CustomTextViewFixSize id_discount_offer_price;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    @Override
    public boolean onBackPressed() {
        if ((DetailOrderFragment.this.getFlag().equals("payment_page"))) {
            MainActivity mainActivity = (MainActivity) DetailOrderFragment.this.getActivity();
            MyBidFragment fragment = new MyBidFragment();
            mainActivity.changeFragment(fragment, true);
            return true;
        }
        else {
            return super.onBackPressed();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment_view = inflater.inflate(R.layout.fragment_detail_order, container, false);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        my_action_bar.showTitle(R.string.title_detail_order);
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });

        fragment_view.setVisibility(View.GONE);
        tvCommisionChargesPercent = (TextView) fragment_view.findViewById(R.id.tv_commission_charges_percent);
        id_service_charge = (TextView) fragment_view.findViewById(R.id.id_service_charge);
        tv_name_product = (TextView) fragment_view.findViewById(R.id.tv_name_product);
        id_offer_price = (TextView) fragment_view.findViewById(R.id.id_offer_price);
        id_shipping_cost = (TextView) fragment_view.findViewById(R.id.id_shipping_cost);
        id_total_amount = (TextView) fragment_view.findViewById(R.id.id_total_amount);
        tv_method_tranfer = (TextView) fragment_view.findViewById(R.id.tv_method_tranfer);
        tv_trade_number = (TextView) fragment_view.findViewById(R.id.tv_trade_number);
        tv_date_of_transaction = (TextView) fragment_view.findViewById(R.id.tv_date_of_transaction);
        tv_payment_method = (TextView) fragment_view.findViewById(R.id.tv_payment_method);
        tv_total_mount = (TextView) fragment_view.findViewById(R.id.tv_total_mount);
        tv_bank_account_number = (TextView) fragment_view.findViewById(R.id.tv_bank_account_number);
        name_product = (TextView) fragment_view.findViewById(R.id.name_product);
        offer_price = (TextView) fragment_view.findViewById(R.id.offer_price);
        name_category = (TextView) fragment_view.findViewById(R.id.name_category);
        name_country = (TextView) fragment_view.findViewById(R.id.name_country);
        bidder_name = (TextView) fragment_view.findViewById(R.id.bidder_name);
        delivery_method = (TextView) fragment_view.findViewById(R.id.delivery_method);
        shipping_fee = (TextView) fragment_view.findViewById(R.id.shipping_fee);
        estimated_delivery = (TextView) fragment_view.findViewById(R.id.estimated_delivery);
        tv_shipping_address = (TextView) fragment_view.findViewById(R.id.tv_shipping_address);
        tv_billing_address = (TextView) fragment_view.findViewById(R.id.tv_billing_address);
        tv_bank_account_information = (TextView) fragment_view.findViewById(R.id.tv_bank_account_information);
        imv_product = (ImageView) fragment_view.findViewById(R.id.imv_product);
        ll_charge_fee = (LinearLayout) fragment_view.findViewById(R.id.ll_charge_fee);
        ll_charge_fee_seller = (LinearLayout) fragment_view.findViewById(R.id.ll_charge_fee_seller);
        ll_total_seller = (LinearLayout) fragment_view.findViewById(R.id.ll_total_seller);
        tv_charge_fee_seller = (TextView) fragment_view.findViewById(R.id.tv_charge_fee_seller);
        tv_price_charge_seller = (TextView) fragment_view.findViewById(R.id.tv_price_charge_seller);
        tv_total_amount_seller = (TextView) fragment_view.findViewById(R.id.tv_total_amount_seller);
        id_discount_offer_price = (CustomTextViewFixSize)fragment_view.findViewById(R.id.id_discount_offer_price);

        if(DetailOrderFragment.this.getFlag().equals("offer_order")) {
            GetDetailOrder(DetailOrderFragment.this.getIdOrder());

            ll_charge_fee.setVisibility(View.VISIBLE);
            ll_charge_fee_seller.setVisibility(View.GONE);
            ll_total_seller.setVisibility(View.GONE);
        }
        else {
            GetDetailOffererOrder(DetailOrderFragment.this.getIdOrder());


            ll_charge_fee.setVisibility(View.GONE);
            ll_charge_fee_seller.setVisibility(View.VISIBLE);
            ll_total_seller.setVisibility(View.VISIBLE);
        }
        return fragment_view;
    }

    private void InitView(OrderModel orderModel){
        try{
            tv_name_product.setText(orderModel.getProductListing().getName().toString());
            name_product.setText(orderModel.getProductListing().getName().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Float fOfferPrice = Float.parseFloat(orderModel.getProduct_offer().getOffer_price().getFractional()) / 100;
            float discountAmountCents = Float.parseFloat(orderModel.getDiscount_amount_cents()) / 100;
            float discountPurchaseLimitCents = Float.parseFloat(orderModel.getDiscount_purchase_limit_cents()) / 100;
            String strOfferPrice = String.format("%.2f", fOfferPrice);
            id_offer_price.setText("¥" + strOfferPrice);
            String strdisscountAmountCents = String.format("%.2f", discountAmountCents);
            if(fOfferPrice >= discountPurchaseLimitCents){
                id_discount_offer_price.setText("- ¥" + strdisscountAmountCents);
            }
            else {
                id_discount_offer_price.setText("- ¥ " + strdisscountAmountCents);
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        try{
            Float fShippingCost = Float.parseFloat(orderModel.getProduct_tender_shipping().getShipping_cost().getFractional()) / 100;
            String strShippingCost = String.format("%.2f", fShippingCost);
            id_shipping_cost.setText("¥" + strShippingCost);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            float offerPrice = 0, shippingCost = 0, totalFee = 0, fServiceCharges = 0;
            fServiceCharges = orderModel.getProduct_offer().getCommission_charges();
            offerPrice = Float.parseFloat(orderModel.getProduct_offer().getOffer_price().getFractional()) / 100;
            shippingCost = Float.parseFloat(orderModel.getProduct_tender_shipping().getShipping_cost().getFractional()) / 100;
            totalFee =offerPrice + shippingCost + fServiceCharges;
            float totalFeeSeller = offerPrice + shippingCost;
            String strTotalFee = String.format("%.2f", totalFee);
            String strTotalFeeSeller = String.format("%.2f", totalFeeSeller);
            id_total_amount.setText("¥" + strTotalFee);


        }catch (Exception e){
            e.printStackTrace();
        }
//      tv_payment_method
        try{
            tv_trade_number.setText(orderModel.getOrder_no().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            String strDate = orderModel.getAgreed_date();
            tv_date_of_transaction.setText(strDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            String strPayMethod = orderModel.getPayment_method();
            strPayMethod = strPayMethod.replace("_", " ");
            String upperString = strPayMethod.substring(0,1).toUpperCase() + strPayMethod.substring(1);
            tv_payment_method.setText(upperString);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            float iTotalMount = (float) orderModel.getAgreed_price_cents() / 100;
            String strTotalMount = String.format("%.2f", iTotalMount);
            tv_total_mount.setText("¥" + strTotalMount);
            if(DetailOrderFragment.this.getFlag().equals("offer_order")){
                id_total_amount.setText("¥"+strTotalMount);
            }
            else{
                float charge_fee = (float) orderModel.getCommission_charges_cents() / 100;
                String str_amount = String.format("%.2f", iTotalMount - charge_fee);
                id_total_amount.setText("¥" + str_amount);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            float fOfferPrice = Float.parseFloat(orderModel.getProduct_offer().getOffer_price().getFractional()) / 100;
            String strOfferPrice = String.format("%.2f", fOfferPrice);
            offer_price.setText("¥" + strOfferPrice);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            HashMap<String, String> hashmapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
            int idCategory = orderModel.getProductListing().getCategory_id();
            for (Map.Entry entry : hashmapCategory.entrySet()) {
                if(String.valueOf(idCategory).equals(entry.getKey())){
                    name_category.setText(hashmapCategory.get(entry.getKey()));
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {

            String strCommisionChargesPercentFormat = "支付服务费 ("+orderModel.getCommission_charges_percent()+"%)";
            tvCommisionChargesPercent.setText(strCommisionChargesPercentFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            String strCommisionChargesPercentFormat = "支付服务费 ("+orderModel.getSeller_commission_charges_percent()+"%)";
            tv_charge_fee_seller.setText(strCommisionChargesPercentFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            tv_price_charge_seller.setText("- ¥" + orderModel.getBuyer_commission_charges_amount());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tv_total_amount_seller.setText("¥" + orderModel.getPayable_amount_to_tenderer());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            float fServiceCharges = orderModel.getProduct_offer().getCommission_charges();
            String str_fee = orderModel.getString_charges_cents();
            String strServiceCharges = String.format("%.2f", fServiceCharges);
            id_service_charge.setText("¥" + str_fee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String strCountry = "";
            if(!orderModel.getProduct_offer().getProduct_tender().getCountry().toString().equals("")) {
                strCountry = orderModel.getProduct_offer().getProduct_tender().getCountry().getName();
            }
            else {
                if(orderModel.getProductListing().isNon_restricted_country() == true){
                    strCountry = "不限国家";
                }
            }
            name_country.setText(strCountry);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            HashMap<String, String> hashmapShippingMethod = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_SHIPPING_METHOD);
            int idShipping = orderModel.getProduct_tender_shipping().getShipping_method_id();
            for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
                if(String.valueOf(idShipping).equals(entry.getKey())){
                    delivery_method.setText(hashmapShippingMethod.get(entry.getKey()));
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            float fShippingCost = Float.parseFloat(orderModel.getProduct_tender_shipping().getShipping_cost().getFractional()) / 100;
            String strShippingCost = String.format("%.2f", fShippingCost);
            shipping_fee.setText("¥" + strShippingCost);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            estimated_delivery.setText(orderModel.getProduct_tender_shipping().getExpected_arrival_days() + "天");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            String strShippingAddress = "";
            if(orderModel.getAddress_1() != null){
                strShippingAddress += orderModel.getAddress_1();
            }
            if(orderModel.getAddress_2() != null && !orderModel.getAddress_2().equals("")){
                strShippingAddress += ", " + orderModel.getAddress_2();
            }
            if(orderModel.getCountry() != null){
                strShippingAddress += ", " + orderModel.getCountry();
            }
            if(orderModel.getStatus() != null){
                strShippingAddress += ", " + orderModel.getState();
            }
            strShippingAddress += "\n";
            if(orderModel.getCity() != null){
                strShippingAddress += orderModel.getCity();
            }
            strShippingAddress += "\n";
            if(orderModel.getPostal_code() != null){
                strShippingAddress += "邮政编码 " + orderModel.getPostal_code();
            }
            tv_shipping_address.setText(strShippingAddress);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            String strBillingAddress = "";
            if(orderModel.getBilling_address_1() != null){
                strBillingAddress += orderModel.getBilling_address_1();
            }
            if(orderModel.getBilling_address_2() != null && !orderModel.getBilling_address_2().equals("")){
                strBillingAddress += ", " + orderModel.getBilling_address_2();
            }
            if(orderModel.getBilling_country() != null){
                strBillingAddress += ", " + orderModel.getBilling_country();
            }
            if(orderModel.getBilling_state() != null){
                strBillingAddress += ", " + orderModel.getBilling_state();
            }
            strBillingAddress += "\n";
            if(orderModel.getBilling_city() != null){
                strBillingAddress += orderModel.getBilling_city();
            }
            strBillingAddress += "\n";
            if(orderModel.getBilling_postal_code() != null){
                strBillingAddress += "邮政编码 " + orderModel.getBilling_postal_code();
            }
            tv_billing_address.setText(strBillingAddress);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            tv_bank_account_number.setText(orderModel.getPayment_setting().getBank_account_no());
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            tv_bank_account_information.setText(orderModel.getPayment_setting().getBank_instructions());
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            String urlImage = ApiServices.BASE_URI + orderModel.getProductListing().product_images.get(0).product_image.thumb.getUrl();
            if (urlImage != null && !urlImage.equals("")) {
                ImageLoader il = ImageLoader.getInstance();
                il.displayImage(urlImage, imv_product);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            bidder_name.setText(orderModel.getProduct_offer().getProduct_tender().getCommon_user().getNickname());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void GetDetailOrder(int id) {
        final ProgressDialog progressDialog = Utils.createProgressDialog(DetailOrderFragment.this.getActivity());
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        Call<ResponseMessage<OrderModel>> call = ws.actionGetDetailOrder(id);
        call.enqueue(new Callback<ResponseMessage<OrderModel>>() {
            @Override
            public void onResponse(Call<ResponseMessage<OrderModel>> call, Response<ResponseMessage<OrderModel>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        OrderModel orderModel = response.body().getData();
                        InitView(orderModel);
                        progressDialog.dismiss();
                        fragment_view.setVisibility(View.VISIBLE);
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(DetailOrderFragment.this.getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(DetailOrderFragment.this.getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<OrderModel>> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }

    /**
     * Get detail order
     *
     * @params: id order
     */

    public void GetDetailOffererOrder(int id) {
        final ProgressDialog progressDialog = Utils.createProgressDialog(DetailOrderFragment.this.getActivity());
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        Call<ResponseMessage<OrderModel>> call = ws.actionGetDetailOffererOrder(id);
        call.enqueue(new Callback<ResponseMessage<OrderModel>>() {
            @Override
            public void onResponse(Call<ResponseMessage<OrderModel>> call, Response<ResponseMessage<OrderModel>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        OrderModel orderModel = response.body().getData();
                        InitView(orderModel);
                        progressDialog.dismiss();
                        fragment_view.setVisibility(View.VISIBLE);
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<OrderModel>> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }
}
