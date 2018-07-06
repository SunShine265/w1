package com.wo1haitao.api.response;

import com.wo1haitao.models.OrderModel;

/**
 * Created by user on 7/25/2017.
 */

public class RsOrder {
    String alipay_url;
    OrderModel order;

    public String getAlipay_url() {
        if(alipay_url == null){
            return "";
        }
        return alipay_url;
    }

    public void setAlipay_url(String alipay_url) {
        this.alipay_url = alipay_url;
    }

    public OrderModel getOrder() {
        if(order == null){
            return new OrderModel();
        }
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }
}
