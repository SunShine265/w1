package com.wo1haitao.dialogs;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.wo1haitao.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by user on 4/26/2017.
 */

public class PopupMenuCustom extends PopupWindow {
    public static PopupWindow getPopupWindowSettingProfile(final Context context, final ImageView iv_setting, final LinearLayout ll_setting) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_menu_profile, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
                iv_setting.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                ll_setting.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
        });
        return popupWindow;
    }

    public static PopupWindow getPopupWindowGetImageProduct(final Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_menu_get_image, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        return popupWindow;
    }

    public static PopupWindow getPopupEval(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.layout_dialog_shipping_method, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        return popupWindow;
    }

    public static PopupWindow getPopupBidProduct(final Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.layout_dialog_bid_product, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        Button bt_close = (Button) popupView.findViewById(R.id.bt_close);
        final EditText et_content = (EditText) popupView.findViewById(R.id.et_content);
        et_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager keyboard = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(et_content, 0);
                }
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        return popupWindow;
    }
}
