package com.wo1haitao.api.response;

/**
 * Created by user on 5/10/2017.
 */

public class ResponseMessage<T>  extends ResponseMessageBase {
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
