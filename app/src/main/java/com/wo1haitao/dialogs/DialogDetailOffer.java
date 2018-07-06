package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.controls.CustomEditextSoftkey;
import com.wo1haitao.models.ProductTenderShippings;
import com.wo1haitao.models.ProductTenders;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.views.CustomViewListImage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 8/7/2017.
 */

public class DialogDetailOffer extends Dialog {
    ProductTenders productTenders;
    String flag = "";
    Activity c;

    public DialogDetailOffer(@NonNull Activity a, ProductTenders offerModel, String flag) {
        super(a);
        this.c = a;
        this.productTenders = offerModel;
        this.flag = flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_detail_offer);
        TextView tvPriceInfo, tvCondition, tvNameProduct, tv_exit, tvCountry, tvTitle;
        CustomEditextSoftkey edt_remarks;
        LinearLayout ll_list_ship;
        CustomViewListImage ll_content_image;
        LinearLayout ln_remarks, ln_image_product;

        ln_remarks = (LinearLayout) findViewById(R.id.ln_remarks);
        tvPriceInfo = (TextView) findViewById(R.id.tvPriceInfo);
        tvCondition = (TextView) findViewById(R.id.tvCondition);
        tvNameProduct = (TextView) findViewById(R.id.tv_name_product);
        tv_exit = (TextView) findViewById(R.id.tv_exit);
        edt_remarks = (CustomEditextSoftkey) findViewById(R.id.edt_remarks);
        ll_list_ship = (LinearLayout) findViewById(R.id.ll_list_method);
        ll_content_image = (CustomViewListImage) findViewById(R.id.ll_list_image);
        ln_image_product = (LinearLayout) findViewById(R.id.ln_image_product);
        tvCountry = (TextView) findViewById(R.id.tv_name_product);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        try {
            if (MyPreferences.getID() != productTenders.getCommon_user().getId()) {
                tvTitle.setText(R.string.title_detail_shop_offer_buyer);
            } else {
                tvTitle.setText(R.string.title_detail_shop_offer_seller);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (productTenders.getProduct_offers().size() == 1) {
                if (flag.equals("offer")) {
                    Float fOfferPrice = Float.parseFloat(productTenders.getProduct_offers().get(0).getOffer_price().getFractional()) / 100;
                    String strOfferPrice = String.format("%.2f", fOfferPrice);
                    tvPriceInfo.setText(strOfferPrice);
                    if (!productTenders.getProduct_offers().get(0).getRemarks().equals("")) {
                        edt_remarks.setText(productTenders.getProduct_offers().get(0).getRemarks());
                    } else {
                        ln_remarks.setVisibility(View.GONE);
                    }
                }
            } else {
                if (flag.equals("offer")) {
                    if (!productTenders.getProduct_offers().get(0).getRemarks().equals("")) {
                        edt_remarks.setText(productTenders.getProduct_offers().get(0).getRemarks());
                    } else {
                        ln_remarks.setVisibility(View.GONE);
                    }
                    Float fOfferPrice = Float.parseFloat(productTenders.getProduct_offers().get(0).getOffer_price().getFractional()) / 100;
                    String strOfferPrice = String.format("%.2f", fOfferPrice);
                    tvPriceInfo.setText(strOfferPrice);
                } else {
                    if (!productTenders.getProduct_offers().get(1).getRemarks().equals("")) {
                        edt_remarks.setText(productTenders.getProduct_offers().get(1).getRemarks());
                    } else {
                        ln_remarks.setVisibility(View.GONE);
                    }
                    Float fOfferPrice = Float.parseFloat(productTenders.getProduct_offers().get(1).getOffer_price().getFractional()) / 100;
                    String strOfferPrice = String.format("%.2f", fOfferPrice);
                    tvPriceInfo.setText(strOfferPrice);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String condition = "";
            if (productTenders.getCondition().equals("new_product")) {
                condition = "全新";
            } else {
                condition = "二手";
            }
            tvCondition.setText(condition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tvNameProduct.setText(productTenders.getProduct_listing().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ArrayList<ProductTenderShippings> productTenderShippingses = new ArrayList<>(productTenders.getProduct_tender_shippings());
//            if (productTenders.getProduct_offers().size() > 1) {
//                Collections.reverse(productTenderShippingses);
//            }
//            SortProductTender(productTenderShippingses);
            for (int i = 0; i < productTenderShippingses.size(); i++) {
                ProductTenderShippings method = productTenderShippingses.get(i);
                View viewItemMethod = ((LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_shipping_method, null, false);
                TextView tvName = (TextView) viewItemMethod.findViewById(R.id.tvName);
                TextView tvPrice = (TextView) viewItemMethod.findViewById(R.id.tvPrice);
                TextView tvDay = (TextView) viewItemMethod.findViewById(R.id.tvDay);
                HashMap<String,String> map_category = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_SHIPPING_METHOD);
                String nameMethod = "";
                if(map_category.get(String.valueOf(method.getShipping_method_id())) != null){
                    nameMethod = map_category.get(String.valueOf(method.getShipping_method_id()));
                }
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> list_url_image = new ArrayList<>();
        if (productTenders.getProduct_images() != null && productTenders.getProduct_images().size() > 0) {
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
        } else {
            ln_image_product.setVisibility(View.GONE);
        }
        ll_content_image.HineButtonDelete();
        try {
            tvCountry.setText(productTenders.getCountry().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
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
                    result = 1;
                } else {
                    result = -1;
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

    /**
     * check report
     *
     * @params:
     * @return:
     */
}
