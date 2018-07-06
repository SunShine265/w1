package com.wo1haitao.models;

/**
 * Created by user on 9/21/2017.
 */

public class PriceModel {
    String fractional;

    public PriceModel(String fractional) {
        this.fractional = fractional;
    }

    public String getFractional() {
        return fractional;
    }

    public void setFractional(String fractional) {
        this.fractional = fractional;
    }
}
