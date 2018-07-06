package com.wo1haitao.models;

/**
 * Created by user on 6/8/2017.
 */

public class OfferTenderModel {
    long id, product_listing_id, country_id;
    String condition;

    public long getId() {
        return id;
    }

    public long getProduct_listing_id() {
        return product_listing_id;
    }

    public long getCountry_id() {
        return country_id;
    }

    public String getCondition() {
        if(condition == null){
            return "";
        }
        return condition;
    }

}
