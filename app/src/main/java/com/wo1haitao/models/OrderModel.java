package com.wo1haitao.models;

import com.wo1haitao.api.response.RsAvartar;
import com.wo1haitao.api.response.UserProfile;

/**
 * Created by user on 6/16/2017.
 */

public class OrderModel {

    public static String STATE_PENDING = "pending";
    public static String STATE_PROCESS = "processing";
    public static String STATE_CONFIRM = "confirmed";

    int id;
    long product_offer_id;
    long product_tender_shipping_id;
    float agreed_price_cents;
    String country;
    String state;
    String city;
    String address_1;
    String address_2;
    String postal_code;
    String billing_country;
    String billing_state;
    String billing_city;
    String billing_address_1;
    String billing_address_2;
    String billing_postal_code;
    String payment_method;
    PriceModel commission_charges;
    String status;
    String order_no;
    String invoice_status;
    String tracking_no;
    String shipping_company_name;
    String seller_delivery_status;
    String buyer_receive_status;String agreed_date;String discount_amount_cents, discount_purchase_limit_cents;
    ProductOffer product_offer;
    ProductTenderShippings product_tender_shipping;
    ProductListing product_listing;
    UserProfile owner;
    RsAvartar order_invoice;
    boolean order_invoice_present;
    PaymentSetting payment_setting;
    int commission_charges_cents;

    String buyer_commission_charges_amount;
    String payable_amount_to_tenderer;

    public String getDiscount_amount_cents() {
        return discount_amount_cents;
    }

    public void setDiscount_amount_cents(String discount_amount_cents) {
        this.discount_amount_cents = discount_amount_cents;
    }

    public String getDiscount_purchase_limit_cents() {
        return discount_purchase_limit_cents;
    }

    public void setDiscount_purchase_limit_cents(String discount_purchase_limit_cents) {
        this.discount_purchase_limit_cents = discount_purchase_limit_cents;
    }

    public String getCommission_charges_percent() {
        if(commission_charges_percent == null){
            commission_charges_percent = "";
        }
        return commission_charges_percent;
    }

    String commission_charges_percent;

    public void setCommission_charges_percent(String commission_charges_percent) {
        this.commission_charges_percent = commission_charges_percent;
    }

    public String getBuyer_commission_charges_amount() {
        return buyer_commission_charges_amount;
    }

    public void setBuyer_commission_charges_amount(String buyer_commission_charges_amount) {
        this.buyer_commission_charges_amount = buyer_commission_charges_amount;
    }

    public String getPayable_amount_to_tenderer() {
        return payable_amount_to_tenderer;
    }

    public void setPayable_amount_to_tenderer(String payable_amount_to_tenderer) {
        this.payable_amount_to_tenderer = payable_amount_to_tenderer;
    }

    public String getSeller_commission_charges_percent() {
        return seller_commission_charges_percent;
    }

    public void setSeller_commission_charges_percent(String seller_commission_charges_percent) {
        this.seller_commission_charges_percent = seller_commission_charges_percent;
    }

    String seller_commission_charges_percent;



    public int getCommission_charges_cents() {
        return commission_charges_cents;
    }

    public void setCommission_charges_cents(int commission_charges_cents) {
        this.commission_charges_cents = commission_charges_cents;
    }

    public String getString_charges_cents(){
        float value = ((float)commission_charges_cents)/100;
        String text = String.format("%.2f", value);
        return text;
    }

    public String getShipping_company_name() {
        if(shipping_company_name == null){
            return "";
        }
        return shipping_company_name;
    }

    public void setShipping_company_name(String shipping_company_name) {
        this.shipping_company_name = shipping_company_name;
    }

    public PaymentSetting getPayment_setting() {
        if(payment_setting == null){
            return new PaymentSetting();
        }
        return payment_setting;
    }

    public void setPayment_setting(PaymentSetting payment_setting) {
        this.payment_setting = payment_setting;
    }

    public String getAgreed_date() {
        if(agreed_date == null){
            return "";
        }
        return agreed_date;
    }

    public void setAgreed_date(String agreed_date) {
        this.agreed_date = agreed_date;
    }

    public boolean isOrder_invoice_present() {
        return order_invoice_present;
    }

    public void setOrder_invoice_present(boolean order_invoice_present) {
        this.order_invoice_present = order_invoice_present;
    }

