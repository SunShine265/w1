package com.wo1haitao.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsAddress;
import com.wo1haitao.api.response.RsAddressResponse;
import com.wo1haitao.fragments.AddressPayFragment;
import com.wo1haitao.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/20/17.
 */

public class AddressListAdapter extends BaseAdapterAddress {
    Activity context;
    Fragment myFragment;
    ArrayList<RsAddress> addresses;
    FrameLayout fl_default, fl_setdefault;
    AddressAdapterInterface interface_address_addpater;

    public AddressAdapterInterface getInterface_address_addpater() {
        return interface_address_addpater;
    }

    public void setInterface_address_addpater(AddressAdapterInterface interface_address_addpater) {
        this.interface_address_addpater = interface_address_addpater;
    }

    public Fragment getMyFragment() {
        return myFragment;
    }

    public void setMyFragment(Fragment myFragment) {
        this.myFragment = myFragment;
    }

    public AddressListAdapter(Activity context, ArrayList<RsAddress> rsAddresses) {
        super(context, 0, rsAddresses);
        this.context = context;
        addresses = rsAddresses;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final RsAddress itemAddress = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_adderss_view, parent, false);
//        }
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_adderss_view, parent, false);
        // Lookup view for data population
        TextView tv_item_address = (TextView) convertView.findViewById(R.id.tv_adress);
        String address1 = "", address2 = "", country = "", state = "", city = "", postalCode = "";
        if(itemAddress.getAddress_1() != null){
            address1 =  itemAddress.getAddress_1().toString();
        }
        if(itemAddress.getAddress_2() != null){
            address2 =  itemAddress.getAddress_2().toString();
        }
        if(itemAddress.getCountry() != null){
            country =  itemAddress.getCountry().toString();
        }
        if(itemAddress.getState() != null){
            state =  itemAddress.getState().toString();
        }
        if(itemAddress.getCity() != null){
            city =  itemAddress.getCity().toString();
        }
        if(itemAddress.getPostal_code() != null){
            postalCode =  itemAddress.getPostal_code().toString();
        }
        String dataAddress = address1 + ", " + address2 + "\n" +
                country + ", " + state + "\n" + city + "\n"
                + "ZIP " + postalCode;
        Boolean primaryAddress = itemAddress.isPrimary_address();
        if (primaryAddress) {

        }
        fl_default = (FrameLayout) convertView.findViewById(R.id.fl_default);
        fl_setdefault = (FrameLayout) convertView.findViewById(R.id.fl_setdefault);
        if (primaryAddress) {
            fl_default.setVisibility(View.VISIBLE);
            fl_setdefault.setVisibility(View.GONE);
        } else {
            fl_default.setVisibility(View.GONE);
            fl_setdefault.setVisibility(View.VISIBLE);
        }
        if(fl_setdefault.getVisibility() == View.VISIBLE) {
            fl_setdefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateBillingAddress(itemAddress, true);
                }
            });
        }
        ImageView iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
        if (addresses.size() >= 2) {
            if (itemAddress.isPrimary_address() == false) {

                iv_add.setVisibility(View.VISIBLE);
                iv_add.setBackgroundResource(R.drawable.minus_icon);

                iv_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ApiServices api = ApiServices.getInstance();
                        WebService ws = api.getRetrofit().create(WebService.class);
                        final ProgressDialog progressDialogLoading = Utils.createProgressDialog(context);
                        Call<ResponseMessage> call = ws.actionDeleteShippingAddress(itemAddress.getId());
                        call.enqueue(new Callback<ResponseMessage>() {
                            @Override
                            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                try {
                                    if (response.body() != null) {
                                        if (response.body().isSuccess()) {


//                                            Toast.makeText(getContext(), "Delete shipping address Success!", Toast.LENGTH_SHORT).show();
                                            progressDialogLoading.dismiss();
                                            if(myFragment != null && myFragment instanceof AddressPayFragment){
                                                AddressPayFragment addressFragment = (AddressPayFragment) myFragment;
                                                addressFragment.GetAddressMe();
                                            }

                                        }
                                    } else if (response.errorBody() != null) {
                                        if (progressDialogLoading != null) {
                                            progressDialogLoading.dismiss();
                                        }
                                        try {
                                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                                            ErrorMessage error = responseMessage.getErrors();
                                            Toast.makeText(getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        if (progressDialogLoading != null) {
                                            progressDialogLoading.dismiss();
                                        }
                                        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Utils.crashLog(e);
                                    if (progressDialogLoading != null) {
                                        progressDialogLoading.dismiss();
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                                if (progressDialogLoading != null) {
                                    progressDialogLoading.dismiss();
                                }
                                Utils.OnFailException(t);
                            }
                        });
                    }
                });
            }
            else {
                iv_add.setVisibility(View.GONE);
            }
        } else {
            iv_add.setVisibility(View.VISIBLE);
            iv_add.setBackgroundResource(R.drawable.plus_icon);
            iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(interface_address_addpater != null){
                        interface_address_addpater.onAddNewAddress();
                    }
                }
            });
        }
        tv_item_address.setText(dataAddress);
        return convertView;
    }

    public void UpdateBillingAddress(RsAddress itemAddress, boolean primaryAddress){
        final ProgressDialog progressDialogLoading = Utils.createProgressDialog(context);
        final ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);

        Call<RsAddressResponse> call = ws.actionUpdateShippingAddress(itemAddress.getId(), itemAddress.getCountry(), itemAddress.getAddress_1(),
                itemAddress.getAddress_2(), itemAddress.getState(), itemAddress.getCity(), itemAddress.getPostal_code(), primaryAddress);
        call.enqueue(new Callback<RsAddressResponse>() {
            @Override
            public void onResponse(Call<RsAddressResponse> call, Response<RsAddressResponse> response) {
                try {
                    if (response.body() != null) {
                        RsAddress billingAddressesModel = response.body();
                        for(int i = 0; i < addresses.size(); i++){
                            if(billingAddressesModel.getId() == addresses.get(i).getId()){
                                addresses.get(i).setPrimary_address(true);

                            }
                            else {
                                addresses.get(i).setPrimary_address(false);
                            }
                        }
                        notifyDataSetChanged();
                        progressDialogLoading.dismiss();
                    }
                    else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialogLoading != null) {
                            progressDialogLoading.dismiss();
                        }
                    } else {
                        if (progressDialogLoading != null) {
                            progressDialogLoading.dismiss();
                        }
                        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Utils.crashLog(e);
                    if (progressDialogLoading != null) {
                        progressDialogLoading.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<RsAddressResponse> call, Throwable t) {
                if (progressDialogLoading != null) {
                    progressDialogLoading.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }

    public interface AddressAdapterInterface {
        void onAddNewAddress();
    }
}

