package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.activities.BaseActivity;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.controls.CustomEditextSoftkey;
import com.wo1haitao.controls.DecimalDigitsInputFilter;
import com.wo1haitao.fragments.ProductDetailFragment;
import com.wo1haitao.models.ItemPicker;
import com.wo1haitao.models.OfferMyBidModel;
import com.wo1haitao.models.ProductOffer;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.CustomViewListImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wo1haitao.CustomApp.getInstance;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class DialogBidShop extends Dialog implements MainActivity.CallBackKeyboard {

    public Activity c;
    public Fragment f;
    public CustomViewListImage ll_content_image;
    View.OnClickListener image_click;
    ScrollView sv_product;
    Spinner sp_choose_condition_product, sp_choose_country;
    CustomEditextSoftkey edtOfferPrice, edtShippingCost, edtShippingCost2, edtShippingCost3,
            edtExpectedArrivalDays, edtRemarks, edtExpectedArrivalDays_2, edtExpectedArrivalDays_3;
    TextView tvCondition, tvNameProduct, sp_choose_shipping_method, sp_choose_shipping_method_2,
            sp_choose_shipping_method_3, tv_message;
    List<String> spListCondition;
    ArrayList<String> spListCountry, spListShippingMethod;
    int conditionID, countryID = 0, methodID;
    Button bt_save_offer, add_offer_method, remove_offer_method;
    List<Float> listImages, listShippingMethod;
    List<String> listOfferPrice, listExpectedArrivalDays, listShippingCost;
    List<Boolean> listReoffer;
    List<String> listRemarks;
    long idProduct = 0, tenderID = 0;
    Boolean isReoffer, isOffered;
    OfferMyBidModel offerDetail;
    String couterOfferPrice;
    RsProduct myProduct;
    ArrayList<LinearLayout> listShipping;
    ArrayList<Button> listButtonRemove;
    LinearLayout lineShipping1, lineShipping2, lineShipping3, ll_button, ll_title, ll_content, contentRootView;
    Button remove_offer_method_1, remove_offer_method_2, remove_offer_method_3;
    Integer idShippingMethod = 0, idShippingMethod2 = 0, idShippingMethod3 = 0;
    int listLine = 1;
    LinearLayout fl_message;
    boolean isShowKeyboard = false;
    boolean isSetHeightSv = false;
    int scroll_height = 0;
    int screen_height = 0;
    int top_component_height = 0;
    int bottom_component_height = 0;
    int long_dimention = 0;
    EditText edt_choose_condition, edt_choose_country, edt_method_shipping, edt_method_shipping2, edt_method_shipping3;
    ArrayList<ItemPicker> list_item_condition, list_item_country, list_item_method, list_item_method2, list_item_method3;

    final HashMap<String, String> hasmap_condition = new HashMap<String, String>() {{
        put("new_product","全新");
        put("used_product","二手");
    }};

    public RsProduct getMyProduct() {
        return myProduct;
    }

    public void setMyProduct(RsProduct myProduct) {
        this.myProduct = myProduct;
    }

    public DialogBidShop(Activity a, long ID, long tenderID, Boolean isReoffer, String couterOfferPrice, Boolean isOffered) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.idProduct = ID;
        this.tenderID = tenderID;
        this.isReoffer = isReoffer;
        this.couterOfferPrice = couterOfferPrice;
        this.isOffered = isOffered;
        if(c instanceof MainActivity){
            ((MainActivity) c).setListenerKeyboard(DialogBidShop.this);
        }

    }

    @Override
    protected void onStop() {
        //c.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_shopbid);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        contentRootView = (LinearLayout) findViewById(R.id.contentRootView);
        ll_content_image = (CustomViewListImage) findViewById(R.id.ll_list_image);
        add_offer_method = (Button) findViewById(R.id.add_offer_method);
        //remove_offer_method = (Button) findViewById(R.id.add_offer_method);
        ll_content_image.initView();
        bt_save_offer = (Button) findViewById(R.id.bt_save_offer);
        sv_product = (ScrollView) findViewById(R.id.sv_product);
        sp_choose_country = (Spinner) findViewById(R.id.sp_choose_country);
        sp_choose_condition_product = (Spinner) findViewById(R.id.sp_condition_product);
        sp_choose_shipping_method = (TextView) findViewById(R.id.sp_method_shipping);
        sp_choose_shipping_method_2 = (TextView) findViewById(R.id.sp_method_shipping_2);
        sp_choose_shipping_method_3 = (TextView) findViewById(R.id.sp_method_shipping_3);
        edtOfferPrice = (CustomEditextSoftkey) findViewById(R.id.edt_offer_price);
        edtShippingCost = (CustomEditextSoftkey) findViewById(R.id.edt_shipping_cost);
        edtShippingCost2 = (CustomEditextSoftkey) findViewById(R.id.edt_shipping_cost2);
        edtShippingCost3 = (CustomEditextSoftkey) findViewById(R.id.edt_shipping_cost3);
        edtExpectedArrivalDays = (CustomEditextSoftkey) findViewById(R.id.edt_expected_arrival_days);
        edtExpectedArrivalDays_2 = (CustomEditextSoftkey) findViewById(R.id.edt_expected_arrival_days_2);
        edtExpectedArrivalDays_3 = (CustomEditextSoftkey) findViewById(R.id.edt_expected_arrival_days_3);
        edtRemarks = (CustomEditextSoftkey) findViewById(R.id.edt_remarks);
//        tvCondition = (TextView) findViewById(R.id.tvCondition);
        tvNameProduct = (TextView) findViewById(R.id.tv_name_product);
        remove_offer_method_1 = (Button) findViewById(R.id.remove_offer_method_1);
        remove_offer_method_2 = (Button) findViewById(R.id.remove_offer_method_2);
        remove_offer_method_3 = (Button) findViewById(R.id.remove_offer_method_3);
        lineShipping1 = (LinearLayout) findViewById(R.id.lineShipping1);
        lineShipping2 = (LinearLayout) findViewById(R.id.lineShipping2);
        lineShipping3 = (LinearLayout) findViewById(R.id.lineShipping3);
        fl_message = (LinearLayout) findViewById(R.id.fl_message);
        tv_message = (TextView) findViewById(R.id.tv_message);
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        ll_button = (LinearLayout) findViewById(R.id.ll_button);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);

        edt_choose_condition = (EditText) findViewById(R.id.edt_choose_condition);
        list_item_condition = new ArrayList<>();
        for(Map.Entry entry : hasmap_condition.entrySet()){
            list_item_condition.add(new ItemPicker(entry.getKey().toString(),entry.getValue().toString(),false));
        }
        edt_choose_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDropDownCondition();
            }
        });
        if(list_item_condition.size() > 0){
            edt_choose_condition.setText(list_item_condition.get(0).getName());
            list_item_condition.get(0).setChecked(true);
        }


        edt_choose_country = (EditText) findViewById(R.id.edt_choose_country);
        HashMap<String, String> mapCountry = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_COUNTRY);
        list_item_country = new ArrayList<>();
        list_item_country.add(new ItemPicker("-1","请选选择国家", false));
        for (Map.Entry entry : mapCountry.entrySet()){
            ItemPicker item_choose = new ItemPicker(entry.getKey().toString(), entry.getValue().toString(), false);
            list_item_country.add(item_choose);
        }
        edt_choose_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDropDownCountry();
            }
        });
        edt_choose_country.setText(list_item_country.get(0).getName());
        list_item_country.get(0).setChecked(true);


        edt_method_shipping = (EditText) findViewById(R.id.edt_method_shipping);
        final HashMap<String, String> hashmapShippingMethod = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_SHIPPING_METHOD);
        list_item_method = new ArrayList<>();
        for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
            list_item_method.add(new ItemPicker(entry.getKey().toString(), entry.getValue().toString(), false));
        }
        edt_method_shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDropDownMethod1();
            }
        });

        if(list_item_method.size() > 0){
            list_item_method.get(0).setChecked(true);
            edt_method_shipping.setText(list_item_method.get(0).getName());
        }


        edt_method_shipping2 = (EditText) findViewById(R.id.edt_method_shipping2);
        list_item_method2 = new ArrayList<>();
        for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
            list_item_method2.add(new ItemPicker(entry.getKey().toString(), entry.getValue().toString(), false));
        }
        edt_method_shipping2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDropDownMethod2();
            }
        });
        if(list_item_method2.size() > 0){
            list_item_method2.get(0).setChecked(true);
            edt_method_shipping2.setText(list_item_method2.get(0).getName());
        }

        edt_method_shipping3 = (EditText) findViewById(R.id.edt_method_shipping3);
        list_item_method3 = new ArrayList<>();
        for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
            list_item_method3.add(new ItemPicker(entry.getKey().toString(), entry.getValue().toString(), false));
        }
        edt_method_shipping3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDropDownMethod3();
            }
        });
        if(list_item_method3.size() > 0){
            list_item_method3.get(0).setChecked(true);
            edt_method_shipping3.setText(list_item_method3.get(0).getName());
        }


        spListShippingMethod = new ArrayList<>();
        spListCondition = new ArrayList<>();
        spListCountry = new ArrayList<>();
        listShipping = new ArrayList<>();
        listButtonRemove = new ArrayList<>();
        listShipping.add(lineShipping1);
        listShipping.add(lineShipping2);
        listShipping.add(lineShipping3);
        listButtonRemove.add(remove_offer_method_1);
        listButtonRemove.add(remove_offer_method_2);
        listButtonRemove.add(remove_offer_method_3);

