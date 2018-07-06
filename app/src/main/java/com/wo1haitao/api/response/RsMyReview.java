package com.wo1haitao.api.response;

import com.wo1haitao.models.ProductListing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 6/16/2017.
 */

public class  RsMyReview {
    long id, product_listing_id, reviewer_id;
    String review_message, rating_score;
    UserProfile reviewee, reviewer;
    ProductListing product_listing;
    Map<String, String > rating;

    public UserProfile getReviewer() {
        if(reviewer == null){
            return new UserProfile();
        }
        return reviewer;
    }

    public void setReviewer(UserProfile reviewer) {
        this.reviewer = reviewer;
    }

    public Map<String, String> getRating() {
        if(rating == null){
            return new HashMap<>();
        }
        return rating;
    }

    public ArrayList<RatingUser> getListRating() {
        ArrayList<RatingUser> list_rating = new ArrayList<>();

        for(String key : rating.keySet()){
            RatingUser ratingUser = new RatingUser();
            ratingUser.title = key;
            ratingUser.rating = rating.get(key);
            list_rating.add(ratingUser);
        }
        Collections.sort(list_rating, new Comparator<RatingUser>() {
            @Override
            public int compare(RatingUser o1, RatingUser o2) {
                return 0;
            }
        });
        return list_rating;
    }

    public void setRating(Map<String, String> rating) {
        this.rating = rating;
    }

    public UserProfile getReviewee() {
        return reviewee;
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

    public void setReviewee(UserProfile reviewee) {
        this.reviewee = reviewee;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProduct_listing_id() {
        return product_listing_id;
    }

    public void setProduct_listing_id(long product_listing_id) {
        this.product_listing_id = product_listing_id;
    }

    public long getReviewer_id() {
        return reviewer_id;
    }

    public void setReviewer_id(long reviewer_id) {
        this.reviewer_id = reviewer_id;
    }

    public String getReview_message() {
        if(review_message == null){
            return review_message;
        }
        return review_message;
    }

    public void setReview_message(String review_message) {
        this.review_message = review_message;
    }

    public String getRating_score() {
        return rating_score;
    }

    public void setRating_score(String rating_score) {
        this.rating_score = rating_score;
    }
}
