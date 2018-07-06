package com.wo1haitao.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.dialogs.DialogDetailCounterOffer;
import com.wo1haitao.dialogs.DialogDetailOffer;
import com.wo1haitao.dialogs.DialogInvoice;
import com.wo1haitao.dialogs.DialogRebid;
import com.wo1haitao.fragments.ChatDetailFragment;
import com.wo1haitao.fragments.DetailOrderFragment;
import com.wo1haitao.fragments.DisputeFormFragment;
import com.wo1haitao.fragments.MyBidFragment;
import com.wo1haitao.models.OrderModel;
import com.wo1haitao.models.ProductOffer;
import com.wo1haitao.models.ProductShow;
import com.wo1haitao.models.ProductTender;
import com.wo1haitao.models.ProductTenders;
import com.wo1haitao.utils.MyPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG_PRODUCT;

/**
 * Created by goodproductssoft on 4/20/17.
 */

public class ProductsMyBidstAdapter extends ArrayAdapter<ProductTenders> {
    Boolean sortView;
    Activity myactivity;
    MyBidFragment myBidFragment;
    static final String SELLER_DELIVERY_SENT = "delivery_sent";
    static final String BUYER_RECEIVE_SENT = "item_received";
    static final String STATUS_ORDER_PROCESSING = "processing";
    static final String STATUS_ORDER_CONFIRMED = "confirmed";
    static final String OFFER_ACCEPT = "accepted";
    static final String OFFER_UNTOUCHED = "untouched";
    static final String OFFER_REJECTED = "rejected";

