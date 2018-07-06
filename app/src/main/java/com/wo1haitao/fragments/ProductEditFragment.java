package com.wo1haitao.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.wo1haitao.R;
import com.wo1haitao.controls.CustomEditextCountry;
import com.wo1haitao.dialogs.DialogPermission;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.CustomViewListImage;
import com.wo1haitao.views.ActionBarProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;

import static android.app.Activity.RESULT_OK;
import static com.wo1haitao.views.CustomViewListImage.REQUEST_IMAGE_CODE;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class ProductEditFragment extends BaseFragment implements View.OnClickListener {

    CustomEditextCountry customEditext;
    LinearLayout list_country_layout, ll_content_image;
    ArrayList<CustomEditextCountry> listCustomEditext;
    List<String> sp_list;
    ScrollView scvProduct;
    FrameLayout flProductEdit;
    int position_current = 0;
    ActionBarProject my_action_bar;
    Spinner sp_choose;
    CustomViewListImage ll_list_image;
    View.OnClickListener image_click;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_product_edit, container, false);
        my_action_bar = (ActionBarProject) view.findViewById(R.id.my_action_bar);
        my_action_bar.getLogoBarBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        listCustomEditext = new ArrayList<>();
        scvProduct = (ScrollView) view.findViewById(R.id.scvProduct);
        sp_choose = (Spinner) view.findViewById(R.id.sp_choose);
        sp_list = Arrays.asList(getActivity().getResources().getStringArray(R.array.form_product_edit_spinner));
        HintSpinner<String> hintSpinner = new HintSpinner<>(
                sp_choose,
                new HintAdapter<String>(getActivity(), R.string.form_product_spinner_hint, sp_list),
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        // Here you handle the on item selected event (this skips the hint selected event)
                    }
                });
        hintSpinner.init();


        customEditext = (CustomEditextCountry) view.findViewById(R.id.customEditext);
        listCustomEditext.add(customEditext);
        listCustomEditext.get(position_current).imageview.setOnClickListener(this);
        list_country_layout = (LinearLayout) view.findViewById(R.id.layout_list_country);
        ll_content_image = (LinearLayout) view.findViewById(R.id.ll_content_image);
        flProductEdit = (FrameLayout) view.findViewById(R.id.flSaveProduct);
        flProductEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        image_click = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result;
                String mPermissions[] = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                List<String> listPermissionsNeeded = new ArrayList<>();

                for (String p : mPermissions) {
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(ProductEditFragment.this.getActivity(), p);
                    if(ContextCompat.checkSelfPermission(ProductEditFragment.this.getActivity(), p) == PackageManager.PERMISSION_DENIED){
                        if (ContextCompat.checkSelfPermission(ProductEditFragment.this.getActivity(), p) == PackageManager.PERMISSION_DENIED && !showRationale) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    DialogPermission dialogPermission = new DialogPermission(ProductEditFragment.this.getActivity());
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(dialogPermission.getWindow().getAttributes());
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                    dialogPermission.show();
                                }
                            });
                            return;
                        } else {
                            result = ContextCompat.checkSelfPermission(ProductEditFragment.this.getActivity(), p);
                            if (result != PackageManager.PERMISSION_GRANTED) {
                                listPermissionsNeeded.add(p);
                            }
                        }
                    }
                }

                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions(ProductEditFragment.this.getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1003);
                }
                else {
                    Intent it = ll_list_image.getImageClick(view, getActivity());
                    startActivityForResult(it, REQUEST_IMAGE_CODE);
                }
            }
        };
        ll_list_image = (CustomViewListImage) view.findViewById(R.id.ll_list_image);
        ll_list_image.setMyClick(image_click, false);

        return view;
    }

    @Override
    public void onClick(View view) {
        RelativeLayout rlayout = (RelativeLayout) view.getParent();
        LinearLayout llayout = (LinearLayout) rlayout.getParent();
        CustomEditextCountry currentEditext = (CustomEditextCountry) llayout.getParent();
        if (currentEditext.isEnable) {
            if (checkString(currentEditext.editext_country.getText().toString())) {
                currentEditext.isEnable = false;
                currentEditext.imageview.setImageResource(R.drawable.bids);
                currentEditext.editext_country.setEnabled(false);
                listCustomEditext.add(currentEditext);
                int index = listCustomEditext.indexOf(currentEditext);
                currentEditext.position = index;
                CustomEditextCountry newEditext = new CustomEditextCountry(getActivity());
                newEditext.imageview.setOnClickListener(this);
                list_country_layout.addView(newEditext);
            }
        } else {

            listCustomEditext.remove(currentEditext);
            list_country_layout.removeView(currentEditext);
        }
    }

    private boolean checkString(String s) {
        s = s.replace(" ", "");
        if (s.equals(""))
            return false;
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CODE) {
                ll_list_image.getActivityRs(data, image_click);
                scvProduct.post(new Runnable() {
                    @Override
                    public void run() {
                        scvProduct.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        }
    }
}
