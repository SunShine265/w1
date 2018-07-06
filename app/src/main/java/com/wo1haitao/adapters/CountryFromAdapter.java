package com.wo1haitao.adapters;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.wo1haitao.R;
import com.wo1haitao.models.CustomCountryForm;

import java.util.ArrayList;

/**
 * Created by user on 4/25/2017.
 */

public class CountryFromAdapter extends ArrayAdapter<CustomCountryForm> {
    Activity activity;
    int resId;
    ArrayList<CustomCountryForm> strings;

    public CountryFromAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull ArrayList<CustomCountryForm> objects) {
        super(context, resource, objects);
        this.activity = context;
        resId = resource;
        strings = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resId, parent, false);
        }
        final EditText edit_country = (EditText) convertView.findViewById(R.id.edt_country);
        edit_country.setText(strings.get(position).getContent());
        final ImageView imv_bt = (ImageView) convertView.findViewById(R.id.imageView);
        if(strings.get(position).isEnable()){
            imv_bt.setImageResource(R.drawable.favories_icon);
            edit_country.setEnabled(true);
        }else{
            imv_bt.setImageResource(R.drawable.bids);
            edit_country.setEnabled(false);
        }
        imv_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strings.get(position).isEnable()){
                    String text = edit_country.getText().toString();
                    strings.get(position).setContent(text);
                    strings.get(position).setEnable(false);
                    strings.add(new CustomCountryForm("",true));

                }else{
                    strings.remove(position);

                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
