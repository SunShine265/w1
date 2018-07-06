package com.wo1haitao.api.response;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.wo1haitao.models.ProductImage;
import com.wo1haitao.models.SetupAccountModel;

import java.util.ArrayList;

/**
 * Created by user on 5/11/2017.
 */

public class UserProfile {
    int id;
    String nickname;
    String email;
    String first_name;
    String last_name;
    String phone_number;
    String country_code;
    int country_id;
    RsAddress billing_address;
    RsAvartar profile_picture;
    SetupAccountModel setup_account;
    String num_of_reviews;
    float average_rating;
    String notify_scope_name;
    ArrayList<RsReviewToMe> reviews;
    ProductImage identity_image;
    String verification_state;
    String alipay_id, alipay_name;
    int unread_notifications;
    int num_of_unread;

    public int getNum_of_unread() {
        return num_of_unread;
    }

    public void setNum_of_unread(int num_of_unread) {
        this.num_of_unread = num_of_unread;
    }

    public int getUnread_notifications() {
        return unread_notifications;
    }

    public void setUnread_notifications(int unread_notifications) {
        this.unread_notifications = unread_notifications;
    }

    public String getAlipay_id() {
        if(alipay_id == null){
            return "";
        }
        return alipay_id;
    }

    public void setAlipay_id(String alipay_id) {
        this.alipay_id = alipay_id;
    }

    public String getAlipay_name() {
        if(alipay_name == null){
            return "";
        }
        return alipay_name;
    }

    public void setAlipay_name(String alipay_name) {
        this.alipay_name = alipay_name;
    }

    public String getVerification_state() {
        if(verification_state == null){
            return "";
        }
        return verification_state;
    }

    public void setVerification_state(String verification_state) {
        this.verification_state = verification_state;
    }

    public ProductImage getIdentity_image() {
        if(identity_image == null){
            return new ProductImage();
        }
        return identity_image;
    }

    public void setIdentity_image(ProductImage identity_image) {
        this.identity_image = identity_image;
    }

    public ArrayList<RsReviewToMe> getReviews() {
        if(reviews == null){
            return new ArrayList<>();
        }
        return reviews;
    }

    public void setReviews(ArrayList<RsReviewToMe> reviews) {
        this.reviews = reviews;
    }

    public String getNotify_scope_name() {
        if(notify_scope_name == null){
            return "";
        }
        return notify_scope_name;
    }

    public void setNotify_scope_name(String notify_scope_name) {
        this.notify_scope_name = notify_scope_name;
    }

    public float getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(float average_rating) {
        this.average_rating = average_rating;
    }

    public String getNum_of_reviews() {
        if(num_of_reviews == null){
            return "";
        }
        return num_of_reviews;
    }

    public void setNum_of_reviews(String num_of_reviews) {
        this.num_of_reviews = num_of_reviews;
    }

    public SetupAccountModel getSetup_account() {
        if(setup_account == null){
            return new SetupAccountModel();
        }
        return setup_account;
    }

    public void setSetup_account(SetupAccountModel setup_account) {
        this.setup_account = setup_account;
    }

    public RsAddress getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(RsAddress billing_address) {
        this.billing_address = billing_address;
    }

    public RsAvartar getProfile_picture() {
        if(profile_picture == null){
            return new RsAvartar();
        }
        return profile_picture;
    }

    public void setProfile_picture(RsAvartar profile_picture) {
        this.profile_picture = profile_picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        if(nickname == null){
            return "";
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        if(email == null){
            return "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        if(first_name == null){
            return "";
        }
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        if(last_name == null){
            return "";
        }
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        if(phone_number == null){
            return "";
        }
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }
}
