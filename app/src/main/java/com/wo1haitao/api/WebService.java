package com.wo1haitao.api;

import com.wo1haitao.api.response.AddressMeResponseMessage;
import com.wo1haitao.api.response.InboxRs;
import com.wo1haitao.api.response.OfferResponseMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsAddress;
import com.wo1haitao.api.response.RsAddressResponse;
import com.wo1haitao.api.response.RsDisputes;
import com.wo1haitao.api.response.RsInformationTermsModel;
import com.wo1haitao.api.response.RsMyReview;
import com.wo1haitao.api.response.RsOrder;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.api.response.RsReportResponse;
import com.wo1haitao.api.response.RsReview;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.api.response.WishlistItemRs;
import com.wo1haitao.models.BillingAddressesModel;
import com.wo1haitao.models.CounterOfferModel;
import com.wo1haitao.models.DisputeModel;
import com.wo1haitao.models.MessageContainer;
import com.wo1haitao.models.NotificationModel;
import com.wo1haitao.models.OfferMeModel;
import com.wo1haitao.models.OrderModel;
import com.wo1haitao.models.ProductListing;
import com.wo1haitao.models.ProductModel;
import com.wo1haitao.models.ProductOffer;
import com.wo1haitao.models.ProductTenders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by user on 5/10/2017.
 */

public interface WebService {
    @Headers({
            "Content-Type: application/x-www-form-urlencoded"
    })
    @FormUrlEncoded
    @POST("api/v1/auth")
    Call<ResponseMessage<UserProfile>> actionRegister(@Field("nickname") String nickname, @Field("email") String email, @Field("password") String password, @Field("password_confirmation") String password_confirmation);

    @FormUrlEncoded
    @POST("api/v1/auth/sign_in")
    Call<ResponseMessage<UserProfile>> actionLogin(@Field("email") String email, @Field("password") String password);

    @DELETE("api/v1/auth/sign_out")
    Call<ResponseMessage<UserProfile>> actionLogout();

    @GET("/api/v1/me/account")
    Call<ResponseMessage<UserProfile>> actionGetUser();

    @FormUrlEncoded
    @POST("/api/v1/auth/password")
    Call<ResponseMessage<UserProfile>> actionResetPassword(@Field("email") String email);


    @Multipart
    @PATCH("/api/v1/me/edit_profile")
    Call<ResponseMessage<UserProfile>> actionUpdateProfile(@Query("first_name") String first_name, @Query("last_name") String last_name, @Query("nickname") String nickname,
                                                           @Query("email") String email , @Query("country_id") int country_id, @Part MultipartBody.Part profile_picture,
                                                           @Query("phone_number") String phone_number);

    @PATCH("/api/v1/me/edit_profile")
    Call<ResponseMessage<UserProfile>> actionUpdateProfile(@Query("first_name") String first_name, @Query("last_name") String last_name, @Query("nickname") String nickname,
                                                           @Query("email") String email , @Query("country_id") int country_id, @Query("phone_number") String phone_number);

    @Multipart
    @POST("/api/v1/want_to_buy")
    Call<ResponseMessage<RsProduct>> actionWanttoBuy(@Query("draft") Boolean draft,@Query("name") String name, @Query("description") String description, @Query("category_id") String category_id,
                                                     @Query("non_restricted_country") boolean non_restricted_country, @Query("new_product") boolean new_product, @Query("used_product") boolean used_product,
                                                     @QueryMap Map<String,Integer> method_id, @Part List<MultipartBody.Part> product_image, @QueryMap Map<String,Integer> country_id);

    @POST("/api/v1/want_to_buy")
    Call<ResponseMessage<RsProduct>> actionWanttoBuy(@Query("draft") Boolean draft, @Query("name") String name, @Query("description") String description, @Query("category_id") String category_id,
                                                     @Query("non_restricted_country") boolean non_restricted_country, @Query("new_product") boolean new_product, @Query("used_product") boolean used_product,
                                                     @QueryMap Map<String,Integer> method_id, @QueryMap Map<String,Integer> country_id);

