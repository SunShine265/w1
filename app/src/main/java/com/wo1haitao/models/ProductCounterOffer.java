package com.wo1haitao.models;

/**
 * Created by user on 6/8/2017.
 */

public class ProductCounterOffer {
    long id, product_offer_id;
    String  remarks;
    PriceModel counter_offer_price;

    public PriceModel getCounter_offer_price() {

        return counter_offer_price;
    }

    public void setCounter_offer_price(PriceModel counter_offer_price) {
        this.counter_offer_price = counter_offer_price;
    }

    public long getId() {
        return id;
    }

    public long getProduct_offer_id() {
        return product_offer_id;
    }

    public String getRemarks() {
        if(remarks == null){
            return "";
        }
        return remarks;
    }
}
