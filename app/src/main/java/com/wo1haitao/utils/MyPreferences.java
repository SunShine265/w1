package com.wo1haitao.utils;

import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;
import com.wo1haitao.CustomApp;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.models.DataModelCategory;
import com.wo1haitao.models.DataModelCountry;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user on 5/11/2017.
 */

public final class MyPreferences {
    public static String TOKEN_FILE_NAME = "token_file";
    public final static String UID_FIELD = "Uid";
    public final static String TOKEN_FIELD = "Access-Token";
    public final static String CLIENT_FIELD = "Client";
    public final static String EXPIRY_FIELD = "Expiry";
    public final static String ID = "id";
    public final static String KEY_SUBSCRIBE = "key-subscribe";
    public final static String KEY_SUBSCRIBE_NATIVE = "key_subscribe_native";

    public final static  String KEY_GET_CATEGORY = "category_file";
    public final static  String KEY_GET_COUNTRY = "country_file";
    public final static  String KEY_GET_SHIPPING_METHOD = "shipping_method_file";
    public final static String CHECK_SETUP_CATEGORY = "check_setup_category";
    public final static String CHECK_SETUP_ACCOUNT = "check_setup_account";
    public final static String CHECK_SETUP_VERIFY = "check_setup_verify";

    public final static  String KEY_GET_IMAGE = "link_image";
    public static String DATA_FILE_NAME = "data_file";
    public static String DEVICE_TOKEN_FILE = "device_token";

    public static void setID(int value){
        SharedPreferences token = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = token.edit();
        edit.putInt(ID, value);
        edit.commit();
    }

    public static  int getID(){
        SharedPreferences preferences = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
        return preferences.getInt(ID, 0);
    }

    public static void setDeviceToken(String value){
        SharedPreferences token = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = token.edit();
        edit.putString(DEVICE_TOKEN_FILE, value);
        edit.commit();
    }

    public static String getDeviceToken(){
        SharedPreferences preferences = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
        return preferences.getString(DEVICE_TOKEN_FILE, "");
    }

    public static  String getKeySubscribeNative(){
        SharedPreferences preferences = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
        return preferences.getString(KEY_SUBSCRIBE_NATIVE, "");
    }

    public static void setKeySubscribeNative(String value){
        SharedPreferences token = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = token.edit();
        edit.putString(KEY_SUBSCRIBE_NATIVE, value);
        edit.commit();
    }

