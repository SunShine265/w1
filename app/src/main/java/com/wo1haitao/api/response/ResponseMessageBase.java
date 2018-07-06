package com.wo1haitao.api.response;

import java.util.ArrayList;

/**
 * Created by user on 6/12/2017.
 */

public abstract class ResponseMessageBase implements IResponseMessage{
    boolean success = true;
    ErrorMessage errors;
    ArrayList<String> error_messages;

    public ArrayList<String> getError_messages() {
        if(error_messages == null){
            return new ArrayList<String>();
        }
        return error_messages;
    }

    public void setError_messages(ArrayList<String> error_messages) {
        this.error_messages = error_messages;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ErrorMessage getErrors() {
        return errors;
    }

    public void setErrors(ErrorMessage errors) {
        this.errors = errors;
    }
}
