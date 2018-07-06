package com.wo1haitao.api.response;

/**
 * Created by user on 7/17/2017.
 */

public class RsReport {
    long id, common_user_id, product_listing_id;
    String report_reason;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCommon_user_id() {
        return common_user_id;
    }

    public void setCommon_user_id(long common_user_id) {
        this.common_user_id = common_user_id;
    }

    public long getProduct_listing_id() {
        return product_listing_id;
    }

    public void setProduct_listing_id(long product_listing_id) {
        this.product_listing_id = product_listing_id;
    }

    public String getReport_reason() {
        return report_reason;
    }

    public void setReport_reason(String report_reason) {
        this.report_reason = report_reason;
    }
}