//        image_click = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent it = ll_content_image.getImageClick(view,c);
//                c.startActivityForResult(it,REQUEST_IMAGE_CODE);
//            }
//        };
//        ll_content_image.setMyClick(image_click,false);
        ll_title.getTop();
        ll_title.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if(ll_title.getHeight() > 10 && top_component_height == 0){
                    top_component_height = ll_title.getHeight();
                }
            }
        });
        ll_button.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


                if(ll_button.getHeight() > 10 && bottom_component_height == 0){
                    bottom_component_height = ll_button.getHeight();
                }
            }
        });
        edtOfferPrice.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        edtShippingCost.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        edtShippingCost2.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        edtShippingCost3.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        WindowManager manager =  (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        long_dimention = point.y;
        //set shipping cost
        sv_product.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(isSetHeightSv == false){
                    ViewGroup.LayoutParams layoutParams = sv_product.getLayoutParams();
                    scroll_height = (int) (long_dimention*0.5);
                    layoutParams.height = (int) (long_dimention*0.5);
                    sv_product.setLayoutParams(layoutParams);
                    isSetHeightSv = true;
                }

            }
        });
        edtShippingCost.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String strValue = edtShippingCost.getText().toString().replaceAll("¥", "");
                if (!b) {
                    if (!strValue.equals("")) {
                        String str = "¥" + strValue;
                        edtShippingCost.setText(str);
                    }
                } else {
                    if (!strValue.equals("")) {
                        edtShippingCost.setText(strValue);
                    }
                }
            }
        });

        edtShippingCost2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String strValue = edtShippingCost2.getText().toString().replaceAll("¥", "");
                if (!b) {
                    if (!strValue.equals("")) {
                        String str = "¥" + strValue;
                        edtShippingCost2.setText(str);
                    }
                } else {
                    if (!strValue.equals("")) {
                        edtShippingCost2.setText(strValue);
                    }
                }
            }
        });

        edtShippingCost3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String strValue = edtShippingCost3.getText().toString().replaceAll("¥", "");
                if (!b) {
                    if (!strValue.equals("")) {
                        String str = "¥" + strValue;
                        edtShippingCost3.setText(str);
                    }
                } else {
                    if (!strValue.equals("")) {
                        edtShippingCost3.setText(strValue);
                    }
                }
            }
        });

        //set expected arrival day
        edtExpectedArrivalDays.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                String str = edtExpectedArrivalDays.getText().toString().replaceAll("天", "");
                if (!hasFocus) {
                    if (!str.equals("")) {
                        str += "天";
                        edtExpectedArrivalDays.setText(str);
                    }
                } else {
                    if (!str.equals("")) {
                        edtExpectedArrivalDays.setText(str);
                    }
                }
            }
        });

        edtExpectedArrivalDays_2.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                String str = edtExpectedArrivalDays_2.getText().toString().replaceAll("天", "");
                if (!hasFocus) {
                    if (!str.equals("")) {
                        str += "天";
                        edtExpectedArrivalDays_2.setText(str);
                    }
                } else {
                    if (!str.equals("")) {
                        edtExpectedArrivalDays_2.setText(str);
                    }
                }
            }
        });

        edtExpectedArrivalDays_3.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                String str = edtExpectedArrivalDays_3.getText().toString().replaceAll("天", "");
                if (!hasFocus) {
                    if (!str.equals("")) {
                        str += "天";
                        edtExpectedArrivalDays_3.setText(str);
                    }
                } else {
                    if (!str.equals("")) {
                        edtExpectedArrivalDays_3.setText(str);
                    }
                }
            }
        });

        //spListCondition.add("请标明状态");
        for (int i = 0; i < getContext().getResources().getStringArray(R.array.list_condition).length; i++) {
            spListCondition.add(getContext().getResources().getStringArray(R.array.list_condition)[i]);
        }
        final ArrayAdapter<String> adapter_condition = new ArrayAdapter<String>(this.getContext(), R.layout.list_item_spinner_custom_size, spListCondition) {
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, DialogBidShop.this.getContext().getResources().getDisplayMetrics());
                    tv.setPadding(size, 0, 0, 0);
                    tv.setTextColor(Color.BLACK);
                    tv.setEnabled(true);
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    tv.setTextColor(Color.BLACK);
                    tv.setEnabled(true);
                }
                return view;
            }
        };
        adapter_condition.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        sp_choose_condition_product.setAdapter(adapter_condition);

        //spListCountry = Arrays.asList(this.getContext().getResources().getStringArray(R.array.list_country_product));
        //spListCountry.remove(0);
        spListCountry.add("请选选择国家");
        HashMap<String, String> hashmapCountry = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_COUNTRY);
        if (hashmapCountry != null) {
            for (Map.Entry entry : hashmapCountry.entrySet()) {
                spListCountry.add(entry.getValue().toString());
            }
        }

        final ArrayAdapter<String> adapter_country = new ArrayAdapter<String>(this.getContext(), R.layout.list_item_spinner_custom_size, spListCountry) {
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
//                    if (position == 0) {
//                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.dialog_text_hint));
//                    } else {
//                        tv.setTextColor(Color.BLACK);
//                    }
                    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, DialogBidShop.this.getContext().getResources().getDisplayMetrics());
                    tv.setPadding(size, 0, 0, 0);
                    tv.setTextColor(Color.BLACK);
                    tv.setEnabled(true);
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.dialog_text_hint));
                        tv.setEnabled(false);
                    } else {
                        tv.setTextColor(Color.BLACK);
                        tv.setEnabled(true);
                    }
