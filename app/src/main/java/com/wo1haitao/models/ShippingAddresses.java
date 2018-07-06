package com.wo1haitao.models;

/**
 * Created by user on 6/12/2017.
 */

public class ShippingAddresses {
    public long getId() {
        return id;
    }

    public long getCommon_user_id() {
        return common_user_id;
    }

    public String getCountry() {
        if(country == null){
            return "";
        }
        return country;
    }

    public String getState() {
        if(state == null){
            return "";
        }
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getAddress_1() {
        return address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public Boolean getPrimary_address() {
        return primary_address;
    }

    long id;
    long common_user_id;
    String country, state, city, address_1, address_2, postal_code;
    Boolean primary_address;

}
