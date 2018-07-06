package com.wo1haitao.api.response;

import com.google.gson.annotations.SerializedName;
import com.wo1haitao.models.BillingAddressesModel;

import java.util.ArrayList;

/**
 * Created by user on 6/12/2017.
 */

public class AddressMeResponseMessage extends  ResponseMessageBase{
    public ArrayList<RsAddress> getShippingAddresses() {
        return shippingAddresses;
    }

    public void setShippingAddresses(ArrayList<RsAddress> shippingAddresses) {
        this.shippingAddresses = shippingAddresses;
    }

    @SerializedName("shipping_addresses")
    private ArrayList<RsAddress> shippingAddresses;


    public BillingAddressesModel getBillingAddress() {
        if(billingAddress == null){
            return  new BillingAddressesModel();
        }
        return billingAddress;
    }

    public void setBillingAddress(BillingAddressesModel billingAddress) {
        this.billingAddress = billingAddress;
    }

    @SerializedName("billing_address")
    private BillingAddressesModel billingAddress;
}
