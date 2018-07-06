package com.wo1haitao.models;

/**
 * Created by user on 5/26/2017.
 */

public class ProductTenderShippings {
    public int id;
    public int shipping_method_id;
    public long product_tender_id;
    public PriceModel shipping_cost;
    public int expected_arrival_days;
    public String created_at;

    public String getCreated_at() {
        if(created_at == null){
            return "";
        }
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public PriceModel getShipping_cost() {

        return shipping_cost;
    }

    public void setShipping_cost(PriceModel shipping_cost) {
        this.shipping_cost = shipping_cost;
    }
    public int getId() {
        return id;
    }

    public int getShipping_method_id() {
        return shipping_method_id;
    }

    public long getProduct_tender_id() {
        return product_tender_id;
    }


    public int getExpected_arrival_days() {
        return expected_arrival_days;
    }
}