//                    tv.setTextColor(Color.BLACK);
//                    tv.setEnabled(true);
                }
                return view;
            }
        };
        adapter_country.setDropDownViewResource(android.R.layout.simple_list_item_1);
        sp_choose_country.setSelection(0);
        sp_choose_country.setAdapter(adapter_country);
        if (hashmapShippingMethod != null) {
            for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
                spListShippingMethod.add(entry.getValue().toString());
            }
        }
//        spListShippingMethod = Arrays.asList(this.getContext().getResources().getStringArray(R.array.list_method_shipping));
        final ArrayAdapter<String> adapter_method_shipping = new ArrayAdapter<String>(this.getContext(), R.layout.list_item_spinner_custom_size, spListShippingMethod);
        for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
            if (spListShippingMethod.get(0).equals(entry.getValue().toString())) {
                idShippingMethod = Integer.parseInt(entry.getKey().toString());
            }
        }
        if (spListShippingMethod.size() > 0) {
            sp_choose_shipping_method.setText(spListShippingMethod.get(0).toString());
        }
        sp_choose_shipping_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogCategory = new AlertDialog.Builder(getContext());
                final ListView lvShippingMethod = new ListView(getContext());
                //adapter_method_shipping.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                lvShippingMethod.setAdapter(adapter_method_shipping);
                dialogCategory.setView(lvShippingMethod);
                final AlertDialog alertDialog = dialogCategory.create();
                alertDialog.show();
                lvShippingMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
                            if (lvShippingMethod.getItemAtPosition(i).equals(entry.getValue().toString())) {
                                idShippingMethod = Integer.parseInt(entry.getKey().toString());
                            }
                        }
                        sp_choose_shipping_method.setText(spListShippingMethod.get(i).toString());
                        alertDialog.dismiss();
                    }
                });
            }
        });

        for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
            if (spListShippingMethod.get(0).equals(entry.getValue().toString())) {
                idShippingMethod2 = Integer.parseInt(entry.getKey().toString());
            }
        }
        if (spListShippingMethod.size() > 0) {
            sp_choose_shipping_method_2.setText(spListShippingMethod.get(0).toString());
        }
        sp_choose_shipping_method_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogCategory = new AlertDialog.Builder(getContext());
                final ListView lvShippingMethod = new ListView(getContext());
                //adapter_method_shipping.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                lvShippingMethod.setAdapter(adapter_method_shipping);
                dialogCategory.setView(lvShippingMethod);
                final AlertDialog alertDialog = dialogCategory.create();
                alertDialog.show();
                lvShippingMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
                            if (lvShippingMethod.getItemAtPosition(i).equals(entry.getValue().toString())) {
                                idShippingMethod2 = Integer.parseInt(entry.getKey().toString());
                            }
                        }
                        sp_choose_shipping_method_2.setText(spListShippingMethod.get(i).toString());
                        alertDialog.dismiss();
                    }
                });
            }
        });

        for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
            if (spListShippingMethod.get(0).equals(entry.getValue().toString())) {
                idShippingMethod3 = Integer.parseInt(entry.getKey().toString());
            }
        }
        if (spListShippingMethod.size() > 0) {
            sp_choose_shipping_method_3.setText(spListShippingMethod.get(0));
        }
        sp_choose_shipping_method_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogCategory = new AlertDialog.Builder(getContext());
                final ListView lvShippingMethod = new ListView(getContext());
                //adapter_method_shipping.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                lvShippingMethod.setAdapter(adapter_method_shipping);
                dialogCategory.setView(lvShippingMethod);
                final AlertDialog alertDialog = dialogCategory.create();
                alertDialog.show();
                lvShippingMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
                            if (lvShippingMethod.getItemAtPosition(i).equals(entry.getValue().toString())) {
                                idShippingMethod3 = Integer.parseInt(entry.getKey().toString());
                            }
                        }
                        sp_choose_shipping_method_3.setText(spListShippingMethod.get(i).toString());
                        alertDialog.dismiss();
                    }
                });
            }
        });
        tvNameProduct.setText(myProduct.getName());
