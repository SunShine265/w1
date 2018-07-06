package com.wo1haitao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.wo1haitao.R;

import java.util.ArrayList;

/**
 * Created by user on 8/24/2017.
 */

public class MultiSpinnerAdapter extends BaseAdapter {
    private ArrayList<String> arrayList;
    private boolean[] selectedList;

    public MultiSpinnerAdapter(Context context, ArrayList<String> arrayList,
                               boolean[] selectedList) {
        super(context, 0, arrayList);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_check_box_multi_spinner, parent, false);
        final CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(arrayList.get(position));
        if(selectedList[position] == true){
            checkbox.setChecked(true);
        }
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == false){
                    checkbox.setChecked(false);
                    selectedList[position] =false;
                }
                else {
                    checkbox.setChecked(true);
                    selectedList[position] = true;
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox.isChecked() == true){
                    checkbox.setChecked(false);
                }
                else{
                    checkbox.setChecked(true);
                }
            }
        });
        return convertView;
    }
}
