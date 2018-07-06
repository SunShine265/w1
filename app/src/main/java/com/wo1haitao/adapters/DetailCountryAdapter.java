package com.wo1haitao.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wo1haitao.R;

import java.util.ArrayList;

/**
 * Created by user on 8/24/2017.
 */

public class DetailCountryAdapter extends BaseAdapter {
    private ArrayList<String> arrayList;
    private boolean[] selectedList;

    public DetailCountryAdapter(Activity activity, ArrayList<String> arrayList,
                                boolean[] selectedList) {
        super(activity, 0, arrayList);
        this.arrayList = arrayList;
        this.selectedList = selectedList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_check_box, parent, false);
        CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(arrayList.get(position));
        if (selectedList != null) {
            checkbox.setChecked(selectedList[position]);
            checkbox.setClickable(false);
        }
        return convertView;
    }
}
