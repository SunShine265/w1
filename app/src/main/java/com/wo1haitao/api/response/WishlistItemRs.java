package com.wo1haitao.api.response;

import com.wo1haitao.models.CountryModel;
import com.wo1haitao.models.ProductImages;

import java.util.ArrayList;

/**
 * Created by goodproductssoft on 10/23/17.
 */

public class WishlistItemRs {
    private ArrayList<ProductImages> product_images;
    private UserProfile common_user;
    private ArrayList<CountryModel> countries;
    private RsProduct product_listing;

    public ArrayList<ProductImages> getProduct_images() {
        return product_images;
    }

    public void setProduct_images(ArrayList<ProductImages> product_images) {
        this.product_images = product_images;
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

    public ArrayList<CountryModel> getCountries() {
        if(countries == null){
            return new ArrayList<>();
        }
        return countries;
    }

    public void setCountries(ArrayList<CountryModel> countries) {
        this.countries = countries;
    }

    public RsProduct getProduct_listing() {
        if(product_listing == null){
            return new RsProduct();
        }
        return product_listing;
    }

    public void setProduct_listing(RsProduct product_listing) {
        this.product_listing = product_listing;
    }
}
