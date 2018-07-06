package com.wo1haitao.models;

/**
 * Created by user on 4/25/2017.
 */

public class CustomCountryForm {
    String content;
    boolean isEnable;

    public CustomCountryForm(String content, boolean isEnable) {
        this.content = content;
        this.isEnable = isEnable;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public String getContent() {
        if(content == null){
            return "";
        }
        return content;
    }

    public boolean isEnable() {
        return isEnable;
    }
}
