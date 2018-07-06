package com.wo1haitao.api.response;

/**
 * Created by user on 8/16/2017.
 */

public class RsInformationTermsModel {
    long id;
    String name, content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
