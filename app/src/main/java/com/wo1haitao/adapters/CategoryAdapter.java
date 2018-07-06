package com.wo1haitao.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.wo1haitao.R;
import com.wo1haitao.models.CategoryModel;

import java.util.ArrayList;

/**
 * Created by user on 6/1/2017.
 */

public class CategoryAdapter extends ArrayAdapter<CategoryModel> {
    private Context context;
    ArrayList<CategoryModel>  categoryModels = null;

    public CategoryAdapter(Activity context, int resource, ArrayList<CategoryModel> data)
    {
        super(context, resource, data);
        this.context = context;
        this.categoryModels = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row == null){
            row = LayoutInflater.from(getContext()).inflate(R.layout.spinner_layout_category, parent, false);
        }
        row.setPadding(80,20,50,20);
        CategoryModel item = categoryModels.get(position);
        if(item != null){
            ImageView img_category = (ImageView) row.findViewById(R.id.imageIcon);
            if(img_category != null)
            {
                img_category.setImageResource(item.getCategoryImage());
                //img_category.setBackgroundDrawable(getResources().getDrawable(item.getCategoryImage()));
            }
        }
        return row;
    }

//    @Override
//    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View row = convertView;
//        if(row == null){
//            row = LayoutInflater.from(getContext()).inflate(R.layout.spinner_layout_category, parent, false);
//        }
//        CategoryModel item = categoryModels.get(position);
//        if(item != null){
//            ImageView img_category = (ImageView) row.findViewById(R.id.imageIcon);
//            if(img_category != null)
//            {
//                img_category.setImageResource(item.getCategoryImage());
//                //img_category.setBackgroundDrawable(getResources().getDrawable(item.getCategoryImage()));
//            }
//        }
//        return row;
//    }
}
