package com.wo1haitao.models;

/**
 * Created by user on 5/26/2017.
 */

public class ShippingMethods {
    public Double id;
    public String name;
    public Boolean active;

    public Double getId() {
        return id;
    }

    public String getName() {
        if(name == null){
            return "";
        }
        return name;
    }

    public Boolean getActive() {
        return active;
    }
}
