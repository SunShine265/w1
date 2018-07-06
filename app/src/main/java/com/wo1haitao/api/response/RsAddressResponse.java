package com.wo1haitao.api.response;

/**
 * Created by user on 7/14/2017.
 */

public class RsAddressResponse extends RsAddress {

    ErrorMessage errors;

    public ErrorMessage getErrors() {
        return errors;
    }

    public void setErrors(ErrorMessage errors) {
        this.errors = errors;
    }
}
