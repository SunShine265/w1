package com.wo1haitao.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsCountry;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.controls.MultiSpinner;
import com.wo1haitao.dialogs.DialogDropdown;
import com.wo1haitao.models.ItemPicker;
import com.wo1haitao.models.ShippingMethods;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.CustomViewListImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.srodrigo.androidhintspinner.HintSpinner;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestPurchaseFragment extends BaseFragment implements MultiSpinner.MultiSpinnerListener {

    Spinner sp_choose_category;
    List<String> sp_list_info;
    ArrayList<String> sp_list_category, sp_list_country;
    FrameLayout flSaveProduct, fr_product_name;
    MultiSpinner mp_country, mp_info;
    HintSpinner<String> hintSpinner1, hintSpinner2;
    ArrayList<ImageView> list_iv_picked;
    CustomViewListImage ll_content_image;
    ScrollView my_scroll_view;
    Switch sw_new, sw_sch, sw_isfull_country;
    EditText et_product_name, et_description, edt_choose_country, edt_choose_category;
    LinearLayout ll_country, ll_name_product, ll_category, ll_edtcountry, ll_ship_type;
    List<Integer> list_ship_method, list_country;
    String categry_id = null;
    TextView tv_message, txt_save_product;
    FrameLayout fl_message;
    Boolean isChooseSw = false;
    RsProduct productToUpdate;
    RsProduct productToDup;
    RsProduct productToRepost;
    ImageView img_back_action;
    HashMap<String, String> hashmapCategory, hashmapCountry;
    ArrayList<Integer> listIDImage;
    View fragment_view;
    String flag = "RequestPurchase";
    ArrayList<ItemPicker> list_item_country;
    ArrayList<ItemPicker> list_item_category;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public RsProduct getProductToDup() {
        return productToDup;
    }

    public RsProduct getProductToRepost() {
        return productToRepost;
    }

    public void setProductToRepost(RsProduct productToRepost) {
        this.productToRepost = productToRepost;
    }

    public void setProductToDup(RsProduct productToDup) {
        this.productToDup = productToDup;
    }

    public RsProduct getProductToUpdate() {
        return productToUpdate;
    }

    public void setProductToUpdate(RsProduct productToUpdate) {
        this.productToUpdate = productToUpdate;
    }

    public RequestPurchaseFragment.ChangeFragment getListenner() {
        if (this.getActivity() instanceof RequestPurchaseFragment.ChangeFragment) {
            return (RequestPurchaseFragment.ChangeFragment) this.getActivity();
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment_view = inflater.inflate(R.layout.fragment_request_purchase, container, false);
        txt_save_product = (TextView) fragment_view.findViewById(R.id.txt_save_product);
        Utils.setTextActionBar(fragment_view, R.string.title_request_purchase, getActivity());
        if (getFlag().equals("edit")) {
            Utils.setTextActionBar(fragment_view, R.string.title_request_purchase_edit, getActivity());
            txt_save_product.setText(R.string.btn_save_request_purchase_edit);
        }
        if (getFlag().equals("duplicate")) {
            Utils.setTextActionBar(fragment_view, R.string.title_request_purchase_duplicate, getActivity());
        }
        tv_message = (TextView) fragment_view.findViewById(R.id.tv_message);
        fl_message = (FrameLayout) fragment_view.findViewById(R.id.fl_message);
        list_country = new ArrayList<>();
        list_ship_method = new ArrayList<>();
        ll_country = (LinearLayout) fragment_view.findViewById(R.id.ll_country);
        img_back_action = (ImageView) fragment_view.findViewById(R.id.ib_back_action);
        et_product_name = (EditText) fragment_view.findViewById(R.id.et_product_name);
        et_description = (EditText) fragment_view.findViewById(R.id.et_description);
//        sp_list_country = Arrays.asList(getActivity().getResources().getStringArray(R.array.list_country_product));
        fr_product_name = (FrameLayout) fragment_view.findViewById(R.id.fr_product_name);
        ll_name_product = (LinearLayout) fragment_view.findViewById(R.id.view_product_name);
        ll_category = (LinearLayout) fragment_view.findViewById(R.id.view_choose_category);
        ll_edtcountry = (LinearLayout) fragment_view.findViewById(R.id.view_country);
        ll_ship_type = (LinearLayout) fragment_view.findViewById(R.id.view_ship_type);
        edt_choose_category = (EditText) fragment_view.findViewById(R.id.edt_choose_category);

        edt_choose_country = (EditText) fragment_view.findViewById(R.id.edt_choose_country);
        edt_choose_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDropDownCountry();
            }
        });
        HashMap<String, String> mapCountry = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_COUNTRY);
        list_item_country = new ArrayList<>();
        for (Map.Entry entry : mapCountry.entrySet()){
            ItemPicker item_choose = new ItemPicker(entry.getKey().toString(), entry.getValue().toString(), false);
            list_item_country.add(item_choose);
        }

        HashMap<String, String> mapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
        list_item_category = new ArrayList<>();
        for (Map.Entry entry : mapCategory.entrySet()){
            ItemPicker item_choose = new ItemPicker(entry.getKey().toString(), entry.getValue().toString(), false);
            list_item_category.add(item_choose);
        }
        edt_choose_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDropDownCategory();
            }
        });

        sp_list_category = new ArrayList<>();
        sp_list_country = new ArrayList<>();
        listIDImage = new ArrayList<>();

        sp_choose_category = (Spinner) fragment_view.findViewById(R.id.sp_choose_category);
