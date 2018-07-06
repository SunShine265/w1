package com.wo1haitao.api.response;

import com.wo1haitao.models.CategoryModel;
import com.wo1haitao.models.OrderModel;
import com.wo1haitao.models.ProductImages;
import com.wo1haitao.models.ProductTenders;
import com.wo1haitao.models.QuestionAnswers;
import com.wo1haitao.models.ShippingMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 5/18/2017.
 */

public class RsProduct {

    int id;
    int category_id;
    String name;
    String description;
    String expired_date;
    boolean draft;
    long common_user_id;
    boolean non_restricted_country;
    boolean closed;
    boolean banned;
    boolean new_product;
    boolean used_product;
    boolean is_favourite;
    long num_of_offers;
    CategoryModel category;
    UserProfile common_user;
    String deleted_at;
    boolean new_alert_flag;
    List<RsCountry> countries;
    ArrayList<ProductImages> product_images;
    Date expired_date_date;
    ArrayList<ProductTenders> product_tenders;
    ArrayList<ShippingMethods> shipping_methods;
    ArrayList<QuestionAnswers> question_answers;
    ArrayList<OrderModel> orders;
    Boolean is_offered;
    Boolean offer_accepted = false;
    String reported_id, want_to_buy_id;
    UserProfile userProfileMyPost;
    String expired_date_text = "";
    boolean expired;
    String expiry_status;

    public String getExpiry_status() {
        if(expiry_status == null){
            if(expired_date_text != null) {
                expiry_status = expired_date_text;
            }
            else{
                expiry_status = "";
            }
        }
        return expiry_status;
    }

