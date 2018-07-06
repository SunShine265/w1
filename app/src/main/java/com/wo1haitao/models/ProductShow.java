package com.wo1haitao.models;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.response.RsCountry;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.utils.MyPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 5/19/2017.
 */

public class ProductShow {
    String name;
    String category;
    String country;
    String date_limit;

    public String getDate_limit_my_post() {
        return date_limit_my_post;
    }

    public void setDate_limit_my_post(String date_limit_my_post) {
        this.date_limit_my_post = date_limit_my_post;
    }

    String date_limit_my_post;

    public void setList_image(ArrayList<String> list_image) {
        this.list_image = list_image;
    }

    ArrayList<String> list_image;
    UserProfile common_user;
    int id;
    String description;
    long num_of_offers;
    boolean is_favourite;
    int day;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public long getNum_of_offers() {
        return num_of_offers;
    }

    public UserProfile getCommon_user() {
        return common_user;
    }

    public boolean is_favourite() {
        return is_favourite;
    }

    public int getDay() {
        String  number = date_limit.replaceAll("[^0-9]", "");
        if(number.isEmpty() == true){
            return -1;
        }
        return 1;
    }
    String expiry_status;

    public String getExpiry_status() {

        return expiry_status;
    }

    public void setExpiry_status(String expiry_status) {
        this.expiry_status = expiry_status;
    }

    public ProductShow(RsProduct product) {
        common_user = product.getCommon_user();
        is_favourite = product.is_favourite();
        num_of_offers = product.getNum_of_offers();
        description = product.getDescription();
        name = product.getName();
        category = "";
//        List<String> product_type = Arrays.asList(CustomApp.getInstance().getResources().getStringArray(R.array.form_category_list));
        ArrayList<String> myListCategory = new ArrayList<>();
        HashMap<String, String> hashmapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
        if(hashmapCategory != null) {
            for (Map.Entry entry : hashmapCategory.entrySet()) {
                myListCategory.add(entry.getValue().toString());
                int id = Integer.parseInt(entry.getKey().toString());
                if (id == product.getCategory_id()) {
                    category = entry.getValue().toString();
                }
            }
        }
        expiry_status = product.getExpiry_status();
//        category = myListCategory.get(product.getCategory_id() - 1);

        String country_product="";
        if(product.isNon_restricted_country()){
            country_product = CustomApp.getInstance().getString(R.string.txt_all_country);
        }
        else
        {
            if(product.getCountries()!=null){
                for(RsCountry country1 : product.getCountries()){
                    if(country_product.isEmpty()){
                        country_product += country1.getName();
                    } else {
                        country_product += ", " + country1.getName();
                    }
                }
            }
        }
        country = country_product;
        date_limit = "";
        Date date =  Calendar.getInstance().getTime();
        Date expert = product.getExpired_date_date();
        if(product.getExpired_date_text().isEmpty() == false){
            date_limit = product.getExpired_date_text();
            date_limit_my_post = product.getExpired_date_text_my_bid();
        }
        else{
            if(expert!=null){
                long restDatesinMillis = expert.getTime()-date.getTime();
                this.day = (int) (restDatesinMillis/(24*60*60*1000));
                if(this.day <= 0){
                    date_limit = "已过期";
                    date_limit_my_post = "已过期";
                }
                else{
                    date_limit = CustomApp.getInstance().getString(R.string.ad_limmit, day);
                    date_limit_my_post = CustomApp.getInstance().getString(R.string.ad_limmit, day);
                }

            }
        }

        if(product.getProduct_images()!=null){
            list_image = new ArrayList<>();
//            for(RsImageUri image : product.getProduct_images()){
//                list_image.add(image.getUrl_thumb());
//            }
            for(int i = 0; i < product.getProduct_images().size(); i++){
                list_image.add(ApiServices.BASE_URI + product.getProduct_images().get(i).getProduct_image().getThumb().url);
            }
        }
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

    public String getCategory() {
        if(category == null){
            return "";
        }
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCountry() {
        if(country == null){
            return "";
        }
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate_limit() {
        if(date_limit == null){
            return "";
        }
        return date_limit;
    }

    public void setDate_limit(String date_limit) {
        this.date_limit = date_limit;
    }

    public List<String> getList_image() {
        if(list_image == null){
            return new ArrayList<>();
        }
        return list_image;
    }

//    public void setList_image(List<String> list_image) {
//        this.list_image = list_image;
//    }
}
