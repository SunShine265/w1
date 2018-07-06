package com.wo1haitao.api;

import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.ResponsePacket;
import com.wo1haitao.api.response.UserProfile;

import retrofit2.Response;

/**
 * Created by user on 5/10/2017.
 */

public final class CustomResponse {
    public static ResponsePacket getResponseMessage( Response<ResponseMessage<UserProfile>> response){
        ResponsePacket packet = new ResponsePacket(null,false);
        if(response.body() != null){
            packet.setResponseMessage(response.body());
            packet.setSuccess(true);
        }
        else if (response.errorBody() != null){
            try {
                ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                packet.setResponseMessage(responseMessage);
                packet.setSuccess(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  packet;
    }

}
