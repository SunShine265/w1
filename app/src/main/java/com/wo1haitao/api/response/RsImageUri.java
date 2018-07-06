package com.wo1haitao.api.response;

/**
 * Created by user on 5/18/2017.
 */

public class RsImageUri {
    String url;
    String url_thumb;

    public String getUrl() {
        if(url == null){
            return "";
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl_thumb() {
        return url_thumb;
    }

    public void setUrl_thumb(String url_thumb) {
        this.url_thumb = url_thumb;
    }
}
