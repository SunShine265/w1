package com.wo1haitao.models;

/**
 * Created by user on 9/21/2017.
 */

public class CurrencyModel {
    String id;
    String decimal_mark;
    String html_entity;
    String iso_code;
    String iso_numeric;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDecimal_mark() {
        return decimal_mark;
    }

    public void setDecimal_mark(String decimal_mark) {
        this.decimal_mark = decimal_mark;
    }

    public String getHtml_entity() {
        return html_entity;
    }

    public void setHtml_entity(String html_entity) {
        this.html_entity = html_entity;
    }

    public String getIso_code() {
        return iso_code;
    }

    public void setIso_code(String iso_code) {
        this.iso_code = iso_code;
    }

    public String getIso_numeric() {
        return iso_numeric;
    }

    public void setIso_numeric(String iso_numeric) {
        this.iso_numeric = iso_numeric;
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

    String name;
}