    @Multipart
    @PATCH("/api/v1/want_to_buy/{id}")
    Call<ResponseMessage<RsProduct>> actionUpdateWanttoBuy(@Path ("id") long id,
                                                           @Part("draft") Boolean draft,
                                                           @Query("name") String name,
                                                           @Query("description") String description,
                                                           @Query("category_id") String category_id,
                                                           @Part("non_restricted_country") boolean non_restricted_country,
                                                           @Part("new_product") boolean new_product,
                                                           @Part("used_product") boolean used_product,
                                                           @Part List<MultipartBody.Part> dynamic_params,
                                                           @Part List<MultipartBody.Part> product_image);

    @Multipart
    @POST("/api/v1/want_to_buy/{id}/duplicate")
    Call<ResponseMessage<RsProduct>> actionDupWanttoBuy(@Path ("id") long id, @Query("draft") Boolean draft,@Query("name") String name, @Query("description") String description, @Query("category_id") String category_id,
                                                           @Query("non_restricted_country") boolean non_restricted_country, @Query("new_product") boolean new_product, @Query("used_product") boolean used_product,
                                                           @QueryMap Map<String,Integer> method_id, @Part List<MultipartBody.Part> product_image, @QueryMap Map<String,Integer> country_id);


    @GET("/api/v1/me/want_to_buy")
    Call<ResponseMessage<ArrayList<RsProduct>>> actionGetMyProducts(@Query("page") int page);

    @GET("/api/v1/search")
    Call<ResponseMessage<ArrayList<RsProduct>>> actionGetAllProduct(@Query("page") int page);

    @GET("/api/v1/want_to_buy/{id}")
    Call<ResponseMessage<RsProduct>> actionGetProductById(@Path ("id") long id);

    @GET("/api/v1/me/addresses")
    Call<AddressMeResponseMessage> actionGetAddressMe();

    @FormUrlEncoded
    @POST("/api/v1/shipping_addresses")
    Call<ResponseMessage<RsAddress>> actionAddShippingAddress(@Field("country") String country,
                                                              @Field("address_1") String address_1,
                                                              @Field("address_2") String address_2,
                                                              @Field("state") String state,
                                                              @Field("city") String city,
                                                              @Field("postal_code") String postal_code,
                                                              @Field("primary_address") boolean primary_address);

    @FormUrlEncoded
    @POST("/api/v1/shipping_addresses")
    Call<ResponseMessage<RsAddress>> actionAddShippingAddress(@Field("country") String country,
                                                              @Field("address_1") String address_1,
                                                              @Field("address_2") String address_2,
                                                              @Field("state") String state,
                                                              @Field("city") String city,
                                                              @Field("postal_code") String postal_code
                                                              );

    @PATCH("/api/v1/shipping_addresses/{id}")
    Call<ResponseMessage<RsAddress>> actionUpdateShippingAddress(@Path("id") long id,
                                                                @Query(value = "country", encoded = true) String country,
                                                                 @Query(value = "address_1", encoded = true) String address_1,
                                                              @Query(value = "address_2", encoded = true) String address_2,
                                                              @Query(value = "state", encoded = true) String state,
                                                              @Query(value = "city", encoded = true) String city,
                                                              @Query(value = "postal_code", encoded = true) String postal_code);

    @GET("/api/v1/search")
    Call<ResponseMessage<ArrayList<RsProduct>>> actionGetSearchProduct(@Query("page") int page,
                                                                       @Query(value = "category",encoded = true) String category ,
                                                                       @Query(value = "country", encoded = true) String country,
                                                                       @Query(value = "name", encoded = true) String name_search,
                                                                       @Query(value = "sort_by",encoded = true) String sort_by,
                                                                       @Query(value = "new_product") boolean new_product,
                                                                       @Query(value = "used_product") boolean used_product);

    @GET("/api/v1/me/favourites")
    Call<ResponseMessage<ArrayList<WishlistItemRs>>> actionGetFavourite();

