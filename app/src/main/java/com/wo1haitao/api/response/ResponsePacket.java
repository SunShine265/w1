package com.wo1haitao.api.response;

/**
 * Created by user on 5/11/2017.
 */

public class ResponsePacket {
    ResponseMessage responseMessage;
    boolean isSuccess;


    public boolean isSet(){
        if(responseMessage==null && isSuccess == false){
            return false;
        }
        return true;
    }


    public ResponsePacket(ResponseMessage responseMessage, boolean isSuccess) {
        this.responseMessage = responseMessage;
        this.isSuccess = isSuccess;
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
