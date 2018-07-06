package com.wo1haitao.api.response;

import com.wo1haitao.models.ProductListing;

/**
 * Created by user on 7/17/2017.
 */

public class RsDisputes {
    long id;
    String reason_to_dispute, details, state;
    ProductListing product_listing;
    UserProfile reporter, reported;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReason_to_dispute() {
        if(reason_to_dispute == null){
            return "";
        }
        return reason_to_dispute;
    }

    public void setReason_to_dispute(String reason_to_dispute) {
        this.reason_to_dispute = reason_to_dispute;
    }

    public String getDetails() {
        if(details == null){
            return "";
        }
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public ProductListing getProduct_listing() {
        if(product_listing == null){
            return new ProductListing();
        }
        return product_listing;
    }

    public void setProduct_listing(ProductListing product_listing) {
        this.product_listing = product_listing;
    }

    public UserProfile getReporter() {
        if(reporter == null){
            return new UserProfile();
        }
        return reporter;
    }

    public void setReporter(UserProfile reporter) {
        this.reporter = reporter;
    }

    public UserProfile getReported() {
        return reported;
    }

    public void setReported(UserProfile reported) {
        this.reported = reported;
    }
}