    @GET("/api/v1/me/offers")
    Call<ResponseMessage<ArrayList<OfferMeModel>>> actionGetChat();

    @GET("/api/v1/me/inbox")
    Call<ResponseMessage<ArrayList<InboxRs>>> actionGetInbox();

    @POST("/api/v1/wishlists/{id}/toggle")
    Call<ResponseMessage<String>> actionGetFouvoriteProduct(@Path("id") long id);

    @Multipart
    @POST("/api/v1/want_to_buy/{id}/offers")
    Call<ResponseMessage<ProductOffer>> actionPostOffer(@Path("id") long id,
                                                        @Query("condition") String condition,
                                                        @Query("country_id") String country_id,
                                                        @Query("tender") long tender,
                                                        @Query("reoffer") Boolean isreoffer,
                                                        @QueryMap Map<String, String> offer_price,
                                                        @QueryMap Map<String, String> remarks,
                                                        @QueryMap Map<String, Boolean> reoffer,
                                                        @QueryMap Map<String, String> shipping_method_id,
                                                        @QueryMap Map<String, String> shipping_cost,
                                                        @QueryMap Map<String, String> expected_arrival_days,
                                                        @Part List<MultipartBody.Part> product_image);
    @POST("/api/v1/want_to_buy/{id}/offers")
    Call<ResponseMessage<ProductOffer>> actionPostOffer(@Path("id") long id,
                                                        @Query("condition") String condition,
                                                        @Query("country_id") String country_id,
                                                        @Query("tender") long tender,
                                                        @Query("reoffer") Boolean isreoffer,
                                                        @QueryMap Map<String, String> offer_price,
                                                        @QueryMap Map<String, String> remarks,
                                                        @QueryMap Map<String, Boolean> reoffer,
                                                        @QueryMap Map<String, String> shipping_method_id,
                                                        @QueryMap Map<String, String> shipping_cost,
                                                        @QueryMap Map<String, String> expected_arrival_days);
    @Multipart
    @POST("/api/v1/want_to_buy/{id}/offers")
    Call<ResponseMessage<ProductOffer>> actionPostOffer(@Path("id") long id,
                                                        @Part("tender") long tender,
                                                        @Part("reoffer") Boolean isreoffer,
                                                        @Part("want_to_buy_id") long product_id,
                                                        @Part List<MultipartBody.Part> dynamic_params,
                                                        @Part List<MultipartBody.Part> product_image);

    @POST("/api/v1/want_to_buy/{id}/offers")
    Call<ResponseMessage<ProductOffer>> actionPostOffer(@Path("id") long id,
                                                        @Query("tender") long tender,
                                                        @Query("reoffer") Boolean isreoffer,
                                                        @Query("want_to_buy_id") long product_id,
                                                        @QueryMap Map<String, String> offer_price,
                                                        @QueryMap Map<String, String> remarks,
                                                        @QueryMap Map<String, Boolean> reoffer,
                                                        @QueryMap Map<String, String> shipping_method_id,
                                                        @QueryMap Map<String, String> shipping_cost,
                                                        @QueryMap Map<String, String> expected_arrival_days,
                                                        @QueryMap Map<String,Integer> _destroy,
                                                        @QueryMap Map<String,String> id_shipping
    );

    @GET("/api/v1/me/offers")
    Call<ResponseMessage<ArrayList<ProductTenders>>> actionGetOfferMe(@Query("page") int page);

    @FormUrlEncoded
    @POST("/api/v1/offers/{id}/counter_offers")
    Call<ResponseMessage<CounterOfferModel>> actionPostCounterOffer(@Path("id") long id,
                                                                    @Field("counter_offer_price") String counter_offer_price,
                                                                    @Field("remarks") String remarks);
    /**
     * Reject offer
     * @params:
     * state (rejected)
     * offer
     * tender
     * @return
     * */
    @PATCH("/api/v1/response/{id}")
    Call<ResponseMessage> actionReject(@Path("id") long id,
                                       @Query("state") String state,
                                       @Query("offer") Long offer,
                                       @Query("tender") Long tender);