//        sp_list_category = Arrays.asList(getActivity().getResources().getStringArray(R.array.form_category_list));
        hashmapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
        if (hashmapCategory != null) {
            for (Map.Entry entry : hashmapCategory.entrySet()) {
                sp_list_category.add(entry.getValue().toString());
            }
        }
        sp_list_category.add(0, "产品分类");
        img_back_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPress();
            }
        });
        final ArrayAdapter<String> adapter_category = new ArrayAdapter<String>(getActivity(), R.layout.list_item_spinner_custom_size, sp_list_category) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                if (position == 0) {
                    tv.setTextColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.hint_text_request_fragment));
                }
                return (View) tv;
            }
        };
        adapter_category.setDropDownViewResource(android.R.layout.simple_list_item_1);
        sp_choose_category.setAdapter(adapter_category);
        sp_choose_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    categry_id = "";
                } else {
                    for (Map.Entry entry : hashmapCategory.entrySet()) {
                        if (sp_choose_category.getItemAtPosition(i).equals(entry.getValue().toString())) {
                            categry_id = entry.getKey().toString();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        hashmapCountry = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_COUNTRY);
        if (hashmapCountry != null) {
            for (Map.Entry entry : hashmapCountry.entrySet()) {
                sp_list_country.add(entry.getValue().toString());
            }
        }
//        sp_list_country.add(0, "全部国家");
        mp_country = (MultiSpinner) fragment_view.findViewById(R.id.mp_country);
        mp_country.setDefaultText("产品所在国家");
        mp_country.setTitleDialog("产品所在国家");
        mp_country.setCheckMultiCountry(true);
        mp_country.setItems(sp_list_country, null, new MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected, View view) {

                list_country.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        for (Map.Entry entry : hashmapCountry.entrySet()) {
                            if (entry.getValue().equals(sp_list_country.get(i))) {
                                list_country.add(Integer.parseInt(entry.getKey().toString()));
                            }
                        }
                        //list_country.add(i + 1);
                    }
                }
            }
        });

        mp_info = (MultiSpinner) fragment_view.findViewById(R.id.mp_ship_type);
        sp_list_info = Arrays.asList(getActivity().getResources().getStringArray(R.array.list_logic_info));
        mp_info.setTitleDialog("优先选择的物流方式");
        mp_info.setCheckMultiCountry(false);
        mp_info.setItems(sp_list_info, null, new MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected, View view) {
                list_ship_method.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        list_ship_method.add(i + 1);
                    }
                }
            }
        });
        flSaveProduct = (FrameLayout) fragment_view.findViewById(R.id.flSaveProduct);
        flSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strCheckFeild = "";
                checkFieldForm();
                if (strCheckFeild.equals("")) {
                    if (productToUpdate != null) {
                        onUpdateProduct(productToUpdate.getId());
                    } else if (productToDup != null) {
                        onDupProduct(productToDup.getId());
                    } else {
                        onCreateProduct();
                    }
                } else {
                    tv_message.setText(strCheckFeild);
                    fl_message.setVisibility(View.VISIBLE);
                }
            }
        });
        my_scroll_view = (ScrollView) fragment_view.findViewById(R.id.activity_main);
        list_iv_picked = new ArrayList<>();
        ll_content_image = (CustomViewListImage) fragment_view.findViewById(R.id.ll_content_image);
        ll_content_image.setMy_fragment(this);
        sw_new = (Switch) fragment_view.findViewById(R.id.sw_new);
        sw_sch = (Switch) fragment_view.findViewById(R.id.sw_sch);
        sw_new.setChecked(true);
        sw_sch.setChecked(false);

