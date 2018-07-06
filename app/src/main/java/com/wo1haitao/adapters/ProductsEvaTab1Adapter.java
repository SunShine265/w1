package com.wo1haitao.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.response.RsReviewAwaitting;
import com.wo1haitao.dialogs.DialogEvaluation;
import com.wo1haitao.utils.MyPreferences;

import java.util.ArrayList;
import java.util.HashMap;

import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG_PRODUCT;

/**
 * Created by goodproductssoft on 4/20/17.
 */

public class ProductsEvaTab1Adapter extends BaseAdapter<RsReviewAwaitting> {
    Boolean sortView;
    Activity context;
    Fragment f;
    ArrayList<RsReviewAwaitting> arrayOfReviewAwaitting;

    public ProductsEvaTab1Adapter(Activity context, ArrayList<RsReviewAwaitting> arrayOfReviewAwaitting, Boolean sortView, Fragment f) {
        super(context, 0, arrayOfReviewAwaitting);
        this.sortView = sortView;
        this.context = context;
        this.arrayOfReviewAwaitting = arrayOfReviewAwaitting;
        this.f = f;
    }

    ProductsEvaTab1Adapter.ChangeFragment getListenner(){
        if (this.getContext() instanceof ProductsEvaTab1Adapter.ChangeFragment) {
            return (ProductsEvaTab1Adapter.ChangeFragment) this.getContext();
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img_product;
        TextView tvName, tvCountry, tv_category, btn_my_review;
        // Get the data item for this position
        final RsReviewAwaitting reviewAwaitting = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_eval_tab_1, parent, false);
        }
        img_product = (ImageView) convertView.findViewById(R.id.img_product);
        tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvCountry = (TextView) convertView.findViewById(R.id.tv_country);
        btn_my_review = (TextView) convertView.findViewById(R.id.btn_my_review);
        tv_category = (TextView) convertView.findViewById(R.id.tv_category);

        if (reviewAwaitting.getProduct_images() != null) {
            imageLoader.displayImage(ApiServices.BASE_URI + reviewAwaitting.getProduct_images().get(0).getProduct_image().getUrl(), img_product, OPTION_DISPLAY_IMG_PRODUCT);
        }
        if(reviewAwaitting.getName() != null){
            tvName.setText(reviewAwaitting.getName());
        }
        try{
            String strCountry = "";
            if(reviewAwaitting.isNon_restricted_country() == true){
                strCountry = getContext().getString(R.string.txt_no_limited_country);
            }
            else {
                for (int i = 0; i < reviewAwaitting.getCountries().size(); i++) {
                    strCountry += reviewAwaitting.getCountries().get(i).getName();
                    if (i < reviewAwaitting.getCountries().size() - 1) {
                        strCountry += ",";
                    }
                }
            }
            tvCountry.setText(context.getString(R.string.country_value, strCountry));
        }catch (Exception e){
            e.printStackTrace();
        }
//        else if(reviewAwaitting.getCountries() != null && reviewAwaitting.getCountries().size() == 0) {
//            String strCountry = "所在国家: 不限国家";
//            tvCountry.setText(strCountry);
//        }

        HashMap<String, String> hashmapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
        try
        {
            String strCategory = "";
            strCategory += hashmapCategory.get(String.valueOf(reviewAwaitting.getCategory_id()));
            tv_category.setText(context.getString(R.string.category_value, strCategory));
        }catch (Exception e){
            e.printStackTrace();
        }

        btn_my_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogEvaluation cdd = new DialogEvaluation(context, reviewAwaitting);
                cdd.setF(f);
                cdd.show();
            }
        });

        img_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListenner().changeDetailProduct(reviewAwaitting.getId(), "EvaTab1");
            }
        });

        return convertView;
    }

    public interface ChangeFragment {
        void changeDetailProduct(long product_id, String flag);
    }
}
