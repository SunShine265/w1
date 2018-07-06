package com.wo1haitao.api;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.wo1haitao.CustomApp;
import com.wo1haitao.activities.LoginActivity;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.RsAvartar;
import com.wo1haitao.api.response.RsImageUri;
import com.wo1haitao.utils.MyPreferences;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 5/10/2017.
 */

public class ApiServices {

    static final String UID_HEAD = "uid";
    static final String TOKEN_HEAD = "access-token";
    static final String CLIENT_HEAD = "client";
    static final String EXPIRY_HEAD = "expiry";

    static HashMap<String, String> list_key = getHashMap();
//    public static final String BASE_URI = "http://192.168.1.169:3000" ;
    public static final String BASE_URI = "https://www.wo1haitao.com";
    //public static final String BASE_URI = "http://210.245.33.102:3000";
    Retrofit retrofit;
    private static ApiServices instance = null;

    public static ApiServices getInstance() {
        if (instance == null) {
            instance = new ApiServices();
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public ApiServices() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader(UID_HEAD, MyPreferences.getUID())
                            .addHeader(TOKEN_HEAD, MyPreferences.getToken())
                            .addHeader(CLIENT_HEAD, MyPreferences.getClient())
                            .addHeader(EXPIRY_HEAD, MyPreferences.getExpiryField())
                            .build();
                    Response response = chain.proceed(request);

                    if(response.isSuccessful()){
                        String jsonReponse = response.body().string();
                        try {
                            JsonObject jsonObject = new JsonParser().parse(jsonReponse).getAsJsonObject();
                            JsonObject jsonObject2 = new JsonParser().parse(jsonReponse).getAsJsonObject();
                            if(jsonObject2.entrySet().size() < 2) {
                                jsonObject2 = new JsonParser().parse(jsonReponse).getAsJsonObject();
                                for (Map.Entry<String, JsonElement> entry : jsonObject2.entrySet()) {
                                    if (!entry.getKey().equals("errors") && !entry.getKey().equals("success")) {
                                        jsonObject.add("data", entry.getValue());
                                        jsonObject.remove(entry.getKey());
                                    }
                                }
                            }
                            response = response.newBuilder().body(ResponseBody.create(MediaType.parse("application/json"), jsonObject.toString())).build();
                        } catch (Exception ex){
                            response = response.newBuilder().body(ResponseBody.create(MediaType.parse("application/json"), jsonReponse)).build();
                        }
                    }
                    else if(response.code() == 401){
                        if(CustomApp.getInstance().mainActivity != null){
                            Intent itent = new Intent(CustomApp.getInstance().mainActivity, LoginActivity.class);
                            CustomApp.getInstance().mainActivity.startActivity(itent);
                            CustomApp.getInstance().mainActivity.finish();
                        }
                    }


                    return response;
                }
            }).build();


            Gson gson = getGsonBuilder().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URI)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
    }

    public static GsonBuilder getGsonBuilder() {
        return new GsonBuilder()
//                .registerTypeAdapter(ResponseMessageBase.class, new JsonObjectConverter<ResponseMessageBase>() {
//                    @Override
//                    public ResponseMessageBase onDeserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                        ResponseMessageBase responseMessageBase = ResponseMessage.class.
//
//                        if(json.isJsonObject()){
//                            json.getAsJsonObject();
//                        }
//
//                        return null;
//                    }
//
//                    @Override
//                    public JsonElement onSerialize(ResponseMessageBase src, Type typeOfSrc, JsonSerializationContext context) {
//                        return null;
//                    }
//                })
                .registerTypeAdapter(Date.class, new JsonObjectConverter<Date>() {
                    @Override
                    public Date onDeserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        try {
                            String time = json.getAsString();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = format.parse(time);
                            return date;
                        } catch (ParseException e) {
                            return Calendar.getInstance().getTime();
                        }
                    }

                    @Override
                    public JsonElement onSerialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                        return null;
                    }
                })
                .registerTypeAdapter(ErrorMessage.class, new JsonObjectConverter<ErrorMessage>() {

                    @Override
                    public ErrorMessage onDeserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        ErrorMessage errorMessage = new ErrorMessage();
                        if (json.isJsonObject()) {
                            JsonObject jsonObject = json.getAsJsonObject();
                            HashMap<String, String> list_value = new HashMap<String, String>();
                            List<String> list_error = new ArrayList<String>();
                            String error = "";
                            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                                String key;
                                if (list_key.get(entry.getKey()) != null) {
                                    key = list_key.get(entry.getKey());
                                } else {
                                    key = entry.getKey();
                                }
                                JsonElement element = entry.getValue();
                                String value = "";
                                if (element.isJsonArray()) {
                                    JsonArray array = element.getAsJsonArray();
                                    for (JsonElement item : array) {
                                        value = value + item.getAsString() + ", ";
                                    }
                                    value = value.substring(0, value.length() - 2);
                                }
                                list_value.put(key, value);
                            }
                            errorMessage.setList_value(list_value);
                            errorMessage.setStringError(errorMessage.getStringErrFormList());
                            errorMessage.setJsonObjectError(jsonObject);
                        } else if (json.isJsonArray()) {
                            JsonArray array = json.getAsJsonArray();
                            String value = "";
                            for (JsonElement item : array) {
                                value = value + item.getAsString() + " ";
                            }
                            errorMessage.setStringError(value);

                        }

                        return errorMessage;
                    }

                    @Override
                    public JsonElement onSerialize(ErrorMessage src, Type typeOfSrc, JsonSerializationContext context) {
                        return null;
                    }
                })
                .registerTypeAdapter(RsAvartar.class, new JsonObjectConverter<RsAvartar>() {
                    @Override
                    public RsAvartar onDeserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        RsAvartar rsp = new RsAvartar();
                        if (json.isJsonObject()) {
                            JsonObject object = json.getAsJsonObject();
                            if (object.has("url") && !object.get("url").isJsonNull()) {
                                String root_img = BASE_URI + object.get("url").getAsString();
                                rsp.setUrl(root_img);
                                if(object.has("updated_at") && object.get("updated_at").getAsString().equals("") == false){
                                    String value =  object.get("updated_at").getAsString();
                                    if(MyPreferences.getValueofImage(root_img).equals(value) == false){
                                        DiskCacheUtils.removeFromCache(root_img, ImageLoader.getInstance().getDiskCache());
                                        MemoryCacheUtils.removeFromCache(root_img, ImageLoader.getInstance().getMemoryCache());
                                        MyPreferences.setDataImage(root_img, value);
                                    }
                                }
                            }
                            if (object.has("small") && object.get("small").isJsonObject() && !object.get("small").isJsonNull()) {
                                JsonObject small = object.getAsJsonObject("small");
                                if (small.has("url") && !small.get("url").isJsonNull()) {
                                    String root_img = BASE_URI + small.get("url").getAsString();
                                    rsp.setUrl(root_img);
                                    if(object.has("updated_at") && object.get("updated_at").getAsString().equals("") == false){
                                        String value =  object.get("updated_at").getAsString();
                                        if(MyPreferences.getValueofImage(root_img).equals(value) == false){
                                            DiskCacheUtils.removeFromCache(root_img, ImageLoader.getInstance().getDiskCache());
                                            MemoryCacheUtils.removeFromCache(root_img, ImageLoader.getInstance().getMemoryCache());
                                            MyPreferences.setDataImage(root_img, value);
                                        }
                                    }
                                }
                            }
                            if (object.has("medium") && object.get("medium").isJsonObject() && !object.get("medium").isJsonNull()) {
                                JsonObject medium = object.getAsJsonObject("medium");
                                if (medium.has("url") && !medium.get("url").isJsonNull()) {
                                    String root_img = BASE_URI + medium.get("url").getAsString();
                                    rsp.setUrl(root_img);
                                    if(object.has("updated_at") && object.get("updated_at").getAsString().equals("") == false){
                                        String value =  object.get("updated_at").getAsString();
                                        if(MyPreferences.getValueofImage(root_img).equals(value) == false){
                                            DiskCacheUtils.removeFromCache(root_img, ImageLoader.getInstance().getDiskCache());
                                            MemoryCacheUtils.removeFromCache(root_img, ImageLoader.getInstance().getMemoryCache());
                                            MyPreferences.setDataImage(root_img, value);
                                        }
                                    }
                                }
                            }
                        }
                        return rsp;
                    }

                    @Override
                    public JsonElement onSerialize(RsAvartar src, Type typeOfSrc, JsonSerializationContext context) {
                        return null;
                    }
                })
                .registerTypeAdapter(RsImageUri.class, new JsonObjectConverter<RsImageUri>() {
                    @Override
                    public RsImageUri onDeserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        RsImageUri image = new RsImageUri();
                        if (json.isJsonObject()) {
                            JsonObject object = json.getAsJsonObject();
                            if (object.has("product_image") && object.get("product_image").isJsonObject()) {
                                JsonObject product_img = object.getAsJsonObject("product_image");
                                if (product_img.has("url")) {
                                    String root_img = BASE_URI + product_img.get("url").getAsString();
                                    image.setUrl(root_img);
                                    if(object.has("updated_at") && object.get("updated_at").getAsString().equals("") == false){
                                        String value =  object.get("updated_at").getAsString();
                                        if(MyPreferences.getValueofImage(root_img).equals(value) == false){
                                            DiskCacheUtils.removeFromCache(root_img, ImageLoader.getInstance().getDiskCache());
                                            MemoryCacheUtils.removeFromCache(root_img, ImageLoader.getInstance().getMemoryCache());
                                            MyPreferences.setDataImage(root_img, value);
                                        }
                                    }
                                }
                                if (product_img.has("thumb") && product_img.get("thumb").isJsonObject()) {
                                    JsonObject object1 = product_img.getAsJsonObject("thumb");
                                    String root_img = BASE_URI + object1.get("url").getAsString();
                                    image.setUrl_thumb(root_img);
                                    if(object.has("updated_at") && object.get("updated_at").getAsString().equals("") == false){
                                        String value =  object.get("updated_at").getAsString();
                                        if(MyPreferences.getValueofImage(root_img).equals(value) == false){
                                            DiskCacheUtils.removeFromCache(root_img, ImageLoader.getInstance().getDiskCache());
                                            MemoryCacheUtils.removeFromCache(root_img, ImageLoader.getInstance().getMemoryCache());
                                            MyPreferences.setDataImage(root_img, value);
                                        }
                                    }
                                }
                            }

                        }
                        return image;
                    }

                    @Override
                    public JsonElement onSerialize(RsImageUri src, Type typeOfSrc, JsonSerializationContext context) {
                        return null;
                    }
                });

    }

    public static String getStringfromError(JsonObject jsonObject, String field) {
        JsonElement element = jsonObject.get(field);
        String value = "";
        if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            for (JsonElement item : array) {
                value = value + item.getAsString() + ", ";
            }
            value = value.substring(0, value.length() - 2);
        }
        return value;
    }

    public static HashMap<String, String> getHashMap() {
        HashMap<String, String> map = new HashMap<>();

        map.put("current_password", "当前密码");
        map.put("name", "名称");
        map.put("base", "需要");
        map.put("description", "描述");
        map.put("product_images", "产品图片");
        return map;
    }
}