    public static  void removeKeySubscribeNative(){
        SharedPreferences preferences = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_SUBSCRIBE_NATIVE);
        editor.apply();
    }

    public static void setKeySubscribe(String value){
        SharedPreferences token = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = token.edit();
        edit.putString(KEY_SUBSCRIBE, value);
        edit.commit();
    }


    public static  String getKeySubscribe(){
        SharedPreferences preferences = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
        return preferences.getString(KEY_SUBSCRIBE, "");
    }

    public static  void removeKeySubscribe(){
        SharedPreferences preferences = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_SUBSCRIBE);
        editor.apply();
    }

    private static String UID = "";
    public static void setUID(String value){
        SharedPreferences token = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = token.edit();
        edit.putString(UID_FIELD, value);
        edit.commit();
        UID = value;
    }

    public static String getUID(){
        if(UID == null || UID.isEmpty()){
            SharedPreferences preferences = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
            UID = preferences.getString(UID_FIELD, "");
        }
        return UID;
    }

    private  static String TOKEN = "";
    public static void setToken(String value){
        SharedPreferences.Editor edit = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE).edit();
        edit.putString(TOKEN_FIELD, value);
        edit.commit();
        TOKEN = value;
    }

    public static String getToken(){
        if(TOKEN == null || TOKEN.isEmpty() == true){
            SharedPreferences preferences = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
            TOKEN = preferences.getString(TOKEN_FIELD, "");
        }
        return TOKEN;
    }

    private  static String CLIENT = "";
    public static void setClient(String value){
        SharedPreferences.Editor edit = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE).edit();
        edit.putString(CLIENT_FIELD, value);
        edit.commit();
        CLIENT = value;
    }

    public static String getClient(){
        if(CLIENT == null || CLIENT.isEmpty() == true){
            SharedPreferences preferences = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
            CLIENT = preferences.getString(CLIENT_FIELD, "");
        }
        return CLIENT;
    }
    private static String EXPIRY = "";
    public static void setExpiryField(String value){
        SharedPreferences.Editor edit = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE).edit();
        edit.putString(EXPIRY_FIELD, value);
        edit.commit();
        EXPIRY = value;
    }

    public static String getExpiryField(){
        if(EXPIRY == null || EXPIRY.isEmpty()) {
            SharedPreferences preferences = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
            EXPIRY = preferences.getString(EXPIRY_FIELD, "");
        }
        return EXPIRY;
    }

    public static void clearToken() {
        setClient("");
        setUID("");
        setExpiryField("");
        setToken("");
    }

    public static boolean checkToken() {
        if(getToken().equals("") || getClient().equals("") || getUID().equals("")){
            return false;
        }
        return true;
    }

    public static void SetDataFromServer(List<Object> objects, String key){
        String jsonCountries = ApiServices.getGsonBuilder().create().toJson(objects);
        SharedPreferences.Editor edit = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE).edit();
        edit.remove(key);
        edit.putString(key, jsonCountries);
        edit.commit();
    }

    public static HashMap<String, String> GetDataFromMyPreferences(String key){
        try {
            SharedPreferences preferences = CustomApp.getInstance().getSharedPreferences(TOKEN_FILE_NAME, MODE_PRIVATE);
            String jsonObject = preferences.getString(key, "[]");
            HashMap<String, String> value = new HashMap<>();
            if(key.equals(KEY_GET_CATEGORY)) {
                Type listType = new TypeToken<List<DataModelCategory>>() {}.getType();
                List<DataModelCategory> listObjects = ApiServices.getGsonBuilder().create().fromJson(jsonObject, listType);
                for (int i = 0; i < listObjects.size(); i++) {
                    if(listObjects.get(i).isActive()) {
                        value.put(String.valueOf(String.valueOf(listObjects.get(i).getId())), listObjects.get(i).getName());
                    }
                }
            }
            if(key.equals(KEY_GET_COUNTRY)){
                Type listType = new TypeToken<List<DataModelCountry>>() {}.getType();
                List<DataModelCountry> listObjects = ApiServices.getGsonBuilder().create().fromJson(jsonObject, listType);
                for (int i = 0; i < listObjects.size(); i++) {
                    if(listObjects.get(i).isActive()) {
                        value.put(String.valueOf(String.valueOf(listObjects.get(i).getId())), listObjects.get(i).getName());
                    }
                }
            }
            if(key.equals(KEY_GET_SHIPPING_METHOD)){
                Type listType = new TypeToken<List<DataModelCategory>>() {}.getType();
                List<DataModelCategory> listObjects = ApiServices.getGsonBuilder().create().fromJson(jsonObject, listType);
                for (int i = 0; i < listObjects.size(); i++) {
                    if(listObjects.get(i).isActive()) {
                        value.put(String.valueOf(String.valueOf(listObjects.get(i).getId())), listObjects.get(i).getName());
                    }
                }
            }
            return value;
        }
        catch (Exception ex){
            return new HashMap<String, String>();
        }
    }

    public static void setDataImage(String key, String value){
        SharedPreferences.Editor edit = CustomApp.getInstance().getSharedPreferences(DATA_FILE_NAME, MODE_PRIVATE).edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getValueofImage(String key){
        SharedPreferences preferences = CustomApp.getInstance().getSharedPreferences(DATA_FILE_NAME, MODE_PRIVATE);
        return preferences.getString(key, "");
    }
}
