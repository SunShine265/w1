package com.wo1haitao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.response.RsDisputes;
import com.wo1haitao.utils.MyPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG_PRODUCT;

/**
 * Created by goodproductssoft on 4/20/17.
 */

public class ProductsFeedBacks extends BaseAdapter<RsDisputes> {
    Boolean sortView;
    Context context;
    ArrayList<RsDisputes> arrayDisputes;

    public ProductsFeedBacks(Context context, ArrayList<RsDisputes> rsDisputes, Boolean sortView) {
        super(context, 0, rsDisputes);
        this.sortView = sortView;
        this.context = context;
        this.arrayDisputes = rsDisputes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img_product;
        TextView tvCountry, tvCategory, tvReasonComplain, tvDetailComplain, tvName;
        // Get the data item for this position
        RsDisputes rsDisputes = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_feedbacks, parent, false);
        }
        tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvCountry = (TextView) convertView.findViewById(R.id.tv_country);
        tvCategory = (TextView) convertView.findViewById(R.id.tv_category);
        tvReasonComplain = (TextView) convertView.findViewById(R.id.id_reason_complain);
        tvDetailComplain = (TextView) convertView.findViewById(R.id.id_detail_complain);
        img_product = (ImageView) convertView.findViewById(R.id.img_product);

        if (rsDisputes.getReason_to_dispute() != null) {
            tvReasonComplain.setText(CustomApp.getInstance().getString(R.string.title_reason_complain, rsDisputes.getReason_to_dispute().toString()));
        }
        if (rsDisputes.getDetails() != null) {
            tvDetailComplain.setText(CustomApp.getInstance().getString(R.string.title_detail_complain, rsDisputes.getDetails().toString()));
        }
        try {
            if (rsDisputes.getProduct_listing().getProduct_images() != null && rsDisputes.getProduct_listing().getProduct_images().size() > 0) {
                imageLoader.displayImage(ApiServices.BASE_URI + rsDisputes.getProduct_listing().getProduct_images().get(0).getProduct_image().getUrl(), img_product, OPTION_DISPLAY_IMG_PRODUCT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            HashMap<String, String> hashmapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
            if (hashmapCategory != null) {
                for (Map.Entry entry : hashmapCategory.entrySet()) {
                    if (String.valueOf(rsDisputes.getProduct_listing().getCategory_id()).equals(entry.getKey())) {
                        tvCategory.setText(context.getString(R.string.category_value, entry.getValue().toString()));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String strCountry = "";
            if (rsDisputes.getProduct_listing().isNon_restricted_country() == true) {
                strCountry = getContext().getString(R.string.txt_no_limited_country);
            } else {
                if (rsDisputes.getProduct_listing().getCountries() != null) {
                    for (int i = 0; i < rsDisputes.getProduct_listing().getCountries().size(); i++) {
                        strCountry = rsDisputes.getProduct_listing().getCountries().get(i).getName();
                        if (i < rsDisputes.getProduct_listing().getCountries().size() - 1) {
                            strCountry += ",";
                        }
                    }
                }
            }
            tvCountry.setText(context.getString(R.string.country_value, strCountry));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rsDisputes.getProduct_listing().getName() != null) {
            tvName.setText(rsDisputes.getProduct_listing().getName().toString());
        }

        return convertView;
    }
}