//        sw_new.setOnCheckedChangeListener(sw_new_old_event);
//        sw_sch.setOnCheckedChangeListener(sw_new_old_event);
        //sw_new.setChecked(true);
        sw_isfull_country = (Switch) fragment_view.findViewById(R.id.sw_isfull_country);
        sw_isfull_country.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
//                    ll_country.setVisibility(View.GONE);
                    mp_country.setItems(sp_list_country, null, new MultiSpinner.MultiSpinnerListener() {
                        @Override
                        public void onItemsSelected(boolean[] selected, View view) {

                            list_country.clear();
                            for (int i = 0; i < selected.length; i++) {
                                if (selected[i]) {
                                    for (Map.Entry entry : hashmapCountry.entrySet()) {
                                        if (entry.getValue().equals(sp_list_country.get(i))) {
                                            list_country.add(Integer.parseInt(entry.getKey().toString()));
                                        }
                                    }
                                    //list_country.add(i + 1);
                                }
                            }
                        }
                    });
//                    ll_edtcountry.setBackgroundResource(R.drawable.custom_layout_border_disable);
//                    mp_country.setEnabled(false);
                } else {
                    ll_edtcountry.setBackgroundResource(R.drawable.custom_layout_border);
                    mp_country.setEnabled(true);
//                    ll_country.setVisibility(View.VISIBLE);
                }
            }
        });


        setContentView();
        return fragment_view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CustomViewListImage.REQUEST_IMAGE_CODE) {
            ll_content_image.setViewOnResult(data);
        }
    }

    private void setContentView() {
        ll_content_image.setList_IDImage(new ArrayList<Integer>());
        if (productToUpdate != null || productToDup != null || productToRepost != null) {
            RsProduct product = productToUpdate != null ? productToUpdate : productToDup;
            if (product == null) {
                product = productToRepost;
            }
            et_product_name.setText(product.getName());
            et_description.setText(product.getDescription());
            if (product.isNon_restricted_country()) {
                sw_isfull_country.setChecked(true);
            }
            if (product.isUsed_product()) {
                sw_sch.setChecked(true);
            }


            String category_key = String.valueOf(product.getCategory_id());
            for(ItemPicker itemPicker : list_item_category){
                if(category_key.equals(itemPicker.getId())){
                    edt_choose_category.setText(itemPicker.getName());
                    itemPicker.setChecked(true);
                    categry_id = category_key;
                    break;
                }
            }

            boolean[] check_method;
            check_method = mp_info.getSelected();
            for (int i = 0; i < product.getShipping_methods().size(); i++) {
                ShippingMethods ct = product.getShipping_methods().get(i);
                String ct_name = ct.getName();
                for (int j = 0; j < sp_list_info.size(); j++) {
                    if (sp_list_info.get(j).toString().equals(ct_name)) {
                        check_method[j] = true;
                        break;
                    }
                }
            }
            mp_info.setViewSpinner(check_method);

            boolean[] check_country;
            check_country = mp_country.getSelected();
            String country = "";
            for (int i = 0; i < product.getCountries().size(); i++) {
                RsCountry ct = product.getCountries().get(i);
                String ct_name = ct.getName();
                for (int j = 0; j < list_item_country.size(); j++) {
                    if (list_item_country.get(j).getName().equals(ct_name)) {
                        list_item_country.get(j).setChecked(true);
                        if(country.isEmpty() == true){
                            country = ct_name;
                        }
                        else{
                            country += ", "+ct_name;
                        }
                        break;
                    }
                }
            }
            edt_choose_country.setText(country);
            mp_country.setViewSpinner(check_country);
            ArrayList<String> list_url_image = new ArrayList<>();
            for (int i = 0; i < product.getProduct_images().size(); i++) {
                String url = ApiServices.BASE_URI + product.getProduct_images().get(i).getProduct_image().getThumb().getUrl();
                list_url_image.add(url);
                listIDImage.add(product.getProduct_images().get(i).getId());
            }
            ll_content_image.setViewFromUrls(list_url_image);
            ll_content_image.setList_IDImage(listIDImage);
        }
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if(requestCode == REQUEST_IMAGE_CODE) {
//                ll_content_image.getActivityRs(data,image_click);
//                my_scroll_view.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        my_scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
//                    }
//                });
//            }
//        }
//    }

    @Override
    public void onItemsSelected(boolean[] selected, View view) {
        if (view.getId() == mp_country.getId()) {
            if (mp_country.getSelectedItem().toString().equals("")) {
                hintSpinner1.init();
            }
        } else {
            if (mp_info.getSelectedItem().toString().equals("")) {
                hintSpinner2.init();
            }
        }
    }

    public interface ChangeFragment {

        void changeDetailProduct(long product_id, String flag);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
//    CompoundButton.OnCheckedChangeListener sw_new_old_event = new CompoundButton.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//            if(isChooseSw){
//                isChooseSw = false;
//            }
//            else{
//                isChooseSw = true;
//                if(compoundButton.getId() == sw_new.getId()){
//                    sw_sch.setChecked(!sw_sch.isChecked());
//                }
//                else{
//                    sw_new.setChecked(!sw_new.isChecked());
//                }
//
//            }
//
//        }
//    };

    private Map<String, Integer> getMapformList(List<Integer> list, String from, String end) {
        Map<String, Integer> map = new HashMap<>();
        if (list.size() == 0) {
            return map;
        } else {
            for (int i = 0; i < list.size(); i++) {
                String key = from + i + end;
                Integer value = list.get(i);
                map.put(key, value);
            }
        }
        return map;
    }

    private Map<String, Integer> getMapformListPicker(List<ItemPicker> list, String from, String end) {
        Map<String, Integer> map = new HashMap<>();
        if (list.size() == 0) {
            return map;
        } else {
            int index_param = 0;
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getChecked() == true){
                    String key = from + index_param + end;
                    index_param++;
                    int value = -1;
                    if(Integer.valueOf(list.get(i).getId()) != null){
                        value = Integer.valueOf(list.get(i).getId());
                    }
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    public String CheckField() {
        String strCheckFill = "";
        if (!et_product_name.getText().toString().equals("")
                && (categry_id != null && !categry_id.equals(""))
                && (list_country.size() != 0 || sw_isfull_country.isChecked() == true)
                && !et_description.getText().toString().equals("")
                && ll_content_image.getList_image_data().size() > 0) {
            return strCheckFill;
        } else {
            if (et_product_name.getText().toString().equals("")) {
                strCheckFill += RequestPurchaseFragment.this.getResources().getString(R.string.product_name_request) + "\n";
            }
            if (categry_id == null) {
                strCheckFill += RequestPurchaseFragment.this.getResources().getString(R.string.category_request) + "\n";
            }
            if (list_country.size() == 0 && sw_isfull_country.isChecked() == false) {
                strCheckFill += RequestPurchaseFragment.this.getResources().getString(R.string.country_request) + "\n";
            }
            if (et_description.getText().toString().equals("")) {
                strCheckFill += RequestPurchaseFragment.this.getResources().getString(R.string.description_request) + "\n";
            }
            if (ll_content_image.getList_image_data().size() == 0) {
                strCheckFill += RequestPurchaseFragment.this.getResources().getString(R.string.list_image_product_request) + "\n";
            }
            return strCheckFill;

        }
    }

    private Boolean checkFieldForm() {
        Boolean isOK = true;
        if (et_product_name.getText().toString().equals("")) {
            fr_product_name.setBackgroundColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.colorPrimary));
            // ll_name_product.setBackgroundColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.colorPrimary));
            fr_product_name.getBackground().setAlpha(20);
            et_product_name.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            //et_product_name.setHintTextColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.colorPrimary));

            isOK = false;
        } else {
            fr_product_name.setBackgroundColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.white));
            //ll_name_product.setBackgroundResource(R.drawable.custom_row_bottom_border);

            fr_product_name.getBackground().setAlpha(100);
            et_product_name.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.grey_text));
        }

        if (categry_id == null || categry_id.equals("")) {
            //  ll_category.setBackgroundColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.colorPrimary)ll_category.getBackground().setAlpha(20);

            final ArrayAdapter<String> adapter_category = new ArrayAdapter<String>(getActivity(), R.layout.list_item_spinner_custom_size, sp_list_category) {
                @NonNull
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView tv = (TextView) super.getView(position, convertView, parent);
                    if (position == 0) {
                        tv.setTextColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.colorPrimary));
                    }
                    return (View) tv;
                }
            };
            adapter_category.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            sp_choose_category.setAdapter(adapter_category);

            isOK = false;
        } else {
            //  ll_category.setBackgroundResource(R.drawable.custom_row_bottom_border);
            ll_category.getBackground().setAlpha(100);
            final ArrayAdapter<String> adapter_category = new ArrayAdapter<String>(getActivity(), R.layout.list_item_spinner_custom_size, sp_list_category) {
                @NonNull
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView tv = (TextView) super.getView(position, convertView, parent);
                    if (position == 0) {
                        tv.setTextColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.grey_text));
                    }
                    return (View) tv;
                }
            };
            int current_selected = sp_choose_category.getSelectedItemPosition();
            adapter_category.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            sp_choose_category.setAdapter(adapter_category);
            sp_choose_category.setSelection(current_selected);
        }

