package com.wo1haitao.controls;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.adapters.MultiSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.wo1haitao.utils.Utils.SMALL_SIZE_SCREEN_DP;

/**
 * Created by user on 5/3/2017.
 */

public class MultiSpinner extends AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> items;
    private boolean[] selected;
    private String defaultText ="优先选择的物流方式";
    private boolean checkMultiCountry = false;
    SparseBooleanArray sparseBooleanArray ;
    private boolean is_scale_custom = true;
    public boolean isCheckMultiCountry() {
        return checkMultiCountry;
    }

    public void setCheckMultiCountry(boolean checkMultiCountry) {
        this.checkMultiCountry = checkMultiCountry;
    }


    public String getTitleDialog() {
        return titleDialog;
    }

    public void setTitleDialog(String titleDialog) {
        this.titleDialog = titleDialog;
    }

    private String titleDialog = "";

    private int hintColor = ContextCompat.getColor(getContext() ,R.color.dialog_text_hint);

    public int getHintColor() {
        return hintColor;
    }

    public void setHintColor(int hintColor) {
        this.hintColor = hintColor;
        updateData();
    }

    public boolean[] getSelected() {
        return selected;
    }

    public void setSelected(boolean[] selected) {
        this.selected = selected;
    }

    private MultiSpinnerListener listener;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (isChecked)
            selected[which] = true;
        else
            selected[which] = false;
    }

    public void updateData(){
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean someUnselected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
                someUnselected = true;
            }
        }

        String spinnerText;
        if (someUnselected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] {spinnerText}){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if(view instanceof TextView){
                    ((TextView)view).setTextColor(((TextView)view).getText().equals(defaultText) ? hintColor : Color.BLACK);
                    initTextSize(((TextView)view));
                }
                return view;
            }

            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if(view instanceof TextView){
                    ((TextView)view).setTextColor(((TextView)view).getText().equals(defaultText) ? hintColor : Color.BLACK);
                    initTextSize(((TextView)view));
                }
                return view;
            }
        };
        setAdapter(adapter);
        listener.onItemsSelected(selected, this);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        updateData();
    }

    @Override
    public boolean performClick() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
////        builder.setAdapter(new MultiSpinnerAdapter(MultiSpinner.this.getContext(),new ArrayList<>(items), selected), null);
//        builder.setMultiChoiceItems(items.toArray(new CharSequence[items.size()]), selected, this);
//        TextView title = new TextView(getContext());
//        title.setText(titleDialog);
//        title.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.profile_menu));
//        title.setPadding(10, 50, 10, 50);
//        title.setGravity(Gravity.CENTER_HORIZONTAL);
//        title.setTextSize(18);
//        title.setTextColor(Color.WHITE);
//        builder.setCustomTitle(title);
//        builder.setPositiveButton("确定",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//        builder.setOnCancelListener(this);
//        AlertDialog alertDialog = builder.create();
//
//        alertDialog.show();
//
//        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
//        positiveButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_drable_profile_menu));
//        positiveButton.setTextColor(Color.WHITE);
//        positiveButton.setGravity(Gravity.CENTER);
//        positiveButton.setTextSize(18);
//        positiveButton.setPadding(0, 50, 0, 50);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        positiveButton.setLayoutParams(params);
//        positiveButton.invalidate();
//
//        if(MultiSpinner.this.isCheckMultiCountry()) {
//            DisplayMetrics displayMetrics = new DisplayMetrics();
//            WindowManager windowManager = (WindowManager) getContext()
//                    .getSystemService(Context.WINDOW_SERVICE);
//            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
//            int displayWidth = displayMetrics.widthPixels;
//            int displayHeight = displayMetrics.heightPixels;
//            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//            layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
//            int dialogWindowWidth = (int) (displayWidth * 1f);
//            int dialogWindowHeight = (int) (displayHeight * 0.8f);
//            layoutParams.width = dialogWindowWidth;
//            layoutParams.height = dialogWindowHeight;
//            alertDialog.getWindow().setAttributes(layoutParams);
//        }
        final Dialog dialog = new Dialog(getContext());

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.multi_spinner, null);
        final ListView lv = (ListView) view.findViewById(R.id.lv_multi);
        TextView tv_send_report = (TextView) view.findViewById(R.id.tv_send_report);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(titleDialog);

        // Change MyActivity.this and myListOfItems to your own values
        final ArrayList<String> arrItems = new ArrayList<>(items);
        MultiSpinnerAdapter multiSpinnerAdapter = new MultiSpinnerAdapter(getContext(), arrItems, selected);
        lv.setAdapter(multiSpinnerAdapter);
        tv_send_report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                dialog.dismiss();
            }
        });

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selected[position])
                    selected[position] = false;
                else
                    selected[position] = true;
                updateData();

            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(MultiSpinner.this.isCheckMultiCountry()) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int displayHeight = displayMetrics.heightPixels;
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            int dialogWindowHeight = (int) (displayHeight * 0.8f);
            layoutParams.height = dialogWindowHeight;
            dialog.getWindow().setAttributes(layoutParams);
        }
        dialog.show();

        return true;
    }

    public void setItems(List<String> items, String allText,
                         MultiSpinnerListener listener) {
        this.items = items;
       // this.defaultText = allText;
        this.listener = listener;

        // all selected by default
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = false;

        updateData();
    }

    public void setViewSpinner(boolean[] selected_param) {
        // all selected by default
        selected = new boolean[items.size()];

        for (int i = 0; i < selected.length; i++){
            selected[i] = selected_param[i];
        }

        updateData();
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected, View view);
    }

    private void initTextSize(TextView textView){
        WindowManager manager =  (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int long_dimention = point.x < point.y ? point.y : point.x;
        int short_dimention = point.x > point.y ? point.y : point.x;

        float scale_by_screen_size = 1;
        if(is_scale_custom == true){
            if(short_dimention < 1200){
                scale_by_screen_size = (float) 0.9;
            }
            else if (short_dimention < 900){
                scale_by_screen_size = (float) 0.85;
            }
        }
        float scale_by_screen_size_screen = 1;
        if(CustomApp.getInstance().getInch() >= 9){
            scale_by_screen_size_screen = (float) 0.8;
        }
        else if(CustomApp.getInstance().getInch() >= 6){
            scale_by_screen_size_screen = (float) 0.9;
        }
        float current_size = 53;
        float new_size = scale_by_screen_size*scale_by_screen_size_screen*long_dimention*current_size/SMALL_SIZE_SCREEN_DP;
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,new_size);
        // get width and height
    }
}
