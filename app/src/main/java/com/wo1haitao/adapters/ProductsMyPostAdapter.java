package com.wo1haitao.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.InboxRs;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.dialogs.DialogConfirmDelete;
import com.wo1haitao.dialogs.DialogConfirmEdit;
import com.wo1haitao.dialogs.DialogConfirmReceipt;
import com.wo1haitao.dialogs.DialogConfirmRepost;
import com.wo1haitao.fragments.ChatDetailFragment;
import com.wo1haitao.fragments.DetailOrderFragment;
import com.wo1haitao.fragments.DisputeFormFragment;
import com.wo1haitao.fragments.MyPostFragment;
import com.wo1haitao.models.ProductListing;
import com.wo1haitao.models.ProductOffer;
import com.wo1haitao.models.ProductShow;
import com.wo1haitao.models.ProductTenders;
import com.wo1haitao.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG_PRODUCT;

/**
 * Created by goodproductssoft on 4/20/17.
 */

public class ProductsMyPostAdapter extends ArrayAdapter<RsProduct> {
    Activity context;
    int layout_id;
    ArrayList<RsProduct> products;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    ArrayList<InboxRs> arrayOfInbox;
    static final String SELLER_DELIVERY_SENT = "delivery_sent";
    static final String BUYER_RECEIVE_SENT = "item_received";
    static final String STATUS_ORDER_PROCESSING = "processing";
    static final String STATUS_ORDER_CONFIRMED = "confirmed";
    static final String STATE_OFFER_ACCPTED = "accepted";
    MyPostFragment f;