    /**
    Accept offer
    * @params:
    * product_tender_shipping_id
    * offer
    * tender
     * @rerurn
    * */
    @FormUrlEncoded
    @POST("/api/v1/response")
    Call<ResponseMessage<OrderModel>> actionAccept(@Field("product_tender_shipping_id") Long product_tender_shipping_id,
                                                   @Field("offer") Long offer,
                                                   @Field("tender") Long tender);
    /**
     * Get offer
     * @params:
     * offer_id
     *return
     * */
    @GET("/api/v1/offers/{id}")
    Call<OfferResponseMessage> actionGetOffer(@Path("id") long id);

    /**
     * Get reviews products
     * @params:
     * @return
     * */
    @GET("/api/v1/me/reviews")
    Call<ResponseMessage<RsReview>> actionGetReview();

    /**
     * Delete item shipping address
     * @params
     * @return
     * */
    @DELETE("/api/v1/shipping_addresses/{id}")
    Call<ResponseMessage> actionDeleteShippingAddress(@Path("id") long id);

    /**Set up user category
     * @params:
     * category_ids[]
     * @return
     * */
    @POST("/api/v1/category_setup")
    Call<ResponseMessage> actionPostUserCategory(@QueryMap Map<String,Integer> category_ids);

    /**
     * Checkout order
     * @params:
     *  address_1
     *  address_2
     *  country
     *  state
     *  postal_code
     *  city
     *  billing_address_1
     *  billing_address_2
     *  billing_country
     *  billing_state
     *  billing_postal_code
     *  billing_city
     *  product_tender_shipping_id
     *  product_offer_id
     *  payment_method
     *  @Return:
     *  Order
     * */
    @FormUrlEncoded
    @POST("/api/v1/orders")
    Call<RsOrder> actionPostOrders(@Field("address_1") String address_1, @Field("address_2") String address_2,
                                   @Field("country") String country, @Field("state") String state,
                                   @Field("postal_code") String postal_code, @Field("city") String city,
                                   @Field("billing_address_1") String billing_address_1, @Field("billing_address_2") String billing_address_2,
                                   @Field("billing_country") String billing_country, @Field("billing_state") String billing_state,
                                   @Field("billing_postal_code") String billing_postal_code, @Field("billing_city") String billing_city,
                                   @Field("product_tender_shipping_id")long product_tender_shipping_id,
                                   @Field("product_offer_id") long product_offer_id, @Field("payment_method") long payment_method);

    /**
     * Get message
     * @params:
     *  idMessage
     * @return:
     * */

    @GET("/api/v1/me/message/{id}")
    Call<ResponseMessage<MessageContainer>> actionGetMessage(@Path("id") int id);

    /**Post reviews
     * @params:
     *  rating
     *  review_message
     *  want_to_buy_id
     * @return:
     * */
    @FormUrlEncoded
    @POST("/api/v1/want_to_buy/{want_to_buy_id}/reviews")
    Call<ResponseMessage<RsMyReview>> actionPostReview(@Path("want_to_buy_id") long id,
                                                       @Field("rating[通讯]") String rating,
                                                       @Field("rating[运输回应]") String rating2,
                                                       @Field("rating[产品描述精度]") String rating3  ,
                                                       @Field("review_message") String review_message,
                                                       @Field("want_to_buy_id") long want_to_buy_id);

    @FormUrlEncoded
    @POST("/api/v1/want_to_buy/{want_to_buy_id}/reviews")
    Call<ResponseMessage<RsMyReview>> actionPostReview(@Path("want_to_buy_id") long id,
                                                       @Field("rating[通讯]") String rating,
                                                       @Field("review_message") String review_message,
                                                       @Field("want_to_buy_id") long want_to_buy_id);

