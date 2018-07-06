package com.wo1haitao.api.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 5/10/2017.
 */
public class ErrorMessage {
    JsonObject jsonObjectError;
    String stringError;
    HashMap<String,String> list_value;

    public List<String> getList_error() {
        return list_error;
    }

    public void setList_error(List<String> list_error) {
        this.list_error = list_error;
    }

    List<String> list_error;

    public JsonArray getJsonArrayError() {
        return jsonArrayError;
    }

    public void setJsonArrayError(JsonArray jsonArrayError) {
        this.jsonArrayError = jsonArrayError;
    }

    JsonArray jsonArrayError;

    public HashMap<String, String> getList_value() {
        return list_value;
    }

    public void setList_value(HashMap<String, String> list_value) {
        this.list_value = list_value;
    }

    public JsonObject getJsonObjectError() {
        return jsonObjectError;
    }

    public void setJsonObjectError(JsonObject jsonObjectError) {
        this.jsonObjectError = jsonObjectError;
    }

    public String getStringError()
    {
        if(stringError == null){
            return "";
        }
        return stringError;
    }

    public void setStringError(String stringError) {
        this.stringError = stringError;
    }

    public String getStringErrFormList(){
        String err ="";
        if(list_value!=null){
           for(String key : list_value.keySet()){
               err += key + ": "+ list_value.get(key) + "\n";
           }
           err = err.substring(0,err.length()-1);
        }
        return err;
    }
}