    public ProductsMyPostAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull ArrayList<RsProduct> objects, MyPostFragment f) {
        super(context, resource, objects);
        this.context = context;
        this.layout_id = resource;
        this.products = objects;
        this.f = f;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // init
        ViewHolderMyPost viewHolderMyPost;
        if(convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layout_id, null);
            viewHolderMyPost = new ViewHolderMyPost();
            viewHolderMyPost.tv_name = (TextView) convertView.findViewById(R.id.tv_name_product);
            //tv_category = (TextView) convertView.findViewById(R.id.tv_category);
            viewHolderMyPost.tv_category = (TextView) convertView.findViewById(R.id.tv_category);
            viewHolderMyPost.tv_country = (TextView) convertView.findViewById(R.id.tv_country);
            viewHolderMyPost.tv_ad_limit = (TextView) convertView.findViewById(R.id.tv_ad_limit);
            viewHolderMyPost.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            viewHolderMyPost.iv_edit = (ImageView) convertView.findViewById(R.id.iv_edit);
            viewHolderMyPost.iv_dispute = (LinearLayout) convertView.findViewById(R.id.iv_dispute);
            viewHolderMyPost.iv_repost = (ImageView) convertView.findViewById(R.id.iv_repost);
            viewHolderMyPost.iv_product_img = (ImageView) convertView.findViewById(R.id.iv_product_img);
            viewHolderMyPost.id_chat = (TextView) convertView.findViewById(R.id.id_chat);
            viewHolderMyPost.bid = (TextView) convertView.findViewById(R.id.bid);

            viewHolderMyPost.id_order_status = (LinearLayout) convertView.findViewById(R.id.id_order_status);
            viewHolderMyPost.id_delivery_status = (LinearLayout) convertView.findViewById(R.id.id_delivery_status);
            viewHolderMyPost.id_receive_status = (LinearLayout) convertView.findViewById(R.id.id_receive_status);
            viewHolderMyPost.txt_order_status = (TextView) convertView.findViewById(R.id.txt_order_status);
            viewHolderMyPost.iv_detail_order = (LinearLayout) convertView.findViewById(R.id.iv_detail_order);
            viewHolderMyPost.fr_confirm_receipt = (TextView) convertView.findViewById(R.id.fr_confirm_receipt);
            viewHolderMyPost.id_buyer_accept = (LinearLayout) convertView.findViewById(R.id.id_buyer_accept);

            viewHolderMyPost.view_empty_1 = convertView.findViewById(R.id.view_empty_1);
            viewHolderMyPost.view_empty_2 = convertView.findViewById(R.id.view_empty_2);
            viewHolderMyPost.view_empty_3 = convertView.findViewById(R.id.view_empty_3);
            viewHolderMyPost.view_empty_4 = convertView.findViewById(R.id.view_empty_4);
            convertView.setTag(viewHolderMyPost);
        }
        else{
            viewHolderMyPost = (ViewHolderMyPost) convertView.getTag();
            if(viewHolderMyPost == null || viewHolderMyPost.bid == null){
                return convertView;
            }
        }
        // Get the data item for this position
        final RsProduct product = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ProductShow productShow = new ProductShow(product);


        String str_num_of_offers = "";
        str_num_of_offers = CustomApp.getInstance().getString(R.string.num_of_offers, product.getNum_of_offers());
        viewHolderMyPost.iv_dispute.setVisibility(View.GONE);
        viewHolderMyPost.tv_ad_limit.setVisibility(View.VISIBLE);
        if(product.isClosed() == true){
            viewHolderMyPost.tv_ad_limit.setVisibility(View.INVISIBLE);
        }
        if (product.getReported_id() != null && product.getWant_to_buy_id() != null) {
            viewHolderMyPost.iv_repost.setVisibility(View.GONE);
            int idOrderAccept = 0;
            for (int i = 0; i < product.getOrders().size(); i++) {
                if (product.getOrders().get(i).getStatus().equals(STATUS_ORDER_PROCESSING)
                        || product.getOrders().get(i).getStatus().equals(STATUS_ORDER_CONFIRMED)) {
                    idOrderAccept = product.getOrders().get(i).getId();
                    break;
                }
            }
            final int finalIdOrderAccept = idOrderAccept;
            if(finalIdOrderAccept > 0){
                viewHolderMyPost.iv_detail_order.setVisibility(View.VISIBLE);
                viewHolderMyPost.iv_detail_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity mainActivity = (MainActivity) ProductsMyPostAdapter.this.getContext();
                        DetailOrderFragment fragment = new DetailOrderFragment();
                        fragment.setIdOrder(finalIdOrderAccept);
                        fragment.setFlag("offer_order");
                        mainActivity.changeFragment(fragment, true);
                    }
                });
            }
            viewHolderMyPost.iv_dispute.setVisibility(View.VISIBLE);
            viewHolderMyPost.iv_dispute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ProductsMyPostAdapter.this.context instanceof MainActivity) {
                        if (product.getReported_id() != null && product.getWant_to_buy_id() != null) {
                            MainActivity mainActivity = (MainActivity) ProductsMyPostAdapter.this.context;
                            DisputeFormFragment fragment = new DisputeFormFragment();
                            fragment.setRsProduct(product);
                            fragment.setFlag("mypost");
                            mainActivity.changeFragment(fragment, true);
                        }
                    }

                }
            });
            viewHolderMyPost.bid.setVisibility(View.GONE);
            if (productShow.getDay() <= 0) {
                String date_limit = "已过期";
                viewHolderMyPost.tv_ad_limit.setText(productShow.getExpiry_status());
            } else {
                viewHolderMyPost.tv_ad_limit.setText(productShow.getExpiry_status());
            }
        } else {

            //iv_dispute.setVisibility(View.GONE);
            if (product.isExpired()) {
                if (!CheckState(product)) {
                   // iv_dispute.setVisibility(View.VISIBLE);
                    viewHolderMyPost.iv_repost.setVisibility(View.VISIBLE);
                }
                else{
                    viewHolderMyPost.iv_repost.setVisibility(View.GONE);
                }
               // iv_dispute.setVisibility(View.VISIBLE);
                if (product.getNum_of_offers() == 0) {
                    //   iv_dispute.setVisibility(View.GONE);
                    viewHolderMyPost.bid.setVisibility(View.GONE);
                    //    iv_dispute.setVisibility(View.VISIBLE);
                } else {
                    viewHolderMyPost.bid.setVisibility(View.VISIBLE);
                    viewHolderMyPost.bid.setText(str_num_of_offers);
                    viewHolderMyPost.bid.setTextColor(Color.WHITE);
                }
                String date_limit = "已过期";
                viewHolderMyPost.tv_ad_limit.setText(productShow.getExpiry_status());
            } else {
                viewHolderMyPost.iv_repost.setVisibility(View.GONE);
                if (product.getNum_of_offers() == 0) {
                 //   iv_dispute.setVisibility(View.GONE);
                    viewHolderMyPost.bid.setVisibility(View.GONE);
                //    iv_dispute.setVisibility(View.VISIBLE);
                } else {
                    viewHolderMyPost.bid.setVisibility(View.VISIBLE);
                    viewHolderMyPost.bid.setText(str_num_of_offers);
                    viewHolderMyPost.bid.setTextColor(Color.WHITE);
                    for (int i = 0; i < product.getProduct_tenders().size(); i++) {
                        ProductTenders tender = product.getProduct_tenders().get(i);
                        for (int j = 0; j < tender.getProduct_offers().size(); j++) {
                            if (tender.getProduct_offers().get(j).getState().equals("accepted")) {
                                viewHolderMyPost.bid.setText(str_num_of_offers);
                                break;
                            }
                        }
                    }
                  //  iv_dispute.setVisibility(View.VISIBLE);
                }
                viewHolderMyPost.tv_ad_limit.setText(productShow.getExpiry_status());

            }
            viewHolderMyPost.iv_dispute.setVisibility(View.GONE);
        }

        if (productShow.getName() != null) {
            viewHolderMyPost.tv_name.setText(productShow.getName());
        }
        if (product.getCategory() != null) {
            viewHolderMyPost.tv_category.setText(context.getString(R.string.category_value, product.getCategory().getName()));
        }
        try {
            String countries = "";
            if (product.isNon_restricted_country() == true) {
                countries = getContext().getString(R.string.txt_no_limited_country);
            } else {
                countries = GetListCountry(product);
            }
            viewHolderMyPost.tv_country.setText(context.getString(R.string.country_value, countries));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (productShow.getList_image().size() != 0) {
                String url = productShow.getList_image().get(0);
                imageLoader.displayImage(url, viewHolderMyPost.iv_product_img, OPTION_DISPLAY_IMG_PRODUCT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolderMyPost.iv_repost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = ProductsMyPostAdapter.this.getContext().getResources().getString(R.string.message_alert_dispute);
                DialogConfirmRepost dialogConfirmRepost = new DialogConfirmRepost(getContext(), msg, false, ProductsMyPostAdapter.this, product.getId());
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialogConfirmRepost.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialogConfirmRepost.show();
            }
        });
        viewHolderMyPost.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CheckState(product)) {
                    String message = ProductsMyPostAdapter.this.getContext().getResources().getString(R.string.message_alert_delete);
                    DialogConfirmDelete dialogConfirmDelete = new DialogConfirmDelete(context, message, ProductsMyPostAdapter.this, product.getId(), false);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialogConfirmDelete.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialogConfirmDelete.show();
                } else {
                    String message = ProductsMyPostAdapter.this.getContext().getResources().getString(R.string.message_alert_waring_delete);
                    DialogConfirmDelete dialogConfirmDelete = new DialogConfirmDelete(context, message, ProductsMyPostAdapter.this, product.getId(), true);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialogConfirmDelete.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialogConfirmDelete.show();
                }
            }
        });

        //show button
        if (product.getNum_of_offers() > 0) {
            viewHolderMyPost.iv_edit.setVisibility(View.GONE);
        } else {
            viewHolderMyPost.iv_edit.setVisibility(View.VISIBLE);
            viewHolderMyPost.iv_delete.setVisibility(View.VISIBLE);
        }
        if (CheckState(product)) {
            viewHolderMyPost.iv_delete.setVisibility(View.GONE);
            viewHolderMyPost.iv_edit.setVisibility(View.GONE);
        }
        viewHolderMyPost.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CheckState(product)) {
                    if (product.getNum_of_offers() == 0) {
                        if (ProductsMyPostAdapter.this.context instanceof MainActivity) {
                            MainActivity mainActivity = (MainActivity) ProductsMyPostAdapter.this.context;
                            mainActivity.changeRequestPurchase(product, "edit");
                        }
                    } else {
                        String message = ProductsMyPostAdapter.this.getContext().getResources().getString(R.string.message_alert_warning_edit);
                        DialogConfirmEdit dialogConfirmDelete = new DialogConfirmEdit(context, message);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialogConfirmDelete.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                        dialogConfirmDelete.show();
                    }
                }
            }
        });
        int tendererID = 0, wanttobuyID = 0;
        try {
            for (int i = 0; i < product.getProduct_tenders().size(); i++) {
                ProductTenders tender = product.getProduct_tenders().get(i);
                for (int j = 0; j < tender.getProduct_offers().size(); j++) {
                    if (tender.getProduct_offers().get(j).getState().equals("accepted")) {
                        tendererID = tender.getCommon_user().getId();
                        break;
                    }
                }
            }
            wanttobuyID = product.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CheckState(product)) {
            viewHolderMyPost.id_chat.setVisibility(View.VISIBLE);
        }
        final int finalTendererID = tendererID;
        final int finalWanttobuyID = wanttobuyID;
        viewHolderMyPost.id_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity mainActivity = (MainActivity) ProductsMyPostAdapter.this.context;
                    ChatDetailFragment fragment = new ChatDetailFragment();
                    fragment.setWantTobuyID(finalWanttobuyID);
                    fragment.setTendererID(finalTendererID);
                    mainActivity.changeFragment(fragment, true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            if (!product.isClosed()) {

                viewHolderMyPost.id_receive_status.setVisibility(View.GONE);
                viewHolderMyPost.id_delivery_status.setVisibility(View.GONE);
                viewHolderMyPost.txt_order_status.setText("等待您的回应");
                viewHolderMyPost.id_order_status.setBackgroundResource(R.color.delivery_warning);
                viewHolderMyPost.id_order_status.setVisibility(View.GONE);
                viewHolderMyPost.id_buyer_accept.setVisibility(View.GONE);
                for (int i = 0; i < product.getProduct_tenders().size(); i++) {
                    if (product.getProduct_tenders().get(i).getProduct_offers() != null
                            && !product.getProduct_tenders().get(i).isClosed()) {
                        if(product.getProduct_tenders().get(i).getProduct_offers().size() == 1){
                            if(product.getProduct_tenders().get(i).getProduct_offers().get(0).getProduct_counter_offer() == null){
                                viewHolderMyPost.id_order_status.setVisibility(View.VISIBLE);
                                break;
                            }
                            else {
                                viewHolderMyPost.id_order_status.setVisibility(View.GONE);
                            }
                        }
                        else {
                            if(product.getProduct_tenders().get(i).getProduct_offers().get(1).getProduct_counter_offer() == null){
                                viewHolderMyPost.id_order_status.setVisibility(View.VISIBLE);
                                break;
                            }
                            else {
                                viewHolderMyPost.id_order_status.setVisibility(View.GONE);}
                        }
                    }
                }
            } else {
                viewHolderMyPost.id_receive_status.setVisibility(View.GONE);
                viewHolderMyPost.id_delivery_status.setVisibility(View.GONE);
                viewHolderMyPost.id_order_status.setVisibility(View.GONE);
                viewHolderMyPost.id_buyer_accept.setVisibility(View.GONE);
                viewHolderMyPost.id_order_status.setBackgroundResource(R.color.delivery_warning);
                int indexOrder = 0;
                indexOrder = -1;
                for (int i = 0; i < product.getOrders().size(); i++) {
                    if (product.getOrders().get(i).getStatus().equals(STATUS_ORDER_PROCESSING)
                            || product.getOrders().get(i).getStatus().equals(STATUS_ORDER_CONFIRMED)) {
                        indexOrder = i;
                        break;
                    }
                }
                //bid.setVisibility(View.GONE);
                if (product.getReported_id() == null && product.getWant_to_buy_id() == null) {
                    viewHolderMyPost.txt_order_status.setText(R.string.label_payment_review);
                    viewHolderMyPost.id_order_status.setVisibility(View.VISIBLE);
                    viewHolderMyPost.id_buyer_accept.setVisibility(View.GONE);
                    viewHolderMyPost.fr_confirm_receipt.setVisibility(View.GONE);
                } else if (product.getReported_id() != null && product.getWant_to_buy_id() != null) {
                    viewHolderMyPost.txt_order_status.setText(R.string.label_waiting_for_delivery);
                    viewHolderMyPost.id_order_status.setBackgroundResource(R.color.button_accept);
                    viewHolderMyPost.id_order_status.setVisibility(View.VISIBLE);
                    viewHolderMyPost.id_buyer_accept.setVisibility(View.VISIBLE);
                    // bid.setVisibility(View.GONE);

                    if (indexOrder != -1) {
                        viewHolderMyPost.bid.setVisibility(View.GONE);
                        viewHolderMyPost.fr_confirm_receipt.setVisibility(View.VISIBLE);
                        if (product.getOrders().get(indexOrder).getBuyer_receive_status().equals(BUYER_RECEIVE_SENT)) {

                            viewHolderMyPost.id_order_status.setVisibility(View.GONE);
                            viewHolderMyPost.id_receive_status.setVisibility(View.VISIBLE);
                            viewHolderMyPost.id_delivery_status.setVisibility(View.GONE);
                            viewHolderMyPost.fr_confirm_receipt.setVisibility(View.GONE);
                        } else if (product.getOrders().get(indexOrder).getSeller_delivery_status().equals(SELLER_DELIVERY_SENT)) {
                            viewHolderMyPost.id_order_status.setVisibility(View.GONE);
                            viewHolderMyPost.id_receive_status.setVisibility(View.GONE);
                            viewHolderMyPost.id_delivery_status.setVisibility(View.VISIBLE);
                        }
                        else{
                            viewHolderMyPost.id_order_status.setVisibility(View.VISIBLE);
                            viewHolderMyPost.id_receive_status.setVisibility(View.GONE);
                            viewHolderMyPost.id_delivery_status.setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int idOrderAccept = 0;
        boolean isOrderAccept = false;
        for (int i = 0; i < product.getOrders().size(); i++) {
            if (product.getOrders().get(i).getStatus().equals(STATUS_ORDER_PROCESSING)
                    || product.getOrders().get(i).getStatus().equals(STATUS_ORDER_CONFIRMED)) {
                idOrderAccept = product.getOrders().get(i).getId();
                isOrderAccept = true;
                break;
            }
        }
        final int finalIdOrderAccept = idOrderAccept;


        //confirm receipt item
        viewHolderMyPost.fr_confirm_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = ProductsMyPostAdapter.this.getContext().getResources().getString(R.string.title_alert_confirm_receipt);

                DialogConfirmReceipt dialogConfirmReceipt = new DialogConfirmReceipt(context, finalIdOrderAccept, f, "确认收货后货款将被支付给代购\n确认收货?");
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialogConfirmReceipt.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialogConfirmReceipt.show();
            }
        });
//        bid.setVisibility(View.GONE);


        int num_of_hide_button = 0;

        ArrayList<View> list_empty_view = new ArrayList<>();
        list_empty_view.add(viewHolderMyPost.view_empty_1);
        list_empty_view.add(viewHolderMyPost.view_empty_2);
        list_empty_view.add(viewHolderMyPost.view_empty_3);
        list_empty_view.add(viewHolderMyPost.view_empty_4);
        viewHolderMyPost.view_empty_1.setVisibility(View.GONE);
        viewHolderMyPost.view_empty_2.setVisibility(View.GONE);
        viewHolderMyPost.view_empty_3.setVisibility(View.GONE);
        viewHolderMyPost.view_empty_4.setVisibility(View.GONE);
        if(viewHolderMyPost.iv_detail_order.getVisibility() != View.VISIBLE){
            list_empty_view.get(num_of_hide_button).setVisibility(View.VISIBLE);
            num_of_hide_button += 1;
        }
        if(viewHolderMyPost.id_chat.getVisibility() != View.VISIBLE){
            list_empty_view.get(num_of_hide_button).setVisibility(View.VISIBLE);
            num_of_hide_button += 1;
        }
        if(viewHolderMyPost.iv_dispute.getVisibility() != View.VISIBLE){
            list_empty_view.get(num_of_hide_button).setVisibility(View.VISIBLE);
            num_of_hide_button += 1;
        }
        if(viewHolderMyPost.fr_confirm_receipt.getVisibility() != View.VISIBLE){
            list_empty_view.get(num_of_hide_button).setVisibility(View.VISIBLE);
            num_of_hide_button += 1;
        }
        return convertView;
    }

    /**
     * Delete product
     *
     * @params: id product
     */

    public void DeleteProduct(final int id) {
        final ProgressDialog progressDialog = Utils.createProgressDialog(context);
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        Call<ResponseMessage> callDeleteProduct = ws.actionDeleteProduct(id);
        callDeleteProduct.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {
                    if (response.body() != null && response.body().getData().equals("Deleted")) {
                        for (int i = 0; i < products.size(); i++) {
                            if (products.get(i).getId() == id) {
                                products.remove(i);
                                notifyDataSetChanged();
                            }
                        }
//                        Toast.makeText(getContext(), "删除完成", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Check state offer
     *
     * @params: product
     */

    public boolean CheckState(RsProduct product) {
        try {
            for (ProductTenders productTender : product.getProduct_tenders()) {
                if (productTender.isClosed()) {
                    for (ProductOffer offer : productTender.getProduct_offers()) {
                        if (offer.getState().equals(STATE_OFFER_ACCPTED)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get list country
     */

    public String GetListCountry(RsProduct product) {
        String countries = "";
        if (product.getCountries() != null && product.getCountries().size() > 0) {
            for (int i = 0; i < product.getCountries().size(); i++) {
                countries += product.getCountries().get(i).getName();
                if (i < product.getCountries().size() - 1) {
                    countries += ", ";
                }
            }
        }
        return countries;
    }

    /**
     * Repost product
     *
     * @params: id product
     */

    public void RepostProduct(int id) {
        final ProgressDialog progressDialog = Utils.createProgressDialog(context);
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        Call<ResponseMessage<ProductListing>> callRepostProduct = ws.actionRepostListing(id);
        callRepostProduct.enqueue(new Callback<ResponseMessage<ProductListing>>() {
            @Override
            public void onResponse(Call<ResponseMessage<ProductListing>> call, Response<ResponseMessage<ProductListing>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        if (f instanceof MyPostFragment) {
                            MyPostFragment myPostFragment = (MyPostFragment) f;
                            myPostFragment.GetDataMyPost(1);
                        }
//                        Toast.makeText(getContext(), "转贴完成", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<ProductListing>> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class ViewHolderMyPost {
    public ImageView iv_delete, iv_edit, iv_report, iv_repost, iv_product_img;
    public TextView tv_name, tv_category, tv_country, tv_ad_limit, bid, txt_order_status;
    public LinearLayout id_order_status, id_delivery_status, id_receive_status, iv_dispute, iv_detail_order, id_buyer_accept;
    public TextView id_chat, fr_confirm_receipt;
    public View view_empty_1,view_empty_2,view_empty_3, view_empty_4;
}
