package com.wo1haitao.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.api.response.WishlistItemRs;
import com.wo1haitao.models.ProductModel;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG_PRODUCT;

/**
 * Created by goodproductssoft on 4/20/17.
 */

public class ProductsGenFavAdapter extends ArrayAdapter<WishlistItemRs> {
    Boolean sortView;
    int layout_id;
    Activity context;
    ImageView iv_product, iv_favories, iv_duplicate;
    TextView tv_name, tv_category, tv_country, tv_bid, expectedArrivalDays;
    ArrayList<WishlistItemRs> productModels;
    LinearLayout ln_bid;

    public ProductsGenFavAdapter(Activity context, ArrayList<WishlistItemRs> productModels, Boolean sortView, int layout_id) {
        super(context, 0, productModels);
        this.sortView = sortView;
        this.layout_id = layout_id;
        this.context = context;
        this.productModels = productModels;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final WishlistItemRs productModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout_id, parent, false);
        }
        expectedArrivalDays = (TextView)convertView.findViewById(R.id.expected_arrival_days);
        iv_product = (ImageView) convertView.findViewById(R.id.iv_product);
        tv_name = (TextView) convertView.findViewById(R.id.tv_name_product);
        tv_category = (TextView) convertView.findViewById(R.id.category);
        tv_country = (TextView) convertView.findViewById(R.id.country);
        tv_bid = (TextView) convertView.findViewById(R.id.bid);
        ln_bid = (LinearLayout) convertView.findViewById(R.id.ln_bid);
        iv_favories = (ImageView) convertView.findViewById(R.id.iv_favories);
        iv_duplicate = (ImageView) convertView.findViewById(R.id.iv_duplicate);
        if(productModel.getProduct_listing() != null) {
            tv_name.setText(productModel.getProduct_listing().getName());
        }
        iv_duplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof MainActivity){
                    MainActivity mainActivity = (MainActivity) context;
                    RsProduct product = productModel.getProduct_listing();
                    product.setProduct_images(productModel.getProduct_images());
                    mainActivity.changeDupRequestPurchase(product,"duplicate");
                }
            }
        });
        String category_text = "";
        if(productModel.getProduct_listing() != null) {
            HashMap<String, String> hashmapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
            int idCategory = productModel.getProduct_listing().getCategory_id();
            for (Map.Entry entry : hashmapCategory.entrySet()) {
                if(String.valueOf(idCategory).equals(entry.getKey())){
                    category_text = hashmapCategory.get(entry.getKey());
                    break;
                }
            }
        }
        tv_category.setText(context.getString(R.string.category_value, category_text));
        try {
            String countries = "";
            if(productModel.getProduct_listing().isNon_restricted_country() == true){
                countries = getContext().getString(R.string.txt_no_limited_country);
            }
            else {
                int coutCountry = productModel.getCountries().size();
                if (coutCountry > 0) {
                    for (int i = 0; i < coutCountry; i++) {
                        countries += productModel.getCountries().get(i).getName();
                        if (i < coutCountry - 1)
                            countries += ", ";
                    }
                }
            }
            tv_country.setText(context.getString(R.string.country_value, countries));
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            int numOfOffer = (int) productModel.getProduct_listing().getNum_of_offers();
            if(numOfOffer > 0){
                ln_bid.setVisibility(View.VISIBLE);
                tv_bid.setText(context.getString(R.string.is_bid, String.valueOf(numOfOffer)));
            }
            else {
                ln_bid.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(productModel.getProduct_images() != null && productModel.getProduct_images().size() != 0){
            String urlImage = ApiServices.BASE_URI + productModel.getProduct_images().get(0).product_image.thumb.getUrl();
            if (urlImage != null && !urlImage.equals("")) {
                ImageLoader il = ImageLoader.getInstance();
                il.displayImage(urlImage, iv_product, OPTION_DISPLAY_IMG_PRODUCT);
            }
        }

        try {
            String date_limit = "";
            Date date =  Calendar.getInstance().getTime();
            Date expert = productModel.getProduct_listing().getExpired_date_date();
            int day = 0;
            if(expert!=null){
                long restDatesinMillis = expert.getTime()-date.getTime();
                day = (int) (restDatesinMillis/(24*60*60*1000) + 1);
                date_limit = CustomApp.getInstance().getString(R.string.ad_limmit,day);
            }
            if (day <= 0) {
                String strDateExpire = "已过期";
                expectedArrivalDays.setText(strDateExpire);
            }
            else {
                expectedArrivalDays.setText(date_limit);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(productModel.getProduct_listing() != null){
            if (productModel.getProduct_listing().is_favourite()) {
                iv_favories.setImageDrawable(null);
                iv_favories.setBackgroundResource(R.drawable.ic_fav_yes);
            } else {
                iv_favories.setImageDrawable(null);
                iv_favories.setBackgroundResource(R.drawable.favories_icon);
            }
        }
        iv_favories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final ProgressDialog progressDialog = Utils.createProgressDialog(context);
                final ApiServices api = ApiServices.getInstance();
                WebService ws = api.getRetrofit().create(WebService.class);
                Call<ResponseMessage<String>> call = ws.actionGetFouvoriteProduct(productModel.getProduct_listing().getId());
                call.enqueue(new Callback<ResponseMessage<String>>() {
                    @Override
                    public void onResponse(Call<ResponseMessage<String>> call, Response<ResponseMessage<String>> response) {
                        try {
                            if (response.body() != null) {
                                ResponseMessage responseMessage = response.body();
                                if (responseMessage.isSuccess()) {
//                                    String isFavourite = (String) responseMessage.getData();
//                                    if (isFavourite.equals("created")) {
//                                        iv_favories.setImageDrawable(null);
//                                        iv_favories.setBackgroundResource(R.drawable.ic_fav_yes);
//                                    } else if (isFavourite.equals("destroyed")) {
//                                        iv_favories.setImageDrawable(null);
//                                        iv_favories.setBackgroundResource(R.drawable.favories_icon);
//                                    }
                                    productModels.remove(position);
                                    notifyDataSetChanged();
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

        return convertView;
    }
}