    /**
     * Post chat message
     * @params:
     *  message_container_id
     *  tenderer_id
     *  want_to_buy_id
     *  message[content]
     *  message_container[tenderer_id]
     *  message_container[purchaser_id]
     * return:
     * */
    @FormUrlEncoded
    @POST("/api/v1/want_to_buy/{want_to_buy_id}/messages")
    Call<ResponseMessage> actionPostMessage(@Path("want_to_buy_id") long id,
                                            @Field("message_container_id") long message_container_id,
                                            @Field("tenderer_id") long tenderer_id,
                                            @Field("want_to_buy_id") long want_to_buy_id,
                                            @Field("message[content]") String message,
                                            @Field("message_container[tenderer_id]") long message_container,
                                            @Field("message_container[purchaser_id]") long message_container2);

    @FormUrlEncoded
    @POST("/api/v1/want_to_buy/{want_to_buy_id}/messages")
    Call<ResponseMessage> actionPostMessage(@Path("want_to_buy_id") long id,
                                            @Field("tenderer_id") long tenderer_id,
                                            @Field("want_to_buy_id") long want_to_buy_id,
                                            @Field("message[content]") String message,
                                            @Field("message_container[tenderer_id]") long message_container,
                                            @Field("message_container[purchaser_id]") long message_container2);

    /**
     * Post dispute
     * @params:
     *  want_to_buy_id
     *  reported_id
     *  reason_to_dispute
     *  details
     * @return:
     * */

    /**
     * Update billing address
     * @params:
     * @return:
     * */
    @PATCH("/api/v1/shipping_addresses/{id}")
    Call<RsAddressResponse> actionUpdateShippingAddress(@Path("id") long id,
                                                        @Query("country") String country,
                                                        @Query("address_1") String address_1,
                                                        @Query("address_2") String address_2,
                                                        @Query("state") String state,
                                                        @Query("city") String city,
                                                        @Query("postal_code") String postal_code,
                                                        @Query("primary_address") boolean primary_address);

    /**
     * Get dispute
     * @params:
     * @return:
     * */
    @GET("/api/v1/me/disputes")
    Call<ResponseMessage<ArrayList<RsDisputes>>> actionGetDisputes();

    /**Get category
     * @params:
     * @return:
     * */
    @GET("/api/v1/categories")
    Call<ArrayList<Object>> actionGetCategory();

    /**Get country
     * @params:
     * @return:
     * */
    @GET("/api/v1/countries")
    Call<ArrayList<Object>> actionGetCountry();

    /**Get country
     * @params:
     * @return:
     * */
    @GET("/api/v1/shipping_methods")
    Call<ArrayList<Object>> actionGetShippingMethod();

    /**
     * Post report
     * @params:
     * product_listing_id
     * report_reason
     * @return:
     * */
    @POST("/api/v1/product_listing_reports")
    Call<RsReportResponse> actionPostReport(@Query("product_listing_id") long product_listing_id, @Query("report_reason") String report_reason);

    /**
     * Post dispute
     * @params:
     *  want_to_buy_id
     *  reported_id
     *  reason_to_dispute
     *  details
     * @return:
     * */
    @FormUrlEncoded
    @POST("/api/v1/want_to_buy/{want_to_buy_id}/disputes")
    Call<ResponseMessage<DisputeModel>> actionPostDispute(@Path("want_to_buy_id") String id
                                            , @Field("want_to_buy_id") String want_to_buy_id
                                            , @Field("reported_id") String reported_id
                                            , @Field("reason_to_dispute") String reason_to_dispute
                                            , @Field("details") String details);

    /**
     * Add billing address
     * @params:
     *  country
     *  address_1
     *  address_2
     *  state
     *  city
     *  postal_code
     * @return:
     * */
    @FormUrlEncoded
    @POST("/api/v1/billing_addresses")
    Call<BillingAddressesModel> actionPostBillingAddress(@Field("country") String country,
                                                         @Field("address_1") String address_1,
                                                         @Field("address_2") String address_2,
                                                         @Field("state") String state,
                                                         @Field("city") String city,
                                                         @Field("postal_code") String postal_code);

