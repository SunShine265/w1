package com.wo1haitao.models;

import com.wo1haitao.api.response.UserProfile;

/**
 * Created by user on 6/7/2017.
 */

public class CounterOfferModel {
    long id;
    long product_offer_id;
    PriceModel counter_offer_price;
    String remarks;
    ProductOffer product_offer;
    UserProfile common_user;

    public long getId() {
        return id;
    }

    public long getProduct_offer_id() {
        return product_offer_id;
    }

    public PriceModel getCounter_offer_price() {
        return counter_offer_price;
    }

    public String getRemarks() {
        return remarks;
    }

    public ProductOffer getProduct_offer() {
        return product_offer;
    }

    public UserProfile getCommon_user() {
        if(common_user == null){
            return new UserProfile();
        }
        return common_user;
    }
}