    public ProductsMyBidstAdapter(Activity activity, ArrayList<ProductTenders> OfferModels, MyBidFragment myBidFragment) {
        super(activity, 0, OfferModels);
        this.myactivity = activity;
        this.myBidFragment = myBidFragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        HolderViewMyBid holderView;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_my_bids, parent, false);
            holderView = new HolderViewMyBid();
            holderView.fr_offer_price = (FrameLayout) convertView.findViewById(R.id.fr_offer_price);
            holderView.fr_counter_offer_price = (FrameLayout) convertView.findViewById(R.id.fr_counter_offer_price);
            holderView.fr_reoffer_price = (FrameLayout) convertView.findViewById(R.id.fr_reoffer_price);
            holderView.iv_invoice = (LinearLayout) convertView.findViewById(R.id.iv_invoice);
            holderView.iv_dispute = (LinearLayout) convertView.findViewById(R.id.iv_dispute);
            holderView.id_chat = (TextView) convertView.findViewById(R.id.id_chat);
            holderView.img_product = (ImageView) convertView.findViewById(R.id.img_product);
            holderView.expected_arrival_days = (TextView) convertView.findViewById(R.id.expected_arrival_days);
            holderView.name_product = (TextView) convertView.findViewById(R.id.name_product);
            holderView.offer_price = (TextView) convertView.findViewById(R.id.offer_price);
            holderView.counter_offer_price = (TextView) convertView.findViewById(R.id.counter_offer_price);
            holderView.reoffer_price = (TextView) convertView.findViewById(R.id.reoffer_price);
            holderView.arrow_counter_offer = (TextView) convertView.findViewById(R.id.arrow_counter_offer);
            holderView.arrow_reoffer = (TextView) convertView.findViewById(R.id.arrow_reoffer);
            holderView.id_order_status = (LinearLayout) convertView.findViewById(R.id.id_order_status);
            holderView.id_delivery_status = (LinearLayout) convertView.findViewById(R.id.id_delivery_status);
            holderView.id_receive_status = (LinearLayout) convertView.findViewById(R.id.id_receive_status);
            holderView.id_buyer_accept = (LinearLayout) convertView.findViewById(R.id.id_buyer_accept);
            holderView.txt_order_status = (TextView) convertView.findViewById(R.id.txt_order_status);
            holderView.ln_status_reoffer = (LinearLayout) convertView.findViewById(R.id.ln_status_reoffer);
            holderView.tv_status_reoffer = (TextView) convertView.findViewById(R.id.tv_status_reoffer);
            holderView.ln_status_offer = (LinearLayout) convertView.findViewById(R.id.ln_status_offer);
            holderView.tv_status_offer = (TextView) convertView.findViewById(R.id.tv_status_offer);
            holderView.iv_detail_order = (LinearLayout) convertView.findViewById(R.id.iv_detail_order);
            holderView.ln_status_offer_accept = (LinearLayout) convertView.findViewById(R.id.ln_status_offer_acceptt);
            holderView.tv_status_offer_accept = (TextView) convertView.findViewById(R.id.tv_status_offer_accept);
            holderView.ln_status_reoffer_acceptt = (LinearLayout) convertView.findViewById(R.id.ln_status_reoffer_acceptt);
            holderView.tv_status_reoffer_accept = (TextView) convertView.findViewById(R.id.tv_status_reoffer_accept);
            holderView.ln_status_inform = (LinearLayout) convertView.findViewById(R.id.ln_status_inform);
            holderView.tv_status_inform = (TextView) convertView.findViewById(R.id.tv_status_inform);
            holderView.ll_status_bar = (LinearLayout) convertView.findViewById(R.id.ll_status_bar);
            holderView.view_empty_1 = convertView.findViewById(R.id.view_empty_1);
            holderView.view_empty_2 = convertView.findViewById(R.id.view_empty_2);
            holderView.view_empty_3 = convertView.findViewById(R.id.view_empty_3);
            holderView.view_empty_4 = convertView.findViewById(R.id.view_empty_4);
            convertView.setTag(holderView);
        }
        else{
            holderView = (HolderViewMyBid) convertView.getTag();
            if(holderView == null || holderView.arrow_reoffer == null){
                return convertView;
            }
        }
        // Get the data item for this position
        final ProductTenders productTender = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view


        holderView.fr_counter_offer_price.setVisibility(View.INVISIBLE);
        holderView.iv_detail_order.setVisibility(View.GONE);
        holderView.iv_invoice.setVisibility(View.GONE);
        holderView.iv_dispute.setVisibility(View.GONE);
        holderView.ln_status_offer.setVisibility(View.GONE);
        holderView.expected_arrival_days.setVisibility(View.VISIBLE);
        holderView.ln_status_offer_accept.setVisibility(View.GONE);
        holderView.counter_offer_price.setVisibility(View.GONE);
        holderView.arrow_counter_offer.setVisibility(View.INVISIBLE);
        holderView.reoffer_price.setVisibility(View.GONE);
        holderView.fr_reoffer_price.setVisibility(View.INVISIBLE);
        holderView.arrow_reoffer.setVisibility(View.INVISIBLE);
        holderView.ln_status_reoffer_acceptt.setVisibility(View.GONE);
        holderView.ln_status_reoffer.setVisibility(View.GONE);
        holderView.ln_status_offer.setVisibility(View.GONE);
        if (productTender.getProduct_listing() != null) {
            holderView.name_product.setText(productTender.getProduct_listing().getName().toString());
        }
        String str_offer_price = "", str_counter_offer = "", str_reoffer = "";
        if (productTender.getProduct_offers() != null && productTender.getProduct_offers().size() > 1) {
            holderView.ln_status_offer.setVisibility(View.GONE);
            holderView.ln_status_offer_accept.setVisibility(View.GONE);
            try {
                if (productTender.getProduct_offers().get(0).getOffer_price() != null) {
                    float fOfferPrice = Float.parseFloat(productTender.getProduct_offers().get(0).getOffer_price().getFractional()) / 100;
                    String strOfferPrice = String.format("%.2f", fOfferPrice);
                    str_offer_price = myactivity.getString(R.string.form_product_bid_price, String.valueOf(strOfferPrice));
                    holderView.offer_price.setText(str_offer_price);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (productTender.getProduct_offers().get(0).getProduct_counter_offer() != null) {
                try {
                    if (productTender.getProduct_offers().get(0).getProduct_counter_offer() != null) {
                        float fCounterOffer = Float.parseFloat(productTender.getProduct_offers().get(0).getProduct_counter_offer().getCounter_offer_price().getFractional()) / 100;
                        String strCounterOffer = String.format("%.2f", fCounterOffer);
                        str_counter_offer = myactivity.getString(R.string.form_product_counter_offer_price, String.valueOf(strCounterOffer));
                        holderView.counter_offer_price.setText(str_counter_offer);
                        holderView.counter_offer_price.setVisibility(View.VISIBLE);
                        holderView.fr_counter_offer_price.setVisibility(View.VISIBLE);
                        holderView.arrow_counter_offer.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (productTender.getProduct_offers().get(1).getOffer_price() != null) {
                        float fReofferPrice = Float.parseFloat(productTender.getProduct_offers().get(1).getOffer_price().getFractional()) / 100;
                        String strReOfferPrice = String.format("%.2f", fReofferPrice);
                        str_reoffer = myactivity.getString(R.string.form_product_re_bid_price, String.valueOf(strReOfferPrice));
                        holderView.reoffer_price.setText(str_reoffer);
                        holderView.reoffer_price.setVisibility(View.VISIBLE);
                        holderView.fr_reoffer_price.setVisibility(View.VISIBLE);
                        holderView.arrow_reoffer.setVisibility(View.VISIBLE);
                        if (productTender.getProduct_offers().get(1).getState().equals("accepted")) {
                            float price = 0;
                            String strReOfferPriceAccept = "";
                            ProductOffer productOfferAccept = GetProductTenderOfferAccept(productTender.getProduct_offers());
                            if (productOfferAccept != null) {
                                price = Float.parseFloat(productOfferAccept.getOffer_price().getFractional()) / 100;
                                strReOfferPriceAccept = String.format("%.2f", price);
                            }
                            holderView.ln_status_reoffer_acceptt.setVisibility(View.VISIBLE);
                            holderView.ln_status_reoffer.setVisibility(View.GONE);
                            holderView.tv_status_reoffer_accept.setText(CustomApp.getInstance().getString(R.string.price_accept, strReOfferPriceAccept));
                        } else {
                            holderView.ln_status_reoffer_acceptt.setVisibility(View.GONE);
                            holderView.ln_status_reoffer.setVisibility(View.VISIBLE);
                            if(productTender.getProduct_offers().get(1).getState().equals("rejected")){
                                holderView.tv_status_reoffer.setText(R.string.label_buyer_reject_offer);
                            }
                            else {
                                holderView.tv_status_reoffer.setText(R.string.label_waiting_response);
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (productTender.getProduct_offers() != null && productTender.getProduct_offers().size() == 1) {
            float fOfferPrice = Float.parseFloat(productTender.getProduct_offers().get(0).getOffer_price().getFractional()) / 100;
            String strReOfferPrice = String.format("%.2f", fOfferPrice);
            str_offer_price = myactivity.getString(R.string.form_product_bid_price, String.valueOf(strReOfferPrice));
            holderView.offer_price.setText(str_offer_price);
            if (productTender.getProduct_offers().get(0).getState().equals("accepted")) {
                holderView.ln_status_offer_accept.setVisibility(View.VISIBLE);
                float price = 0;
                String strOfferPriceAccept = "";
                ProductOffer productOfferAccept = GetProductTenderOfferAccept(productTender.getProduct_offers());
                if (productOfferAccept != null) {
                    price = Float.parseFloat(productOfferAccept.getOffer_price().getFractional()) / 100;
                    strOfferPriceAccept = String.format("%.2f", price);
                }
                holderView.tv_status_offer_accept.setText(CustomApp.getInstance().getString(R.string.price_accept, strOfferPriceAccept));
                holderView.ln_status_offer.setVisibility(View.GONE);
            } else {
                holderView.ln_status_offer_accept.setVisibility(View.GONE);
                holderView.ln_status_offer.setVisibility(View.VISIBLE);
                if(productTender.getProduct_offers().get(0).getState().equals("rejected")){
                    holderView.tv_status_offer.setText(R.string.label_buyer_reject_offer);
                }
                else {
                    holderView.tv_status_offer.setText(R.string.label_waiting_response);
                }
            }
            try {
                if (productTender.getProduct_offers().get(0).getProduct_counter_offer() != null) {
                    float fCounterOffer = Float.parseFloat(productTender.getProduct_offers().get(0).getProduct_counter_offer().getCounter_offer_price().getFractional()) / 100;
                    String strCounterOfferPrice = String.format("%.2f", fCounterOffer);
                    str_counter_offer = myactivity.getString(R.string.form_product_counter_offer_price, String.valueOf(strCounterOfferPrice));
                    holderView.counter_offer_price.setText(str_counter_offer);
                    holderView.counter_offer_price.setVisibility(View.VISIBLE);
                    holderView.fr_counter_offer_price.setVisibility(View.VISIBLE);
                    holderView.arrow_counter_offer.setVisibility(View.VISIBLE);
                    holderView.ln_status_offer.setVisibility(View.GONE);
                    holderView.ln_status_offer_accept.setVisibility(View.GONE);
                } else {
                    holderView.arrow_counter_offer.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String str_expected_arrival_days = "";
        String date_limit = "";
        try {
            if (productTender.getProduct_tender_shippings() != null) {
                if (productTender.getProduct_tender_shippings().size() != 0) {
                    if(productTender.getProduct_listing().getExpired_date_text().isEmpty() == false){
                        holderView.expected_arrival_days.setText(productTender.getProduct_listing().getExpiry_status());
                    }
                    else{
                        Date date = Calendar.getInstance().getTime();
                        Date expert = productTender.getProduct_listing().getExpired_date();
                        int expireDate = 0;
                        if (expert != null) {
                            long restDatesinMillis = expert.getTime() - date.getTime();
                            expireDate = (int) (restDatesinMillis / (24 * 60 * 60 * 1000));
                            date_limit = CustomApp.getInstance().getString(R.string.ad_limmit, expireDate);
                        }
                        if (expireDate <= 0) {
                            String strDateExpire = "已过期";
                            holderView.expected_arrival_days.setText(productTender.getProduct_listing().getExpiry_status());
                        } else {
                            holderView.expected_arrival_days.setText(productTender.getProduct_listing().getExpiry_status());
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String urlImage = ApiServices.BASE_URI + productTender.getProduct_listing().product_images.get(0).product_image.thumb.getUrl();
            if (urlImage != null && !urlImage.equals("")) {
                ImageLoader il = ImageLoader.getInstance();
                il.displayImage(urlImage, holderView.img_product, OPTION_DISPLAY_IMG_PRODUCT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //chat
        holderView.id_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tendererID = 0, wanttobuyID = 0;
                try {
                    tendererID = MyPreferences.getID();
                    wanttobuyID = productTender.getProduct_listing().getId();
                    MainActivity mainActivity = (MainActivity) ProductsMyBidstAdapter.this.myactivity;
                    ChatDetailFragment fragment = new ChatDetailFragment();
                    fragment.setWantTobuyID(wanttobuyID);
                    fragment.setTendererID(tendererID);
                    mainActivity.changeFragment(fragment, true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //dispute product
        try {
            boolean isAcceptDispute = CheckOfferAccept(productTender);
            if (isAcceptDispute) {
                if (productTender.getProduct_listing().getReported_id() != null
                        && productTender.getProduct_listing().getWant_to_buy_id() != null) {
                    final OrderModel orderAccept = GetOrderAccept(productTender.getProduct_listing().getOrders());
                    if(orderAccept != null){
                        holderView.iv_detail_order.setVisibility(View.VISIBLE);
                        holderView.iv_detail_order.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MainActivity mainActivity = (MainActivity) ProductsMyBidstAdapter.this.getContext();
                                DetailOrderFragment fragment = new DetailOrderFragment();
                                fragment.setIdOrder(orderAccept.getId());
                                fragment.setFlag("offerer_order");
                                mainActivity.changeFragment(fragment, true);
                            }
                        });
                    }

                    holderView.iv_dispute.setVisibility(View.VISIBLE);
//                    iv_dispute.setImageResource(R.drawable.ic_dispute_cp);
                    holderView.iv_dispute.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ProductsMyBidstAdapter.this.myactivity instanceof MainActivity) {
                                MainActivity mainActivity = (MainActivity) ProductsMyBidstAdapter.this.myactivity;
                                DisputeFormFragment fragment = new DisputeFormFragment();
                                RsProduct product = new RsProduct(productTender);
                                fragment.setRsProduct(product);
                                fragment.setFlag("mybid");
                                mainActivity.changeFragment(fragment, true);
                            }
                        }
                    });
                    holderView.iv_invoice.setVisibility(View.VISIBLE);
                    holderView.iv_invoice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int idOrder = 0;
                            for (OrderModel order : productTender.getProduct_listing().getOrders()) {
                                if (order.getStatus().equals(STATUS_ORDER_PROCESSING) || order.getStatus().equals(STATUS_ORDER_CONFIRMED)) {
                                    idOrder = order.getId();
                                    break;
                                }
                            }
                            DialogInvoice dialogInvoice = new DialogInvoice(ProductsMyBidstAdapter.this.myactivity, idOrder, myBidFragment);
//                            dialogInvoice.setMyBidFragment(myBidFragment);
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            Window window = dialogInvoice.getWindow();
                            lp.copyFrom(window.getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            dialogInvoice.show();
                            dialogInvoice.getWindow().setAttributes(lp);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holderView.fr_offer_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String flag = "offer";
                DialogDetailOffer dialogDetailOffer = new DialogDetailOffer(ProductsMyBidstAdapter.this.myactivity, productTender, flag);
                dialogDetailOffer.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialogDetailOffer.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
            }
        });

        holderView.fr_reoffer_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String flag = "reoffer";
                DialogDetailOffer dialogDetailOffer = new DialogDetailOffer(ProductsMyBidstAdapter.this.myactivity, productTender, flag);
                dialogDetailOffer.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialogDetailOffer.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
            }
        });

        holderView.fr_counter_offer_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDetailCounterOffer dialogDetailCounterOffer = new DialogDetailCounterOffer(ProductsMyBidstAdapter.this.myactivity, productTender);
                dialogDetailCounterOffer.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialogDetailCounterOffer.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
            }
        });
        boolean delivery = false, receive = false;
        try {

                if (productTender.getProduct_listing().isClosed()) {
                    holderView.expected_arrival_days.setVisibility(View.INVISIBLE);
                    if (productTender.getProduct_listing().getReported_id() == null && productTender.getProduct_listing().getWant_to_buy_id() == null) {
                        holderView.txt_order_status.setText(R.string.label_payment_review);
                        holderView.id_order_status.setVisibility(View.VISIBLE);
                        holderView.id_buyer_accept.setVisibility(View.GONE);

                    }
                    if (productTender.getProduct_listing().getReported_id() != null && productTender.getProduct_listing().getWant_to_buy_id() != null) {
                        holderView.id_buyer_accept.setVisibility(View.VISIBLE);
                        holderView.txt_order_status.setText(R.string.label_waiting_for_delivery);
                        holderView.id_order_status.setBackgroundResource(R.color.button_accept);
                        holderView.id_order_status.setVisibility(View.VISIBLE);
                        holderView.id_receive_status.setVisibility(View.GONE);
                        holderView.id_delivery_status.setVisibility(View.GONE);
                        OrderModel orderAccept = GetOrderAccept(productTender.getProduct_listing().getOrders());
                        if(orderAccept != null){
                            if (orderAccept.getBuyer_receive_status().equals(BUYER_RECEIVE_SENT)) {
                                holderView.id_order_status.setVisibility(View.GONE);
                                holderView.id_receive_status.setVisibility(View.VISIBLE);
                                holderView.id_delivery_status.setVisibility(View.GONE);
                            } else if (orderAccept.getSeller_delivery_status().equals(SELLER_DELIVERY_SENT)) {
                                holderView.id_order_status.setVisibility(View.GONE);
                                holderView.id_receive_status.setVisibility(View.GONE);
                                holderView.id_delivery_status.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }

        int idOrderAccept = 0;
        boolean isOrderAccept = false;
        try {
            idOrderAccept = GetOrderAccept(productTender.getProduct_listing().getOrders()).getId();
            if (GetOrderAccept(productTender.getProduct_listing().getOrders()) != null) {
                isOrderAccept = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final int finalIdOrderAccept = idOrderAccept;
        if (isOrderAccept == true && idOrderAccept != 0) {

        }
        if(productTender.getProduct_offers().get(productTender.getProduct_offers().size() - 1).getState().equals("rejected")){
            holderView.ln_status_inform.setVisibility(View.GONE);
        }
        else {
            try {
                if (productTender.getProduct_listing().isClosed()) {
                    if (productTender.getProduct_offers().size() == 1
                            && productTender.getProduct_offers().get(0).getProduct_counter_offer() != null) {
                        holderView.ln_status_inform.setVisibility(View.VISIBLE);
                        holderView.tv_status_inform.setText(productTender.getProduct_listing().getCommon_user().getNickname() + " 已经接受了其他用户的出价");
                    } else {
                        holderView.ln_status_inform.setVisibility(View.GONE);
                    }
                }
                //Reoffer tag
                else if (productTender.getProduct_listing().isClosed() == false) {
                    if (productTender.getProduct_offers().size() == 1
                            && productTender.getProduct_offers().get(0).getProduct_counter_offer() != null) {
                        holderView.ln_status_inform.setVisibility(View.VISIBLE);
                        holderView.tv_status_inform.setText("重新出价");
                        if(holderView.tv_status_inform.getParent() instanceof LinearLayout){
                            ((LinearLayout)holderView.tv_status_inform.getParent()).setBackgroundColor(ContextCompat.getColor(CustomApp.getInstance(),R.color.button_reoffer));
                        }
                        final ProductTenders product_tender_to_reoffer = productTender;
                        holderView.ln_status_inform.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                float fCounterOfferPrice = Float.parseFloat(product_tender_to_reoffer.getProduct_offers().get(0).getProduct_counter_offer().getCounter_offer_price().getFractional()) / 100;
                                String strCounterOfferPrice = String.format("%.2f", fCounterOfferPrice);
                                String str_counter_offer_price = strCounterOfferPrice;
                                String finalOfferPrice = String.valueOf(Float.parseFloat(product_tender_to_reoffer.getProduct_offers().get(0).getOffer_price().getFractional()) / 100);
                                DialogRebid dialog_bidshop = new DialogRebid(myactivity, product_tender_to_reoffer.getProduct_listing().getId(), product_tender_to_reoffer.getId(), true, str_counter_offer_price, true,
                                        finalOfferPrice, productTender.getProduct_offers().get(0).getProduct_counter_offer().getRemarks());
                                dialog_bidshop.setCanceledOnTouchOutside(true);
                                dialog_bidshop.f = myBidFragment;
                                RsProduct myProduct = product_tender_to_reoffer.getProduct_in_tender();
                                if (myProduct != null) {
                                    dialog_bidshop.setMyProduct(myProduct);
                                }
                                dialog_bidshop.setProductTenders(product_tender_to_reoffer);
                                dialog_bidshop.show();


                                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                                layoutParams.copyFrom(dialog_bidshop.getWindow().getAttributes());
                                int dialogWindowWidth = WindowManager.LayoutParams.MATCH_PARENT;
                                int dialogWindowHeight = WindowManager.LayoutParams.WRAP_CONTENT;
                                layoutParams.width = dialogWindowWidth;
                                layoutParams.height = dialogWindowHeight;
                                dialog_bidshop.getWindow().setAttributes(layoutParams);
                            }
                        });

                    } else {
                        holderView.ln_status_inform.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            if (productTender.getProduct_listing() != null && productTender.getProduct_listing().isClosed() == true) {
                if (productTender.getProduct_offers().size() > 1) {
                    if (productTender.getProduct_offers().get(1).getState().equals("rejected")) {
                        holderView.ln_status_reoffer.setVisibility(View.VISIBLE);
                        holderView.tv_status_reoffer.setText(R.string.label_buyer_reject_offer);
                    } else if(productTender.getProduct_offers().get(1).getState().equals("untouched")){
                        holderView.ln_status_reoffer.setVisibility(View.VISIBLE);
                        holderView.tv_status_reoffer.setText(productTender.getProduct_listing().getCommon_user().getNickname() + " 已经接受了其他用户的出价");
                    }
                    else {
                        holderView.ln_status_reoffer.setVisibility(View.GONE);
                    }
                } else {
                    if (productTender.getProduct_offers().get(0).getState().equals("rejected")) {
                        holderView.ln_status_offer.setVisibility(View.VISIBLE);
                        holderView.tv_status_offer.setText(R.string.label_buyer_reject_offer);
                    }
                    else if(productTender.getProduct_offers().get(0).getState().equals("untouched") && productTender.getProduct_offers().get(0).getProduct_counter_offer() == null){
                        holderView.ln_status_offer.setVisibility(View.VISIBLE);
                        holderView.tv_status_offer.setText(productTender.getProduct_listing().getCommon_user().getNickname() + " 已经接受了其他用户的出价");
                    }
                    else

                        {
                            holderView.ln_status_offer.setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(CheckOfferAccept(productTender) == true){
            holderView.ll_status_bar.setVisibility(View.VISIBLE);
        }
        else{
            holderView.ll_status_bar.setVisibility(View.GONE);
        }
        int num_of_hide_button = 0;
        holderView.view_empty_1.setVisibility(View.GONE);
        holderView.view_empty_2.setVisibility(View.GONE);
        holderView.view_empty_3.setVisibility(View.GONE);

        ArrayList<View> list_empty_view = new ArrayList<>();
        list_empty_view.add(holderView.view_empty_1);
        list_empty_view.add(holderView.view_empty_2);
        list_empty_view.add(holderView.view_empty_3);
        if(holderView.iv_detail_order.getVisibility() != View.VISIBLE){
            list_empty_view.get(num_of_hide_button).setVisibility(View.VISIBLE);
            num_of_hide_button += 1;
        }
        if(holderView.iv_invoice.getVisibility() != View.VISIBLE){
            list_empty_view.get(num_of_hide_button).setVisibility(View.VISIBLE);
            num_of_hide_button += 1;
        }
        if(holderView.iv_dispute.getVisibility() != View.VISIBLE){
            list_empty_view.get(num_of_hide_button).setVisibility(View.VISIBLE);
            num_of_hide_button += 1;
        }
        return convertView;
    }

    /**
     * Check offer accept
     */
    public boolean CheckOfferAccept(ProductTenders offerMeModel) {
        try {
            if (offerMeModel.isClosed()) {
                for (ProductOffer offer : offerMeModel.getProduct_offers()) {
                    if (offer.getState().equals("accepted")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get order confirm or prcessing
     *
     * @params:
     * @return:
     */
    private OrderModel GetOrderAccept(ArrayList<OrderModel> listOrder) {
        for (int i = 0; i < listOrder.size(); i++) {
            if (listOrder.get(i).getStatus().equals(STATUS_ORDER_CONFIRMED)
                    || listOrder.get(i).getStatus().equals(STATUS_ORDER_PROCESSING)) {
                return listOrder.get(i);
            }
        }
        return null;
    }

    /**
     * Get order confirm or prcessing
     *
     * @params:
     * @return:
     */
    private ProductOffer GetProductTenderOfferAccept(ArrayList<ProductOffer> productOffers) {
        for (int i = 0; i < productOffers.size(); i++) {
            if (productOffers.get(i).getState().equals(OFFER_ACCEPT)) {
                return productOffers.get(i);
            }
        }
        return null;
    }
}

class HolderViewMyBid {
    public ImageView img_product;
    public TextView id_chat;
    public TextView expected_arrival_days, name_product, offer_price, counter_offer_price, reoffer_price,
            arrow_counter_offer, arrow_reoffer, txt_order_status, tv_status_reoffer, tv_status_offer,
            tv_status_offer_accept, tv_status_reoffer_accept, tv_status_inform;
    public FrameLayout fr_offer_price, fr_counter_offer_price, fr_reoffer_price;
    public LinearLayout id_order_status, id_buyer_accept, id_delivery_status, id_receive_status, ln_status_reoffer,
            ln_status_offer, ln_status_offer_accept, ln_status_reoffer_acceptt, iv_dispute,
            iv_invoice, iv_detail_order, ln_status_inform, ll_status_bar;
    public View view_empty_1, view_empty_2, view_empty_3, view_empty_4;
}