    /**
     * Delete product
     * @params:
     * @return:
     * */
    @DELETE("/api/v1/product_listings/{id}")
    Call<ResponseMessage> actionDeleteProduct(@Path("id") int id);

    /**
     * Repost Product
     * @params:
     * @return:
     * */
    @PATCH("/api/v1/want_to_buy/{id}/relist")
    Call<ResponseMessage<ProductListing>> actionRepostListing(@Path("id") long id);

    /**
     * Get messager Product
     * @params:
     * @return:
     * */
    @GET("/api/v1/want_to_buy/{want_to_buy_id}/messages/new")
    Call<ResponseMessage<MessageContainer>> actionGetMessager(@Path("want_to_buy_id") long id, @Query("tenderer_id") int tenderer_id);

    /**
     * Get invoice
     * @params:
     * @return:
     * */
    @GET("/api/v1/orders/{id}/new_invoice")
    Call<ResponseMessage<OrderModel>> actionGetInvoice(@Path("id") long id);

    /**
     * Create invoice
     * */
    @Multipart
    @PUT("/api/v1/orders/{id}/update_invoice")
    Call<ResponseMessage> actionCreateInvoice(@Path("id") long id
            , @Query("tracking_no") String tracking_no
            , @Part MultipartBody.Part order_invoice
            , @Query("check_for_invoice_tracking_no") boolean check_for_invoice_tracking_no
            , @Query("shipping_company_name") String shipping_company_name);

    @PUT("/api/v1/orders/{id}/update_invoice")
    Call<ResponseMessage> actionCreateInvoice(@Path("id") long id
            , @Query("tracking_no") String tracking_no
            , @Query("check_for_invoice_tracking_no") boolean check_for_invoice_tracking_no
            , @Query("shipping_company_name") String shipping_company_name);

    /**
     * Get countries with country codes
     * @params:
     * @return;
     * */
    @GET("/api/v1/address_countries_with_codes")
    Call<ResponseMessage> actionGetCountryWithCode();

    /**
     * Get user reviews
     * @params:
     * @return:
     * */
    @GET("/api/v1/users/{id}/reviews")
    Call<ResponseMessage<UserProfile>> actionGetUserReview(@Path("id") long id);

    /**
     * Get user notification
     * @params:
     * @return:
     * */
    @GET("/api/v1/me/notifications")
    Call<ResponseMessage<ArrayList<NotificationModel>>> actionGetUserNotifications(@Query("page") int page);

    /**
     * Post notification
     * @params:
     *  id
     * @return:
     * */
    @POST("/api/v1/notifications/{id}/mark_read")
    Call<ResponseMessage<NotificationModel>> actionPostNotifications(@Path("id") long id);

    /**
     * Post verification
     * @params:
     *  identity_image
     * @return:
     * */
    @Multipart
    @POST("/api/v1/profile_setup/verification")
    Call<ResponseMessage> actionPostVerification(@Part MultipartBody.Part identity_image);

    /**
     * Update billing address
     * @params:
     * @return:
     * */
    @PUT("/api/v1/me/update_alipay")
    Call<ResponseMessage> actionUpdateAlipay(@Query("check_for_alipay_details") boolean check_for_alipay_details ,
                                               @Query("alipay_id") String alipay_id,
                                               @Query("alipay_name") String alipay_name);

    @PUT("/api/v1/me/update_alipay")
    Call<ResponseMessage> actionUpdateAlipay(@Query("check_for_alipay_details") boolean check_for_alipay_details ,
                                             @Query(value = "alipay_id", encoded = true) String alipay_id,
                                             @Query(value = "alipay_name", encoded = true) String alipay_name,
                                             @Query(value = "alipay_id_confirmation", encoded = true) String alipay_id_confirm);

