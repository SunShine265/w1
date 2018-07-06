package com.wo1haitao.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.controls.RoundedImageView;
import com.wo1haitao.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG;
import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG_PRODUCT;

/**
 * Created by goodproductssoft on 4/20/17.
 */

public class ProductListAdapter extends BaseAdapter<RsProduct> {
    Boolean sortView;
    int layout_id;
    Activity context;
    ArrayList<RsProduct> products;
    boolean isListView;

    public ProductListAdapter(Activity context, ArrayList<RsProduct> productModels, Boolean sortView, int layout_id, boolean isListView) {
        super(context, 0, productModels);
        this.sortView = sortView;
        this.layout_id = layout_id;
        this.context = context;
        products = productModels;
        this.isListView = isListView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final RsProduct product = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view


        final HolderViewProductList holderView;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(layout_id, parent, false);
            holderView = new HolderViewProductList();

            holderView.name = (TextView) convertView.findViewById(R.id.tv_name_product);
            //ImageView picture = (ImageView) convertView.findViewById(R.id.picture);
//        TextView category = (TextView) convertView.findViewById(R.id.category);
//        TextView country = (TextView) convertView.findViewById(R.id.country);
            holderView.id_name = (TextView) convertView.findViewById(R.id.nickname);
            holderView.bid = (TextView) convertView.findViewById(R.id.bid);
            holderView.picture = (ImageView) convertView.findViewById(R.id.picture);
            holderView.favories = (ImageView) convertView.findViewById(R.id.iv_favories);
            holderView.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            holderView.riv_avatar = (ImageView) convertView.findViewById(R.id.riv_avatar);
            holderView.tv_info_product = (TextView) convertView.findViewById(R.id.tv_info_product);
            holderView.ll_product_name = (LinearLayout) convertView.findViewById(R.id.ll_product_name);
            convertView.setTag(holderView);
        }
        else{
            holderView = (HolderViewProductList) convertView.getTag();
            if(holderView == null || holderView.bid == null){
                return convertView;
            }
        }

        // Lookup view for data population

        View line_vertical_favorite = (View) convertView.findViewById(R.id.line_vertical_favorite);

        holderView.name.setText(product.getName());

        try{
            imageLoader.displayImage(ApiServices.BASE_URI + product.getProduct_images().get(0).getProduct_image().getUrl(), holderView.picture, OPTION_DISPLAY_IMG_PRODUCT);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            imageLoader.displayImage(product.getCommon_user().getProfile_picture().getUrl(), holderView.riv_avatar, OPTION_DISPLAY_IMG);
        } catch (Exception e){
            e.printStackTrace();
        }
        //Log.i("TAG"," "+product.getCategory_id());
//        ArrayList<String> myListCategory = new ArrayList<>();
//        HashMap<String, String> hashmapCategory = MyPreferences.getDataServer("category");
//        for(Map.Entry entry : hashmapCategory.entrySet()){
//            myListCategory.add(entry.getValue().toString());
//        }
//        String category_text = context.getString(R.string.category_value, Utils.getStringFromListCategory(context, product.getCategory_id()));
//        category.setText(category_text);
//        String country_product = "";
//        if (product.isNon_restricted_country()) {
//            country_product = context.getString(R.string.txt_all_country);
//        } else {
//            if (product.getCountries() != null) {
//                for (RsCountry country1 : product.getCountries()) {
//                    if (country_product.isEmpty()) {
//                        country_product += country1.getName();
//                    } else {
//                        country_product += ", " + country1.getName();
//                    }
//                }
//            }
//        }
//        country_product = context.getString(R.string.country_value, country_product);
//        country.setText(country_product);

//        bid.setText("");
//        if(true){
//            bid.setVisibility(View.GONE);
//        }
        try {
            if (product.getCommon_user().getNickname() != null) {
                String strName = product.getCommon_user().getNickname();
                holderView.id_name.setText(strName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            float ratingStar= (float)(product.getCommon_user().getAverage_rating());
            int intpart = (int)ratingStar;
            float decpart = ratingStar - intpart;
            if(decpart != 0.0f)
            {
                ratingStar = intpart + 0.5f;
            }
            holderView.ratingBar.setRating(ratingStar);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (product.is_favourite()) {
            holderView.favories.setImageDrawable(null);
            holderView.favories.setBackgroundResource(R.drawable.ic_fav_yes);
        } else {
            holderView.favories.setImageDrawable(null);
            holderView.favories.setBackgroundResource(R.drawable.favories_icon);
        }

        //String str_num_of_offers ="";
        if(isListView == true) {
            String str_num_of_offers = CustomApp.getInstance().getString(R.string.num_of_offers_product_list, product.getNum_of_offers());
            holderView.bid.setVisibility(View.VISIBLE);
            holderView.bid.setText(str_num_of_offers);
            holderView.bid.setTextColor(ProductListAdapter.this.getContext().getResources().getColor(R.color.white));
        }
        else {
            String str_num_of_offers = String.valueOf(product.getNum_of_offers());
            holderView.bid.setVisibility(View.VISIBLE);
            holderView.bid.setText(str_num_of_offers);
            holderView.bid.setTextColor(ProductListAdapter.this.getContext().getResources().getColor(R.color.black));

        }
        if(product.getNum_of_offers() == 0){
            holderView.bid.setVisibility(View.GONE);
        }
        else {
            holderView.bid.setVisibility(View.VISIBLE);
        }


        holderView.favories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final ProgressDialog progressDialog = Utils.createProgressDialog(context);
                final ApiServices api = ApiServices.getInstance();
                WebService ws = api.getRetrofit().create(WebService.class);
                Call<ResponseMessage<String>> call = ws.actionGetFouvoriteProduct(product.getId());
                call.enqueue(new Callback<ResponseMessage<String>>() {
                    @Override
                    public void onResponse(Call<ResponseMessage<String>> call, Response<ResponseMessage<String>> response) {
                        try {
                            if (response.body() != null) {
                                ResponseMessage responseMessage = response.body();
                                if (responseMessage.isSuccess()) {
                                    String isFavourite = (String) responseMessage.getData();
                                    if (isFavourite.equals("created")) {
                                        holderView.favories.setImageDrawable(null);
                                        holderView.favories.setBackgroundResource(R.drawable.ic_fav_yes);
                                    } else if (isFavourite.equals("destroyed")) {
                                        holderView.favories.setImageDrawable(null);
                                        holderView.favories.setBackgroundResource(R.drawable.favories_icon);
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

        return convertView;
    }

}

class HolderViewProductList {
    public TextView name, id_name, bid, tv_info_product;
    public ImageView picture, favories, riv_avatar;
    public  RatingBar ratingBar;
    public LinearLayout ll_product_name;
}

