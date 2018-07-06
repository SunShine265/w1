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
import android.util.Log;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
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
import com.wo1haitao.fragments.MyBidFragment;
import com.wo1haitao.fragments.ProductDetailFragment;
import com.wo1haitao.models.ItemPicker;
import com.wo1haitao.models.OfferMyBidModel;
import com.wo1haitao.models.ProductOffer;
import com.wo1haitao.models.ProductTenderShippings;
import com.wo1haitao.models.ProductTenders;
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

/**
 * Created by goodproductssoft on 7/8/17.
 */

public class DialogRebid extends Dialog implements MainActivity.CallBackKeyboard {

    public Activity c;
    public Fragment f;
    public CustomViewListImage ll_content_image;
    View.OnClickListener image_click;
    ScrollView sv_product;

    //MultiSpinner sp_choose_shipping_method;

    CustomEditextSoftkey edtOfferPrice, edtShippingCost, edtShippingCost2, edtShippingCost3, edtExpectedArrivalDays, edtRemarks, edtExpectedArrivalDays_2, edtExpectedArrivalDays_3;
    TextView tvCondition, tvCountry, sp_choose_shipping_method, sp_choose_shipping_method_2, sp_choose_shipping_method_3, remark_counter_offer;
    List<String> spListCondition;
    ArrayList<String> spListCountry, spListShippingMethod;
    int conditionID, countryID = 1, methodID;
    Button bt_save_offer, add_offer_method, remove_offer_method;
    List<Integer> listImages, listShippingNew, listShippingMethod;
    ArrayList<Integer> listIDImage;
    List<String> listShippingCost, listOfferPrice, listExpectedArrivalDays;
    List<Boolean> listReoffer;
    List<String> listRemarks;
    long idProduct = 0, tenderID = 0;
    Boolean isReoffer, isOffered;
    OfferMyBidModel offerDetail;
    String couterOfferPrice, offerPrice, remarkCounterOffer;
    RsProduct myProduct;
    ArrayList<LinearLayout> listShipping;
    ArrayList<Button> listButtonRemove;
    LinearLayout lineShipping1, lineShipping2, lineShipping3;
    Button remove_offer_method_1, remove_offer_method_2, remove_offer_method_3;
    Integer idShippingMethod = 0, idShippingMethod2 = 0, idShippingMethod3 = 0;
    ArrayList<Integer> listIDShippingMehthod;
    Map<String, String> mapIDShipping;
    Map<String, Integer> mapShippingDestroy;
    int countShipping = 0, indexAdd = 0;

    public ProductTenders getProductTenders() {
        return productTenders;
    }

    public void setProductTenders(ProductTenders productTenders) {
        this.productTenders = productTenders;
    }
    EditText edt_method_shipping1, edt_method_shipping2, edt_method_shipping3;
    ArrayList<ItemPicker> list_item_method1, list_item_method2, list_item_method3;
    ProductTenders productTenders;
    FrameLayout fl_message;
    TextView tv_message;
    LinearLayout ll_button;
    boolean isShowKeyboard = false;
    boolean isSetHeightSv = false;
    int scroll_height = 0;
    int screen_height = 0;
    int top_component_height = 0;
    int bottom_component_height = 0;
    int long_dimention = 0;
    public RsProduct getMyProduct() {
        return myProduct;
    }

    public void setMyProduct(RsProduct myProduct) {
        this.myProduct = myProduct;
    }

