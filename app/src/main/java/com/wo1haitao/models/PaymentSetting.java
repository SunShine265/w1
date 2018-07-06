package com.wo1haitao.models;

/**
 * Created by user on 8/28/2017.
 */

public class PaymentSetting {
    int id;
    String bank_account_no;
    String bank_instructions;
    String commission_charges_percent;
    String seller_commission_charges_percent;

    public String getSeller_commission_charges_percent() {
        if(seller_commission_charges_percent == null){
            seller_commission_charges_percent = "";
        }
        return seller_commission_charges_percent;
    }

    public void setSeller_commission_charges_percent(String seller_commission_charges_percent) {
        this.seller_commission_charges_percent = seller_commission_charges_percent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBank_account_no() {
        return bank_account_no;
    }

    public void setBank_account_no(String bank_account_no) {
        this.bank_account_no = bank_account_no;
    }

    public String getBank_instructions() {
        return bank_instructions;
    }

    public void setBank_instructions(String bank_instructions) {
        this.bank_instructions = bank_instructions;
    }

    public String getCommission_charges_percent() {
        return commission_charges_percent;
    }

    public void setCommission_charges_percent(String commission_charges_percent) {
        this.commission_charges_percent = commission_charges_percent;
    }
}