    public void setExpiry_status(String expiry_status) {
        this.expiry_status = expiry_status;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getExpired_date_text_my_bid() {
        if(expired_date_text == null){
            expired_date_text = "";
        }
        if(expired_date_text.length() < 6){
            expired_date_text = expired_date_text.replace("广告","");
        }
        return expired_date_text;
    }

    public void setExpired_date_text(String expired_date_text) {

        this.expired_date_text = expired_date_text;
    }
    public String getExpired_date_text() {
        if(expired_date_text == null){
            expired_date_text = "";
        }
        return expired_date_text;
    }

    public CategoryModel getCategory() {
        if(category == null){
            return new CategoryModel();
        }
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public UserProfile getUserProfileMyPost() {
        if(userProfileMyPost == null){
            return new UserProfile();
        }
        return userProfileMyPost;
    }

    public void setUserProfileMyPost(UserProfile userProfileMyPost) {
        this.userProfileMyPost = userProfileMyPost;
    }

    public RsProduct(ProductTenders offerMeModel){
        want_to_buy_id = offerMeModel.getProduct_listing().getWant_to_buy_id();
        reported_id = offerMeModel.getProduct_listing().getReported_id();
        name = offerMeModel.getProduct_listing().getName();
        category_id = offerMeModel.getProduct_listing().getCategory_id();
        userProfileMyPost = offerMeModel.getCommon_user();
        product_images = offerMeModel.getProduct_listing().getProduct_images();
    }
    public RsProduct(){

    }

    public ArrayList<OrderModel> getOrders() {
        if(orders == null){
            return new ArrayList<>();
        }
        return orders;
    }

    public void setOrders(ArrayList<OrderModel> orders) {
        this.orders = orders;
    }

    public String getReported_id() {

        return reported_id;
    }

    public void setReported_id(String reported_id) {
        this.reported_id = reported_id;
    }

    public String getWant_to_buy_id() {

        return want_to_buy_id;
    }

    public void setWant_to_buy_id(String want_to_buy_id) {
        this.want_to_buy_id = want_to_buy_id;
    }

    public Boolean getOffer_accepted() {
        for(OrderModel order : orders){
            if(order.getStatus().equals(OrderModel.STATE_CONFIRM) || order.getStatus().equals(OrderModel.STATE_PROCESS)){
                offer_accepted = true;
            }
        }
        return offer_accepted;
    }

    public void setOffer_accepted(Boolean offer_accepted) {
        this.offer_accepted = offer_accepted;
    }

    public ArrayList<OrderModel> getOrderModels() {
        if(orders == null){
            return new ArrayList<>();
        }
        return orders;
    }

    public void setOrderModels(ArrayList<OrderModel> orderModels) {
        this.orders = orderModels;
    }

    public void setCommon_user(UserProfile common_user) {
        this.common_user = common_user;
    }

    public void setProduct_images(ArrayList<ProductImages> product_images) {
        this.product_images = product_images;
    }

    public void setProduct_tenders(ArrayList<ProductTenders> product_tenders) {
        this.product_tenders = product_tenders;
    }

    public ArrayList<QuestionAnswers> getQuestion_answers() {
        if(question_answers == null){
            return new ArrayList<>();
        }
        return question_answers;
    }

    public void setQuestion_answers(ArrayList<QuestionAnswers> question_answers) {
        this.question_answers = question_answers;
    }

    public Boolean getIs_offered() {
        return is_offered;
    }

    public void setIs_offered(Boolean is_offered) {
        this.is_offered = is_offered;
    }

    public ArrayList<ShippingMethods> getShipping_methods() {
        if(shipping_methods == null){
            return new ArrayList<>();
        }
        return shipping_methods;
    }

    public void setShipping_methods(ArrayList<ShippingMethods> shipping_methods) {
        this.shipping_methods = shipping_methods;
    }

    public ArrayList<ProductImages> getProduct_images() {
        if(product_images == null){
            new ArrayList<>();
        }
        return product_images;
    }

    public ArrayList<ProductTenders> getProduct_tenders() {
        if(product_tenders == null){
            return new ArrayList<>();
        }
        return product_tenders;
    }

    public UserProfile getCommon_user() {
        if(common_user == null){
            return new UserProfile();
        }
        return common_user;
    }

    public long getNum_of_offers() {
        return num_of_offers;
    }

    public void setNum_of_offers(long num_of_offers) {
        this.num_of_offers = num_of_offers;
    }

    public boolean is_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(boolean is_favourite) {
        this.is_favourite = is_favourite;
    }

    public List<RsCountry> getCountries() {
        if(countries == null){
            return new ArrayList<>();
        }
        return countries;
    }

    public void setCountries(List<RsCountry> countries) {
        this.countries = countries;
    }

//    public List<RsImageUri> getProduct_images() {
//        return product_images;
//    }
//
//    public void setProduct_images(List<RsImageUri> product_images) {
//        this.product_images = product_images;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
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

    public String getDescription() {
        if(description == null){
            return "";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getExpired_date() {
        return expired_date;
    }

    public void setExpired_date(String expired_date) {
        this.expired_date = expired_date;
    }

    public Date getExpired_date_date() {
       try {
           if(expired_date!=null) {
               SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
               Date date = format.parse(expired_date);
               return date;
           }
        } catch (ParseException e) {
            e.printStackTrace();
           return expired_date_date;
        }
        return null;
    }

    public void setExpired_date_date(Date expired_date_date) {
        this.expired_date_date = expired_date_date;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public long getCommon_user_id() {
        return common_user_id;
    }

    public void setCommon_user_id(long common_user_id) {
        this.common_user_id = common_user_id;
    }

    public boolean isNon_restricted_country() {
        return non_restricted_country;
    }

    public void setNon_restricted_country(boolean non_restricted_country) {
        this.non_restricted_country = non_restricted_country;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public boolean isNew_product() {
        return new_product;
    }

    public void setNew_product(boolean new_product) {
        this.new_product = new_product;
    }

    public boolean isUsed_product() {
        return used_product;
    }

    public void setUsed_product(boolean used_product) {
        this.used_product = used_product;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public boolean isNew_alert_flag() {
        return new_alert_flag;
    }

    public void setNew_alert_flag(boolean new_alert_flag) {
        this.new_alert_flag = new_alert_flag;
    }
}
