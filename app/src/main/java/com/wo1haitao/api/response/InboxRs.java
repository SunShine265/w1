package com.wo1haitao.api.response;

import java.util.ArrayList;

/**
 * Created by goodproductssoft on 6/23/17.
 */

public class InboxRs {
    int id;
    UserProfile purchaser;
    UserProfile tenderer;
    String pusher_channel;
    ArrayList<MessageRs> messages;
    RsProduct product_listing;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPusher_channel() {
        if(pusher_channel == null){
            return "";
        }
        return pusher_channel;
    }

    public void setPusher_channel(String pusher_channel) {
        this.pusher_channel = pusher_channel;
    }

    public ArrayList<MessageRs> getMessages() {
        if(messages == null){
            return new ArrayList<MessageRs>();
        }
        return messages;
    }

    public void setMessages(ArrayList<MessageRs> messages) {
        this.messages = messages;
    }

    public RsProduct getProduct_listing() {
        if(product_listing == null){
            return new RsProduct();
        }
        return product_listing;
    }

    public void setProduct_listing(RsProduct product_listing) {
        this.product_listing = product_listing;
    }
}
