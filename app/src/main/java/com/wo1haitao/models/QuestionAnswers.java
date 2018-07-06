package com.wo1haitao.models;

import com.wo1haitao.api.response.UserProfile;

import java.util.ArrayList;

/**
 * Created by nguyenphucthinh on 6/19/17.
 */

public class QuestionAnswers {
    public long id;
    public UserProfile common_user;
    public ProductListing product_listing;
    public String content;
    public String created_at;
    public ArrayList<QuestionReplyModel> question_answer_replies;
    String flag;
    boolean is_shown = false;

    public boolean is_shown() {
        return is_shown;
    }

    public void setIs_shown(boolean is_shown) {
        this.is_shown = is_shown;
    }

    public String getCreated_at() {
        if(created_at == null){
            return "";
        }
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getFlag() {
        if(flag == null){
            return "";
        }
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public ArrayList<QuestionReplyModel> getQuestion_answer_replies() {
        if(question_answer_replies == null){
            return new ArrayList<>();
        }
        return question_answer_replies;
    }

    public void setQuestion_answer_replies(ArrayList<QuestionReplyModel> question_answer_replies) {
        this.question_answer_replies = question_answer_replies;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserProfile getCommon_user() {
        if(common_user == null){
            return new UserProfile();
        }
        return common_user;
    }

    public void setCommon_user(UserProfile common_user) {
        this.common_user = common_user;
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

    public String getContent() {
        if(content == null){
            return "";
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
