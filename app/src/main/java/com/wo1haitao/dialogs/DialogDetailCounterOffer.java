package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wo1haitao.R;
import com.wo1haitao.models.ProductTenders;
import com.wo1haitao.utils.MyPreferences;

/**
 * Created by user on 8/7/2017.
 */

public class DialogDetailCounterOffer extends Dialog {
    ProductTenders productTenders;
    Activity c;
    public DialogDetailCounterOffer(@NonNull Activity a, ProductTenders productTenders) {
        super(a);
        this.c = a;
        this.productTenders = productTenders;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_detail_counter_offer);
        TextView tvPriceInfo, tv_exit;
        TextView edt_remarks, tvTitle;
        tvPriceInfo = (TextView) findViewById(R.id.tvPriceInfo);
        tv_exit = (TextView) findViewById(R.id.tv_exit);
        edt_remarks =(TextView) findViewById(R.id.edt_remarks);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        try{
            if(MyPreferences.getID() != productTenders.getCommon_user().getId()){
                tvTitle.setText(R.string.title_detail_shop_counter_buyer);
            }
            else {
                tvTitle.setText(R.string.title_detail_counter_shop_bid_seller);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(productTenders.getProduct_offers().size() == 1) {
                Float fOfferPrice = Float.parseFloat(productTenders.getProduct_offers().get(0).getProduct_counter_offer().getCounter_offer_price().getFractional()) / 100;
                String strOfferPrice = String.format("%.2f", fOfferPrice);
                tvPriceInfo.setText("¥" + strOfferPrice);
                edt_remarks.setText(productTenders.getProduct_offers().get(0).getProduct_counter_offer().getRemarks());
            }
            else {
                Float fOfferPrice = Float.parseFloat(productTenders.getProduct_offers().get(0).getProduct_counter_offer().getCounter_offer_price().getFractional()) / 100;
                String strOfferPrice = String.format("%.2f", fOfferPrice);
                tvPriceInfo.setText("¥" + strOfferPrice);
                edt_remarks.setText(productTenders.getProduct_offers().get(0).getProduct_counter_offer().getRemarks());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
