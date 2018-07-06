package com.wo1haitao.models;

import android.util.Log;

import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.api.response.UserProfile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by user on 6/7/2017.
 */

public class ProductTenders {
    int id;
    String condition;
    boolean closed;
    String created_at;
    ArrayList<ProductOffer> product_offers;
    UserProfile common_user;
    String uid;
    ArrayList<ProductTenderShippings> product_tender_shippings;
    public ArrayList<ProductImages> product_images;
    ProductListing product_listing;
    RsProduct product_in_tender;
    Integer tenderer_id;
    CountryModel country;

    public RsProduct getProduct_in_tender() {
        if(product_in_tender ==null){
            product_in_tender = new RsProduct();
            product_in_tender.setClosed(product_listing.isClosed());
            product_in_tender.setCommon_user(product_listing.getCommon_user());
            product_in_tender.setName(product_listing.getName());
            product_in_tender.setNew_product(product_listing.isNew_product());
        }
        return product_in_tender;
    }

    public void setProduct_in_tender(RsProduct product_in_tender) {
        this.product_in_tender = product_in_tender;
    }

    public CountryModel getCountry() {
        if(country == null){
            return new CountryModel();
        }
        return country;
    }

    public void setCountry(CountryModel country) {
        this.country = country;
    }

    public ArrayList<ProductImages> getProduct_images() {
        if(product_images == null){
            return new ArrayList<>();
        }
        return product_images;
    }

    public void setProduct_images(ArrayList<ProductImages> product_images) {
        this.product_images = product_images;
    }
    public Integer getTenderer_id() {
        for (ProductOffer productOffer : product_offers) {
            if (productOffer.getState().equals("accepted")) {
                return common_user.getId();
            }
        }
        return -1;
    }

    public void setTenderer_id(Integer tenderer_id) {
        this.tenderer_id = tenderer_id;
    }

    public String getCreated_at() {
        if(created_at == null){
            return "";
        }
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public ProductListing getProduct_listing() {
        if(product_listing == null){
            return new ProductListing();
        }
        return product_listing;
    }

    public ArrayList<ShippingMethods> getShipping_methods() {
        if(shipping_methods == null){
            return new ArrayList<>();
        }
        return shipping_methods;
    }

    ArrayList<ShippingMethods> shipping_methods;

    public String getUid() {
        return uid;
    }

    public int getId() {
        return id;
    }

    public UserProfile getCommon_user() {
        if(common_user == null){
            return new UserProfile();
        }
        return common_user;
    }

    public String getCondition() {
        if(condition == null){
            return "";
        }
        return condition;
    }

    public ArrayList<ProductOffer> getProduct_offers() {
        ArrayList<ProductOffer> list = product_offers;
        if(list == null){
            list = new ArrayList<>();
        }
        Collections.sort(list, OFFER_SORT);

        return list;
    }

    public ArrayList<ProductTenderShippings> getProduct_tender_shippings() {
        SortProductTender(product_tender_shippings);
        return product_tender_shippings;
    }

    private static Comparator<ProductOffer> OFFER_SORT = new Comparator<ProductOffer>() {
        public int compare(ProductOffer str1, ProductOffer str2) {
            boolean p1_reoffer = str1.getReoffer();
            boolean p2_reoffer = str2.getReoffer();
            boolean p1_ctof = str1.getProduct_counter_offer() != null;
            boolean p2_ctof = str2.getProduct_counter_offer() != null;
            int res = 0;
            if (!p1_reoffer && p2_reoffer) {
                Log.i("TAG", "1");
                res = -1;
            }
            if (p1_reoffer && !p2_reoffer) {
                Log.i("TAG", "-1");
                res = 1;
            }
            if (!p1_ctof && p2_ctof) {
                Log.i("TAG", "-1");
                res = 1;
            }
            if (p1_ctof && !p2_ctof) {
                Log.i("TAG", "1");
                res = -1;
            }
            Log.i("TAG", "xx" + res);
            return res;
        }
    };

    /**
     * Compare 2 item Message follow date
     *
     * @params:
     * @return:
     */
    public static Comparator<ProductTenderShippings> PRODUCTTENDER_SORT = new Comparator<ProductTenderShippings>() {
        public int compare(ProductTenderShippings productTenders1, ProductTenderShippings productTenders2) {
            Date date1, date2;
            int result = 0;
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            String strDate1 = "", strDate2 = "";
            try {
                if (productTenders1.getCreated_at() != null) {
                    strDate1 = productTenders1.getCreated_at();
                }
                if (productTenders2.getCreated_at() != null) {
                    strDate2 = productTenders2.getCreated_at();
                }
                date1 = inputFormat.parse(strDate1);
                date2 = inputFormat.parse(strDate2);
                if (date1.after(date2)) {
                    result = 1;
                } else {
                    result = -1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return result;
        }
    };

    /**
     * Sort list message follow date
     */
    public ArrayList<ProductTenderShippings> SortProductTender(ArrayList<ProductTenderShippings> list) {
        Collections.sort(list, PRODUCTTENDER_SORT);
        return list;
    }
}
