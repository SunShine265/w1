package com.wo1haitao.api.response;

import com.wo1haitao.models.ProductListing;

/**
 * Created by goodproductssoft on 9/28/17.
 */

public class CommentItemView {
    public static final int TYPE_COMMENT = 0;
    public static final int TYPE_RECOMMENT = 1;
    public static final int TYPE_BOX = 2;
    public static final int TYPE_REBOX = 3;

    public long id;
    public UserProfile common_user;
    public String content;
    public String created_at;
    public int type_of_view = TYPE_COMMENT;

    private boolean has_box_current = false;

    public boolean isHas_box_current() {
        return has_box_current;
    }

    public void setHas_box_current(boolean has_box_current) {
        this.has_box_current = has_box_current;
    }

    public boolean is_rebox_show() {
        return is_rebox_show;
    }

    public void setIs_rebox_show(boolean is_rebox_show) {
        this.is_rebox_show = is_rebox_show;
    }

    private boolean is_reply = false;
    private boolean is_rebox_show = false;

    public boolean is_reply() {
        return is_reply;
    }

    public void setIs_reply(boolean is_reply) {
        this.is_reply = is_reply;
    }

    public CommentItemView(long id, UserProfile common_user, String content, String created_at) {
        this.id = id;
        this.common_user = common_user;
        this.content = content;
        this.created_at = created_at;
    }

    public CommentItemView(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserProfile getCommon_user() {
        return common_user;
    }

    public void setCommon_user(UserProfile common_user) {
        this.common_user = common_user;
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

    public int getType_of_view() {
        return type_of_view;
    }

    public void setType_of_view(int type_of_view) {
        this.type_of_view = type_of_view;
    }
}
