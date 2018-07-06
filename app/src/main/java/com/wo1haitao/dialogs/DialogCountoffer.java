package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.controls.CustomEditextSoftkey;
import com.wo1haitao.fragments.ProductDetailFragment;
import com.wo1haitao.models.ProductTenderShippings;
import com.wo1haitao.models.ProductTenders;
import com.wo1haitao.views.ActionBarProject;
import com.wo1haitao.views.CustomViewListImage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class DialogCountoffer extends Dialog {

    public Activity c;
    TextView tv_cancel, tv_accept, tv_offer;
    MyCallback listener;
    Long offer_id, tender_id, product_tender_shipping_id, offerIDlast;
    String str_offer_price, str_reoffer_offer_price, str_counter_offer_price;
    TextView offerPrice, counterOfferPrice, reofferPrice, arrawCounterOffer, arrowReoffer
            , tv_country, tv_title_offer_price, tv_commodity_status;
    ActionBarProject my_action_bar;
    LinearLayout ll_list_ship, ln_remarks, ln_image_product;
    ProductTenders productTenders;
    RsProduct rsProduct;
    CustomViewListImage ll_content_image;
    ProductDetailFragment fProductDetail;
    CustomEditextSoftkey edt_remarks;

    public DialogCountoffer(Activity a, long offer_id, long tender_id, long product_tender_shipping_id
            , String str_offer_price, String str_counter_offer_price, String str_reoffer_offer_price
            , long offerIDlast, ProductTenders productTenders, RsProduct rsProduct, ProductDetailFragment fProductDetail) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        if (a instanceof MyCallback) {
            listener = (MyCallback) a;
        }
        this.offer_id = offer_id;
        this.tender_id = tender_id;
        this.product_tender_shipping_id = product_tender_shipping_id;
        this.str_offer_price = str_offer_price;
        this.str_reoffer_offer_price = str_reoffer_offer_price;
        this.str_counter_offer_price = str_counter_offer_price;
        this.offerIDlast = offerIDlast;
        this.productTenders = productTenders;
        this.rsProduct = rsProduct;
        this.fProductDetail = fProductDetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_countoffer);
        offerPrice = (TextView) findViewById(R.id.tv_offer_price);
        counterOfferPrice = (TextView) findViewById(R.id.tv_counter_offer_price);
        reofferPrice = (TextView) findViewById(R.id.tv_reoffer_price);
        tv_cancel = (TextView) findViewById(R.id.tv_destroy_offer);
        tv_accept = (TextView) findViewById(R.id.tv_accept);
        tv_offer = (TextView) findViewById(R.id.tv_offer);
        arrawCounterOffer = (TextView) findViewById(R.id.arrow_counter_offer);
        arrowReoffer = (TextView) findViewById(R.id.arrow_reoffer);
        ll_list_ship = (LinearLayout) findViewById(R.id.ll_list_method);
        ll_content_image = (CustomViewListImage) findViewById(R.id.ll_list_image);
        ln_image_product = (LinearLayout) findViewById(R.id.ln_image_product);
        ln_remarks = (LinearLayout) findViewById(R.id.ln_remarks);
        edt_remarks = (CustomEditextSoftkey) findViewById(R.id.edt_remarks);
        tv_country =(TextView)findViewById(R.id.tv_country);
        tv_title_offer_price = (TextView) findViewById(R.id.tv_title_offer_price);
        tv_commodity_status = (TextView) findViewById(R.id.tv_commodity_status);

        if (!str_offer_price.equals("")) {
            float fOfferPrice = Float.parseFloat(productTenders.getProduct_offers().get(0).getOffer_price().getFractional()) / 100;
            String strOfferPrice = String.format("%.2f", fOfferPrice);
            offerPrice.setText("代购的出价: ¥" + strOfferPrice);
            offerPrice.setVisibility(View.VISIBLE);
            tv_accept.setVisibility(View.VISIBLE);
            tv_cancel.setVisibility(View.VISIBLE);
            tv_offer.setVisibility(View.VISIBLE);
        }
        if (!str_counter_offer_price.equals("")) {
            counterOfferPrice.setText(str_counter_offer_price);
            counterOfferPrice.setVisibility(View.VISIBLE);
            arrawCounterOffer.setVisibility(View.VISIBLE);
        }
        if (!str_reoffer_offer_price.equals("")) {
            reofferPrice.setText(str_reoffer_offer_price);
            reofferPrice.setVisibility(View.VISIBLE);
            arrowReoffer.setVisibility(View.VISIBLE);
            tv_cancel.setVisibility(View.VISIBLE);
            tv_accept.setVisibility(View.VISIBLE);
            tv_offer.setVisibility(View.GONE);
        }
        try {
            if(productTenders.getProduct_offers().size() == 1) {
                if (!productTenders.getProduct_offers().get(0).getRemarks().equals("")) {
                    edt_remarks.setText(productTenders.getProduct_offers().get(0).getRemarks());
                } else {
                    ln_remarks.setVisibility(View.GONE);
                }
            }
            else {
                if (!productTenders.getProduct_offers().get(1).getRemarks().equals("")) {
                    edt_remarks.setText(productTenders.getProduct_offers().get(1).getRemarks());
                } else {
                    ln_remarks.setVisibility(View.GONE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
                DialogCancelOffer cdd = new DialogCancelOffer(c, DialogCountoffer.this, offerIDlast, tender_id, rsProduct.getId(), fProductDetail);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(cdd.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                cdd.show();
            }
        });
        try {
            ArrayList<ProductTenderShippings> productTenderShippingses = new ArrayList<>(productTenders.getProduct_tender_shippings());
//            if(productTenders.getProduct_offers().size() > 1) {
//                Collections.reverse(productTenderShippingses);
//            }
//            SortProductTender(productTenderShippingses);
            for (int i = 0; i < productTenderShippingses.size(); i++) {
                ProductTenderShippings method = productTenderShippingses.get(i);
                View viewItemMethod = ((LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_shipping_method, null, false);
                TextView tvName = (TextView) viewItemMethod.findViewById(R.id.tvName);
                TextView tvPrice = (TextView) viewItemMethod.findViewById(R.id.tvPrice);
                TextView tvDay = (TextView) viewItemMethod.findViewById(R.id.tvDay);
                List<String> list_method = Arrays.asList(CustomApp.getInstance().getResources().getStringArray(R.array.list_method_shipping));
                String nameMethod = method.getShipping_method_id() > 0 && method.getShipping_method_id() <= list_method.size() ? list_method.get((int) (method.getShipping_method_id() - 1)) : "";
                tvName.setText(nameMethod);
                Float fShippingPrice = Float.parseFloat(method.getShipping_cost().getFractional()) / 100;
                String strShippingPrice = "¥" + String.format("%.2f", fShippingPrice);
                tvPrice.setText(strShippingPrice);
                tvDay.setText(method.getExpected_arrival_days() + "天");
                tvDay.setTextColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.colorPrimary));
                tvPrice.setTextColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.colorPrimary));
                tvName.setTextColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.colorPrimary));
                if (i % 2 == 0) {
                    viewItemMethod.setBackgroundColor(ContextCompat.getColor(CustomApp.getInstance(), android.R.color.transparent));
                }
                ll_list_ship.addView(viewItemMethod);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                DialogShipMethod dialogShipMethod = new DialogShipMethod(c, productTenders);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialogShipMethod.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialogShipMethod.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialogShipMethod.show();
            }
        });
        tv_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
                DialogOffer cdd = new DialogOffer(c, DialogCountoffer.this, offer_id);
                if(fProductDetail != null) {
                    cdd.f = fProductDetail;
                }
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(cdd.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                cdd.show();
            }
        });

        ArrayList<String> list_url_image = new ArrayList<>();
        if(productTenders.getProduct_images() != null && productTenders.getProduct_images().size() > 0) {
//            if (productTenders.getProduct_offers().size() < 2) {
                for (int i = 0; i < productTenders.getProduct_images().size(); i++) {
                    String url = ApiServices.BASE_URI + productTenders.getProduct_images().get(i).getProduct_image().getThumb().getUrl();
                    list_url_image.add(url);
                }
//            }else {
//                for (int i = productTenders.getProduct_images().size() - 1; i >= 0; i--) {
//                    String url = ApiServices.BASE_URI + productTenders.getProduct_images().get(i).getProduct_image().getThumb().getUrl();
//                    list_url_image.add(url);
//                }
//            }
            ll_content_image.setViewFromUrls(list_url_image);
        }
        else {
            ln_image_product.setVisibility(View.GONE);
        }
        ll_content_image.HineButtonDelete();
        try {
            tv_country.setText(productTenders.getCountry().getName());
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            Float fOfferPrice = Float.parseFloat(productTenders.getProduct_offers().get(0).getOffer_price().getFractional()) / 100;
            String strOfferPrice = String.format("%.2f", fOfferPrice);
            tv_title_offer_price.setText(strOfferPrice);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            String strCondition = "";
            if(productTenders.getCondition().equals("used_product")){
                strCondition = "二手";
            }
            else {
                strCondition = "全新";
            }
            tv_commodity_status.setText(strCondition);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface MyCallback {
        void changeAddress();

//        void changeAcceptOffer(OrderModel orderModel);
    }

    /**
     * Compare 2 item Message follow date
     *
     * @params:
     * @return:
     */
    public static Comparator<ProductTenderShippings> PRODUCTTENDER_SORT = new Comparator<ProductTenderShippings>() {
        public int compare(ProductTenderShippings productTenders1, ProductTenderShippings productTenders2) {
            Date date1, date2;
            int result = 0;
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            String strDate1 = "", strDate2 = "";
            try {
                if (productTenders1.getCreated_at() != null) {
                    strDate1 = productTenders1.getCreated_at();
                }
                if (productTenders2.getCreated_at() != null) {
                    strDate2 = productTenders2.getCreated_at();
                }
                date1 = inputFormat.parse(strDate1);
                date2 = inputFormat.parse(strDate2);
                if (date1.after(date2)) {
                    result = -1;
                } else {
                    result = 1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return result;
        }
    };

    /**
     * Sort list message follow date
     */
    public ArrayList<ProductTenderShippings> SortProductTender(ArrayList<ProductTenderShippings> list) {
        Collections.sort(list, PRODUCTTENDER_SORT);
        return list;
    }

}
