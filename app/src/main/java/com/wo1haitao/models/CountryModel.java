package com.wo1haitao.models;

/**
 * Created by user on 5/26/2017.
 */

public class CountryModel {
    public int id;
    public int  region_id;
    public String name;
    public Boolean active;
    public String slug;

    public int getId() {
        return id;
    }

    public int getRegion_id() {
        return region_id;
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

    public String getSlug() {
        return slug;
    }
}
