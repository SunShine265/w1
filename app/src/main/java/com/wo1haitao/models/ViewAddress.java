package com.wo1haitao.models;

/**
 * Created by user on 5/17/2017.
 */

public class ViewAddress {
    String full_address;
    boolean is_default;

    public String getFull_address() {
        return full_address;
    }

    public ViewAddress() {

    }

    public ViewAddress(String full_address, boolean is_default) {
        this.full_address = full_address;
        this.is_default = is_default;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public boolean is_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }
}
