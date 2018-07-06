package com.wo1haitao.models;

import com.wo1haitao.api.response.RsCountry;
import com.wo1haitao.api.response.UserProfile;
import java.util.ArrayList;

/**
 * Created by user on 5/26/2017.
 */

public class OfferMeModel {
    public int id;
    public String condition;
    public int num_of_offers;
    public UserProfile common_user;
    public ProductListing product_listing;
    public RsCountry country;
    public ArrayList<ProductImages> product_images;
    public  ArrayList<ProductOffer> product_offers;
    public ArrayList<ProductTenderShippings> product_tender_shippings;
    public ArrayList<ShippingMethods> shipping_methods;
    boolean closed;

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public int getId() {
        return id;
    }

    public String getCondition() {
        if(condition == null){
            return "";
        }
        return condition;
    }

    public int getNum_of_offers() { return num_of_offers; }

    public UserProfile getCommon_user() {
        if(common_user == null){
            return new UserProfile();
        }
        return common_user;
    }

    public ProductListing getProduct_listing() {
        if(product_listing == null){
            return new ProductListing();
        }
        return product_listing;
    }

    public RsCountry getCountry() {
        if(country == null){
            return new RsCountry();
        }
        return country;
    }

    public ArrayList<ProductImages> getProduct_images() {
        if(product_images == null){
            return new ArrayList<>();
        }
        return product_images;
    }

    public ArrayList<ProductOffer> getProduct_offers() {
        if(product_offers == null){
            return new ArrayList<>();
        }
        return product_offers;
    }

    public ArrayList<ProductTenderShippings> getProduct_tender_shippings() {
        return product_tender_shippings;
    }

    public ArrayList<ShippingMethods> getShipping_methods() {
        return shipping_methods;
    }
}
