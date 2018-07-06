package com.wo1haitao.models;

import java.util.ArrayList;

/**
 * Created by user on 6/8/2017.
 */

public class ProductTender {
    long id, product_listing_id, country_id;
    String condition;

    public ArrayList<ProductOffer> getProduct_offers() {
        if(product_offers == null){
            return new ArrayList<>();
        }
        return product_offers;
    }

    public ArrayList<ProductTenderShippings> getProduct_tender_shippings() {
        return product_tender_shippings;
    }

    ArrayList<ProductOffer> product_offers;
    ArrayList<ProductTenderShippings> product_tender_shippings;

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