//        if (list_country.size() == 0 && sw_isfull_country.isChecked() == false) {
//            ll_edtcountry.setBackgroundColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.colorPrimary));
//            ll_edtcountry.getBackground().setAlpha(20);
//            mp_country.setHintColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.colorPrimary));
//            isOK = false;
//        } else {
//            ll_edtcountry.setBackgroundResource(R.drawable.custom_row_bottom_border);
//            ll_edtcountry.getBackground().setAlpha(100);
//            mp_country.setHintColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.grey_text));
//        }
//        if(list_ship_method.size() == 0){
//            ll_ship_type.setBackgroundColor(ContextCompat.getColor(CustomApp.getInstance(),R.color.colorPrimary));
//            ll_ship_type.getBackground().setAlpha(20);
//            mp_info.setHintColor(ContextCompat.getColor(CustomApp.getInstance(),R.color.colorPrimary));
//            isOK = false;
//        }
//        else{
        ll_ship_type.setBackgroundResource(R.drawable.custom_row_bottom_border);
        ll_ship_type.getBackground().setAlpha(100);
        mp_info.setHintColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.grey_text));
//        }
//        if (et_description.getText().toString().equals("")) {
//            et_description.setBackgroundColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.colorPrimary));
//            et_description.getBackground().setAlpha(20);
//            et_description.setHintTextColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.colorPrimary));
//            isOK = false;
//        } else {
//            et_description.setBackgroundResource(R.drawable.custom_layout_border);
//            et_description.setHintTextColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.grey_text));
//            et_description.getBackground().setAlpha(100);
//        }

        return isOK;
    }

    public void onCreateProduct() {
        final ProgressDialog progressDialog = Utils.createProgressDialog(RequestPurchaseFragment.this.getActivity());
        LinkedHashMap<String, MultipartBody.Part> map = ll_content_image.getBitmapData(getActivity());
        List<MultipartBody.Part> list_image = new ArrayList<>(map.values());
        Collections.reverse(list_image);
        Map<String, RequestBody> requestBodyData = ll_content_image.getRequestBodyData();
        fl_message.setVisibility(View.GONE);
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);

        sw_isfull_country.setChecked(false);
        Map<String, Integer> map_ct = getMapformListPicker(list_item_country, "product_listing_countries_attributes[", "][country_id]");
        if (map_ct.size() == 0) {
            sw_isfull_country.setChecked(true);
        }
        Call<ResponseMessage<RsProduct>> call;
        if (ll_content_image.getList_image_data().size() == 0) {
            call = ws.actionWanttoBuy(false, et_product_name.getText().toString(), et_description.getText().toString(), getCategoryId(),
                    sw_isfull_country.isChecked(), sw_new.isChecked(), sw_sch.isChecked(),
                    getMapformList(list_ship_method, "product_listing_shippings_attributes[", "][shipping_method_id]"),
                    map_ct);
        } else {
            call = ws.actionWanttoBuy(false, et_product_name.getText().toString(), et_description.getText().toString(), getCategoryId(),
                    sw_isfull_country.isChecked(), sw_new.isChecked(), sw_sch.isChecked(),
                    getMapformList(list_ship_method, "product_listing_shippings_attributes[", "][shipping_method_id]"),
                    list_image,
                    map_ct);
        }
        call.enqueue(new Callback<ResponseMessage<RsProduct>>() {
            @Override
            public void onResponse(Call<ResponseMessage<RsProduct>> call, Response<ResponseMessage<RsProduct>> response) {
                try {
                    if (response.errorBody() != null) {

                        ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                        ErrorMessage error = responseMessage.getErrors();
                        String strError = "";
                        ArrayList<String> list_error = responseMessage.getError_messages();
                        if (list_error != null) {
                            for (String error_item : list_error) {
                                if (strError.isEmpty() == true) {
                                    strError = "\u2022 " + error_item;
                                } else {
                                    strError += "\n\u2022 " + error_item;
                                }
                            }
                        }
                        if (!strError.equals("")) {
                            tv_message.setText(strError);
                            fl_message.setVisibility(View.VISIBLE);
                            my_scroll_view.post(new Runnable() {
                                @Override
                                public void run() {
                                    my_scroll_view.fullScroll(ScrollView.FOCUS_UP);
                                }
                            });
                        }

                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                    } else if (response.body() != null && response.body().isSuccess()) {
                        fl_message.setVisibility(View.GONE);
                        ResponseMessage message = response.body();
                        RsProduct product = (RsProduct) message.getData();
                        if (getListenner() != null) {
//                            Toast.makeText(getActivity(), "Create product Success!", Toast.LENGTH_SHORT).show();
                            getListenner().changeDetailProduct(product.getId(), flag);
                            progressDialog.dismiss();
                        }
                        //getActivity().backPress();
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<RsProduct>> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }

    public void onUpdateProduct(long id_product) {
        final ProgressDialog progressDialog = Utils.createProgressDialog(RequestPurchaseFragment.this.getActivity());
//        LinkedHashMap<String, MultipartBody.Part> map = ll_content_image.getBitmapData(getActivity());
//        List<MultipartBody.Part> list_image = new ArrayList<>(map.values());
//        Collections.reverse(list_image);
        Map<String, Integer> mapDestroy = new HashMap<>();

        sw_isfull_country.setChecked(false);
        Map<String, RequestBody> requestBodyData = ll_content_image.getRequestBodyData();
        Map<String, Integer> map_ct = getMapformListPicker(list_item_country, "product_listing_countries_attributes[", "][country_id]");
        if (map_ct.size() == 0) {
            sw_isfull_country.setChecked(true);
        }
//        Map<String, Integer> mapIDImage = getMapformList(listIDImage, "product_images_attributes[", "][id]");
        Map<String, Integer> mapListMethod = getMapformList(list_ship_method, "product_listing_shippings_attributes[", "][shipping_method_id]");

        List<MultipartBody.Part> map_param = new ArrayList<>();
        for (Map.Entry entry : map_ct.entrySet()) {
            map_param.add(Utils.createStringPart(entry.getKey().toString(), entry.getValue().toString()));
        }
//        for(Map.Entry entry : mapIDImage.entrySet()){
//            map_param.add(Utils.createStringPart(entry.getKey().toString(), entry.getValue().toString()));
//        }
        for (Map.Entry entry : mapListMethod.entrySet()) {
            map_param.add(Utils.createStringPart(entry.getKey().toString(), entry.getValue().toString()));
        }
//        for(Map.Entry entry : mapDestroy.entrySet()){
//            map_param.add(Utils.createStringPart(entry.getKey().toString(), entry.getValue().toString()));
//        }

        List<MultipartBody.Part> listImage = ll_content_image.getListBitmapDataDelete();
        ArrayList<MultipartBody.Part> listReserseImage = new ArrayList<MultipartBody.Part>(listImage);
        Collections.reverse(listReserseImage);

        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<RsProduct>> call;
        call = ws.actionUpdateWanttoBuy(id_product, false, et_product_name.getText().toString(), et_description.getText().toString(), getCategoryId(),
                sw_isfull_country.isChecked(), sw_new.isChecked(), sw_sch.isChecked(), map_param, listReserseImage);
        call.enqueue(new Callback<ResponseMessage<RsProduct>>() {
            @Override
            public void onResponse(Call<ResponseMessage<RsProduct>> call, Response<ResponseMessage<RsProduct>> response) {
                try {
                    if (response.errorBody() != null) {

                        ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                        String strError = "";
                        ArrayList<String> list_error = responseMessage.getError_messages();
                        if (list_error != null) {
                            for (String error_item : list_error) {
                                if (strError.isEmpty() == true) {
                                    strError = "\u2022 " + error_item;
                                } else {
                                    strError += "\n\u2022 " + error_item;
                                }
                            }
                        }
                        if (!strError.equals("")) {
                            tv_message.setText(strError);
                            fl_message.setVisibility(View.VISIBLE);
                            my_scroll_view.post(new Runnable() {
                                @Override
                                public void run() {
                                    my_scroll_view.fullScroll(ScrollView.FOCUS_UP);
                                }
                            });
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                    } else if (response.body() != null && response.body().isSuccess()) {
                        fl_message.setVisibility(View.GONE);
                        ResponseMessage message = response.body();
                        RsProduct product = (RsProduct) message.getData();
                        if (getListenner() != null) {
//                            Toast.makeText(getActivity(), "Update product Success!", Toast.LENGTH_SHORT).show();
                            getListenner().changeDetailProduct(product.getId(), flag);
                            progressDialog.dismiss();
                        }
                        //getActivity().backPress();
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<RsProduct>> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }

    public void onDupProduct(long id_product) {
        final ProgressDialog progressDialog = Utils.createProgressDialog(RequestPurchaseFragment.this.getActivity());
        LinkedHashMap<String, MultipartBody.Part> map = ll_content_image.getBitmapData(getActivity());
        List<MultipartBody.Part> list_image = new ArrayList<>(map.values());
        Collections.reverse(list_image);

        sw_isfull_country.setChecked(false);
        Map<String, RequestBody> requestBodyData = ll_content_image.getRequestBodyData();
        Utils.setTextActionBar(fragment_view, R.string.title_duplicate, getActivity());
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);

        Map<String, Integer> map_ct = getMapformListPicker(list_item_country, "product_listing_countries_attributes[", "][country_id]");
        if (map_ct.size() == 0) {
            sw_isfull_country.setChecked(true);
        }
        Call<ResponseMessage<RsProduct>> call;

        call = ws.actionDupWanttoBuy(id_product, false, et_product_name.getText().toString(), et_description.getText().toString(), getCategoryId(),
                sw_isfull_country.isChecked(), sw_new.isChecked(), sw_sch.isChecked(),
                getMapformList(list_ship_method, "product_listing_shippings_attributes[", "][shipping_method_id]"),
                list_image,
                map_ct);
        call.enqueue(new Callback<ResponseMessage<RsProduct>>() {
            @Override
            public void onResponse(Call<ResponseMessage<RsProduct>> call, Response<ResponseMessage<RsProduct>> response) {
                try {
                    if (response.errorBody() != null) {

                        ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                        String strError = "";
                        ArrayList<String> list_error = responseMessage.getError_messages();
                        if (list_error != null) {
                            for (String error_item : list_error) {
                                if (strError.isEmpty() == true) {
                                    strError = "\u2022 " + error_item;
                                } else {
                                    strError += "\n\u2022 " + error_item;
                                }
                            }
                        }
                        if (!strError.equals("")) {
                            tv_message.setText(strError);
                            fl_message.setVisibility(View.VISIBLE);
                            my_scroll_view.post(new Runnable() {
                                @Override
                                public void run() {
                                    my_scroll_view.fullScroll(ScrollView.FOCUS_UP);
                                }
                            });
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                    } else if (response.body() != null && response.body().isSuccess()) {
                        fl_message.setVisibility(View.GONE);
                        ResponseMessage message = response.body();
                        RsProduct product = (RsProduct) message.getData();
                        if (getListenner() != null) {
//                            Toast.makeText(getActivity(), "Update product Success!", Toast.LENGTH_SHORT).show();
                            getListenner().changeDetailProduct(product.getId(), flag);
                            progressDialog.dismiss();
                        }
                        //getActivity().backPress();
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<RsProduct>> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }

    private void showDialogDropDownCountry(){

        DialogDropdown dropdown = new DialogDropdown(this.getActivity(), true, list_item_country, edt_choose_country);

        dropdown.setCanceledOnTouchOutside(true);
        dropdown.show();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dropdown.getWindow().getAttributes());

        layoutParams.width = size.x*4/5;
        layoutParams.height = size.y*4/5;
        dropdown.getWindow().setAttributes(layoutParams);
    }

    private void showDialogDropDownCategory(){

        DialogDropdown dropdown = new DialogDropdown(this.getActivity(), false, list_item_category, edt_choose_category);

        dropdown.setCanceledOnTouchOutside(true);
        dropdown.show();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dropdown.getWindow().getAttributes());

        layoutParams.width = size.x*4/5;
        layoutParams.height = size.y*4/5;
        dropdown.getWindow().setAttributes(layoutParams);
    }

    private String getCategoryId(){
        for(ItemPicker itemPicker : list_item_category){
            if(itemPicker.getChecked() == true){
               return itemPicker.getId();
            }
        }
        return "";
    }
}
