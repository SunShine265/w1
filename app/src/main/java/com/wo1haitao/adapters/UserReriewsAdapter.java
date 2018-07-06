package com.wo1haitao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.response.RatingUser;
import com.wo1haitao.api.response.RsReviewToMe;
import com.wo1haitao.utils.MyPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by goodproductssoft on 4/20/17.
 */

public class UserReriewsAdapter extends BaseAdapter<RsReviewToMe> {
    Boolean sortView;
    Context context;

    public UserReriewsAdapter(Context context, ArrayList<RsReviewToMe> rsReviewToMes, Boolean sortView) {
        super(context, 0, rsReviewToMes);
        this.sortView = sortView;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RsReviewToMe rsMyReview = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_reviews, parent, false);
        }
        TextView tv_name, tv_country, tv_comment, tv_category;
        RatingBar ratingBar1, ratingBar2, ratingBar3;
        LinearLayout ll_ratingBar1, ll_ratingBar2, ll_ratingBar3;
        TextView tvRating1, tvRating2, tvRating3;
        tvRating1 = (TextView) convertView.findViewById(R.id.tvRating1);
        tvRating2 = (TextView) convertView.findViewById(R.id.tvRating2);
        tvRating3 = (TextView) convertView.findViewById(R.id.tvRating3);
        ll_ratingBar1 = (LinearLayout) convertView.findViewById(R.id.ll_ratingBar1);
        ll_ratingBar2 = (LinearLayout) convertView.findViewById(R.id.ll_ratingBar2);
        ll_ratingBar3 = (LinearLayout) convertView.findViewById(R.id.ll_ratingBar3);
        ImageView img_product;
        tv_category = (TextView) convertView.findViewById(R.id.tv_category);
        tv_name= (TextView) convertView.findViewById(R.id.tv_name);
        tv_country = (TextView) convertView.findViewById(R.id.tv_country);
        img_product = (ImageView) convertView.findViewById(R.id.img_product);
        tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
        ratingBar1 = (RatingBar) convertView.findViewById(R.id.ratingBar1);
        ratingBar2 = (RatingBar) convertView.findViewById(R.id.ratingBar2);
        ratingBar3 = (RatingBar) convertView.findViewById(R.id.ratingBar3);

        try{
            imageLoader.displayImage(ApiServices.BASE_URI + rsMyReview.getProduct_listing().getProduct_images().get(0).getProduct_image().getUrl(), img_product);
            tv_name.setText(rsMyReview.getProduct_listing().getName());
            tv_comment.setText(rsMyReview.getReview_message());
            for(int i = 0; i < rsMyReview.getProduct_listing().getCountries().size(); i++){
                String strCountry = rsMyReview.getProduct_listing().getCountries().get(i).getName();
                if(i < rsMyReview.getProduct_listing().getCountries().size() - 1){
                    strCountry += "/";
                }
                tv_country.setText(strCountry);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        HashMap<String, String> hashmapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
        try
        {
            String strCategory = "分类: ";
            strCategory += hashmapCategory.get(String.valueOf(rsMyReview.getProduct_listing().getCategory_id()));
            tv_category.setText(strCategory);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            //float fRating = Float.parseFloat(rsMyReview.getRating_score());
            float fRatingDescripe = 0, fRatingShipping = 0, fRatingCommunication = 0;
            ArrayList<RatingUser> list_rating = rsMyReview.getListRating();
            Map<String, String> rating = rsMyReview.getRating();
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


        return convertView;
    }
}
