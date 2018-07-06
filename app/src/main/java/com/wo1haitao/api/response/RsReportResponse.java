package com.wo1haitao.api.response;

/**
 * Created by user on 7/17/2017.
 */

public class RsReportResponse extends RsReport {
    ErrorMessage errors;

    public ErrorMessage getErrors() {
        return errors;
    }

    public void setErrors(ErrorMessage errors) {
        this.errors = errors;
    }
}
