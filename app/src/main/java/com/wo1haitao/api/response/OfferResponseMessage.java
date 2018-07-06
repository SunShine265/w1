package com.wo1haitao.api.response;

        import com.google.gson.annotations.SerializedName;
        import com.wo1haitao.models.OfferDetailModel;
        import com.wo1haitao.models.OfferTenderModel;

/**
 * Created by user on 6/12/2017.
 */

public class OfferResponseMessage extends  ResponseMessageBase {
    @SerializedName("tender")
    private OfferTenderModel offerTenderModel;
    @SerializedName("offer")
    private OfferDetailModel offerDetailModel;

    public OfferTenderModel getOfferTenderModel() {
        if(offerTenderModel == null){
            return new OfferTenderModel();
        }
        return offerTenderModel;
    }

    public void setOfferTenderModel(OfferTenderModel offerTenderModel) {
        this.offerTenderModel = offerTenderModel;
    }

    public OfferDetailModel getOfferDetailModel() {
        if(offerDetailModel == null){
            return new OfferDetailModel();
        }
        return offerDetailModel;
    }

    public void setOfferDetailModel(OfferDetailModel offerDetailModel) {
        this.offerDetailModel = offerDetailModel;
    }

}
