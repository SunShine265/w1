package com.wo1haitao.models;

/**
 * Created by user on 6/29/2017.
 */

public class QuestionAnswer {
    long id;
    String created_at;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
