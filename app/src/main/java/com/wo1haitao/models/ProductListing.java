package com.wo1haitao.models;

import com.wo1haitao.api.response.RsCountry;
import com.wo1haitao.api.response.UserProfile;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 5/26/2017.
 */

public class ProductListing {
    public int id;
    public int category_id;
    public String name;
    public String description;
    public UserProfile common_user;
    public  Boolean is_offered;
    public int num_of_offers;
    public ArrayList<ProductImages> product_images;
    public ArrayList<RsCountry> countries;
    Date expired_date;
    String reported_id, want_to_buy_id;
    boolean non_restricted_country;
    boolean closed;
    ArrayList<OrderModel> orders;
    boolean is_favourite;
    boolean new_product;
    boolean used_product;
    String expired_date_text = "";
    String expiry_status;

    public String getExpiry_status() {
        if(expiry_status == null){
            if(expired_date_text != null) {
                expiry_status = expired_date_text;
            }
            else{
                expiry_status = "";
            }
        }
        return expiry_status;
    }

    public void setExpiry_status(String expiry_status) {
        this.expiry_status = expiry_status;
    }

    public String getExpired_date_text_my_bid() {
        if(expired_date_text.length() < 6){
            expired_date_text = expired_date_text.replace("广告","");
        }
        return expired_date_text;
    }

    public void setExpired_date_text(String expired_date_text) {
        this.expired_date_text = expired_date_text;
    }
    public String getExpired_date_text() {

        return expired_date_text;
    }



    public boolean isNew_product() {
        return new_product;
    }

    public void setNew_product(boolean new_product) {
        this.new_product = new_product;
    }

    public boolean isUsed_product() {
        return used_product;
    }

    public void setUsed_product(boolean used_product) {
        this.used_product = used_product;
    }

    public boolean is_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(boolean is_favourite) {
        this.is_favourite = is_favourite;
    }

    public ArrayList<OrderModel> getOrders() {
        if(orders == null){
            return new ArrayList<>();
        }
        return orders;
    }

    public void setOrders(ArrayList<OrderModel> orders) {
        this.orders = orders;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isNon_restricted_country() {
        return non_restricted_country;
    }

    public void setNon_restricted_country(boolean non_restricted_country) {
        this.non_restricted_country = non_restricted_country;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCommon_user(UserProfile common_user) {
        this.common_user = common_user;
    }

    public void setIs_offered(Boolean is_offered) {
        this.is_offered = is_offered;
    }

    public void setNum_of_offers(int num_of_offers) {
        this.num_of_offers = num_of_offers;
    }

    public String getReported_id() {

        return reported_id;
    }

    public void setReported_id(String reported_id) {
        this.reported_id = reported_id;
    }

    public String getWant_to_buy_id() {

        return want_to_buy_id;
    }

    public void setWant_to_buy_id(String want_to_buy_id) {
        this.want_to_buy_id = want_to_buy_id;
    }

    public Date getExpired_date() {

        return expired_date;
    }

    public void setExpired_date(Date expired_date) {
        this.expired_date = expired_date;
    }

    public ArrayList<ProductImages> getProduct_images() {
        if(product_images == null){
            return new ArrayList<>();
        }
        return product_images;
    }

    public void setProduct_images(ArrayList<ProductImages> product_images) {
        this.product_images = product_images;
    }

    public ArrayList<RsCountry> getCountries() {
        if(countries == null){
            return new ArrayList<>();
        }
        return countries;
    }

    public void setCountries(ArrayList<RsCountry> countries) {
        this.countries = countries;
    }

    public Boolean getIs_offered() {
        return is_offered;
    }

    public int getNum_of_offers() {
        return num_of_offers;
    }

    public int getId() {
        return id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public String getName() {
        if(name == null){
            return "";
        }
        return name;
    }

    public String getDescription() {
        if(description == null){
            return "";
        }
        return description;
    }

    public UserProfile getCommon_user() {
        if(common_user == null){
            return new UserProfile();
        }
        return common_user;
    }
}
