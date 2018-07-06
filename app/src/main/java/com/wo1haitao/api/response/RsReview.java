package com.wo1haitao.api.response;

import java.util.ArrayList;

/**
 * Created by user on 6/16/2017.
 */

public class RsReview {
    long id;
    String nickname, email, first_name, last_name;
    ArrayList<RsReviewAwaitting> reviews_awaiting;
    ArrayList<RsReviewToMe> reviews_as_buyer;
    ArrayList<RsMyReview> reviews_as_seller;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        if(nickname == null){
            return "";
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public ArrayList<RsReviewAwaitting> getReviews_awaiting() {
        if(reviews_awaiting == null){
            return new ArrayList<>();
        }
        return reviews_awaiting;
    }

    public void setReviews_awaiting(ArrayList<RsReviewAwaitting> reviews_awaiting) {
        this.reviews_awaiting = reviews_awaiting;
    }

    public ArrayList<RsReviewToMe> getReviews_as_buyer() {
        if(reviews_as_buyer == null){
            return new ArrayList<>();
        }
        return reviews_as_buyer;
    }

    public void setReviews_as_buyer(ArrayList<RsReviewToMe> reviews_as_buyer) {
        this.reviews_as_buyer = reviews_as_buyer;
    }

    public ArrayList<RsMyReview> getReviews_as_seller() {
        if(reviews_as_seller == null){
            return new ArrayList<>();
        }
        return reviews_as_seller;
    }

    public void setReviews_as_seller(ArrayList<RsMyReview> reviews_as_seller) {
        this.reviews_as_seller = reviews_as_seller;
    }

}
