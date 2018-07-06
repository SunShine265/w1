package com.wo1haitao.models;

import com.wo1haitao.api.response.UserProfile;

/**
 * Created by user on 8/4/2017.
 */

public class NotificationModel {
    long id, common_user_id, notified_by_id, product_offer_id;
    String updated_at;
    String created_at;
    String current_time;
    UserProfile common_user, notified_by;
    ProductOffer product_offer;
    String notice_type;
    ProductListing product_listing;
    String created_at_in_words = "...";

    public String getCreated_at_in_words() {
        return created_at_in_words;
    }

    public void setCreated_at_in_words(String created_at_in_words) {
        this.created_at_in_words = created_at_in_words;
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

    public String getNotice_type() {
        if(notice_type == null){
            return "";
        }
        return notice_type;
    }

    public void setNotice_type(String notice_type) {
        this.notice_type = notice_type;
    }

    public String getCurrent_time() {
        if(current_time == null){
            return "";
        }
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public String getCreated_at() {
        if(created_at == null){
            return "";
        }
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCommon_user_id() {
        return common_user_id;
    }

    public void setCommon_user_id(long common_user_id) {
        this.common_user_id = common_user_id;
    }

    public long getNotified_by_id() {
        return notified_by_id;
    }

    public void setNotified_by_id(long notified_by_id) {
        this.notified_by_id = notified_by_id;
    }

    public long getProduct_offer_id() {
        return product_offer_id;
    }

    public void setProduct_offer_id(long product_offer_id) {
        this.product_offer_id = product_offer_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public UserProfile getCommon_user() {
        if(common_user == null){
            return new UserProfile();
        }
        return common_user;
    }

    public void setCommon_user(UserProfile common_user) {
        this.common_user = common_user;
    }

    public UserProfile getNotified_by() {
        if(notified_by == null){
            return new UserProfile();
        }
        return notified_by;
    }

    public void setNotified_by(UserProfile notified_by) {
        this.notified_by = notified_by;
    }

    public ProductOffer getProduct_offer() {
        if(product_offer == null){
            return new ProductOffer();
        }
        return product_offer;
    }

    public void setProduct_offer(ProductOffer product_offer) {
        this.product_offer = product_offer;
    }
}
