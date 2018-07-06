package com.wo1haitao.api.response;

/**
 * Created by goodproductssoft on 6/23/17.
 */

public class MessageRs {
    int id;
    String content;
    String created_at;
    String current_time;
    String created_at_in_words = "...";

    public String getCreated_at_in_words() {
        return created_at_in_words;
    }

    public void setCreated_at_in_words(String created_at_in_words) {
        this.created_at_in_words = created_at_in_words;
    }

    public String getCurrent_time() {
        if(current_time == null){
            return "";
        }
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
