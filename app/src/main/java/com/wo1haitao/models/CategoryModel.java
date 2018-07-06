package com.wo1haitao.models;

/**
 * Created by user on 6/1/2017.
 */

public class CategoryModel {
    int categoryImage;
    int id;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public CategoryModel(String categoryName, int categoryImage) {
        this.name = categoryName;
        this.categoryImage = categoryImage;
    }
    public CategoryModel(){

    }
    public String getCategoryName() {
        if(name == null){
            return "";
        }
        return name;
    }

    public int getCategoryImage() {

        return categoryImage;
    }
    public String toString(){
        return name;
    }
}
