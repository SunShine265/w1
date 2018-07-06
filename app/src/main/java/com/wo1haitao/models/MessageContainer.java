package com.wo1haitao.models;

import com.wo1haitao.api.response.UserProfile;

import java.util.ArrayList;

/**
 * Created by user on 7/10/2017.
 */

public class MessageContainer {
    int id;
    String pusher_channel;
    UserProfile purchaser, tenderer;
    ProductListing product_listing;
    ArrayList<Message> messages;
    float average_rating;

    public float getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(float average_rating) {
        this.average_rating = average_rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPusher_channel() {
        return pusher_channel;
    }

    public void setPusher_channel(String pusher_channel) {
        this.pusher_channel = pusher_channel;
    }

    public UserProfile getPurchaser() {
        if(purchaser == null){
            return new UserProfile();
        }
        return purchaser;
    }

    public void setPurchaser(UserProfile purchaser) {
        this.purchaser = purchaser;
    }

    public UserProfile getTenderer() {
        if(tenderer == null){
            return new UserProfile();
        }
        return tenderer;
    }

    public void setTenderer(UserProfile tenderer) {
        this.tenderer = tenderer;
    }

    public ProductListing getProduct_listing() {
        if(product_listing == null){
            return new ProductListing();
        }
        return product_listing;
    }

    public void setProduct_listing(ProductListing product_listing) {
        this.product_listing = product_listing;
    }

    public ArrayList<Message> getMessages() {
        if(messages == null){
            return new ArrayList<>();
        }
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
