package com.wo1haitao.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.wo1haitao.R;
import com.wo1haitao.adapters.AddressListAdapter;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.AddressMeResponseMessage;
import com.wo1haitao.api.response.CountryCodeRs;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsAddress;
import com.wo1haitao.controls.CustomSwitch;
import com.wo1haitao.controls.MultiSpinner;
import com.wo1haitao.dialogs.DialogDropdown;
import com.wo1haitao.models.BillingAddressesModel;
import com.wo1haitao.models.ItemPicker;
import com.wo1haitao.models.OrderModel;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressPayFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    MyCallBack getListener() {
        if (this.getActivity() instanceof MyCallBack) {
            return (MyCallBack) this.getActivity();
        }
        return null;
    }

    ;
    Button bt_to_commit;
    Spinner sp_choose_country;
    EditText et_form_location_address, et_form_province, et_form_post_office_no, et_form_city, edt_choose_country;

    CustomSwitch customSwitch;
    FrameLayout fl_add_adr, fl_default, fl_setdefault;
    View fragment_view, footer_view;
    MultiSpinner mp_country;
    List<Integer> num_list_country;
    List<String> name_list_country;
    Integer country_id = null;
    ActionBarProject my_action_bar;
    OrderModel acceptOrtherModel;
    ArrayList<RsAddress> shippingAddresses = null;
    BillingAddressesModel billingAddressesModels = null;
    ArrayList<String> sp_list_country;
    String flag = "AddressPay";
    ArrayList<CountryCodeRs> mapCountryCode;
    ArrayAdapter<String> adapter_country;
    LinearLayout ln_check_fill;
    TextView tv_check_fill;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<ItemPicker> list_item_country;

    RsAddress primaryShipAddress;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public OrderModel getAcceptOrtherModel() {
        return acceptOrtherModel;
    }

    public void setAcceptOrtherModel(OrderModel acceptOrtherModel) {
        this.acceptOrtherModel = acceptOrtherModel;
    }

    public AddressPayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment_view = inflater.inflate(R.layout.fragment_address_pay, container, false);
        footer_view = ((LayoutInflater) AddressPayFragment.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_address_pay, null, false);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        bt_to_commit = (Button) footer_view.findViewById(R.id.bt_to_commit);

        if (AddressPayFragment.this.flag.equals("AddressPay")) {
            my_action_bar.showTitle(R.string.title_my_addresspay);
            bt_to_commit.setText(R.string.btn_commit_order_address);
        } else {
            bt_to_commit.setText(R.string.btn_commit_address);
            my_action_bar.showTitle(R.string.title_my_addresspay_acceptBid);
        }
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        ListView myListView = (ListView) fragment_view.findViewById(R.id.ll_adress_info);
        myListView.addFooterView(footer_view);
        myListView.setItemsCanFocus(true);
        mapCountryCode = new ArrayList<>();
        num_list_country = new ArrayList<>();
        name_list_country = new ArrayList<>();
        et_form_location_address = (EditText) footer_view.findViewById(R.id.et_form_location_address);
        et_form_province = (EditText) footer_view.findViewById(R.id.et_form_province);
        et_form_post_office_no = (EditText) footer_view.findViewById(R.id.et_form_post_office_no);
        et_form_city = (EditText) footer_view.findViewById(R.id.et_form_city);
        customSwitch = (CustomSwitch) footer_view.findViewById(R.id.customSwitch);
        sp_list_country = new ArrayList<>();
        sp_choose_country = (Spinner) footer_view.findViewById(R.id.sp_choose_country);
        ln_check_fill = (LinearLayout) footer_view.findViewById(R.id.ln_check_fill);
        tv_check_fill = (TextView) footer_view.findViewById(R.id.tv_check_fill);
        swipeRefreshLayout = (SwipeRefreshLayout) fragment_view.findViewById(R.id.swipe_refresh_layout);
        edt_choose_country = (EditText) footer_view.findViewById(R.id.edt_choose_country);
        list_item_country = new ArrayList<>();
        GetCountriesWithCode();
        edt_choose_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDropDownCountry();
            }
        });
        fl_add_adr = (FrameLayout) footer_view.findViewById(R.id.fl_add_adr);
        fl_add_adr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String formlocationaddress, formprovince, formpostofficeno, formcity;

                boolean formaddress;
                String strCheckFill = CheckFill();
                if (!strCheckFill.equals("")) {
                    ln_check_fill.setVisibility(View.VISIBLE);
                    tv_check_fill.setText("请修改以下:" + strCheckFill);
                    return;
                }
                formlocationaddress = et_form_location_address.getText().toString();
                formprovince = et_form_province.getText().toString();
                formpostofficeno = et_form_post_office_no.getText().toString();
                formcity = et_form_city.getText().toString();
                formaddress = customSwitch.isChecked();
                String list_country = "";
                String country = "";
                country = edt_choose_country.getText().toString();
                for (ItemPicker itemPicker : list_item_country){
                    if(itemPicker.getChecked() == true){
                        country = itemPicker.getId();
                    }
                }
                final ProgressDialog progressDialogAdding = Utils.createProgressDialog(AddressPayFragment.this.getActivity());

                ApiServices api = ApiServices.getInstance();
                WebService ws = api.getRetrofit().create(WebService.class);
                ws.actionAddShippingAddress(country, formlocationaddress, null,
                        formprovince, formcity, formpostofficeno, true).enqueue(new Callback<ResponseMessage<RsAddress>>() {
                    @Override
                    public void onResponse(Call<ResponseMessage<RsAddress>> call, Response<ResponseMessage<RsAddress>> response) {
                        try {
                            if (response.body() != null) {
                                ResponseMessage responseMessage = response.body();
                                if (responseMessage.isSuccess()) {
                                    GetAddressMe();
                                    //hintSpinner1.init();
                                    sp_choose_country.setAdapter(adapter_country);
                                    et_form_location_address.setText("");
                                    et_form_province.setText("");
                                    et_form_post_office_no.setText("");
                                    et_form_city.setText("");
                                    customSwitch.setChecked(true);
                                    progressDialogAdding.dismiss();
                                }
                            } else if (response.errorBody() != null) {
                                try {
                                    ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                                    ErrorMessage error = responseMessage.getErrors();
                                    Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                }

                                if (progressDialogAdding != null) {
                                    progressDialogAdding.dismiss();
                                }
                            } else {
                                if (progressDialogAdding != null) {
                                    progressDialogAdding.dismiss();
                                }
                                Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Utils.crashLog(e);
                            if (progressDialogAdding != null) {
                                progressDialogAdding.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage<RsAddress>> call, Throwable t) {
                        if (progressDialogAdding != null) {
                            progressDialogAdding.dismiss();
                        }
                        Utils.OnFailException(t);
                    }
                });
            }
        });

        swipeRefreshLayout.setOnRefreshListener(AddressPayFragment.this);

        bt_to_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getListener() != null && AddressPayFragment.this.getAcceptOrtherModel() != null) {
                    BillingAddressesModel address = new BillingAddressesModel();
                    String country, formlocationaddress, formprovince, formpostofficeno, formcity;
                    country = edt_choose_country.getText().toString();
                    formlocationaddress = et_form_location_address.getText().toString();
                    formprovince = et_form_province.getText().toString();
                    formpostofficeno = et_form_post_office_no.getText().toString();
                    formcity = et_form_city.getText().toString();

                    String strCheckFill = CheckFill();
                    if (!strCheckFill.equals("")) {
                        ln_check_fill.setVisibility(View.VISIBLE);
                        tv_check_fill.setText("请修改以下:" + strCheckFill);
                        return;
                    }
                    for(ItemPicker itemPicker : list_item_country){
                        if(itemPicker.getChecked() == true){
                            country = itemPicker.getId();
                            break;
                        }
                    }
                    address.setAddress_1(formlocationaddress);
                    address.setCountry(country);
                    address.setState(formprovince);
                    address.setCity(formcity);
                    address.setPostal_code(formpostofficeno);

                    billingAddressesModels = address;

                    shippingAddresses = new ArrayList<RsAddress>();
                    RsAddress address1 = new RsAddress();
                    address1.setAddress_1(formlocationaddress);
                    address1.setCountry(country);
                    address1.setState(formprovince);
                    address1.setCity(formcity);
                    address1.setPostal_code(formpostofficeno);
                    shippingAddresses.add(address1);

                    getListener().onChangeCommitPayment(AddressPayFragment.this.getAcceptOrtherModel(), shippingAddresses, address, customSwitch.isChecked());
                } else {
                    View view_forcus = getActivity().getCurrentFocus();
                    if (view_forcus != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view_forcus.getWindowToken(), 0);
                    }
                    if(primaryShipAddress != null){
                        //update
                        UpdateShipAddress();
                    }
                    else{
                        //create
                        CreateShipAddress();
                    }
                }
            }
        });
        return fragment_view;
    }

    /**
     * Get address me from API
     *
     * @params:
     * @return:
     */
    public void GetAddressMe() {
        swipeRefreshLayout.setRefreshing(true);
        final ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);

        Call<AddressMeResponseMessage> call = ws.actionGetAddressMe();
        call.enqueue(new Callback<AddressMeResponseMessage>() {
            @Override
            public void onResponse(Call<AddressMeResponseMessage> call, Response<AddressMeResponseMessage> response) {
                try {
                    if (response.body() != null && response.body().isSuccess()) {
                        if (AddressPayFragment.this.getActivity() != null) {
                            shippingAddresses = response.body().getShippingAddresses();
                            for(RsAddress address : shippingAddresses){
                                if(address.isPrimary_address() == true){
                                    primaryShipAddress = address;
                                    et_form_province.setText(primaryShipAddress.getState());
                                    et_form_post_office_no.setText(primaryShipAddress.getPostal_code());
                                    et_form_city.setText(primaryShipAddress.getCity());
                                    et_form_location_address.setText(primaryShipAddress.getAddress_1());
                                    edt_choose_country.setText("");
                                    for (int i = 0; i < list_item_country.size(); i++) {
                                        if (list_item_country.get(i).getId().equals(primaryShipAddress.getCountry()) == true) {
                                            list_item_country.get(i).setChecked(true);
                                            edt_choose_country.setText(list_item_country.get(i).getName());
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }

                            billingAddressesModels = response.body().getBillingAddress();
                            fl_add_adr.setVisibility(View.GONE);
                            ListView myListView = (ListView) fragment_view.findViewById(R.id.ll_adress_info);
                            AddressListAdapter addresslistadapter = new AddressListAdapter(AddressPayFragment.this.getActivity(),
                                    new ArrayList<RsAddress>());
                            addresslistadapter.setInterface_address_addpater(new AddressListAdapter.AddressAdapterInterface() {
                                @Override
                                public void onAddNewAddress() {
                                    et_form_province.setText("");
                                    et_form_post_office_no.setText("");
                                    et_form_city.setText("");
                                    et_form_location_address.setText("");
                                    edt_choose_country.setText("");
                                    for (int i = 0; i < list_item_country.size(); i++) {
                                        if (list_item_country.get(i).getChecked() == true) {
                                            list_item_country.get(i).setChecked(false);
                                        }
                                    }
                                }
                            });
                            addresslistadapter.setMyFragment(AddressPayFragment.this);
                            myListView.setAdapter(addresslistadapter);
                            addresslistadapter.notifyDataSetChanged();


                            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (shippingAddresses != null && shippingAddresses.get(position) != null) {
                                        RsAddress address_choosen = shippingAddresses.get(position);
                                        String strLocationAddress = "";
                                        if (address_choosen.getAddress_1() != null) {
                                            strLocationAddress += address_choosen.getAddress_1().toString();
                                        }
                                        if (address_choosen.getAddress_2() != null) {
                                            strLocationAddress += " " + address_choosen.getAddress_2().toString();
                                        }
                                        if (!strLocationAddress.equals("")) {
                                            et_form_location_address.setText(strLocationAddress);
                                        }
                                        et_form_province.setText(address_choosen.getState());
                                        et_form_post_office_no.setText(address_choosen.getPostal_code());
                                        et_form_city.setText(address_choosen.getCity());
                                        for (int i = 0; i < list_item_country.size(); i++) {
                                            if (list_item_country.get(i).getName().equals(address_choosen.getCountry())) {
                                                list_item_country.get(i).setChecked(true);
                                                edt_choose_country.setText(list_item_country.get(i).getName());
                                            }
                                            else{
                                                list_item_country.get(i).setChecked(false);
                                            }
                                        }
                                    }
                                }
                            });


                            customSwitch.setChecked(true);
                        }
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(false);
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<AddressMeResponseMessage> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Utils.OnFailException(t);
            }
        });

    }

    /**
     * Get countries with country codes
     *
     * @params;
     * @return:
     */
    public void GetCountriesWithCode() {
        final ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage> callGetCountriesCode = ws.actionGetCountryWithCode();
        callGetCountriesCode.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        List<Object> arrCountries = Arrays.asList(response.body().getData());
                        mapCountryCode = GetCountryCodeObject(arrCountries);

                        for (CountryCodeRs entry : mapCountryCode) {
                            sp_list_country.add(entry.name);
                            list_item_country.add(new ItemPicker(entry.code, entry.name, false));
                        }
                        SetCountryCodeToSpinner();
                        GetAddressMe();
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Utils.OnFailException(t);
            }
        });
    }

    /**
     * Convert coutries with code
     *
     * @params: listCountries
     * @return:
     */

    public HashMap<String, String> GetCountryCode(List<Object> arrCountries) {
        List<Objects> listCountries = (List<Objects>) arrCountries.get(0);
        List<List<String>> array = new ArrayList<List<String>>();
        int index = 0;
        for (Object value : listCountries) {
            List<String> item = (List<String>) value;
            array.add(item);
            index++;
        }

        HashMap<String, String> mapCountryCode = new HashMap<>();
        String key, value;
        for (List<String> item : array) {
            key = item.get(1);
            value = item.get(0);
            mapCountryCode.put(key, value);
        }
        return mapCountryCode;
    }

    public ArrayList<CountryCodeRs> GetCountryCodeObject(List<Object> arrCountries) {
        List<Objects> listCountries = (List<Objects>) arrCountries.get(0);
        ArrayList<CountryCodeRs> array_object = new ArrayList<>();
        int index = 0;
        for (Object value : listCountries) {
            List<String> item = (List<String>) value;
            if(item.size() == 2){
                CountryCodeRs codeRs = new CountryCodeRs();
                codeRs.code = item.get(1);
                codeRs.name = item.get(0);
                array_object.add(codeRs);
            }
        }
        return array_object;
    }

    /**
     * Set countryocde to spinner
     *
     * @params:
     * @return:
     */
    public void SetCountryCodeToSpinner() {
        adapter_country = new ArrayAdapter<String>(getActivity(), R.layout.list_item_spinner_custom_size, sp_list_country) {
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
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
        adapter_country.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        sp_choose_country.setAdapter(adapter_country);
    }

    /**
     * Check fill
     *
     * @params:
     * @return:
     */
    public String CheckFill() {
        String strCheckFill = "";
        if (!et_form_location_address.getText().toString().equals("") && !et_form_province.getText().toString().equals("")
                && !et_form_city.getText().toString().equals("") && !et_form_post_office_no.getText().toString().equals("") && !edt_choose_country.getText().toString().equals("")) {
            return strCheckFill;
        } else {
            if (edt_choose_country.getText().toString().equals("")) {
                strCheckFill += "\n请选择您所在的国家";
            }
            if (et_form_location_address.getText().toString().equals("")) {
                strCheckFill += "\n省份必填";
            }
            if (et_form_province.getText().toString().equals("")) {
                strCheckFill += "\n城市必填";
            }
            if (et_form_city.getText().toString().equals("")) {
                strCheckFill += "\n地址必填";
            }
            if (et_form_post_office_no.getText().toString().equals("")) {
                strCheckFill += "\n邮政编码必填";
            }
            return strCheckFill;
        }
    }

    @Override
    public void onRefresh() {

    }

    public interface MyCallBack {
        void onChangeCommitPayment(OrderModel orderModel, ArrayList<RsAddress> shippingAddresses, BillingAddressesModel billingAddressesModel, Boolean isShipandBill);
    }

    private void showDialogDropDownCountry(){

        DialogDropdown dropdown = new DialogDropdown(this.getActivity(), false, list_item_country, edt_choose_country);

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

    private void CreateShipAddress(){
        String formlocationaddress, formprovince, formpostofficeno, formcity;

        boolean formaddress;
        String strCheckFill = CheckFill();
        if (!strCheckFill.equals("")) {
            ln_check_fill.setVisibility(View.VISIBLE);
            tv_check_fill.setText("请修改以下:" + strCheckFill);
            return;
        }
        formlocationaddress = et_form_location_address.getText().toString();
        formprovince = et_form_province.getText().toString();
        formpostofficeno = et_form_post_office_no.getText().toString();
        formcity = et_form_city.getText().toString();
        formaddress = customSwitch.isChecked();
        String list_country = "";
        String country = "";
        country = edt_choose_country.getText().toString();
        for (ItemPicker itemPicker : list_item_country){
            if(itemPicker.getChecked() == true){
                country = itemPicker.getId();
            }
        }
        ProgressDialog progressDialogAdding1 = null;
        try{
            progressDialogAdding1 = Utils.createProgressDialog(AddressPayFragment.this.getActivity());
        }
        catch (Exception e){

        }

        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        final ProgressDialog finalProgressDialogAdding = progressDialogAdding1;
        ws.actionAddShippingAddress(country, formlocationaddress, null,
                formprovince, formcity, formpostofficeno,true).enqueue(new Callback<ResponseMessage<RsAddress>>() {
            @Override
            public void onResponse(Call<ResponseMessage<RsAddress>> call, Response<ResponseMessage<RsAddress>> response) {
                try {
                    if (response.body() != null) {
                        ResponseMessage responseMessage = response.body();
                        if (responseMessage.isSuccess()) {
                            backPress();
                            //hintSpinner1.init();

                            if (finalProgressDialogAdding != null) {
                                finalProgressDialogAdding.dismiss();
                            }
                        }
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }

                        if (finalProgressDialogAdding != null) {
                            finalProgressDialogAdding.dismiss();
                        }
                    } else {
                        if (finalProgressDialogAdding != null) {
                            finalProgressDialogAdding.dismiss();
                        }
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                    if (finalProgressDialogAdding != null) {
                        finalProgressDialogAdding.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<RsAddress>> call, Throwable t) {
                if (finalProgressDialogAdding != null) {
                    finalProgressDialogAdding.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }

    private void UpdateShipAddress(){
        String formlocationaddress, formprovince, formpostofficeno, formcity;

        boolean formaddress;
        String strCheckFill = CheckFill();
        if (!strCheckFill.equals("")) {
            ln_check_fill.setVisibility(View.VISIBLE);
            tv_check_fill.setText("请修改以下:" + strCheckFill);
            return;
        }
        formlocationaddress = et_form_location_address.getText().toString();
        formprovince = et_form_province.getText().toString();
        formpostofficeno = et_form_post_office_no.getText().toString();
        formcity = et_form_city.getText().toString();
        formaddress = customSwitch.isChecked();
        String list_country = "";
        String country = "";
        country = edt_choose_country.getText().toString();
        for (ItemPicker itemPicker : list_item_country){
            if(itemPicker.getChecked() == true){
                country = itemPicker.getId();
            }
        }
        ProgressDialog progressDialogUpdate = null;
        try{
            progressDialogUpdate = Utils.createProgressDialog(AddressPayFragment.this.getActivity());
        }
        catch (Exception e){
            Crashlytics.log("error");
        }


        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        final ProgressDialog finalProgressDialogUpdate = progressDialogUpdate;
        ws.actionUpdateShippingAddress(primaryShipAddress.getId(),country, formlocationaddress, null,
                formprovince, formcity, formpostofficeno).enqueue(new Callback<ResponseMessage<RsAddress>>() {
            @Override
            public void onResponse(Call<ResponseMessage<RsAddress>> call, Response<ResponseMessage<RsAddress>> response) {
                try {
                    if (response.body() != null) {
                        ResponseMessage responseMessage = response.body();
                        if (responseMessage.isSuccess()) {
                            backPress();
                            //hintSpinner1.init();
                            if (finalProgressDialogUpdate != null) {
                                finalProgressDialogUpdate.dismiss();
                            }
                        }
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }

                        if (finalProgressDialogUpdate != null) {
                            finalProgressDialogUpdate.dismiss();
                        }
                    } else {
                        if (finalProgressDialogUpdate != null) {
                            finalProgressDialogUpdate.dismiss();
                        }
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                    if (finalProgressDialogUpdate != null) {
                        finalProgressDialogUpdate.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<RsAddress>> call, Throwable t) {
                if (finalProgressDialogUpdate != null) {
                    finalProgressDialogUpdate.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }

}
