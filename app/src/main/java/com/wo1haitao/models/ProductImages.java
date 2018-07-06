package com.wo1haitao.models;

/**
 * Created by user on 5/26/2017.
 */

public class ProductImages {
    public int id;
    public ProductImage product_image;

    public int getId() {
        return id;
    }

    public ProductImage getProduct_image() {
        if(product_image == null){
            return new ProductImage();
        }
        return product_image;
    }
}
