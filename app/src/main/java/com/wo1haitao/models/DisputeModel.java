package com.wo1haitao.models;

import com.wo1haitao.api.response.UserProfile;

/**
 * Created by user on 7/17/2017.
 */

public class DisputeModel {
    long id, reporter_id, reported_id;
    String reason_to_dispute, details;
    ProductListing product_listing;
    UserProfile reporter, reported;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReporter_id() {
        return reporter_id;
    }

    public void setReporter_id(long reporter_id) {
        this.reporter_id = reporter_id;
    }

    public long getReported_id() {
        return reported_id;
    }

    public void setReported_id(long reported_id) {
        this.reported_id = reported_id;
    }

    public String getReason_to_dispute() {
        return reason_to_dispute;
    }

    public void setReason_to_dispute(String reason_to_dispute) {
        this.reason_to_dispute = reason_to_dispute;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
