package com.wo1haitao.models;

/**
 * Created by user on 5/26/2017.
 */

public class ProductImage {
    public String url;
    public ThumbUrl thumb;

    public String getUrl() {
        if(url == null){
            return "";
        }
        return url;
    }

    public ThumbUrl getThumb() {
        if(thumb == null){
            return new ThumbUrl();
        }
        return thumb;
    }
}
