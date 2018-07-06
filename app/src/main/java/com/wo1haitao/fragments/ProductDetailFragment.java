package com.wo1haitao.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.activities.DialogActivityBidShop;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.adapters.ProductDetailAdapter;
import com.wo1haitao.adapters.ProductImagePagerAdapter;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.CommentItemView;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.controls.JustifyTextView;
import com.wo1haitao.controls.RoundedImageView;
import com.wo1haitao.dialogs.DialogBidShop;
import com.wo1haitao.dialogs.DialogFeedback;
import com.wo1haitao.dialogs.DialogRebid;
import com.wo1haitao.dialogs.PopupMenuCustom;
import com.wo1haitao.models.ProductOffer;
import com.wo1haitao.models.ProductShow;
import com.wo1haitao.models.ProductTenders;
import com.wo1haitao.models.QuestionReplyModel;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG;
import static com.wo1haitao.utils.Utils.crashLog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    ActionBarProject my_action_bar;
    ProductImagePagerAdapter product_img_adapter;
    LinearLayout ll_dots, ln_country;
    ImageView[] dots;
    ViewPager vp_products;
    DialogBidShop dialog_bidshop;
    DialogRebid dialog_rebid;
    ImageView iv_feedback, iv_favories, group_product, iv_duplicate, id_verifies, verifies_user, imageView6;

    TextView tv_name_us, tv_ad_limit, tv_name_pd, tv_num_of_review, id_category, id_country, ship_method;
    long product_id, tenderID = 0;
    List<Long> offer_id, tender_id, product_tender_shipping_id;
    int count_img = 0;
    RoundedImageView iv_user;
    JustifyTextView tv_description;
    String flag = "", couterOfferPrice = "", offerPrice = "", remarkCounterOffer = "";
    Boolean isReoffer = false, isOffered = false;
    RatingBar ratingBar;
    boolean[] checkCountry;
    ArrayList<String> listCountry;
    RsProduct myProduct;
    ListView myListviewProductsDetail;
    View fragment_view, headerView;
    RsProduct rsProduct;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout ll_layout_content;

    public ActionListener getActionListener() {
        if (this.getActivity() instanceof ActionListener) {
            return (ActionListener) this.getActivity();
        }
        return null;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public ProductDetailFragment() {
        product_id = -1;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment_view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        headerView = ((LayoutInflater) ProductDetailFragment.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header_products_detail, null, false);
        myListviewProductsDetail = (ListView) fragment_view.findViewById(R.id.lv_product_detail);
        myListviewProductsDetail.addHeaderView(headerView, null, true);
        myListviewProductsDetail.setItemsCanFocus(true);
        group_product = (ImageView) fragment_view.findViewById(R.id.group_product);
        swipeRefreshLayout = (SwipeRefreshLayout) fragment_view.findViewById(R.id.swipe_refresh_layout);
        imageView6 = (ImageView) fragment_view.findViewById(R.id.imageView6);

        InputMethodManager imm = (InputMethodManager) ProductDetailFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(fragment_view.getWindowToken(), 0);
        myListviewProductsDetail.setItemsCanFocus(true);
        initControl();
        setView(product_id);
        UpdateCountry();
        swipeRefreshLayout.setOnRefreshListener(ProductDetailFragment.this);
        fragment_view.post(new Runnable() {
            @Override
            public void run() {
                int widthViewPaper = fragment_view.getMeasuredWidth();
                int heightViewPaper = fragment_view.getMeasuredHeight();
                int min = Math.min(widthViewPaper, heightViewPaper);
                vp_products.getLayoutParams().height = min;
                vp_products.getLayoutParams().width = min;
                imageView6.getLayoutParams().height = min;
                imageView6.getLayoutParams().width = min;
            }
        });
        return fragment_view;
    }

    @Override
    public boolean onBackPressed() {
        if (getActionListener() != null && "RequestPurchase".equals(ProductDetailFragment.this.getFlag())) {
            getActionListener().changeMyPost(ProductDetailFragment.this.getFlag());
            return true;
        } else {
            return super.onBackPressed();
        }
    }

    private void initControl() {
        offer_id = new ArrayList<>();
        tender_id = new ArrayList<>();
        product_tender_shipping_id = new ArrayList<>();
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        my_action_bar.showLogo();
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        my_action_bar.HideProductTitleHeader();
        vp_products = (ViewPager) headerView.findViewById(R.id.vp_products);

        vp_products.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position, count_img);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ll_dots = (LinearLayout) headerView.findViewById(R.id.ll_dots);
        iv_feedback = (ImageView) headerView.findViewById(R.id.iv_feedback);

        iv_duplicate = (ImageView) headerView.findViewById(R.id.iv_duplicate);
        iv_duplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myProduct != null && myProduct.getId() > 0) {
                    getActionListener().changeDupRequestPurchase(myProduct, "duplicate");
                }

            }
        });
        ll_layout_content = (LinearLayout) headerView.findViewById(R.id.ll_layout_content);
        ship_method = (TextView) headerView.findViewById(R.id.ship_method);
        id_verifies = (ImageView) headerView.findViewById(R.id.id_verifies);
        verifies_user = (ImageView) headerView.findViewById(R.id.verifies_user);
        id_category = (TextView) headerView.findViewById(R.id.id_category);
        id_country = (TextView) headerView.findViewById(R.id.id_country);
        tv_name_us = (TextView) headerView.findViewById(R.id.tv_name_us);
        tv_ad_limit = (TextView) headerView.findViewById(R.id.tv_ad_limit);
        tv_name_pd = (TextView) headerView.findViewById(R.id.tv_name_pd);
        iv_favories = (ImageView) headerView.findViewById(R.id.iv_favories);
        iv_user = (RoundedImageView) headerView.findViewById(R.id.iv_user);
        tv_num_of_review = (TextView) headerView.findViewById(R.id.tv_num_of_review);
        tv_description = (JustifyTextView) headerView.findViewById(R.id.tv_description);
        ratingBar = (RatingBar) headerView.findViewById(R.id.ratingBar);
        ln_country = (LinearLayout) headerView.findViewById(R.id.ln_country);
        listCountry = new ArrayList<>();

    }

    private void addBottomDots(int position, int length) {

        dots = new ImageView[length];
        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getActivity());
            ViewGroup.LayoutParams lo_param = new ViewGroup.LayoutParams(25, 25);
            dots[i].setLayoutParams(lo_param);
            dots[i].setPadding(5, 5, 5, 5);
            dots[i].setImageResource(R.drawable.dot_no_select);
            ll_dots.addView(dots[i]);
        }
        if (dots.length > 0) {
            LinearLayout.LayoutParams lo_param = new LinearLayout.LayoutParams(35, 35);
            dots[position].setLayoutParams(lo_param);
            dots[position].setImageResource(R.drawable.dot_selected);
        }
    }

    public void setView(final long product_id) {
        couterOfferPrice = "";
        try{
            View view = this.getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        catch (Exception e){
            crashLog(e);
        }

        //Get data detail product
        swipeRefreshLayout.setRefreshing(true);
        ApiServices api = ApiServices.getInstance();
        final WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<RsProduct>> call = ws.actionGetProductById(product_id);
        call.enqueue(new Callback<ResponseMessage<RsProduct>>() {
            @Override
            public void onResponse(Call<ResponseMessage<RsProduct>> call, Response<ResponseMessage<RsProduct>> response) {
                try {
                    if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body() != null && response.body().isSuccess()) {
                        final ProductShow product = new ProductShow(response.body().getData());
                        rsProduct = response.body().getData();
                        myProduct = rsProduct;

                        my_action_bar.ShowProductTitleHeader();
                        iv_feedback.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CheckReport(rsProduct.getId());
                            }
                        });

                        //user reviews
                        iv_user.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MainActivity mainActivity = (MainActivity) ProductDetailFragment.this.getActivity();
                                UserReviews fragment = new UserReviews();
                                fragment.setUserProfile(product.getCommon_user());
                                mainActivity.changeFragment(fragment, true);
                            }
                        });
                        long idUser = MyPreferences.getID();
                        ArrayList<Object> arrayItems = new ArrayList<>();
                        if (rsProduct.getQuestion_answers() != null && rsProduct.getQuestion_answers().size() > 0) {
                            arrayItems.add("titleQuestionAnswer");
                        }

                        if (rsProduct.getQuestion_answers() != null) {
                            for (int i = 0; i < rsProduct.getQuestion_answers().size(); i++) {
                                CommentItemView viewComment = new CommentItemView(rsProduct.getQuestion_answers().get(i).getId(), rsProduct.getQuestion_answers().get(i).getCommon_user(), rsProduct.getQuestion_answers().get(i).getContent(), rsProduct.getQuestion_answers().get(i).getCreated_at());
                                viewComment.setType_of_view(CommentItemView.TYPE_COMMENT);
                                arrayItems.add(viewComment);
                                if (rsProduct.getQuestion_answers().get(i).getQuestion_answer_replies() == null || rsProduct.getQuestion_answers().get(i).getQuestion_answer_replies().size() == 0) {
                                    viewComment.setIs_reply(true);
                                } else {
                                    viewComment.setIs_reply(false);
                                    for (int j = 0; j < rsProduct.getQuestion_answers().get(i).getQuestion_answer_replies().size(); j++) {
                                        QuestionReplyModel questionReplyModel = rsProduct.getQuestion_answers().get(i).getQuestion_answer_replies().get(j);
                                        CommentItemView viewReComment = new CommentItemView(rsProduct.getQuestion_answers().get(i).getId(), questionReplyModel.getCommon_user(), questionReplyModel.getContent(), questionReplyModel.getCreated_at());
                                        viewReComment.setType_of_view(CommentItemView.TYPE_RECOMMENT);
                                        viewReComment.setIs_reply(false);
                                        if (j == rsProduct.getQuestion_answers().get(i).getQuestion_answer_replies().size() - 1) {
                                            viewReComment.setIs_reply(true);
                                        }
                                        arrayItems.add(viewReComment);
                                    }
                                }

                            }
                        }

                        if (idUser != rsProduct.getCommon_user().getId()) {
                            CommentItemView viewBox = new CommentItemView();
                            viewBox.setType_of_view(CommentItemView.TYPE_BOX);
                            arrayItems.add(viewBox);
                        }
                        String state = "none";
                        for (int i = 0; i < rsProduct.getProduct_tenders().size(); i++) {
                            for (ProductOffer offer : rsProduct.getProduct_tenders().get(i).getProduct_offers()) {
                                if (offer.getState().equals("accepted")) {
                                    state = "accept";
                                    break;
                                }
                            }
                            if (state.equals("accept")) {
                                break;
                            }
                        }
                        if (rsProduct.getProduct_tenders() != null && rsProduct.getProduct_tenders().size() > 0) {
                            String numTender = String.valueOf(rsProduct.getProduct_tenders().size());
                            arrayItems.add(numTender);
                        }
                        if (rsProduct.getProduct_tenders() != null) {
                            ArrayList<ProductTenders> tempElements = new ArrayList<ProductTenders>(rsProduct.getProduct_tenders());
                            SortProductTender(tempElements);
                            arrayItems.addAll(tempElements);
                        }
                        ProductDetailAdapter productsDetailAdapter = new ProductDetailAdapter(ProductDetailFragment.this.getActivity(), arrayItems, rsProduct, state, ProductDetailFragment.this);
                        myListviewProductsDetail.setAdapter(productsDetailAdapter);
                        productsDetailAdapter.notifyDataSetChanged();
                        try {
                            if (!rsProduct.getOrders().isEmpty()) {
                                if (rsProduct.getOrders().get(0).getOrder_invoice().getUrl() != null) {
                                    my_action_bar.HideProductTitleHeader();
                                }
                            } else {
                                my_action_bar.ShowProductTitleHeader();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (rsProduct.getCommon_user() != null) {
                            if (idUser == rsProduct.getCommon_user().getId()) {
                                if (rsProduct.getNum_of_offers() > 0) {
                                    my_action_bar.setTextSaler(false, null);
                                } else {
                                    my_action_bar.setTextSaler(true, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getActionListener().changeRequestPurchase(myProduct, "edit");
                                        }
                                    });
                                }
                            } else {
                                my_action_bar.ShowProductTitleHeader();
                                iv_feedback.setVisibility(View.VISIBLE);
                                boolean isCheckOrder = false;
                                for (int i = 0; i < rsProduct.getProduct_tenders().size(); i++) {
                                    if (rsProduct.getProduct_tenders().get(i).isClosed()) {
                                        for (ProductOffer offer : rsProduct.getProduct_tenders().get(i).getProduct_offers()) {
                                            if (offer.getState().equals("accepted")) {
                                                isCheckOrder = true;
                                            }
                                        }
                                    }
                                }
                                if (isCheckOrder && rsProduct.getOrders() != null && rsProduct.getOrders().size() > 0) {
                                    my_action_bar.HideProductTitleHeader();
                                } else {
                                    boolean closeToTender = true;
                                    for (int i = 0; i < rsProduct.getProduct_tenders().size(); i++) {
                                        if (rsProduct.getProduct_tenders() != null) {
                                            if (idUser == rsProduct.getProduct_tenders().get(i).getCommon_user().getId()) {
                                                if (!rsProduct.getProduct_tenders().get(i).isClosed()) {
                                                    closeToTender = false;
                                                    if (rsProduct.getProduct_tenders().get(i).getProduct_offers().size() == 1) {
                                                        my_action_bar.HideProductTitleHeader();

                                                        if (rsProduct.getProduct_tenders().get(i).getProduct_offers().get(0).getProduct_counter_offer() != null) {
                                                            my_action_bar.changeProductTextReoffer();
                                                            float value_counter = Float.parseFloat(rsProduct.getProduct_tenders().get(i).getProduct_offers().get(0).getProduct_counter_offer().getCounter_offer_price().getFractional()) / 100;
                                                            couterOfferPrice = String.format("%.2f", value_counter);
                                                            remarkCounterOffer = rsProduct.getProduct_tenders().get(i).getProduct_offers().get(0).getProduct_counter_offer().getRemarks().toString();
                                                            offerPrice = String.valueOf(Float.parseFloat(rsProduct.getProduct_tenders().get(i).getProduct_offers().get(0).getOffer_price().getFractional()) / 100);
                                                            tenderID = rsProduct.getProduct_tenders().get(i).getId();
                                                        }
                                                    }
                                                    if (rsProduct.getProduct_tenders().get(i).getProduct_offers().size() > 1) {
                                                        if (rsProduct.getProduct_tenders().get(i).getProduct_offers().get(1) != null) {
                                                            my_action_bar.HideProductTitleHeader();
                                                        }
                                                    }
                                                    break;
                                                } else {
                                                    my_action_bar.HideProductTitleHeader();
                                                }
                                            }
                                        }
                                    }
                                    if (closeToTender == true) {
                                        my_action_bar.ShowProductTitleHeader();
                                    }
                                }

                                my_action_bar.showLLProduct_text(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (couterOfferPrice.equals("") == false) {
                                            dialog_rebid = new DialogRebid(getActivity(),
                                                    product_id,
                                                    tenderID,
                                                    isReoffer,
                                                    couterOfferPrice,
                                                    isOffered, offerPrice,
                                                    remarkCounterOffer);
                                            dialog_rebid.f = ProductDetailFragment.this;
                                            if (myProduct != null) {
                                                dialog_rebid.setMyProduct(myProduct);
                                            }
                                            dialog_rebid.setCanceledOnTouchOutside(true);
                                            dialog_rebid.show();


                                            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                                            layoutParams.copyFrom(dialog_rebid.getWindow().getAttributes());
                                            int dialogWindowWidth = WindowManager.LayoutParams.MATCH_PARENT;
                                            int dialogWindowHeight = WindowManager.LayoutParams.WRAP_CONTENT;
                                            layoutParams.width = dialogWindowWidth;
                                            layoutParams.height = dialogWindowHeight;
                                            dialog_rebid.getWindow().setAttributes(layoutParams);
                                        } else {
                                            dialog_bidshop = new DialogBidShop(getActivity(),
                                                    product_id,
                                                    tenderID,
                                                    isReoffer,
                                                    couterOfferPrice,
                                                    isOffered);
                                            dialog_bidshop.f = ProductDetailFragment.this;
                                            if (myProduct != null) {
                                                dialog_bidshop.setMyProduct(myProduct);
                                            }
                                            dialog_bidshop.setCanceledOnTouchOutside(true);

                                            dialog_bidshop.show();


                                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                            lp.copyFrom(dialog_bidshop.getWindow().getAttributes());
                                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                            dialog_bidshop.getWindow().setAttributes(lp);
                                           // dialog_bidshop.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


//                                            Intent it = new Intent(getActivity(),DialogActivityBidShop.class);
//                                            getActivity().startActivity(it);



                                        }
                                    }
                                });
                            }
                        }
                        try {
                            String categoryName = rsProduct.getCategory().getCategoryName();
                            SetIconCategory(categoryName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ArrayList<String> myListCategory = new ArrayList<>();
                        HashMap<String, String> hashmapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
                        if (hashmapCategory != null) {
                            for (Map.Entry entry : hashmapCategory.entrySet()) {
                                myListCategory.add(entry.getValue().toString());
                            }
                        }
                        String strCategory = hashmapCategory.get(String.valueOf(rsProduct.getCategory_id()));
                        id_category.setText(strCategory);
                        if (rsProduct.isNon_restricted_country()) {
                            id_country.setText("不限国家");
                        } else {
                            if (rsProduct.getCountries() != null) {
                                String strCountry = "";
                                for (int i = 0; i < rsProduct.getCountries().size(); i++) {
                                    String country = rsProduct.getCountries().get(i).getName();
                                    if (i < rsProduct.getCountries().size() - 1) {
                                        country += ", ";
                                    }
                                    strCountry += country;
                                }
                                id_country.setText(strCountry);
                            }
                        }

                        tv_name_us.setText(product.getCommon_user().getNickname());
                        if (product.getDay() <= 0) {
                            String date_limit = CustomApp.getInstance().getString(R.string.ad_limmit, 0);
                            tv_ad_limit.setText(product.getDate_limit());
                        } else {
                            tv_ad_limit.setText(product.getDate_limit());
                        }
                        count_img = product.getList_image().size();
                        product_img_adapter = new ProductImagePagerAdapter(getActivity(), product.getList_image());

                        vp_products.setAdapter(product_img_adapter);
                        tv_name_pd.setText(product.getName());
                        if (product.getCommon_user().getProfile_picture().getUrl() != null) {
                            String url_avt = product.getCommon_user().getProfile_picture().getUrl();
                            if (url_avt != null && !url_avt.equals("")) {
                                ImageLoader il = ImageLoader.getInstance();
                                il.displayImage(url_avt, iv_user, OPTION_DISPLAY_IMG);
                            }
                        }

                        if (rsProduct.getCommon_user().getSetup_account() != null) {
                            if (rsProduct.getCommon_user().getVerification_state().equals("closed")) {
                                id_verifies.setBackgroundResource(R.drawable.verified);
                                verifies_user.setBackgroundResource(R.drawable.green_icon);
                            } else {
                                id_verifies.setBackgroundResource(R.drawable.unverifyimg);
                                verifies_user.setBackgroundResource(R.drawable.yellow_icon);
                            }
                        }
                        try {
                            float ratingStar = (float) (product.getCommon_user().getAverage_rating());
                            int intpart = (int) ratingStar;
                            float decpart = ratingStar - intpart;
                            if (decpart != 0.0f) {
                                ratingStar = intpart + 0.5f;
                            }
                            ratingBar.setRating(ratingStar);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            tv_num_of_review.setText("(" + product.getCommon_user().getNum_of_reviews().toString() + ")");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (product.getDescription() != null) {
                            tv_description.setText(product.getDescription());
                        }
                        if (product.is_favourite()) {
                            iv_favories.setImageDrawable(null);
                            iv_favories.setBackgroundResource(R.drawable.ic_fav_yes);
                        } else {
                            iv_favories.setImageDrawable(null);
                            iv_favories.setBackgroundResource(R.drawable.favories_icon);
                        }

                        iv_favories.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                final ProgressDialog progressDialog = Utils.createProgressDialog(ProductDetailFragment.this.getActivity());
                                final ApiServices api = ApiServices.getInstance();
                                WebService ws = api.getRetrofit().create(WebService.class);
                                Call<ResponseMessage<String>> call = ws.actionGetFouvoriteProduct(rsProduct.getId());
                                call.enqueue(new Callback<ResponseMessage<String>>() {
                                    @Override
                                    public void onResponse(Call<ResponseMessage<String>> call, Response<ResponseMessage<String>> response) {
                                        try {
                                            if (response.body() != null) {
                                                ResponseMessage responseMessage = response.body();
                                                if (responseMessage.isSuccess()) {
                                                    String isFavourite = (String) responseMessage.getData();
                                                    if (isFavourite.equals("created")) {
                                                        iv_favories.setBackgroundResource(R.drawable.ic_fav_yes);
                                                    } else if (isFavourite.equals("destroyed")) {
                                                        iv_favories.setBackgroundResource(R.drawable.favories_icon);
                                                    }
                                                    progressDialog.dismiss();
                                                }
                                            } else if (response.errorBody() != null) {
                                                try {
                                                    ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                                                    ErrorMessage error = responseMessage.getErrors();
                                                    Toast.makeText(view.getContext(), "Can't post data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                                                } catch (IOException e) {
                                                    Toast.makeText(view.getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                                }
                                                if (progressDialog != null) {
                                                    progressDialog.dismiss();
                                                }
                                            } else {
                                                if (progressDialog != null) {
                                                    progressDialog.dismiss();
                                                }
                                                Toast.makeText(view.getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            Utils.crashLog(e);
                                            if (progressDialog != null) {
                                                progressDialog.dismiss();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseMessage<String>> call, Throwable t) {
                                        if (progressDialog != null) {
                                            progressDialog.dismiss();
                                        }
                                        Utils.OnFailException(t);
                                    }
                                });
                            }
                        });

                        try {
                            String strShippingMethod = "";
                            for (int i = 0; i < rsProduct.getShipping_methods().size(); i++) {
                                strShippingMethod += (i + 1) + ". " + rsProduct.getShipping_methods().get(i).getName() + "    ";
                            }
                            if (!strShippingMethod.equals("")) {
                                ship_method.setText(strShippingMethod);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        addBottomDots(0, count_img);
                    } else {
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(false);
                } catch (Exception e) {
//                    progressDialog.dismiss();
                    Utils.crashLog(e);

                }

            }

            @Override
            public void onFailure(Call<ResponseMessage<RsProduct>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Utils.OnFailException(t);
            }
        });
    }

    public void SetIconCategory(String categoryName) {
        switch (categoryName) {
            case "电器":
                group_product.setImageResource(R.drawable.electronic);
                break;
            case "家具":
                group_product.setImageResource(R.drawable.furniture);
                break;
            case "服装":
                group_product.setImageResource(R.drawable.fashion);
                break;
            case "首饰":
                group_product.setImageResource(R.drawable.jewellery);
                break;
            case "健康食品":
                group_product.setImageResource(R.drawable.helthy_food_product);
                break;
            case "手表":
                group_product.setImageResource(R.drawable.watch);
                break;
            case "收藏品":
                group_product.setImageResource(R.drawable.collectable);
                break;
            case "包包":
                group_product.setImageResource(R.drawable.bags);
                break;
            case "运动用品":
                group_product.setImageResource(R.drawable.worout_equipment);
                break;
            default:
                group_product.setImageResource(R.drawable.others);
                break;
        }
    }

    /**
     * Update country
     *
     * @params:
     * @return:
     */
    public void UpdateCountry() {
        HashMap<String, String> hashmapCountry;
        hashmapCountry = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_COUNTRY);
        checkCountry = new boolean[hashmapCountry.size()];
        if (hashmapCountry != null) {
            int i = 0;
            for (Map.Entry entry : hashmapCountry.entrySet()) {
                listCountry.add(entry.getValue().toString());
                checkCountry[i] = false;
                i++;
            }
        }

        final ArrayList<String> countryChoice = new ArrayList<>();
        ln_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public interface ActionListener {
        void changeRequestPurchase(RsProduct product, String flag);

        void changeMyPost(String flag);

        void changeDupRequestPurchase(RsProduct product, String flag);
    }

    /**
     * Compare 2 item Message follow date
     *
     * @params:
     * @return:
     */
    public static Comparator<ProductTenders> PRODUCTTENDER_SORT = new Comparator<ProductTenders>() {
        public int compare(ProductTenders productTenders1, ProductTenders productTenders2) {
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
    public ArrayList<ProductTenders> SortProductTender(ArrayList<ProductTenders> list) {
        Collections.sort(list, PRODUCTTENDER_SORT);
        return list;
    }

    /**
     * check report
     *
     * @params:
     * @return:
     */
    public void CheckReport(int id) {
        ApiServices services = ApiServices.getInstance();
        WebService api = services.getRetrofit().create(WebService.class);
        Call<ResponseMessage> call;
        call = api.actionGetReport(id);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.body() != null && response.isSuccessful()) {
                    String title, nameBtn, content;
                    title = ProductDetailFragment.this.getActivity().getResources().getString(R.string.title_feed_back);
                    nameBtn = ProductDetailFragment.this.getActivity().getResources().getString(R.string.btn_feed_back);
                    content = "";
                    DialogFeedback dl = new DialogFeedback(getActivity(), rsProduct.getId(), ProductDetailFragment.this, title, nameBtn, content);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dl.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dl.show();
                } else if (response.errorBody() != null) {
                    String title, nameBtn, content;
                    title = ProductDetailFragment.this.getActivity().getResources().getString(R.string.title_alert_feedback);
                    nameBtn = ProductDetailFragment.this.getActivity().getResources().getString(R.string.btn_alert_feedback);
                    content = ProductDetailFragment.this.getActivity().getResources().getString(R.string.message_alert_feedback);
                    DialogFeedback dl = new DialogFeedback(getActivity(), rsProduct.getId(), ProductDetailFragment.this, title, nameBtn, content);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dl.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dl.show();
                } else {
                    Toast.makeText(getActivity(), R.string.no_advesting, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Utils.OnFailException(t);
            }
        });
    }

    @Override
    public void onRefresh() {
        setView(product_id);
    }
}

