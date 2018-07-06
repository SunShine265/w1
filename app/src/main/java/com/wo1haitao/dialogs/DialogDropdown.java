package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.models.CategoryModel;
import com.wo1haitao.models.ItemPicker;

import java.util.ArrayList;

/**
 * Created by goodproductssoft on 11/29/17.
 */

public class DialogDropdown extends Dialog {

    Activity activity;
    Boolean isMultiChoose = true;
    ArrayList<ItemPicker> list_item;

    Button bt_done;
    ListView lv_dropdown;
    AddapterDropdown addapter;
    EditText editDropdown;


    public DialogDropdown(@NonNull Activity context, Boolean isMultiChoose, ArrayList<ItemPicker> list_item, EditText editText) {
        super(context);
        this.activity = context;
        this.isMultiChoose = isMultiChoose;
        this.list_item = list_item;
        this.editDropdown = editText;
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {

        super.setOnDismissListener(listener);


    }

    @Override
    protected void onStop() {
        String value = "";
        for(ItemPicker item : list_item){
            if(item.getChecked() == true){
                if(value.isEmpty() == true){
                    value += item.getName();
                }
                else{
                    value += ", "+item.getName();
                }
            }
        }
        editDropdown.setText(value);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_dropdown);
        bt_done = (Button) findViewById(R.id.bt_done);
        lv_dropdown = (ListView) findViewById(R.id.lv_item);
        addapter = new AddapterDropdown(this.activity,R.layout.item_dropdown_blur,list_item, isMultiChoose, this);
        lv_dropdown.setAdapter(addapter);
        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });
        if(isMultiChoose == true){
            bt_done.setVisibility(View.VISIBLE);
        }
        else{
            bt_done.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void setLayoutListView(int height){
        int height_of_all_item = height*list_item.size();

        Resources r = activity.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int fit_height = 0;
        if((height_of_all_item+px) >= (size.y*4/5)){
            fit_height = (int) (size.y*4/5 - px);
        }
        else{
            fit_height = (int) (height_of_all_item);
        }

        ViewGroup.LayoutParams layoutParams = lv_dropdown.getLayoutParams();
        layoutParams.height = fit_height;
        lv_dropdown.setLayoutParams(layoutParams);
    }


}

class AddapterDropdown extends ArrayAdapter<ItemPicker> {
    Context context;
    ArrayList<ItemPicker> list_item;
    int layout_id;
    Boolean isMultiChoose = true;
    DialogDropdown dialogDropdown;
    Boolean isSetLayoutListView = false;
    Boolean isGetHeight = false;
    ItemPicker itemCheckedSingle;


    public AddapterDropdown(@NonNull Context context, @LayoutRes int resource, @NonNull  ArrayList<ItemPicker> objects, Boolean isMultiChoose, DialogDropdown dialogDropdown) {
        super(context, resource, objects);
        this.context = context;
        this.list_item = objects;
        this.layout_id = resource;
        this.isMultiChoose = isMultiChoose;
        this.dialogDropdown = dialogDropdown;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row =  LayoutInflater.from(getContext()).inflate(this.layout_id, parent, false);
        final TextView tv_title = (TextView) row.findViewById(R.id.tv_title);
        final FrameLayout fr_background = (FrameLayout) row.findViewById(R.id.fr_background);
        final FrameLayout fr_content_view = (FrameLayout) row.findViewById(R.id.fr_content_view);
        final ItemPicker itemPicker = list_item.get(position);
        if(isMultiChoose  == true){
            if(itemPicker.getChecked() == true){
                fr_content_view.setBackgroundColor(ContextCompat.getColor(CustomApp.getInstance(),R.color.highlight_color_dropdown));
                tv_title.setTextColor(ContextCompat.getColor(CustomApp.getInstance(),R.color.black));
                //itemPicker.setChecked(false);
            }
            else{

                fr_content_view.setBackgroundColor(ContextCompat.getColor(CustomApp.getInstance(),R.color.transparent));
                tv_title.setTextColor(ContextCompat.getColor(CustomApp.getInstance(),R.color.time_chat));
                //itemPicker.setChecked(true);
            }
           // notifyDataSetChanged();
        }
        if(isSetLayoutListView == false){
            isSetLayoutListView = true;
            fr_background.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if(fr_background.getHeight() > 10 && isGetHeight == false){
                        isGetHeight = true;
                        dialogDropdown.setLayoutListView(fr_background.getHeight());
                    }
                }
            });
        }
        if(itemPicker.getChecked() == true && isMultiChoose == false){
            if(itemCheckedSingle != null){
                itemCheckedSingle.setChecked(false);
            }
            itemCheckedSingle = itemPicker;
        }
        tv_title.setText(itemPicker.getName());
        fr_content_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMultiChoose  == true){
                    if(itemPicker.getChecked() == true){
                        fr_content_view.setBackgroundColor(ContextCompat.getColor(CustomApp.getInstance(),R.color.transparent));
                        tv_title.setTextColor(ContextCompat.getColor(CustomApp.getInstance(),R.color.time_chat));
                        itemPicker.setChecked(false);
                    }
                    else{
                        fr_content_view.setBackgroundColor(ContextCompat.getColor(CustomApp.getInstance(),R.color.highlight_color_dropdown));
                        tv_title.setTextColor(ContextCompat.getColor(CustomApp.getInstance(),R.color.black));
                        itemPicker.setChecked(true);
                    }
                   // notifyDataSetChanged();
                }
                else{
                    if(itemCheckedSingle != null){
                        itemCheckedSingle.setChecked(false);
                    }
                    itemCheckedSingle = itemPicker;
                    itemCheckedSingle.setChecked(true);
                    dialogDropdown.dismiss();
                }

            }
        });

        return row;
    }
}