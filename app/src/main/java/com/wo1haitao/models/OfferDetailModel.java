package com.wo1haitao.models;

/**
 * Created by user on 6/8/2017.
 */

public class OfferDetailModel {
    long id;
    String offer_price, remarks;
    ProductCounterOffer product_counter_offer;
    ProductTender product_tender;
    ProductListing product_listing;

    public long getId() {
        return id;
    }

    public String getOffer_price() {
        if(offer_price == null){
            return "";
        }
        return offer_price;
    }

    public String getRemarks() {
        return remarks;
    }

    public ProductCounterOffer getProduct_counter_offer() {
        return product_counter_offer;
    }

    public ProductTender getProduct_tender() {
        return product_tender;
    }

    public ProductListing getProduct_listing() {
        if(product_listing == null){
            return new ProductListing();
        }
        return product_listing;
    }
}
