package com.wo1haitao.models;

/**
 * Created by user on 6/26/2017.
 */

public class SetupAccountModel {
    long id, common_user_id;
    boolean setup_category, setup_country, setup_verification;

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

    public boolean getSetup_category() {
        return setup_category;
    }

    public void setSetup_category(boolean setup_category) {
        this.setup_category = setup_category;
    }

    public boolean getSetup_country() {
        return setup_country;
    }

    public void setSetup_country(boolean setup_country) {
        this.setup_country = setup_country;
    }

    public boolean getSetup_verification() {
        return setup_verification;
    }

    public void setSetup_verification(boolean setup_verification) {
        this.setup_verification = setup_verification;
    }
}
