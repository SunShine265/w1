package com.wo1haitao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wo1haitao.R;
import com.wo1haitao.models.ProductModel;

import java.util.ArrayList;

/**
 * Created by goodproductssoft on 4/20/17.
 */

public class ProductsFeedBacks2 extends BaseAdapter<ProductModel> {
    Boolean sortView;
    Context context;
    ArrayList<ProductModel> productModels;

    public ProductsFeedBacks2(Context context, ArrayList<ProductModel> productModels, Boolean sortView) {
        super(context, 0, productModels);
        this.sortView = sortView;
        this.context = context;
        this.productModels = productModels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img_product;
        TextView tvName, tvCountry;
        // Get the data item for this position
        ProductModel productModels = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_eval_tab_1, parent, false);
        }

        return convertView;
    }
}
