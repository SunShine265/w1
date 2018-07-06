package com.wo1haitao.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by user on 5/10/2017.
 */

public abstract class JsonObjectConverter<T>  implements JsonDeserializer<T>, JsonSerializer<T> {
    public abstract T onDeserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException;
    public abstract JsonElement onSerialize(T src, Type typeOfSrc, JsonSerializationContext context);

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return onDeserialize(json, typeOfT, context);
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return onSerialize(src, typeOfSrc, context);
    }
}