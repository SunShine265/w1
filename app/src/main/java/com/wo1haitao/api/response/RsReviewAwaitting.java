package com.wo1haitao.api.response;

import com.wo1haitao.models.ProductImages;

import java.util.ArrayList;

/**
 * Created by user on 6/16/2017.
 */

public class RsReviewAwaitting {
    long id, category_id;
    String name, description;
    ArrayList<RsCountry> countries;
    ArrayList<ProductImages> product_images;
    UserProfile common_user;
    boolean non_restricted_country;

    public boolean isNon_restricted_country() {
        return non_restricted_country;
    }

    public void setNon_restricted_country(boolean non_restricted_country) {
        this.non_restricted_country = non_restricted_country;
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

    public ArrayList<RsCountry> getCountries() {
        if(countries == null){
            return new ArrayList<>();
        }
        return countries;
    }

    public void setCountries(ArrayList<RsCountry> countries) {
        this.countries = countries;
    }

    public ArrayList<ProductImages> getProduct_images() {
        return product_images;
    }

    public void setProduct_images(ArrayList<ProductImages> product_images) {
        this.product_images = product_images;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        if(name == null){
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        if(description == null){
            return "";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