    public DialogRebid(Activity a, long ID, long tenderID, Boolean isReoffer, String couterOfferPrice, Boolean isOffered, String offerPrice, String remrakCounterOffer) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.idProduct = ID;
        this.tenderID = tenderID;
        this.isReoffer = isReoffer;
        this.offerPrice = offerPrice;
        this.couterOfferPrice = couterOfferPrice;
        this.isOffered = isOffered;
        this.remarkCounterOffer = remrakCounterOffer;
        if(c instanceof MainActivity){
            ((MainActivity) c).setListenerKeyboard(DialogRebid.this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_rebid);
        ll_content_image = (CustomViewListImage) findViewById(R.id.ll_list_image);
        add_offer_method = (Button) findViewById(R.id.add_offer_method);
        //remove_offer_method = (Button) findViewById(R.id.add_offer_method);
        ll_content_image.initView();
        bt_save_offer = (Button) findViewById(R.id.bt_save_offer);
        sv_product = (ScrollView) findViewById(R.id.sv_product);
        remark_counter_offer = (TextView) findViewById(R.id.remark_counter_offer);
        sp_choose_shipping_method = (TextView) findViewById(R.id.sp_method_shipping);
        sp_choose_shipping_method_2 = (TextView) findViewById(R.id.sp_method_shipping_2);
        sp_choose_shipping_method_3 = (TextView) findViewById(R.id.sp_method_shipping_3);
        edtOfferPrice = (CustomEditextSoftkey) findViewById(R.id.edt_re_offer_price);
        edtShippingCost = (CustomEditextSoftkey) findViewById(R.id.edt_shipping_cost);
        edtShippingCost2 = (CustomEditextSoftkey) findViewById(R.id.edt_shipping_cost2);
        edtShippingCost3 = (CustomEditextSoftkey) findViewById(R.id.edt_shipping_cost3);
        edtExpectedArrivalDays = (CustomEditextSoftkey) findViewById(R.id.edt_expected_arrival_days);
        edtExpectedArrivalDays_2 = (CustomEditextSoftkey) findViewById(R.id.edt_expected_arrival_days_2);
        edtExpectedArrivalDays_3 = (CustomEditextSoftkey) findViewById(R.id.edt_expected_arrival_days_3);
        edtRemarks = (CustomEditextSoftkey) findViewById(R.id.edt_remarks);
        tvCondition = (TextView) findViewById(R.id.tvCondition);
        tvCountry = (TextView) findViewById(R.id.tvCountry);
        remove_offer_method_1 = (Button) findViewById(R.id.remove_offer_method_1);
        remove_offer_method_2 = (Button) findViewById(R.id.remove_offer_method_2);
        remove_offer_method_3 = (Button) findViewById(R.id.remove_offer_method_3);
        lineShipping1 = (LinearLayout) findViewById(R.id.lineShipping1);
        lineShipping2 = (LinearLayout) findViewById(R.id.lineShipping2);
        lineShipping3 = (LinearLayout) findViewById(R.id.lineShipping3);
        fl_message = (FrameLayout) findViewById(R.id.fl_message);
        tv_message = (TextView) findViewById(R.id.tv_message);
        ll_button = (LinearLayout) findViewById(R.id.ll_button);
        WindowManager manager =  (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        long_dimention = point.y;
        sv_product.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(isSetHeightSv == false){
                    ViewGroup.LayoutParams layoutParams = sv_product.getLayoutParams();
                    scroll_height = (int) (long_dimention*0.6);
                    layoutParams.height = (int) (long_dimention*0.6);
                    sv_product.setLayoutParams(layoutParams);
                    isSetHeightSv = true;
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
        edtOfferPrice.setText(this.couterOfferPrice);
        spListShippingMethod = new ArrayList<>();
        spListCondition = new ArrayList<>();
        spListCountry = new ArrayList<>();
        listImages = new ArrayList<>();
        listShipping = new ArrayList<>();
        listButtonRemove = new ArrayList<>();
        listShippingNew = new ArrayList<>();
        listShipping.add(lineShipping1);
        listShipping.add(lineShipping2);
        listShipping.add(lineShipping3);
        listButtonRemove.add(remove_offer_method_1);
        listButtonRemove.add(remove_offer_method_2);
        listButtonRemove.add(remove_offer_method_3);

        edt_method_shipping1 = (EditText) findViewById(R.id.edt_method_shipping1);
        final HashMap<String, String> hashmapShippingMethod = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_SHIPPING_METHOD);
        list_item_method1 = new ArrayList<>();
        for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
            list_item_method1.add(new ItemPicker(entry.getKey().toString(), entry.getValue().toString(), false));
        }
        edt_method_shipping1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDropDownMethod1();
            }
        });


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

        edtOfferPrice.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        edtShippingCost.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        edtShippingCost2.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        edtShippingCost3.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});
        //set shipping cost
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
        //set spinner condition
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

        spListCondition.add("请标明状态");
        for (int i = 0; i < getContext().getResources().getStringArray(R.array.list_condition).length; i++) {
            spListCondition.add(getContext().getResources().getStringArray(R.array.list_condition)[i]);
        }
        final ArrayAdapter<String> adapter_condition = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, spListCondition) {
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.dialog_text_hint));
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
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
                }
                return view;
            }
        };

        HashMap<String, String> hashmapCountry = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_COUNTRY);
        if (hashmapCountry != null) {
            for (Map.Entry entry : hashmapCountry.entrySet()) {
                spListCountry.add(entry.getValue().toString());
            }
        }
        spListCountry.add(0, "新加坡");
        final ArrayAdapter<String> adapter_country = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, spListCountry) {
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.dialog_text_hint));
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
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
                }
                return view;
            }
        };


        if (hashmapShippingMethod != null) {
            for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
                spListShippingMethod.add(entry.getValue().toString());
            }
        }
        for (Map.Entry entry : hashmapShippingMethod.entrySet()) {
            if (spListShippingMethod.get(0).equals(entry.getValue().toString())) {
                idShippingMethod = Integer.parseInt(entry.getKey().toString());
            }
        }
        if (spListShippingMethod.size() > 0) {
            sp_choose_shipping_method.setText(spListShippingMethod.get(0).toString());
        }
        final ArrayAdapter<String> adapter_method_shipping = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, spListShippingMethod);
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
//                        idShippingMethod = i + 1;
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
//                        idShippingMethod2 = i + 1;
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
//                        idShippingMethod3 = i + 1;
                        sp_choose_shipping_method_3.setText(spListShippingMethod.get(i).toString());
                        alertDialog.dismiss();
                    }
                });
            }
        });
        if(productTenders == null){
            for (int i = 0; i < myProduct.getProduct_tenders().size(); i++) {
                if (myProduct.getProduct_tenders().get(i).getId() == this.tenderID) {
                    productTenders = myProduct.getProduct_tenders().get(i);
                }
            }
        }


        //Setview rebid
        ll_content_image.setList_IDImage(new ArrayList<Integer>());
        listIDImage = new ArrayList<>();
        if (myProduct != null) {
            ArrayList<String> list_url_image = new ArrayList<>();
//            if (productTenders.getProduct_offers().size() < 2) {
            for (int i = 0; i < productTenders.getProduct_images().size(); i++) {
                String url = ApiServices.BASE_URI + productTenders.getProduct_images().get(i).getProduct_image().getThumb().getUrl();
                list_url_image.add(url);
                String s = String.valueOf(productTenders.getProduct_images().get(i).getId());
                listIDImage.add(Integer.parseInt(s));
            }
            listIDShippingMehthod = new ArrayList<>();
            for (int j = 0; j < productTenders.getProduct_tender_shippings().size(); j++) {
                listIDShippingMehthod.add(productTenders.getProduct_tender_shippings().get(j).getId());
            }
            listButtonRemove.get(0).setVisibility(View.INVISIBLE);
            if (productTenders.getShipping_methods().size() > 0) {
                ArrayList<ProductTenderShippings> productTenderShippingses = new ArrayList<>(productTenders.getProduct_tender_shippings());
//                Collections.reverse(productTenderShippingses);
                sp_choose_shipping_method.setText(spListShippingMethod.get(productTenderShippingses.get(0).getShipping_method_id() - 1));
                for(ItemPicker itemPicker : list_item_method1){
                    if(String.valueOf(productTenderShippingses.get(0).getShipping_method_id()) != null){
                        if(String.valueOf(productTenderShippingses.get(0).getShipping_method_id()).equals(itemPicker.getId())){
                            edt_method_shipping1.setText(itemPicker.getName());
                            itemPicker.setChecked(true);
                            break;
                        }
                    }
                }
                float fShippingCost1 = Float.parseFloat(productTenderShippingses.get(0).getShipping_cost().getFractional()) / 100;
                edtShippingCost.setText("¥" + String.valueOf(fShippingCost1));
                edtExpectedArrivalDays.setText(String.valueOf(productTenderShippingses.get(0).getExpected_arrival_days()) + "天");
                if (productTenders.getShipping_methods().size() > 1) {
//                    remove_offer_method_1.setVisibility(View.VISIBLE);
//                    remove_offer_method_2.setVisibility(View.VISIBLE);
                    lineShipping2.setVisibility(View.VISIBLE);
                    sp_choose_shipping_method_2.setText(spListShippingMethod.get(productTenderShippingses.get(1).getShipping_method_id() - 1));
                    for(ItemPicker itemPicker : list_item_method2){
                        if(String.valueOf(productTenderShippingses.get(1).getShipping_method_id()) != null){
                            if(String.valueOf(productTenderShippingses.get(1).getShipping_method_id()).equals(itemPicker.getId())){
                                edt_method_shipping2.setText(itemPicker.getName());
                                itemPicker.setChecked(true);
                                break;
                            }
                        }
                    }
                    float fShippingCost2 = Float.parseFloat(productTenderShippingses.get(1).getShipping_cost().getFractional()) / 100;
                    edtShippingCost2.setText("¥" + String.valueOf(fShippingCost2));
                    edtExpectedArrivalDays_2.setText(String.valueOf(productTenderShippingses.get(1).getExpected_arrival_days()) + "天");
                    if (productTenders.getShipping_methods().size() > 2) {
                        remove_offer_method_3.setVisibility(View.VISIBLE);
                        lineShipping3.setVisibility(View.VISIBLE);
                        sp_choose_shipping_method_3.setText(spListShippingMethod.get(productTenderShippingses.get(2).getShipping_method_id() - 1));
                        for(ItemPicker itemPicker : list_item_method3){
                            if(String.valueOf(productTenderShippingses.get(2).getShipping_method_id()) != null){
                                if(String.valueOf(productTenderShippingses.get(2).getShipping_method_id()).equals(itemPicker.getId())){
                                    edt_method_shipping3.setText(itemPicker.getName());
                                    itemPicker.setChecked(true);
                                    break;
                                }
                            }
                        }
                        float fShippingCost = Float.parseFloat(productTenderShippingses.get(2).getShipping_cost().getFractional()) / 100;
                        edtShippingCost3.setText("¥" + String.valueOf(fShippingCost));
                        edtExpectedArrivalDays_3.setText(String.valueOf(productTenderShippingses.get(2).getExpected_arrival_days()) + "天");
                    }
                }
            }
            ll_content_image.setViewFromUrls(list_url_image);
            ll_content_image.setList_IDImage(listIDImage);

            if (myProduct.isNew_product()) {
                tvCondition.setText(CustomApp.getInstance().getResources().getString(R.string.form_new));
            } else {
                tvCondition.setText(CustomApp.getInstance().getResources().getString(R.string.form_used));
            }
            tvCountry.setText(productTenders.getCountry().getName());

            TextView tvCounterPrice = (TextView) findViewById(R.id.tvCounterPrice);
            String number = couterOfferPrice.replaceAll("[^0-9.,]+", "");
            tvCounterPrice.setText(number);
            remark_counter_offer.setText(remarkCounterOffer);
            TextView tvOfferPrice = (TextView) findViewById(R.id.tvPriceInfo);
            tvOfferPrice.setText(offerPrice);
//            if (myProduct.getShipping_methods() != null && myProduct.getShipping_methods().size() > 0) {
//                LinearLayout viewListShipMathod = (LinearLayout) findViewById(R.id.list_Shipmethod);
//                for (int i = 0; i < myProduct.getShipping_methods().size(); i++) {
//                    View viewItemShipMethod = ((LayoutInflater) DialogRebid.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_shipmethod, null, false);
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

        }
        mapIDShipping = new HashMap<>();
        countShipping = productTenders.getProduct_tender_shippings().size() - 1;
        add_offer_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMethodRow();
            }
        });
        listReoffer = new ArrayList<>();
        listReoffer.add(Boolean.TRUE);
        for (int i = 0; i < 6; i++) {
            listShippingNew.add(i, 0);
        }
        for (int i = 0; i < productTenders.getProduct_tender_shippings().size(); i++) {
            listShippingNew.set(i, 1);
        }
        mapShippingDestroy = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            listButtonRemove.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                        for(ItemPicker itemPicker : list_item_method1){
                            itemPicker.setChecked(false);
                        }
                        if(list_item_method1.size() > 0){
                            list_item_method1.get(0).setChecked(true);
                            edt_method_shipping1.setText(list_item_method1.get(0).getName());
                        }
                        sp_choose_shipping_method.setText(spListShippingMethod.get(0));
                        edtShippingCost.setText("");
                        edtExpectedArrivalDays.setText("");
                    }
                    if (finalI == 1) {
                        for(ItemPicker itemPicker : list_item_method2){
                            itemPicker.setChecked(false);
                        }
                        if(list_item_method2.size() > 0){
                            list_item_method2.get(0).setChecked(true);
                            edt_method_shipping2.setText(list_item_method2.get(0).getName());
                        }
                        sp_choose_shipping_method_2.setText(spListShippingMethod.get(0));
                        edtShippingCost2.setText("");
                        edtExpectedArrivalDays_2.setText("");
                    }
                    if (finalI == 2) {
                        for(ItemPicker itemPicker : list_item_method3){
                            itemPicker.setChecked(false);
                        }
                        if(list_item_method3.size() > 0){
                            list_item_method3.get(0).setChecked(true);
                            edt_method_shipping3.setText(list_item_method3.get(0).getName());
                        }
                        sp_choose_shipping_method_3.setText(spListShippingMethod.get(0));
                        edtShippingCost3.setText("");
                        edtExpectedArrivalDays_3.setText("");
                    }

                    if (listShippingNew.get(finalI) == 1) {
                        listShippingNew.set(finalI, 0);
                    } else {
                        listShippingNew.set(countShipping + finalI, 0);
                    }

                    String key;
                    key = "product_tender_shippings_attributes[" + finalI + "][_destroy]";
                    int value = 1;
                    mapShippingDestroy.put(key, value);
                    DialogRebid.this.hideMethodRow(finalI);
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
                listRemarks = new ArrayList<String>();
                Map<String, MultipartBody.Part> map = ll_content_image.getBitmapData(DialogRebid.this.c);
                List<MultipartBody.Part> list_image = new ArrayList<>(map.values());
                //Collections.reverse(list_image);
                //Collections.reverse(listIDImage);
                Integer ShippingMethod1 = 0;
                for(ItemPicker itemPicker : list_item_method1){
                    if(itemPicker.getChecked() == true && Integer.valueOf(itemPicker.getId()) != null){
                        ShippingMethod1 = Integer.valueOf(itemPicker.getId());
                        break;
                    }
                }


                Integer ShippingMethod2 = 0;
                for(ItemPicker itemPicker : list_item_method2){
                    if(itemPicker.getChecked() == true && Integer.valueOf(itemPicker.getId()) != null){
                        ShippingMethod2 = Integer.valueOf(itemPicker.getId());
                        break;
                    }
                }

                Integer ShippingMethod3 = 0;
                for(ItemPicker itemPicker : list_item_method3){
                    if(itemPicker.getChecked() == true && Integer.valueOf(itemPicker.getId()) != null){
                        ShippingMethod3 = Integer.valueOf(itemPicker.getId());
                        break;
                    }
                }

                try {
                    String strOfferPrice = "";
                    strOfferPrice = edtOfferPrice.getText().toString();
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
                final LinkedHashMap<String, String> mapMethodShipping = GetMapFromListShippingInteger(listShippingMethod, "product_tender_shippings_attributes[", "][shipping_method_id]");
                final Map<String, String> mapOfferPrice = getMapformListString(listOfferPrice, "product_offers_attributes[", "][offer_price]");
                final LinkedHashMap<String, String> mapShippingCost = GetMapFromListShippingLinked(listShippingCost, "product_tender_shippings_attributes[", "][shipping_cost]");
                final LinkedHashMap<String, String> mapExpectArrivalDay = GetMapFromListShipping(listExpectedArrivalDays, "product_tender_shippings_attributes[", "][expected_arrival_days]");
                final Map<String, String> mapRemarks = getMapformListString(listRemarks, "product_offers_attributes[", "][remarks]");
                final Map<String, Boolean> mapReoffer = getMapformListBoolean(listReoffer, "product_offers_attributes[", "][reoffer]");
                final LinkedHashMap<String, String> mapShipingID = getMapformList(listIDShippingMehthod, "product_tender_shippings_attributes[", "][id]");
                final LinkedHashMap<String, Integer> mapIDImage = getMapIntegerformList(listIDImage, "product_images_attributes[", "][id]");
//                if (idProduct != 0 && tenderID != 0 && !mapOfferPrice.isEmpty()
//                        && !mapRemarks.isEmpty() && !mapReoffer.isEmpty() && !mapMethodShipping.isEmpty() && !mapExpectArrivalDay.isEmpty()) {
                final ProgressDialog progressDialog = Utils.createProgressDialog(c);
                ApiServices api = ApiServices.getInstance();
                WebService ws = api.getRetrofit().create(WebService.class);
                Call<ResponseMessage<ProductOffer>> call;
                if (ll_content_image.getList_image_data().size() == 0) {
                    call = ws.actionPostOffer(idProduct, tenderID, true, idProduct, mapOfferPrice,
                            mapRemarks, mapReoffer, mapMethodShipping, mapShippingCost, mapExpectArrivalDay, mapShippingDestroy, mapShipingID);
                } else {
                    List<MultipartBody.Part> map_param = new ArrayList<>();
                    for (Map.Entry entry : mapMethodShipping.entrySet()) {
                        map_param.add(Utils.createStringPart(entry.getKey().toString(), entry.getValue().toString()));
                    }
                    for (Map.Entry entry : mapOfferPrice.entrySet()) {
                        map_param.add(Utils.createStringPart(entry.getKey().toString(), entry.getValue().toString()));
                    }
                    for (Map.Entry entry : mapShippingCost.entrySet()) {
                        map_param.add(Utils.createStringPart(entry.getKey().toString(), entry.getValue().toString()));
                    }
                    for (Map.Entry entry : mapExpectArrivalDay.entrySet()) {
                        map_param.add(Utils.createStringPart(entry.getKey().toString(), entry.getValue().toString()));
                    }
                    for (Map.Entry entry : mapRemarks.entrySet()) {
                        map_param.add(Utils.createStringPart(entry.getKey().toString(), entry.getValue().toString()));
                    }
                    for (Map.Entry entry : mapReoffer.entrySet()) {
                        map_param.add(Utils.createStringPart(entry.getKey().toString(), entry.getValue().toString()));
                    }
                    for (Map.Entry entry : mapShipingID.entrySet()) {
                        map_param.add(Utils.createStringPart(entry.getKey().toString(), entry.getValue().toString()));
                    }
                    for (Map.Entry entry : mapShippingDestroy.entrySet()) {
                        map_param.add(Utils.createStringPart(entry.getKey().toString(), entry.getValue().toString()));
                    }
//                    for(Map.Entry entry : mapIDImage.entrySet()){
//                        map_param.add(Utils.createStringPart(entry.getKey().toString(), entry.getValue().toString()));
//                    }
//                    List<MultipartBody.Part> listImage = ll_content_image.getListBitmapData();
//                    ArrayList<MultipartBody.Part> listReserseImage = new ArrayList<MultipartBody.Part>(listImage);
//                    Collections.reverse(listReserseImage);

                    List<MultipartBody.Part> listImage = ll_content_image.getListBitmapDataDelete();
                    ArrayList<MultipartBody.Part> listReserseImage = new ArrayList<MultipartBody.Part>(listImage);
                    Collections.reverse(listReserseImage);
                    call = ws.actionPostOffer(idProduct, tenderID, true, idProduct, map_param, listReserseImage);
                }

                call.enqueue(new Callback<ResponseMessage<ProductOffer>>() {
                    @Override
                    public void onResponse(Call<ResponseMessage<ProductOffer>> call, Response<ResponseMessage<ProductOffer>> response) {
                        try {
                            if (response.body() != null) {
                                if (response.body().isSuccess()) {
                                    ResponseMessage message = response.body();
//                                    Toast.makeText(getContext(), "成功重新招标", Toast.LENGTH_SHORT).show();
                                    if (f instanceof ProductDetailFragment) {
                                        ProductDetailFragment productDetail = (ProductDetailFragment) f;
                                        productDetail.setView(productDetail.getProduct_id());
                                    }
                                    else if (f instanceof MyBidFragment){
                                        MyBidFragment fragment = (MyBidFragment) f;
                                        fragment.GetMyBid(1);
                                    }
                                    fl_message.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    dismiss();
                                }
                            } else if (response.errorBody() != null) {
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
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                            } else {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(CustomApp.getInstance(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Utils.crashLog(e);
                        }
                        bt_save_offer.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage<ProductOffer>> call, Throwable t) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Utils.OnFailException(t);
                        bt_save_offer.setEnabled(true);
                    }
                });
            }
//            }
        });

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
                if (listShippingNew.get(countShipping + indexAdd + 1) != 1) {
                    indexAdd++;
                    listShippingNew.set(countShipping + indexAdd, 1);
                }
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


    private LinkedHashMap<String, String> getMapformList(List<Integer> list, String from, String end) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (list.size() == 0) {
            return map;
        } else {
            for (int i = 0; i < list.size(); i++) {
                String key = from + i + end;
                String value = String.valueOf(list.get(i));
                map.put(key, value);
            }
        }
        return map;
    }

    private LinkedHashMap<String, Integer> getMapIntegerformList(List<Integer> list, String from, String end) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        if (list.size() == 0) {
            return map;
        } else {
            for (int i = 0; i < list.size(); i++) {
                String key = from + i + end;
                map.put(key, list.get(i));
            }
        }
        return map;
    }

    private LinkedHashMap<String, String> GetMapFromListShipping(List<String> list, String from, String end) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (list.size() == 0) {
            return map;
        } else {
            int dem = 0;
            for (int i = 0; i < 6; i++) {
                if (listShippingNew.get(i) == 1) {
                    String key = from + i + end;
                    String value = "";
                    try{
                        value = Integer.parseInt(list.get(dem)) > 0 ? String.valueOf(list.get(dem)) : "";
                    }
                    catch (Exception e){
                        Crashlytics.logException(e);
                    }
                    dem++;
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    private LinkedHashMap<String, String> GetMapFromListShippingInteger(List<Integer> list, String from, String end) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (list.size() == 0) {
            return map;
        } else {
            int dem = 0;
            for (int i = 0; i < 6; i++) {
                if (listShippingNew.get(i) == 1) {
                    String key = from + i + end;
                    String value = list.get(dem) > 0 ? String.valueOf(list.get(dem)) : "";
                    dem++;
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    private LinkedHashMap<String, String> GetMapFromListShippingLinked(List<String> list, String from, String end) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (list.size() == 0) {
            return map;
        } else {
            int dem = 0;
            for (int i = 0; i < 6; i++) {
                if (listShippingNew.get(i) == 1) {
                    String key = from + i + end;
                    String value = "";
                    try{
                        value = Float.parseFloat(list.get(dem)) > 0 ? String.valueOf(list.get(dem)) : "";
                    }
                    catch (Exception e){
                        Log.i("wo1",e.getMessage());
                    }
                    dem++;
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
        ViewGroup.LayoutParams layoutParams = sv_product.getLayoutParams();
        layoutParams.height = scroll_height;
        sv_product.setLayoutParams(layoutParams);
    }

    @Override
    public void actionWhenShowKeyboard() {
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
            scale_by_screen_size*=2.5;
        }
//        if(long_dimention > 2000){
//            scale_by_screen_size = scale_by_screen_size*2;
//        }

        ViewGroup.LayoutParams layoutParams = sv_product.getLayoutParams();
        int height_out_bottom = (long_dimention - scroll_height - bottom_component_height) / 2;
        int height_full = long_dimention - BaseActivity.keyboard_height - height_out_bottom;
        //int new_height = scroll_height - BaseActivity.keyboard_height + height_out_bottom + scale_by_screen_size;
        int new_height = height_full - bottom_component_height;
        int extract_height = 0;
        if(long_dimention > 1800 && long_dimention < 2000){
            extract_height = 30;
        }
        else if(long_dimention > 2100){
            extract_height = 0;
        }
        layoutParams.height = (int) (new_height + extract_height + scale_by_screen_size);
        if(DialogRebid.this.c instanceof MainActivity){
            layoutParams.height += ((MainActivity)c).getBottom_bar_height();
        }
        sv_product.setLayoutParams(layoutParams);
        sv_product.postDelayed(new Runnable() {
            @Override
            public void run() {
                sv_product.scrollTo((int) sv_product.getX(), sv_product.getScrollY() + 10);
            }
        }, 1000);
    }

    private void showDialogDropDownMethod1(){
        DialogDropdown dropdown = new DialogDropdown(this.c, false, list_item_method1, edt_method_shipping1);

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
