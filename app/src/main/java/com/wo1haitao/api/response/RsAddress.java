package com.wo1haitao.api.response;

/**
 * Created by user on 5/22/2017.
 */

public class RsAddress {
    long id;
    long common_user_id;
    String country;
    String state;
    String city;
    String address_1;
    String address_2;
    String postal_code;
    boolean primary_address;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCommon_user_id() {
        return common_user_id;
    }

    public void setCommon_user_id(long common_user_id) {
        this.common_user_id = common_user_id;
    }

    public String getCountry() {
        if(country == null){
            return "";
        }
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        if(state == null){
            return "";
        }
        return state;
    }

    public void setState(String state) {


        this.state = state;
    }

    public String getCity() {
        if(city == null){
            return "";
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress_1() {
        if(address_1 == null){
            return "";
        }
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        if(address_2 == null){
            return "";
        }
        return address_2;
    }

    public void setAddress_2(String address_2) {

        this.address_2 = address_2;
    }

    public String getPostal_code() {
        if(postal_code == null){
            return "";
        }
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public boolean isPrimary_address() {
        return primary_address;
    }

    public void setPrimary_address(boolean primary_address) {
        this.primary_address = primary_address;
    }
}
