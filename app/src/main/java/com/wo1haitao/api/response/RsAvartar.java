package com.wo1haitao.api.response;

import com.wo1haitao.models.PictureMedium;
import com.wo1haitao.models.PictureSmall;

/**
 * Created by user on 5/16/2017.
 */

public class RsAvartar {
    public String url;
    public PictureSmall small;
    public PictureMedium medium;

    public String getUrl() {
        if(url == null){
            return "";
        }
        return url;
    }

    public PictureSmall getUrlSmall() {
        return small;
    }

    public PictureMedium getMedium() {
        return medium;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSmall(PictureSmall small) {
        this.small = small;
    }

    public void setMedium(PictureMedium medium) {
        this.medium = medium;
    }


//    String uri_medium;

//    public String getUri_root() {
//        return uri_root;
//    }
//
//    public void setUri_root(String uri_root) {
//        this.uri_root = uri_root;
//    }
//
//    public String getUri_small() {
//        return uri_small;
//    }
//
//    public void setUri_small(String uri_small) {
//        this.uri_small = uri_small;
//    }
//
//    public String getUri_medium() {
//        return uri_medium;
//    }
//
//    public void setUri_medium(String uri_medium) {
//        this.uri_medium = uri_medium;
//    }
}
