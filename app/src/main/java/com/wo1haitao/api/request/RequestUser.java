package com.wo1haitao.api.request;

import java.io.Serializable;

/**
 * Created by user on 5/10/2017.
 */

public class RequestUser implements Serializable{
    String nickname;
    String email;
    String password;
    String password_confirmation;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public RequestUser(String nickname, String email, String password, String password_confirmation) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }
}
