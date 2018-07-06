package com.wo1haitao.models;

/**
 * Created by user on 5/26/2017.
 */

public class ProductOffer {
    public Long id;
    public PriceModel offer_price;
    public int product_listing_id;
    public int country_id;
    public String condition;
    public  String remarks;
    public Boolean reoffer;
    public int product_tender_id;
    public String state;
    public ProductCounterOffer product_counter_offer;
    OrderModel order;
    ProductListing product_listing;
    ProductTenders product_tender;
    float commission_charges;
    long commission_charges_percent;
    String order_status, order_inv_status;
    Boolean order_invoice_present;


    public PriceModel getOffer_price() {

        return offer_price;
    }

    public void setOffer_price(PriceModel offer_price) {
        this.offer_price = offer_price;
    }

    public long getCommission_charges_percent() {
        return commission_charges_percent;
    }

    public void setCommission_charges_percent(long commission_charges_percent) {
        this.commission_charges_percent = commission_charges_percent;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_inv_status() {
        return order_inv_status;
    }

    public void setOrder_inv_status(String order_inv_status) {
        this.order_inv_status = order_inv_status;
    }

    public Boolean isOrder_invoice_present() {
        return order_invoice_present;
    }

    public void setOrder_invoice_present(Boolean order_invoice_present) {
        this.order_invoice_present = order_invoice_present;
    }

    public float getCommission_charges() {

        return commission_charges;
    }

    public void setCommission_charges(float commission_charges) {
        this.commission_charges = commission_charges;
    }

    public ProductTenders getProduct_tender() {
        if(product_tender == null){
            return new ProductTenders();
        }
        return product_tender;
    }

    public void setProduct_tender(ProductTenders product_tender) {
        this.product_tender = product_tender;
    }

    public ProductListing getProduct_listing() {
        if(product_listing == null){
            return new ProductListing();
        }
        return product_listing;
    }

    public void setProduct_listing(ProductListing product_listing) {

        this.product_listing = product_listing;
    }

    public OrderModel getOrder() {
        if(order == null){
            return new OrderModel();
        }
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
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

    public ProductCounterOffer getProduct_counter_offer() {
        return product_counter_offer;
    }

    public Long getId() {
        return id;
    }

    public String getRemarks() {
        if(remarks == null){
            return "";
        }
        return remarks;
    }

    public Boolean getReoffer() {
        return reoffer;
    }

    public int getProduct_tender_id() {
        return product_tender_id;
    }

    public int getProduct_listing_id() {
        return product_listing_id;
    }

    public int getCountry_id() {
        return country_id;
    }

    public String getCondition() {
        if(condition == null){
            return "";
        }
        return condition;
    }

}
