package com.wo1haitao.models;

import java.util.ArrayList;

/**
 * Created by goodproductssoft on 4/20/17.
 */

public class ProductModel {
    public String name;
    public String picture;
    public String category;
    public String country;
    public int bid;
    public int id;
    public ArrayList<ProductImages> product_images;
    public ProductListing product_listing;
    public ArrayList<CountryModel> countries;

    public String getPicture() {
        return picture;
    }

    public ArrayList<ProductImages> getProduct_images() {
        if(product_images == null){
            return new ArrayList<>();
        }
        return product_images;
    }

    public ProductListing getProduct_listing() {
        if(product_listing == null){
            return new ProductListing();
        }
        return product_listing;
    }

    public ArrayList<CountryModel> getCountries() {
        if(countries == null){
            return new ArrayList<>();
        }
        return countries;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        if(name == null){
            return "";
        }
        return name;
    }

    public String getCategory() {
        if(category == null){
            return "";
        }
        return category;
    }

    public String getCountry() {
        if(country == null){
            return "";
        }
        return country;
    }

    public int getBid() {
        return bid;
    }


    public ProductModel(String name, String picture, String category, String country, int bid, Boolean is_favourite) {
        this.name = name;
        this.picture = picture;
        this.category = category;
        this.country = country;
        this.bid = bid;
    }


}
