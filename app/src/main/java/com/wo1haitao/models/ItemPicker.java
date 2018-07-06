package com.wo1haitao.models;

/**
 * Created by goodproductssoft on 11/29/17.
 */

public class ItemPicker {
    private String id = "";
    private String name = "";
    private Boolean isChecked = false;

    public ItemPicker() {
    }

    public ItemPicker(String id, String name, Boolean isChecked) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
