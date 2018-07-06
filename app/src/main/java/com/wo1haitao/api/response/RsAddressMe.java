package com.wo1haitao.api.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/22/2017.
 */

public class RsAddressMe {
    ArrayList<RsAddress> shipping_addresses;
    RsAddress billing_address;

    public List<RsAddress> getShipping_addresses() {
        if(shipping_addresses == null){
            return new ArrayList<>();
        }
        return shipping_addresses;
    }

    public void setShipping_addresses(ArrayList<RsAddress> shipping_addresses) {
        this.shipping_addresses = shipping_addresses;
    }

    public RsAddress getBilling_address() {
        if(billing_address == null){
            return new RsAddress();
        }
        return billing_address;
    }

    public void setBilling_address(RsAddress billing_address) {
        this.billing_address = billing_address;
    }
}
