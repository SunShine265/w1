package com.wo1haitao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.response.RatingUser;
import com.wo1haitao.api.response.RsReviewToMe;
import com.wo1haitao.utils.MyPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG_PRODUCT;

/**
 * Created by goodproductssoft on 4/20/17.
 */

public class ProductsEvaTab2Adapter extends BaseAdapter<RsReviewToMe> {
    Boolean sortView;
    Context context;
    ArrayList<RsReviewToMe> rsReviewToMe;

    public ProductsEvaTab2Adapter(Context context, ArrayList<RsReviewToMe> rsReviewToMes, Boolean sortView) {
        super(context, 0, rsReviewToMes);
        this.sortView = sortView;
        this.context = context;
        this.rsReviewToMe = rsReviewToMes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RsReviewToMe rsReviewToMe = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_eval_tab_2, parent, false);
        }

        TextView tv_name, tv_country, tv_comment, tv_username, tv_category;
        ImageView img_product;
        RatingBar ratingBar1, ratingBar2, ratingBar3;
        TextView tvRating1, tvRating2, tvRating3;
        tvRating1 = (TextView) convertView.findViewById(R.id.tvRating1);
        tvRating2 = (TextView) convertView.findViewById(R.id.tvRating2);
        tvRating3 = (TextView) convertView.findViewById(R.id.tvRating3);

        tv_category = (TextView) convertView.findViewById(R.id.tv_category);
        tv_username = (TextView) convertView.findViewById(R.id.tv_username);
        tv_name= (TextView) convertView.findViewById(R.id.tv_name);
        tv_country = (TextView) convertView.findViewById(R.id.tv_country);
        img_product = (ImageView) convertView.findViewById(R.id.img_product);
        tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
        LinearLayout ll_ratingBar1, ll_ratingBar2, ll_ratingBar3;
        ll_ratingBar1 = (LinearLayout) convertView.findViewById(R.id.ll_ratingBar1);
        ll_ratingBar2 = (LinearLayout) convertView.findViewById(R.id.ll_ratingBar2);
        ll_ratingBar3 = (LinearLayout) convertView.findViewById(R.id.ll_ratingBar3);
        ratingBar1 = (RatingBar) convertView.findViewById(R.id.ratingBar1);
        ratingBar2 = (RatingBar) convertView.findViewById(R.id.ratingBar2);
        ratingBar3 = (RatingBar) convertView.findViewById(R.id.ratingBar3);

        try{
            imageLoader.displayImage(ApiServices.BASE_URI + rsReviewToMe.getProduct_listing().getProduct_images().get(0).getProduct_image().getUrl(), img_product, OPTION_DISPLAY_IMG_PRODUCT);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            tv_name.setText(rsReviewToMe.getProduct_listing().getName());
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            tv_comment.setVisibility(View.VISIBLE);
            tv_comment.setText(rsReviewToMe.getReview_message());
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            String strCountry = "";
            if(rsReviewToMe.getProduct_listing().isNon_restricted_country() == true){
                strCountry = getContext().getString(R.string.txt_no_limited_country);
            }
            else {
                for (int i = 0; i < rsReviewToMe.getProduct_listing().getCountries().size(); i++) {
                    strCountry = rsReviewToMe.getProduct_listing().getCountries().get(i).getName();
                    if (i < rsReviewToMe.getProduct_listing().getCountries().size() - 1) {
                        strCountry += ",";
                    }
                }
            }
            tv_country.setText(context.getString(R.string.country_value, strCountry));

        }catch (Exception e){
            e.printStackTrace();
        }
        HashMap<String, String> hashmapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
        try
        {
            String strCategory = "";
            strCategory += hashmapCategory.get(String.valueOf(rsReviewToMe.getProduct_listing().getCategory_id()));
            tv_category.setText(context.getString(R.string.category_value, strCategory));
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            float fRatingDescripe = 0, fRatingShipping = 0, fRatingCommunication = 0;
            ArrayList<RatingUser> list_rating = rsReviewToMe.getListRating();
            Map<String, String> rating = rsReviewToMe.getRating();
            if(list_rating.size() == 1){
                String strRatingDescripe = list_rating.get(0).rating;
                fRatingDescripe = Float.parseFloat(strRatingDescripe);
                String title_rating = list_rating.get(0).title;
                int intpart = (int)fRatingDescripe;
                float decpart = fRatingDescripe - intpart;
                if(decpart != 0.0f)
                {
                    fRatingDescripe = intpart + 0.5f;
                }
                ratingBar1.setRating(fRatingDescripe);
                tvRating1.setText(title_rating);
                ll_ratingBar3.setVisibility(View.GONE);
                ll_ratingBar2.setVisibility(View.GONE);
                ll_ratingBar1.setVisibility(View.VISIBLE);
            }
            else if(list_rating.size() == 3){

                String rating_value1 = list_rating.get(0).rating;
                String rating_value2 = list_rating.get(1).rating;
                String rating_value3 = list_rating.get(2).rating;
                String title_rating1 = list_rating.get(0).title;
                String title_rating2 = list_rating.get(1).title;
                String title_rating3 = list_rating.get(2).title;
                float rating1 = Float.parseFloat(rating_value1);
                float rating2 = Float.parseFloat(rating_value2);
                float rating3 = Float.parseFloat(rating_value3);
                int intpart = (int)rating1;
                float decpart = rating1 - intpart;
                if(decpart != 0.0f)
                {
                    rating1 = intpart + 0.5f;
                }
                int intpart2 = (int)rating2;
                float decpart2 = rating2 - intpart2;
                if(decpart2 != 0.0f)
                {
                    rating2 = intpart2 + 0.5f;
                }
                int intpart3 = (int)rating3;
                float decpart3 = rating3 - intpart3;
                if(decpart3 != 0.0f)
                {
                    rating3 = intpart3 + 0.5f;
                }
                ratingBar1.setRating(rating3);
                ratingBar2.setRating(rating2);
                ratingBar3.setRating(rating1);
                tvRating1.setText(title_rating3);
                tvRating2.setText(title_rating2);
                tvRating3.setText(title_rating1);
                ll_ratingBar1.setVisibility(View.VISIBLE);
                ll_ratingBar2.setVisibility(View.VISIBLE);
                ll_ratingBar3.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        try {
            tv_username.setVisibility(View.VISIBLE);
            String strUserReview = CustomApp.getInstance().getString(R.string.user_name_review, rsReviewToMe.getReviewer().getNickname());
            //String reviewerName = rsReviewToMe.getReviewer().getNickname() + "给你的评价";
            tv_username.setText(strUserReview);
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }
}
