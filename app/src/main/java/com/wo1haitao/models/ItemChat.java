package com.wo1haitao.models;

/**
 * Created by user on 7/10/2017.
 */

public class ItemChat {
    String content, created_at;
    int idSender;
    int flag;

    public ItemChat(String content, String created_at, int idSender, int flag) {
        this.content = content;
        this.created_at = created_at;
        this.idSender = idSender;
        this.flag = flag;
    }

    public int isFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }
}
