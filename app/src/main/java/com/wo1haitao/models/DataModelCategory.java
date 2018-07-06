package com.wo1haitao.models;

/**
 * Created by user on 7/20/2017.
 */

public class DataModelCategory {
    long id;
    String name;
    boolean active;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