    public String getSeller_delivery_status() {
        if(seller_delivery_status == null){
            return "";
        }
        return seller_delivery_status;
    }

    public void setSeller_delivery_status(String seller_delivery_status) {
        this.seller_delivery_status = seller_delivery_status;
    }

    public String getBuyer_receive_status() {
        if(buyer_receive_status == null){
            return "";
        }
        return buyer_receive_status;
    }

    public void setBuyer_receive_status(String buyer_receive_status) {
        this.buyer_receive_status = buyer_receive_status;
    }

    public String getTracking_no() {
        if(tracking_no == null){
            return "";
        }
        return tracking_no;
    }

    public void setTracking_no(String tracking_no) {
        this.tracking_no = tracking_no;
    }

    public RsAvartar getOrder_invoice() {
        if(order_invoice == null){
            return new RsAvartar();
        }
        return order_invoice;
    }

    public void setOrder_invoice(RsAvartar order_invoice) {
        this.order_invoice = order_invoice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_no() {
        if(order_no == null){
            return "";
        }
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getInvoice_status() {
        if(invoice_status == null){
            return "";
        }
        return invoice_status;
    }

    public void setInvoice_status(String invoice_status) {
        this.invoice_status = invoice_status;
    }

    public String getStatus() {
        if(status == null){
            return "";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public void setCommission_charges_percent(PriceModel commission_charges_percent) {
        this.commission_charges = commission_charges_percent;
    }

    public ProductListing getProductListing() {
        if(product_listing == null){
            return new ProductListing();
        }
        return product_listing;
    }

    public void setProductListing(ProductListing productListing) {
        this.product_listing = productListing;
    }

    public UserProfile getOwner() {
        if(owner == null){
            return new UserProfile();
        }
        return owner;
    }

    public void setOwner(UserProfile owner) {
        this.owner = owner;
    }

    public String getPayment_method() {
        if(payment_setting == null){
            return "";
        }
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public void setProduct_offer_id(long product_offer_id) {
        this.product_offer_id = product_offer_id;
    }

    public void setProduct_tender_shipping_id(long product_tender_shipping_id) {
        this.product_tender_shipping_id = product_tender_shipping_id;
    }

    public void setAgreed_price_cents(float agreed_price_cents) {
        this.agreed_price_cents = agreed_price_cents;
    }

    public void setProduct_offer(ProductOffer product_offer) {
        this.product_offer = product_offer;
    }

    public void setProduct_tender_shipping(ProductTenderShippings product_tender_shipping) {
        this.product_tender_shipping = product_tender_shipping;
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

    public String getBilling_country() {
        if(billing_country == null){
            return "";
        }
        return billing_country;
    }

    public void setBilling_country(String billing_country) {
        this.billing_country = billing_country;
    }

    public String getBilling_state() {
        if(billing_state == null){
            return "";
        }
        return billing_state;
    }

    public void setBilling_state(String billing_state) {
        this.billing_state = billing_state;
    }

    public String getBilling_city() {
        if(billing_city == null){
            return "";
        }
        return billing_city;
    }

    public void setBilling_city(String billing_city) {
        this.billing_city = billing_city;
    }

    public String getBilling_address_1() {
        if(billing_address_1 == null){
            return "";
        }
        return billing_address_1;
    }

    public void setBilling_address_1(String billing_address_1) {
        this.billing_address_1 = billing_address_1;
    }

    public String getBilling_address_2() {
        if(billing_address_2 == null){
            return "";
        }
        return billing_address_2;
    }

    public void setBilling_address_2(String billing_address_2) {

        this.billing_address_2 = billing_address_2;
    }

    public String getBilling_postal_code() {
        if(billing_postal_code == null){
            return "";
        }
        return billing_postal_code;
    }

    public void setBilling_postal_code(String billing_postal_code) {
        this.billing_postal_code = billing_postal_code;
    }

    public long getProduct_offer_id() {
        return product_offer_id;
    }

    public long getProduct_tender_shipping_id() {
        return product_tender_shipping_id;
    }

    public float getAgreed_price_cents() {
        return agreed_price_cents;
    }

    public ProductOffer getProduct_offer() {
        if(product_offer == null){
            return new ProductOffer();
        }
        return product_offer;
    }

    public ProductTenderShippings getProduct_tender_shipping() {
        if(product_tender_shipping == null){
            return new ProductTenderShippings();
        }
        return product_tender_shipping;
    }

}