//        if (myProduct != null) {
//            tvNameProduct.setText(myProduct.getName());
//
//            if (myProduct.getShipping_methods() != null && myProduct.getShipping_methods().size() > 0) {
//                LinearLayout viewListShipMathod = (LinearLayout) findViewById(R.id.list_Shipmethod);
//                for (int i = 0; i < myProduct.getShipping_methods().size(); i++) {
//                    View viewItemShipMethod = ((LayoutInflater) DialogBidShop.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_shipmethod, null, false);
//                    TextView tvShipmethod = (TextView) viewItemShipMethod.findViewById(R.id.tvShipmethod);
//                    tvShipmethod.setText((i + 1) + "." + myProduct.getShipping_methods().get(i).getName());
//                    if (i % 2 != 0) {
//                        viewItemShipMethod.setBackgroundResource(R.color.dark_color_dialog);
//                    } else {
//                        viewItemShipMethod.setBackgroundResource(R.color.background_color_dialog);
//                    }
//                    viewListShipMathod.addView(viewItemShipMethod);
//                }
//
//            }
//
//        }
        listButtonRemove.get(0).setVisibility(View.INVISIBLE);
        add_offer_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMethodRow();
            }
        });


        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            listButtonRemove.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                        for(ItemPicker itemPicker : list_item_method){
                            if(itemPicker.getChecked() == true){
                                itemPicker.setChecked(false);
                            }
                        }
                        if(list_item_method.size()>0){
                            list_item_method.get(0).setChecked(true);
                            edt_method_shipping.setText(list_item_method.get(0).getName());
                        }

                        edtShippingCost.setText("");
                        edtExpectedArrivalDays.setText("");
                    }
                    if (finalI == 1) {
                        for(ItemPicker itemPicker : list_item_method2){
                            if(itemPicker.getChecked() == true){
                                itemPicker.setChecked(false);
                            }
                        }
                        if(list_item_method2.size()>0){
                            list_item_method2.get(0).setChecked(true);
                            edt_method_shipping2.setText(list_item_method2.get(0).getName());
                        }
                        edtShippingCost2.setText("");
                        edtExpectedArrivalDays_2.setText("");
                    }
                    if (finalI == 2) {
                        for(ItemPicker itemPicker : list_item_method3){
                            if(itemPicker.getChecked() == true){
                                itemPicker.setChecked(false);
                            }
                        }
                        if(list_item_method3.size()>0){
                            list_item_method3.get(0).setChecked(true);
                            edt_method_shipping3.setText(list_item_method3.get(0).getName());
                        }
                        edtShippingCost3.setText("");
                        edtExpectedArrivalDays_3.setText("");
                    }
                    DialogBidShop.this.hideMethodRow(finalI);
                }
            });
        }

        bt_save_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_save_offer.setEnabled(false);
                listShippingMethod = new ArrayList<>();
                listOfferPrice = new ArrayList<>();
                listShippingCost = new ArrayList<>();
                listExpectedArrivalDays = new ArrayList<>();
                listRemarks = new ArrayList<>();
                listReoffer = new ArrayList<>();
                listImages = new ArrayList<>();
                countryID = 0;
                Map<String, MultipartBody.Part> map = ll_content_image.getBitmapData(DialogBidShop.this.c);
                List<MultipartBody.Part> list_image = new ArrayList<>(map.values());
                Collections.reverse(list_image);
                String strCondition = "";

                for(ItemPicker itemPicker : list_item_condition){
                    if(itemPicker.getChecked() == true){
                        strCondition = itemPicker.getId();
                        break;
                    }
                }

                for(ItemPicker itemPicker : list_item_country){
                    if(itemPicker.getChecked() == true && Integer.valueOf(itemPicker.getId()) != null){
                        countryID = Integer.valueOf(itemPicker.getId());
                        break;
                    }
                }



                float ShippingMethod1 = 0;
                for (ItemPicker item : list_item_method) {
                    if (item.getChecked() == true) {
                        ShippingMethod1 = Float.parseFloat(item.getId());
                        break;
                    }
                }

                float ShippingMethod2 = 0;
                for (ItemPicker item : list_item_method2) {
                    if (item.getChecked() == true) {
                        ShippingMethod2 = Float.parseFloat(item.getId());
                        break;
                    }
                }

                float ShippingMethod3 = 0;
                for (ItemPicker item : list_item_method3) {
                    if (item.getChecked() == true) {
                        ShippingMethod3 = Float.parseFloat(item.getId());
                        break;
                    }
                }

                try {
                    String strOfferPrice = "";
                    strOfferPrice = edtOfferPrice.getText().toString();
//                    float valueOfferPrice = Float.parseFloat(strOfferPrice);
                    listOfferPrice.add(strOfferPrice);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String strRemarks = edtRemarks.getText().toString();
                listRemarks.add(strRemarks);

                String strExpectArrivalDay = "";
                try {
                    if (!edtExpectedArrivalDays.getText().toString().equals("")) {
                        strExpectArrivalDay = edtExpectedArrivalDays.getText().toString().replaceAll("天", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String strExpectArrivalDay2 = "";
                try {
                    if (!edtExpectedArrivalDays_2.getText().toString().equals("")) {
                        strExpectArrivalDay2 = edtExpectedArrivalDays_2.getText().toString().replaceAll("天", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String strExpectArrivalDay3 = "";
                try {
                    if (!edtExpectedArrivalDays_3.getText().toString().equals("")) {
                        strExpectArrivalDay3 = edtExpectedArrivalDays_3.getText().toString().replaceAll("天", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String strShippingCost = "";
                try {
                    if (!edtShippingCost.getText().toString().equals("")) {
                        strShippingCost = edtShippingCost.getText().toString().replaceAll("¥", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String strShippingCost2 = "";
                try {
                    if (!edtShippingCost2.getText().toString().equals("")) {
                        strShippingCost2 = edtShippingCost2.getText().toString().replaceAll("¥", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String strShippingCost3 = "";
                try {
                    if (!edtShippingCost3.getText().toString().equals("")) {
                        strShippingCost3 = edtShippingCost3.getText().toString().replaceAll("¥", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (lineShipping1.getVisibility() == View.VISIBLE) {
                    listShippingCost.add(strShippingCost);
                    listExpectedArrivalDays.add(strExpectArrivalDay);
                    listShippingMethod.add(ShippingMethod1);
                }
                if (lineShipping2.getVisibility() == View.VISIBLE) {
                    listShippingCost.add(strShippingCost2);
                    listExpectedArrivalDays.add(strExpectArrivalDay2);
                    listShippingMethod.add(ShippingMethod2);
                }
                if (lineShipping3.getVisibility() == View.VISIBLE) {
                    listShippingCost.add(strShippingCost3);
                    listExpectedArrivalDays.add(strExpectArrivalDay3);
                    listShippingMethod.add(ShippingMethod3);
                }

                listReoffer.add(Boolean.FALSE);
                LinkedHashMap<String, String> mapMethodShipping = getMapformList(listShippingMethod, "product_tender_shippings_attributes[", "][shipping_method_id]");
                Map<String, String> mapOfferPrice = getMapformListString(listOfferPrice, "product_offers_attributes[", "][offer_price]");
                Map<String, String> mapShippingCost = getMapformListString(listShippingCost, "product_tender_shippings_attributes[", "][shipping_cost]");
                Map<String, String> mapExpectArrivalDay = getMapformListString(listExpectedArrivalDays, "product_tender_shippings_attributes[", "][expected_arrival_days]");
                Map<String, String> mapRemarks = getMapformListString(listRemarks, "product_offers_attributes[", "][remarks]");
                Map<String, Boolean> mapReoffer = getMapformListBoolean(listReoffer, "product_offers_attributes[", "][reoffer]");
                Map<String, String> mapProductImages = getMapformList(listImages, "product_images_attributes[", "][product_image]");
//                    fl_message.setVisibility(View.GONE);
//                    if (idProduct != 0 && strConditionConvert != "" && countryID != 0 && !mapOfferPrice.isEmpty()
//                            && !mapReoffer.isEmpty() && !mapMethodShipping.isEmpty() && !mapExpectArrivalDay.isEmpty()) {
                final ProgressDialog progressDialog = Utils.createProgressDialog(c);
                ApiServices api = ApiServices.getInstance();
                WebService ws = api.getRetrofit().create(WebService.class);
                Call<ResponseMessage<ProductOffer>> call;
                String str_country = countryID > 0 ? String.valueOf(countryID) : "";
                if (ll_content_image.getList_image_data().size() == 0) {
                    call = ws.actionPostOffer(idProduct, strCondition, str_country, tenderID, false, mapOfferPrice,
                            mapRemarks, mapReoffer, mapMethodShipping, mapShippingCost, mapExpectArrivalDay);
                } else {
                    call = ws.actionPostOffer(idProduct, strCondition, str_country, tenderID, false, mapOfferPrice,
                            mapRemarks, mapReoffer, mapMethodShipping, mapShippingCost, mapExpectArrivalDay, list_image);
                }

                call.enqueue(new Callback<ResponseMessage<ProductOffer>>() {
                    @Override
                    public void onResponse(Call<ResponseMessage<ProductOffer>> call, Response<ResponseMessage<ProductOffer>> response) {
                        try {
                            if (response.body() != null) {
                                if (response.body().isSuccess()) {
                                    ResponseMessage message = response.body();
//                                            Toast.makeText(getContext(), "成功投标", Toast.LENGTH_SHORT).show();
                                    if (f instanceof ProductDetailFragment) {
                                        ProductDetailFragment productDetail = (ProductDetailFragment) f;
                                        productDetail.setView(productDetail.getProduct_id());
                                        progressDialog.dismiss();
                                        dismiss();
                                        fl_message.setVisibility(View.GONE);
                                    }
                                }
                            } else if (response.errorBody() != null) {
                                try {
                                    ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                                    ErrorMessage error = responseMessage.getErrors();
                                    ArrayList<String> list_error = responseMessage.getError_messages();
                                    String strError = "";
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
                                        fl_message.setVisibility(View.VISIBLE);
                                        tv_message.setText(strError);
                                    }
//                                            fl_message.setVisibility(View.VISIBLE);
//                                            tv_message.setText(error.getStringErrFormList());
                                    // Toast.makeText(getInstance(), "Can't post data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getInstance(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                }
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                            } else {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(getInstance(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Utils.crashLog(e);
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        }
                        bt_save_offer.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage<ProductOffer>> call, Throwable t) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        bt_save_offer.setEnabled(true);
                        Utils.OnFailException(t);
                    }
                });
//                    }
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public String CheckField() {
        String strCheckFill = "";
        if (!edtOfferPrice.getText().toString().equals("")
                && (countryID != 0)
                && (!edtShippingCost.getText().toString().equals(""))
                && (!edtExpectedArrivalDays.getText().toString().equals(""))) {
            return strCheckFill;
        } else {
            if (edtOfferPrice.getText().toString().equals("")) {
                strCheckFill += DialogBidShop.this.getContext().getResources().getString(R.string.dialog_bid_price_product) + "\n";
            }
            if (edtShippingCost.getText().toString().equals("")) {
                strCheckFill += DialogBidShop.this.getContext().getResources().getString(R.string.dialog_bid_shipping_cost) + "\n";
            }
            if (edtExpectedArrivalDays.getText().toString().equals("")) {
                strCheckFill += DialogBidShop.this.getContext().getResources().getString(R.string.dialog_bid_estimated_delivery_time) + "\n";
            }
            if (countryID == 0) {
                strCheckFill += DialogBidShop.this.getContext().getResources().getString(R.string.dialog_bid_country) + "\n";
            }
            return strCheckFill;

        }
    }

    public void hideMethodRow(int position) {

        listShipping.get(position).setVisibility(View.GONE);
//        listLine--;
//        if (listLine == 1) {
//            for (int i = 0; i < listShipping.size(); i++) {
//                if (listShipping.get(i).getVisibility() == View.VISIBLE) {
//                    listButtonRemove.get(i).setVisibility(View.INVISIBLE);
//                    break;
//                }
//            }
//        }
    }

    private void addMethodRow() {
        for (int i = 0; i < 3; i++) {
            if (listShipping.get(i).getVisibility() == View.GONE) {
                listShipping.get(i).setVisibility(View.VISIBLE);
//                listButtonRemove.get(i).setVisibility(View.VISIBLE);
//                for (int j = 0; j < 3; j++) {
//                    if (listShipping.get(j).getVisibility() == View.VISIBLE) {
//                        listButtonRemove.get(j).setVisibility(View.VISIBLE);
//                    }
//                }
//                listLine++;
                break;
            }
        }
    }

    public CustomViewListImage get_content_image() {
        return ll_content_image;
    }

    public void setImage(Intent data, View.OnClickListener click) {
        ll_content_image.getActivityRs(data, click);
    }

    public void setImage_click(View.OnClickListener click) {
        image_click = click;
    }

    public ScrollView getSv_product() {
        return sv_product;
    }

    private LinkedHashMap<String, String> getMapformList(List<Float> list, String from, String end) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (list.size() == 0) {
            return map;
        } else {
            for (int i = 0; i < list.size(); i++) {
                String key = from + i + end;
                if (list.get(i) > 0) {
                    String value = String.valueOf(list.get(i));
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    private LinkedHashMap<String, String> getMapformListInteger(List<Integer> list, String from, String end) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (list.size() == 0) {
            return map;
        } else {
            for (int i = 0; i < list.size(); i++) {
                String key = from + i + end;
                if (list.get(i) > 0) {
                    String value = String.valueOf(list.get(i));
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    private Map<String, Float> getMapformListParseNumber(List<Float> list, String from, String end) {
        Map<String, Float> map = new HashMap<>();
        if (list.size() == 0) {
            return map;
        } else {
            for (int i = 0; i < list.size(); i++) {
                String key = from + i + end;
                if (list.get(i) > 0) {
                    Float value = list.get(i);
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    private Map<String, String> getMapformListString(List<String> list, String from, String end) {
        Map<String, String> map = new HashMap<>();
        if (list.size() == 0) {
            return map;
        } else {
            for (int i = 0; i < list.size(); i++) {
                String key = from + i + end;
                String value = list.get(i);
                map.put(key, value);
            }
        }
        return map;
    }

    private Map<String, Boolean> getMapformListBoolean(List<Boolean> list, String from, String end) {
        Map<String, Boolean> map = new HashMap<>();
        if (list.size() == 0) {
            return map;
        } else {
            for (int i = 0; i < list.size(); i++) {
                String key = from + i + end;
                Boolean value = list.get(i);
                map.put(key, value);
            }
        }
        return map;
    }

    @Override
    public void actionWhenHideKeyboard() {
       // if(isShowKeyboard == true){
        View parentView = contentRootView.getRootView();
        Point point = Utils.getLocationOnScreen(ll_title);
        ll_title.getTop();
            ViewGroup.LayoutParams layoutParams = sv_product.getLayoutParams();
            layoutParams.height = scroll_height;

        //Set layout
            sv_product.setLayoutParams(layoutParams);
//      //  }
        isShowKeyboard = false;
    }

    @Override
    public void actionWhenShowKeyboard() {
       // if(isShowKeyboard == false){
        View parentView = contentRootView.getRootView();
        Point point = Utils.getLocationOnScreen(ll_title);
         double screen_inch = CustomApp.getInstance().getInch();

        int scale_by_screen_size = 0;
        DisplayMetrics metrics = c.getResources().getDisplayMetrics();
        int densityDpi = (int)(metrics.density * 160f);
        if(screen_inch >= 8){
            scale_by_screen_size =  70*densityDpi/320;

        }
        else if(screen_inch >= 6){
            scale_by_screen_size = 50*densityDpi/320;
        }
        else{
            scale_by_screen_size = 20*densityDpi/320;
        }
        if(long_dimention > 2200 && (densityDpi > 570 || densityDpi < 500)){
            scale_by_screen_size*=2.55;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
            ViewGroup.LayoutParams layoutParams = sv_product.getLayoutParams();
            int height_out_bottom = (long_dimention - scroll_height - top_component_height - bottom_component_height) / 2;
            int height_full = long_dimention - BaseActivity.keyboard_height -height_out_bottom;
            //int new_height = scroll_height - BaseActivity.keyboard_height + height_out_bottom + scale_by_screen_size;
            int new_height = height_full - top_component_height - bottom_component_height;
            if(DialogBidShop.this.c instanceof MainActivity){
                new_height += ((MainActivity)c).getBottom_bar_height();
            }

            layoutParams.height = (int) (new_height + scale_by_screen_size);

            sv_product.setLayoutParams(layoutParams);

            sv_product.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Point point = Utils.getLocationOnScreen(ll_button);
                    int diff_bot_key = long_dimention - BaseActivity.keyboard_height - point.y + bottom_component_height;

                    sv_product.scrollTo((int) sv_product.getX(), sv_product.getScrollY() + 10);
                }
            }, 1000);

     //   }
        isShowKeyboard = true;
    }

    private void showDialogDropDownCondition(){

        DialogDropdown dropdown = new DialogDropdown(this.c, false, list_item_condition, edt_choose_condition);

        dropdown.setCanceledOnTouchOutside(true);
        dropdown.show();

        Display display = this.c.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dropdown.getWindow().getAttributes());

        layoutParams.width = size.x*4/5;
        layoutParams.height = size.y*4/5;
        dropdown.getWindow().setAttributes(layoutParams);
    }

    private void showDialogDropDownCountry(){

        DialogDropdown dropdown = new DialogDropdown(this.c, false, list_item_country, edt_choose_country);

        dropdown.setCanceledOnTouchOutside(true);
        dropdown.show();

        Display display = this.c.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dropdown.getWindow().getAttributes());

        layoutParams.width = size.x*4/5;
        layoutParams.height = size.y*4/5;
        dropdown.getWindow().setAttributes(layoutParams);
    }

    private void showDialogDropDownMethod1(){
        DialogDropdown dropdown = new DialogDropdown(this.c, false, list_item_method, edt_method_shipping);

        dropdown.setCanceledOnTouchOutside(true);
        dropdown.show();

        Display display = this.c.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dropdown.getWindow().getAttributes());

        layoutParams.width = size.x*4/5;
        layoutParams.height = size.y*4/5;
        dropdown.getWindow().setAttributes(layoutParams);
    }

    private void showDialogDropDownMethod2(){
        DialogDropdown dropdown = new DialogDropdown(this.c, false, list_item_method2, edt_method_shipping2);

        dropdown.setCanceledOnTouchOutside(true);
        dropdown.show();

        Display display = this.c.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dropdown.getWindow().getAttributes());

        layoutParams.width = size.x*4/5;
        layoutParams.height = size.y*4/5;
        dropdown.getWindow().setAttributes(layoutParams);
    }

    private void showDialogDropDownMethod3(){
        DialogDropdown dropdown = new DialogDropdown(this.c, false, list_item_method3, edt_method_shipping3);

        dropdown.setCanceledOnTouchOutside(true);
        dropdown.show();

        Display display = this.c.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dropdown.getWindow().getAttributes());

        layoutParams.width = size.x*4/5;
        layoutParams.height = size.y*4/5;
        dropdown.getWindow().setAttributes(layoutParams);
    }
}

