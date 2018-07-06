package com.wo1haitao.models;

import com.wo1haitao.api.response.UserProfile;

/**
 * Created by user on 7/10/2017.
 */

public class Message {
    long id, message_container_id;
    String content, created_at;
    UserProfile sender;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMessage_container_id() {
        return message_container_id;
    }

    public void setMessage_container_id(long message_container_id) {
        this.message_container_id = message_container_id;
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

    public String getCreated_at() {
        if(created_at == null){
            return "";
        }
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public UserProfile getSender() {
        if(sender == null){
            return new UserProfile();
        }
        return sender;
    }

    public void setSender(UserProfile sender) {
        this.sender = sender;
    }
}
