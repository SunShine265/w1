package com.wo1haitao.api.response;

/**
 * Created by user on 5/18/2017.
 */

public class RsProductImage {
    int id;
    RsImageUri product_image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RsImageUri getProduct_image() {
        if(product_image == null){
            return new RsImageUri();
        }
        return product_image;
    }

    public void setProduct_image(RsImageUri product_image) {
        this.product_image = product_image;
    }
}