    /**
     * Get information terms
     * @params:
     * @return:
     * */
    @GET("/api/v1/information/{id}")
    Call<RsInformationTermsModel> actionGetInformationTerms(@Path(value = "id", encoded = true) String id);

    /**
     * Get confirm receive
     * @params:
     *  id: identify order
     * @return:
     * */
    @GET("/api/v1/orders/{id}/confirm_received")
    Call<ResponseMessage<OrderModel>> actionGetConfirmReceive(@Path("id") long id);

    /**
     * Get confirm delivery
     * @params:
     *  id: identify order
     * @return:
     * */
    @GET("/api/v1/orders/{id}/confirm_delivery")
    Call<ResponseMessage<OrderModel>> actionGetConfirmDelivery(@Path("id") long id);

    /**
     * Post country profile
     * @params:
     *  id
     * @return:
     * */
    @Multipart
    @POST("/api/v1/profile_setup/country")
    Call<ResponseMessage> actionPostCountryProfile(@Field("first_name") String first_name, @Field("last_name") String last_name, @Field("nickname") String nickname,
                                                   @Field("email") String email , @Field("country_id") int country_id, @Field("country_code") String country_code, @Part MultipartBody.Part profile_picture,
                                                   @Field("phone_number") String phone_number, @Field("check_for_name_and_contact_details") boolean check_for_name_and_contact_details);

    @FormUrlEncoded
    @POST("/api/v1/profile_setup/country")
    Call<ResponseMessage> actionPostCountryProfile(@Field("first_name") String first_name, @Field("last_name") String last_name, @Field("nickname") String nickname,
                                                   @Field("email") String email , @Field("country_id") int country_id, @Field("country_code") String country_code, @Field("phone_number") String phone_number, @Field("check_for_name_and_contact_details") boolean check_for_name_and_contact_details);

    /**
     * Post question answer
     * @params:
     *  id want to buy
     *  content
     * @return
     * */
    @FormUrlEncoded
    @POST("/api/v1/want_to_buy/{id_want_to_buy}/question_answers")
    Call<ResponseMessage> actionPostQuestion(@Path("id_want_to_buy") long id, @Field("content") String content);

    /**
     * Post reply question answer
     * @params;
     *  id want to buy
     *  content
     * @return
     * */
    @FormUrlEncoded
    @POST("/api/v1/question_answers/{id_question}/question_answer_replies")
    Call<ResponseMessage> actionPostAnswer(@Path("id_question") long id, @Field("content") String content);

    /**
     * Get get detail order
     * @params:
     *  id: id order
     * @return:
     * */
    @GET("/api/v1/orders/{order_id}/order_details")
    Call<ResponseMessage<OrderModel>> actionGetDetailOrder(@Path("order_id") long id);



    @GET("/api/v1/orders/{order_id}/offerer_order_details")
    Call<ResponseMessage<OrderModel>> actionGetDetailOffererOrder(@Path("order_id") long id);
    /**
     * Get report
     * @params:
     * @return:
     * */
    @GET("/api/v1/product_listing_reports/new")
    Call<ResponseMessage> actionGetReport(@Query("product_listing_id") long product_listing_id);

    /**
     * Change password
     * @params:
     *  password
     *  new password
     *  password confirm
     * @return:
     * */
    @PATCH("/api/v1/me/update_password")
    Call<ResponseMessage> actionUpdatePassword(@Query(value = "password", encoded = true) String password,
                                               @Query(value = "password_confirmation", encoded = true) String password_confirmation,
                                               @Query(value = "current_password", encoded = true) String current_password);

    /**
     * Confirm payment
     * @params:
     * @return:
     * */
    @GET("/api/v1/orders/{id}/confirm_payment")
    Call<ResponseMessage> actionConfirmPayment(@Path("id") long id);

    @GET("/api/v1/me/register_device")
    Call<ResponseMessage> actionRegisterDevice(@Query("device_token")String device_token);

    @GET("/api/v1/me/un_register_device")
    Call<ResponseMessage> actionUnRegisterDevice(@Query("device_token")String device_token);
}
