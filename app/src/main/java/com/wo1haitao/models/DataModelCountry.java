package com.wo1haitao.models;

/**
 * Created by user on 7/20/2017.
 */

public class DataModelCountry {
    long id, region_id;
    String name;
    boolean active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRegion_id() {
        return region_id;
    }

    public void setRegion_id(long region_id) {
        this.region_id = region_id;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
