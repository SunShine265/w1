package com.wo1haitao.models;

/**
 * Created by user on 6/8/2017.
 */

public class OfferMyBidModel {
    OfferTenderModel tender;
    OfferDetailModel offer;

    public OfferTenderModel getTender() {
        return tender;
    }

    public OfferDetailModel getOffer() {
        if(offer == null){
            return new OfferDetailModel();
        }
        return offer;
    }
}
