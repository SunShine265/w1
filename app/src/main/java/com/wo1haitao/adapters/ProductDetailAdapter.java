package com.wo1haitao.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.CommentItemView;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.controls.RoundedImageView;
import com.wo1haitao.dialogs.DialogBidShop;
import com.wo1haitao.dialogs.DialogCountoffer;
import com.wo1haitao.dialogs.DialogDetailCounterOffer;
import com.wo1haitao.dialogs.DialogDetailOffer;
import com.wo1haitao.dialogs.DialogRebid;
import com.wo1haitao.fragments.ChatDetailFragment;
import com.wo1haitao.fragments.ProductDetailFragment;
import com.wo1haitao.models.OrderModel;
import com.wo1haitao.models.ProductOffer;
import com.wo1haitao.models.ProductTenders;
import com.wo1haitao.models.QuestionAnswers;
import com.wo1haitao.models.QuestionReplyModel;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ButtonTender;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG;

/**
 * Created by user on 6/27/2017.
 */

public class ProductDetailAdapter extends BaseAdapter<Object> implements MainActivity.CallBackKeyboard {
    Activity context;
    ArrayList<Object> arrayList;
    RsProduct rsProduct;
    DialogBidShop dialog_bidshop;
    Boolean isReoffer = false, isOffered = false, isTitleQuestion = false;
    String state = "";
    ProductDetailFragment f;
    long idUser = MyPreferences.getID();
    HashMap<String, View> listQAView;
    HashMap<Long, View> listForm;
    private Handler handler = new Handler();
    LinearLayout bt_confirm_order;
    TextView id_pending, id_received, id_finish_transaction, arrow_pending, arrow_received, arrow_finish_transaction, arrow_end;
    static final String STATUS_ORDER_PENDDING = "pending";
    static final String STATUS_ORDER_PROCESSING = "processing";
    static final String STATUS_ORDER_CONFIRMED = "confirmed";
    static final String BUYER_RECEIVE_STATUS_PENDING = "item_pending";
    static final String BUYER_RECEIVE_STATUS_RECEIVED = "item_received";
    static final String SELLER_DELIVERY_STATUS_PENDING = "delivery_pending";
    static final String SELLER_DELIVERY_STATUS_SENT = "delivery_sent";
    static final String OFFER_ACCEPT = "accepted";
    static final String OFFER_UNTOUCHED = "untouched";
    static final String OFFER_REJECTED = "rejected";


    QuestionAnswers question_form;
    Integer index_of_question_form;
    View current_anwser_view;
    ListView listView;
    boolean requestFocus = false;
    EditText current_edittext;
    int num_of_times_request = 0;
    CommentItemView current_box_show;
    View FormQuestion, FormAnswer;
    CommentItemView current_reply_box, current_question_box, current_commet_content_reply;
    int current_question_box_position = 0;
    boolean isHidekeyboard = false;
    private String strCounterOfferPrice = "";

    public ProductDetailAdapter(Activity context, ArrayList<Object> arrayList, RsProduct rsProduct, String state, ProductDetailFragment f) {
        super(context, 0, arrayList);
        this.context = context;
        if (context instanceof MainActivity) {
            ((MainActivity) context).setListenerKeyboard(ProductDetailAdapter.this);
        }
        this.arrayList = arrayList;
        this.rsProduct = rsProduct;
        this.state = state;
        this.f = f;
        listQAView = new HashMap<>();
        listForm = new HashMap<>();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        listView = (ListView) parent;
        Object item = getItem(position);
//        Draw view item question answers
        if (item instanceof CommentItemView) {
            CommentItemView itemView = ((CommentItemView) item);
            if (itemView.getType_of_view() == CommentItemView.TYPE_BOX) {
                current_question_box_position = position;
                current_question_box = itemView;
                if (listQAView.containsKey(String.valueOf("Q")) == false) {
                    listQAView.put("Q", FormQuestion(itemView, position));
                }
                return listQAView.get("Q");
            } else if (itemView.getType_of_view() == CommentItemView.TYPE_REBOX) {
                if (listQAView.containsKey(String.valueOf(itemView.getId())) == false) {
                    listQAView.put(String.valueOf(itemView.getId()), FormAnswer(itemView, position));
                }
                return listQAView.get(String.valueOf(itemView.getId()));
            } else if (itemView.getType_of_view() == CommentItemView.TYPE_COMMENT) {
                return QuestionBox(itemView, position);
            } else if (itemView.getType_of_view() == CommentItemView.TYPE_RECOMMENT) {
                return ReplyBox(itemView, position);
            }
            return FormQuestion(itemView, position);

        } else if (item instanceof String) {
            if (item.equals("titleQuestionAnswer")) {
                View viewQuestionAnswer = ((LayoutInflater) ProductDetailAdapter.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.title_question_answer, null, false);
                return viewQuestionAnswer;
            } else {
                View viewNumBid = ((LayoutInflater) ProductDetailAdapter.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.num_bid, null, false);
                TextView tvNumBid = (TextView) viewNumBid.findViewById(R.id.num_bid);
                if (!item.equals("")) {
                    tvNumBid.setText(item.toString() + "个出价");
                }
                return viewNumBid;
            }
        }
//        Draw view item product tender
        else {
            if (item instanceof ProductTenders) {
                ProductTenders productTender = (ProductTenders) item;
                Boolean isAccept = false;
                if (this.state.equals("accept")) {
                    if (productTender.isClosed()) {
                        for (ProductOffer offer : productTender.getProduct_offers()) {
                            if (offer.getState().equals("accepted")) {
                                isAccept = true;
                                break;
                            }
                        }
                        return CreateItemTender(productTender, isAccept);
                    } else {
                        return CreateItemTender(productTender, isAccept);
                    }
                } else {
                    return CreateItemTender(productTender, isAccept);
                }
            } else {
                return new View(this.context);
            }
        }
    }

    /**
     * Create item question answer
     *
     * @param: QuestionAnswers
     * @return: QuestionAnswers
     */

    public View QuestionBox(CommentItemView item, final int position) {
        View viewQuestionAnswer = ((LayoutInflater) ProductDetailAdapter.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_comment, null, false);

        final CommentItemView questionAnswers = (CommentItemView) item;

        RoundedImageView id_avatar = (RoundedImageView) viewQuestionAnswer.findViewById(R.id.id_avatar);
        TextView id_name = (TextView) viewQuestionAnswer.findViewById(R.id.id_name);
        TextView id_content_comment = (TextView) viewQuestionAnswer.findViewById(R.id.id_content_comment);
        final TextView numreview = (TextView) viewQuestionAnswer.findViewById(R.id.numreview);
        final ImageView id_verifies = (ImageView) viewQuestionAnswer.findViewById(R.id.id_verifies);
        ImageView verifies_user = (ImageView) viewQuestionAnswer.findViewById(R.id.verifies_user);
        RatingBar ratingBarComment = (RatingBar) viewQuestionAnswer.findViewById(R.id.ratingBar);
        if (questionAnswers.getCommon_user() != null) {
            if (questionAnswers.getCommon_user().getProfile_picture() != null) {
                if (questionAnswers.getCommon_user().getProfile_picture() != null) {
                    String url_avt = questionAnswers.getCommon_user().getProfile_picture().getUrl();
                    if (url_avt != null && !url_avt.equals("")) {
                        ImageLoader il = ImageLoader.getInstance();
                        il.displayImage(url_avt, id_avatar);
                    }
                }
            }
            ratingBarComment.setRating(questionAnswers.getCommon_user().getAverage_rating());
            if (questionAnswers.getCommon_user().getSetup_account() != null) {
                if (questionAnswers.getCommon_user().getVerification_state().equals("closed")) {
                    id_verifies.setBackgroundResource(R.drawable.verified);
                    verifies_user.setBackgroundResource(R.drawable.green_icon);
                } else {
                    id_verifies.setBackgroundResource(R.drawable.unverifyimg);
                    verifies_user.setBackgroundResource(R.drawable.yellow_icon);
                }
            }

            if (questionAnswers.getCommon_user().getNum_of_reviews() != null) {
                numreview.setText("(" + questionAnswers.getCommon_user().getNum_of_reviews().toString() + ")");
            }
        }

        if (questionAnswers.getCreated_at() != null) {
            TextView dateCteateComment = (TextView) viewQuestionAnswer.findViewById(R.id.dateCteateComment);
            String formatDate = "";
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String myStringDate = questionAnswers.getCreated_at();

            Date myDate = null;
            try {
                myDate = inputFormat.parse(myStringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formatDate = outputFormat.format(myDate);
            dateCteateComment.setText(formatDate);
        }

        if (questionAnswers.getCommon_user().getNickname() != null) {
            id_name.setText(questionAnswers.getCommon_user().getNickname().toString());
        }

        if (questionAnswers.getContent() != null) {
            id_content_comment.setText(questionAnswers.getContent());
        }
        ImageView id_image_reply = (ImageView) viewQuestionAnswer.findViewById(R.id.id_image_reply);
        if (questionAnswers.is_reply()) {
            id_image_reply.setVisibility(View.VISIBLE);
            id_image_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (questionAnswers.isHas_box_current() == false) {
                        if (current_commet_content_reply != null) {
                            current_commet_content_reply.setHas_box_current(false);
                        }
                        CommentItemView viewReBox = new CommentItemView();
                        viewReBox.setType_of_view(CommentItemView.TYPE_REBOX);
                        viewReBox.setId(questionAnswers.getId());
                        arrayList.add(position + 1, viewReBox);
                        if (current_reply_box != null) {
                            arrayList.remove(current_reply_box);
                        }
                        current_reply_box = viewReBox;
                        if (current_question_box != null) {
                            arrayList.remove(current_question_box);
                        }
                        questionAnswers.setHas_box_current(true);
                        current_commet_content_reply = questionAnswers;
                    } else {
                        if (current_reply_box != null) {
                            arrayList.remove(current_reply_box);
                        }
                        if (current_question_box != null) {
                            arrayList.add(current_question_box_position, current_question_box);
                        }
                        questionAnswers.setHas_box_current(false);
                    }
                    notifyDataSetChanged();
                }
            });
        } else {
            id_image_reply.setVisibility(View.GONE);
        }


        return viewQuestionAnswer;
    }

    public View ReplyBox(CommentItemView item, final int position) {
        View viewReplyCommentNew = ((LayoutInflater) ProductDetailAdapter.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_reply_comment, null, false);


        final CommentItemView questionAnswers = (CommentItemView) item;

        ImageView id_verifies_reply = (ImageView) viewReplyCommentNew.findViewById(R.id.id_verifies_reply);
        ImageView verifies_user_reply = (ImageView) viewReplyCommentNew.findViewById(R.id.verifies_user_reply);
        TextView nameReply = (TextView) viewReplyCommentNew.findViewById(R.id.id_name_reply);
        TextView txtdateCreate = (TextView) viewReplyCommentNew.findViewById(R.id.dateCreate);
        TextView replyComment = (TextView) viewReplyCommentNew.findViewById(R.id.id_reply_comment);
        RoundedImageView img_profile = (RoundedImageView) viewReplyCommentNew.findViewById(R.id.ln_profile);
        TextView reply_num_review = (TextView) viewReplyCommentNew.findViewById(R.id.reply_num_review);
        RatingBar ratingBarRepComment = (RatingBar) viewReplyCommentNew.findViewById(R.id.ratingBar);

        if (item.getCommon_user() != null) {
            nameReply.setText(item.getCommon_user().getNickname().toString());
            if (item.getCommon_user().getProfile_picture() != null) {
                String url_avt_reply = item.getCommon_user().getProfile_picture().getUrl();
                if (url_avt_reply != null && !url_avt_reply.equals("")) {
                    ImageLoader ilReply = ImageLoader.getInstance();

                    ilReply.displayImage(url_avt_reply, img_profile);
                }
            }
            if (item.getCommon_user().getVerification_state().equals("closed")) {
                id_verifies_reply.setBackgroundResource(R.drawable.verified);
                verifies_user_reply.setBackgroundResource(R.drawable.green_icon);
            } else {
                id_verifies_reply.setBackgroundResource(R.drawable.unverifyimg);
                verifies_user_reply.setBackgroundResource(R.drawable.yellow_icon);
            }
        }
        try {
            float ratingStar = (float) (item.getCommon_user().getAverage_rating());
            int intpart = (int) ratingStar;
            float decpart = ratingStar - intpart;
            if (decpart != 0.0f) {
                ratingStar = intpart + 0.5f;
            }
            ratingBarRepComment.setRating(ratingStar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (item.getCommon_user().getNum_of_reviews() != null) {
            reply_num_review.setText("(" + item.getCommon_user().getNum_of_reviews().toString() + ")");
        }

        if (item.getCreated_at() != null) {
            String formatDate = "";
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String myStringDate = item.getCreated_at();

            Date myDate = null;
            try {
                myDate = inputFormat.parse(myStringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formatDate = outputFormat.format(myDate);
            txtdateCreate.setText(formatDate);
        }
        if (item.getContent() != null) {
            replyComment.setText(item.getContent().toString());
        }
        ImageView id_button_reply = (ImageView) viewReplyCommentNew.findViewById(R.id.id_image_reply);
        if (item.is_reply()) {
            id_button_reply.setVisibility(View.VISIBLE);
            id_button_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (questionAnswers.isHas_box_current() == false) {
                        if (current_commet_content_reply != null) {
                            current_commet_content_reply.setHas_box_current(false);
                        }
                        CommentItemView viewReBox = new CommentItemView();
                        viewReBox.setType_of_view(CommentItemView.TYPE_REBOX);
                        viewReBox.setId(questionAnswers.getId());
                        arrayList.add(position + 1, viewReBox);
                        if (current_reply_box != null) {
                            arrayList.remove(current_reply_box);
                        }
                        current_reply_box = viewReBox;
                        if (current_question_box != null) {
                            arrayList.remove(current_question_box);
                        }
                        questionAnswers.setHas_box_current(true);
                        current_commet_content_reply = questionAnswers;
                    } else {
                        if (current_reply_box != null) {
                            arrayList.remove(current_reply_box);
                        }
                        if (current_question_box != null) {
                            arrayList.add(current_question_box_position, current_question_box);
                        }
                        questionAnswers.setHas_box_current(false);
                    }
                    notifyDataSetChanged();

                }
            });
        } else {
            id_button_reply.setVisibility(View.GONE);
        }
        return viewReplyCommentNew;
    }

    public View FormQuestion(CommentItemView item, final int position) {
        final CommentItemView questionAnswers = (CommentItemView) item;

        final View viewFormCommnet = ((LayoutInflater) ProductDetailAdapter.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.form_comment_product, null, true);
        final EditText edt_content = (EditText) viewFormCommnet.findViewById(R.id.edtContentCmt);


        edt_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (current_edittext != edt_content) {
                        requestFocus = true;
                        current_edittext = edt_content;

                        listView.setSelection(position + 1);
                        edt_content.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (edt_content.hasFocus() == false) {
                                    edt_content.requestFocus();
                                }
                            }
                        }, 500);
                    }
                }

            }
        });

        LinearLayout ll_button_send = (LinearLayout) viewFormCommnet.findViewById(R.id.ll_button_send);
        ll_button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text_content = "";
                if (edt_content != null) {
                    text_content = edt_content.getText().toString();
                }
                final ApiServices api = ApiServices.getInstance();
                WebService ws = api.getRetrofit().create(WebService.class);
                if (rsProduct != null) {
                    final ProgressDialog pg = Utils.createProgressDialog(context);
                    pg.show();
                    Call<ResponseMessage> acction_send_question = ws.actionPostQuestion(rsProduct.getId(), text_content);
                    acction_send_question.enqueue(new Callback<ResponseMessage>() {
                        @Override
                        public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                            pg.dismiss();
                            if (f instanceof ProductDetailFragment) {
                                ProductDetailFragment productDetail = (ProductDetailFragment) f;
                                productDetail.setView(productDetail.getProduct_id());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseMessage> call, Throwable t) {
                            pg.dismiss();
                            Utils.OnFailException(t);
                        }
                    });
                }
            }
        });
        return viewFormCommnet;
    }

    public View FormAnswer(CommentItemView item, final int position) {
        final CommentItemView questionAnswers = (CommentItemView) item;

        final View viewFormCommnet = ((LayoutInflater) ProductDetailAdapter.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.form_recomment_product, null, true);
        final EditText edt_content = (EditText) viewFormCommnet.findViewById(R.id.edtContentCmt);

        edt_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (current_edittext != edt_content) {
                        requestFocus = true;
                        current_edittext = edt_content;

                        listView.setSelection(position + 1);
                        edt_content.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (edt_content.hasFocus() == false) {
                                    edt_content.requestFocus();
                                }
                            }
                        }, 500);
                    }
                }
            }
        });

        LinearLayout ll_button_send = (LinearLayout) viewFormCommnet.findViewById(R.id.ll_button_send);
        ll_button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text_content = "";
                if (edt_content != null) {
                    text_content = edt_content.getText().toString();
                }
                final ApiServices api = ApiServices.getInstance();
                WebService ws = api.getRetrofit().create(WebService.class);
                if (questionAnswers != null) {
                    final ProgressDialog pg = Utils.createProgressDialog(context);
                    pg.show();
                    Call<ResponseMessage> acction_send_question = ws.actionPostAnswer(questionAnswers.getId(), text_content);
                    acction_send_question.enqueue(new Callback<ResponseMessage>() {
                        @Override
                        public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                            pg.dismiss();
                            if (f instanceof ProductDetailFragment) {
                                ProductDetailFragment productDetail = (ProductDetailFragment) f;
                                productDetail.setView(productDetail.getProduct_id());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseMessage> call, Throwable t) {
                            pg.dismiss();
                            Utils.OnFailException(t);
                        }
                    });
                }
            }
        });
        return viewFormCommnet;
    }

    public View QABoxView(View convertView) {
        if (convertView == null) {
            convertView = ((LayoutInflater) ProductDetailAdapter.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.form_comment_product, null);
            final EditText editext = (EditText) convertView
                    .findViewById(R.id.edtContentCmt);

            editext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        if (current_edittext != editext) {
                            requestFocus = true;
                            current_edittext = editext;

                            editext.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (editext.hasFocus() == false) {
                                        editext.requestFocus();
                                    }
                                }
                            }, 1500);
                        }
                    }

                }
            });
        }
        //Fill EditText with the value you have in data source


        //we need to update adapter once we finish with editing

        return convertView;
    }

    public void onScrollMore(final EditText edt, int position) {
        listView.setSelection(position);
        isHidekeyboard = false;
        edt.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isHidekeyboard == false) {
                    edt.requestFocus();
                }

            }
        }, 500);

    }

    @Override
    public void actionWhenHideKeyboard() {
        if (current_edittext != null) {
            notifyDataSetChanged();
            listView.clearChoices();
            notifyDataSetChanged();
            isHidekeyboard = true;
            current_edittext.clearFocus();
            current_edittext = null;
        }
    }

    @Override
    public void actionWhenShowKeyboard() {

    }


    /**
     * Create item reply question answer
     *
     * @param: ReplyQuestionAnswers
     * @return: ReplyQuestionAnswers
     */


    /**
     * Create item tender
     *
     * @param : Tender
     * @return :
     * View Tender
     */

    public View CreateItemTender(final ProductTenders productTender, Boolean isAccept) {
        long offer_id = 0, offerPriceIDLast = 0, tender_id = 0, product_tender_shipping_id = 0;

        RoundedImageView img_offer;
        final TextView name, offer_price, arrow_offer_price, counter_offer_price, arrow_reoffer,
                reoffer_price;
        TextView titleButtonOffer, title_button_confirm_order;
        String str_offer_price = "", str_reoffer_offer_price = "", str_counter_offer_price = "", remarkCounterOffer = "";
        LinearLayout offer;
        TextView tvNumBid, num_review_tender, dateCteateComment;
        ImageView verifies_user_tender, verifies_tender, id_chat;
        RatingBar ratingBarTender;
        ButtonTender bt_tender;
        View viewProductTender = ((LayoutInflater) ProductDetailAdapter.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_product_tender, null, false);
        offer = (LinearLayout) viewProductTender.findViewById(R.id.offer);
        name = (TextView) viewProductTender.findViewById(R.id.tv_name_product);
        offer_price = (TextView) viewProductTender.findViewById(R.id.id_offer_price);
        arrow_offer_price = (TextView) viewProductTender.findViewById(R.id.arrow_offer_price);
        counter_offer_price = (TextView) viewProductTender.findViewById(R.id.id_counter_offer_price);
        arrow_reoffer = (TextView) viewProductTender.findViewById(R.id.arrow_reoffer);
        reoffer_price = (TextView) viewProductTender.findViewById(R.id.id_reoffer_price);
        num_review_tender = (TextView) viewProductTender.findViewById(R.id.num_review_tender);
        verifies_user_tender = (ImageView) viewProductTender.findViewById(R.id.verifies_user_tender);
        verifies_tender = (ImageView) viewProductTender.findViewById(R.id.verifies_tender);

        title_button_confirm_order = (TextView) viewProductTender.findViewById(R.id.title_button_confirm_order);
        img_offer = (RoundedImageView) viewProductTender.findViewById(R.id.img_offer);
        ratingBarTender = (RatingBar) viewProductTender.findViewById(R.id.ratingBar);
        dateCteateComment = (TextView) viewProductTender.findViewById(R.id.create_tender);
        bt_tender = (ButtonTender) viewProductTender.findViewById(R.id.bt_tender);
        arrow_end = (TextView) viewProductTender.findViewById(R.id.arrow_end);
        id_pending = (TextView) viewProductTender.findViewById(R.id.id_pending);
        id_received = (TextView) viewProductTender.findViewById(R.id.id_received);
        id_finish_transaction = (TextView) viewProductTender.findViewById(R.id.id_finish_transaction);
        arrow_pending = (TextView) viewProductTender.findViewById(R.id.arrow_pending);
        arrow_received = (TextView) viewProductTender.findViewById(R.id.arrow_received);
        arrow_finish_transaction = (TextView) viewProductTender.findViewById(R.id.arrow_finish_transaction);
        id_chat = (ImageView) viewProductTender.findViewById(R.id.id_chat);
        boolean isEnableTenderClick = true;

        if (productTender.getCreated_at() != null) {
            String formatDate = "";
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d'T',yyyy");

            String myStringDate = productTender.getCreated_at();

            Date myDate = null;
            try {
                myDate = inputFormat.parse(myStringDate);
                int day = Integer.parseInt(new SimpleDateFormat("d").format(myDate));
                String day_str = getDayNumberSuffix(day);
                String month = new SimpleDateFormat("MMMM").format(myDate);
                String year = new SimpleDateFormat("yyyy").format(myDate);
                formatDate = month + " " + day_str + ", " + year;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateCteateComment.setText(formatDate);
        }

        if (productTender.getCommon_user().getSetup_account() != null) {
            if (productTender.getCommon_user().getVerification_state().equals("closed")) {
                verifies_tender.setBackgroundResource(R.drawable.verified);
                verifies_user_tender.setBackgroundResource(R.drawable.green_icon);
            } else {
                verifies_tender.setBackgroundResource(R.drawable.unverifyimg);
                verifies_user_tender.setBackgroundResource(R.drawable.yellow_icon);
            }
        }
        try {
            float ratingStar = (float) (productTender.getCommon_user().getAverage_rating());
            int intpart = (int) ratingStar;
            float decpart = ratingStar - intpart;
            if (decpart != 0.0f) {
                ratingStar = intpart + 0.5f;
            }
            ratingBarTender.setRating(ratingStar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (productTender.getCommon_user().getNum_of_reviews() != null) {
            num_review_tender.setText("(" + productTender.getCommon_user().getNum_of_reviews().toString() + ")");
        }
        final long productID = Long.valueOf(rsProduct.getId());

        //buyer
        if (idUser == rsProduct.getCommon_user().getId()) {
            id_chat.setVisibility(View.VISIBLE);
            id_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int tendererID = productTender.getCommon_user().getId();
                        int wanttobuyID = rsProduct.getId();
                        MainActivity mainActivity = (MainActivity) ProductDetailAdapter.this.context;
                        ChatDetailFragment fragment = new ChatDetailFragment();
                        fragment.setWantTobuyID(wanttobuyID);
                        fragment.setTendererID(tendererID);
                        mainActivity.changeFragment(fragment, true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            boolean flagAccepted = false;

            LinearLayout bt_confirm_order = (LinearLayout) viewProductTender.findViewById(R.id.bt_confirm_order);
            offer.setVisibility(View.VISIBLE);
            tender_id = productTender.getId();
            Drawable bg_button_tender = ContextCompat.getDrawable(CustomApp.getInstance(), R.drawable.button_drable_dark);
            bt_tender.setBg_default(bg_button_tender);
            bt_tender.setTextTender("回应");

            if (productTender.getProduct_offers() != null) {
                if (productTender.getProduct_offers().size() != 0) {
                    bt_tender.setVisibility(View.VISIBLE);
                    arrow_end.setVisibility(View.VISIBLE);
                    if (productTender.getProduct_offers().get(0).getProduct_counter_offer() != null) {
                        arrow_offer_price.setVisibility(View.VISIBLE);
                        float fCounterOfferPrice = Float.parseFloat(productTender.getProduct_offers().get(0).getProduct_counter_offer().getCounter_offer_price().getFractional()) / 100;
                        strCounterOfferPrice = String.format("%.2f", fCounterOfferPrice);
                        str_counter_offer_price = CustomApp.getInstance().getString(R.string.product_counter_offer_1
                                , strCounterOfferPrice);
                        counter_offer_price.setText(str_counter_offer_price);
                        counter_offer_price.setVisibility(View.VISIBLE);

                        bt_tender.setVisibility(View.VISIBLE);
                        arrow_end.setVisibility(View.VISIBLE);
                        bt_tender.setEnabled(false);
//                    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, ProductDetailAdapter.this.getContext().getResources().getDisplayMetrics());
                        bg_button_tender = ContextCompat.getDrawable(CustomApp.getInstance(), R.drawable.red_btn);
                        bt_tender.setBg_default(bg_button_tender);
                        isEnableTenderClick = false;
                        bt_tender.setTextTender("等待对方回应");

//                        btn_post_offer.setVisibility(View.GONE);
//                        arrow_end.setVisibility(View.GONE);
                        if (productTender.getProduct_offers().size() > 1) {
                            arrow_reoffer.setVisibility(View.VISIBLE);
                            float fReOfferPrice = Float.parseFloat(productTender.getProduct_offers().get(1).getOffer_price().getFractional()) / 100;
                            String strReOfferPrice = String.format("%.2f", fReOfferPrice);
                            str_reoffer_offer_price = CustomApp.getInstance().getString(R.string.reoffer_price_1, strReOfferPrice);
                            reoffer_price.setVisibility(View.VISIBLE);
                            reoffer_price.setText(str_reoffer_offer_price);
                            if (!str_reoffer_offer_price.equals("")) {
                                bt_tender.setVisibility(View.VISIBLE);
                                arrow_end.setVisibility(View.VISIBLE);
                                bg_button_tender = ContextCompat.getDrawable(CustomApp.getInstance(), R.drawable.button_drable_dark);
                                bt_tender.setBg_default(bg_button_tender);
                                bt_tender.setTextTender("回应");
                                bt_tender.setEnabled(true);
                                isEnableTenderClick = true;
//                    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, ProductDetailAdapter.this.getContext().getResources().getDisplayMetrics());
                            }
                        }
                    }
                }
            }

            if (productTender.getCommon_user().getProfile_picture().getUrl() != null) {
                String url_avt = productTender.getCommon_user().getProfile_picture().getUrl();
                if (url_avt != null && !url_avt.equals("")) {
                    ImageLoader il = ImageLoader.getInstance();
                    il.displayImage(url_avt, img_offer, OPTION_DISPLAY_IMG);
                }
            }

            if (productTender.getProduct_listing() != null) {
                isReoffer = productTender.getProduct_listing().getIs_offered();
            }

            if (rsProduct.getIs_offered() != null) {
                isOffered = rsProduct.getIs_offered();
            }
            if (productTender.getProduct_offers() != null) {
                offer_id = productTender.getProduct_offers().get(0).getId();
                offerPriceIDLast = productTender.getProduct_offers().get(productTender.getProduct_offers().size() - 1).getId();
            }
            if (rsProduct.getProduct_tenders() != null) {
                tender_id = productTender.getId();
            }
            if (productTender.getShipping_methods() != null) {
                product_tender_shipping_id = productTender.getProduct_tender_shippings().get(0).getShipping_method_id();
            }
            //btn_post_offer.setVisibility(View.VISIBLE);
            if (productTender.getCommon_user() != null) {
                name.setText(productTender.getCommon_user().getNickname().toString());
            }
            if (productTender.getProduct_offers() != null) {
                float fOfferPrice = Float.parseFloat(productTender.getProduct_offers().get(0).getOffer_price().getFractional()) / 100;
                String strOfferPrice = String.format("%.2f", fOfferPrice);
                str_offer_price = CustomApp.getInstance().getString(R.string.offer_price_1, strOfferPrice);
                offer_price.setText(str_offer_price);
            }
            final String finalStr_counter_offer_price = str_counter_offer_price;
            final String finalStr_reoffer_offer_price = str_reoffer_offer_price;
            final String finalStr_offer_price = str_offer_price;
            //if (offer_id != 0 && tender_id != 0 && product_tender_shipping_id != 0 && offerIDLast != 0) {
            final long finalOffer_id = offer_id;
            final long finalTender_id = tender_id;
            final long finalProduct_tender_shipping_id = product_tender_shipping_id;
            final long finalOfferPriceIDLast = offerPriceIDLast;
            final ProductTenders finalProductTenders = productTender;
            bt_tender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogCountoffer cdd = new DialogCountoffer(context
                            , finalOffer_id
                            , finalTender_id
                            , finalProduct_tender_shipping_id
                            , finalStr_offer_price
                            , finalStr_counter_offer_price
                            , finalStr_reoffer_offer_price
                            , finalOfferPriceIDLast
                            , finalProductTenders
                            , rsProduct
                            , f
                    );
                    Rect displayRectangle = new Rect();
                    cdd.setCanceledOnTouchOutside(true);
                    cdd.show();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(cdd.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    cdd.getWindow().setAttributes(lp);

                }
            });

            if (productTender.isClosed()) {
                if (isAccept) {

                    bt_tender.setVisibility(View.VISIBLE);
                    arrow_end.setVisibility(View.VISIBLE);
                    bt_tender.setEnabled(false);
                    float price = 0;
                    String strReOfferPrice = "";
                    ProductOffer productOfferAccept = GetProductTenderOfferAccept(productTender.getProduct_offers());
                    if (productOfferAccept != null) {
                        price = Float.parseFloat(productOfferAccept.getOffer_price().getFractional()) / 100;
                        strReOfferPrice = String.format("%.2f", price);
                    }
//                    if (rsProduct.getProduct_tenders() != null && rsProduct.getOrderModels().size() > 0) {
//                        price = rsProduct.getOrderModels().get(0).getAgreed_price_cents() / 100;
//                        strReOfferPrice = String.format("%.2f", price);
//                    }
                    flagAccepted = true;

                    bg_button_tender = ContextCompat.getDrawable(CustomApp.getInstance(), R.drawable.violet_btn);
                    bt_tender.setTextTender("买家已接受出价 ¥ " + strReOfferPrice);
                    bt_tender.setBg_default(bg_button_tender);

                } else {
                    arrow_end.setVisibility(View.VISIBLE);
                    bt_tender.setVisibility(View.VISIBLE);
                    arrow_end.setVisibility(View.VISIBLE);
                    bt_tender.setEnabled(false);
//                    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, ProductDetailAdapter.this.getContext().getResources().getDisplayMetrics());
                    bg_button_tender = ContextCompat.getDrawable(CustomApp.getInstance(), R.drawable.red_btn);
                    bt_tender.setBg_default(bg_button_tender);
                    bt_tender.setTextTender("买家已拒绝出价");
                }
            }

            int idTenderClosed = -1;
            for (int i = 0; i < rsProduct.getProduct_tenders().size(); i++) {
                for (ProductOffer item : rsProduct.getProduct_tenders().get(i).getProduct_offers()) {
                    if (item.getState().equals("accepted")) {
                        idTenderClosed = rsProduct.getProduct_tenders().get(i).getId();
                        break;
                    }
                }
            }

            if (rsProduct.isClosed() == true) {
                if (idTenderClosed != productTender.getId()) {
                    boolean isReject = false;
                    for (ProductOffer item : productTender.getProduct_offers()) {
                        if (item.getState().equals("rejected")) {
                            isReject = true;
                        }
                    }
                    if (isReject == false) {
                        arrow_end.setVisibility(View.GONE);
                        bt_tender.setVisibility(View.GONE);
                    }
                }
            }

//            }
            offer_price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String flag = "offer";
                    DialogDetailOffer dialogDetailOffer = new DialogDetailOffer(ProductDetailAdapter.this.context, productTender, flag);
                    dialogDetailOffer.show();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    Window window = dialogDetailOffer.getWindow();
                    lp.copyFrom(window.getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    window.setAttributes(lp);
                }
            });
            reoffer_price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String flag = "reoffer";
                    DialogDetailOffer dialogDetailOffer = new DialogDetailOffer(ProductDetailAdapter.this.context, productTender, flag);
                    dialogDetailOffer.show();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    Window window = dialogDetailOffer.getWindow();
                    lp.copyFrom(window.getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    window.setAttributes(lp);
                }
            });

            counter_offer_price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogDetailCounterOffer dialogDetailCounterOffer = new DialogDetailCounterOffer(ProductDetailAdapter.this.context, productTender);
                    dialogDetailCounterOffer.show();
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    Window window = dialogDetailCounterOffer.getWindow();
                    lp.copyFrom(window.getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    window.setAttributes(lp);
                }
            });
            //}
        } else {
            // seller
            if (idUser == productTender.getCommon_user().getId()) {
                bt_confirm_order = (LinearLayout) viewProductTender.findViewById(R.id.bt_confirm_order);
                offer.setVisibility(View.VISIBLE);
                boolean isOrderedSuccess = false;
                if (productTender != null
                        && productTender.getProduct_offers() != null
                        && productTender.getProduct_offers().size() > 0) {
                    offer_id = productTender.getProduct_offers().get(0).getId();
                    offerPriceIDLast = productTender.getProduct_offers().get(productTender.getProduct_offers().size() - 1).getId();
                    tender_id = productTender.getId();

                    if (productTender.getProduct_offers() != null) {
                        isReoffer = productTender.getProduct_offers().get(0).getReoffer();
                        bt_tender.setVisibility(View.VISIBLE);
                        arrow_end.setVisibility(View.VISIBLE);
//                    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, ProductDetailAdapter.this.getContext().getResources().getDisplayMetrics());
                        Drawable bg_button_tender = ContextCompat.getDrawable(CustomApp.getInstance(), R.drawable.red_btn);
                        bt_tender.setEnabled(false);
                        bt_tender.setBg_default(bg_button_tender);
                        bt_tender.setTextTender("等待对方回应");
                        isEnableTenderClick = false;
                    }

                    if (productTender.getShipping_methods() != null) {
                        product_tender_shipping_id = productTender.getProduct_tender_shippings().get(0).getId();
                    }

                    if (productTender.getCommon_user() != null) {
                        name.setText(productTender.getCommon_user().getNickname());
                    }
                    if (productTender.getProduct_offers() != null) {
                        if (productTender.getProduct_offers().size() != 0) {
//                            btn_post_offer.setVisibility(View.GONE);
//                            arrow_end.setVisibility(View.GONE);
                            float fOfferPrice = Float.parseFloat(productTender.getProduct_offers().get(0).getOffer_price().getFractional()) / 100;
                            String strOfferPrice = String.format("%.2f", fOfferPrice);
                            str_offer_price = CustomApp.getInstance().getString(R.string.offer_price, strOfferPrice);
                        }
                    }
                    offer_price.setText(str_offer_price);

                    if (productTender.getProduct_offers().get(0).getProduct_counter_offer() != null) {
                        arrow_offer_price.setVisibility(View.VISIBLE);
                        float fCounterOfferPrice = Float.parseFloat(productTender.getProduct_offers().get(0).getProduct_counter_offer().getCounter_offer_price().getFractional()) / 100;
                        strCounterOfferPrice = String.format("%.2f", fCounterOfferPrice);
                        str_counter_offer_price = CustomApp.getInstance().getString(R.string.product_counter_offer_1
                                , strCounterOfferPrice);
                        remarkCounterOffer = productTender.getProduct_offers().get(0).getProduct_counter_offer().getRemarks().toString();
                        counter_offer_price.setText(str_counter_offer_price);
                        counter_offer_price.setVisibility(View.VISIBLE);
                        arrow_end.setVisibility(View.VISIBLE);
                        bt_tender.setVisibility(View.VISIBLE);
                        arrow_end.setVisibility(View.VISIBLE);
//                    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, ProductDetailAdapter.this.getContext().getResources().getDisplayMetrics());

                        Drawable bg_button_tender = ContextCompat.getDrawable(CustomApp.getInstance(), R.drawable.button_drable_dark);
                        bt_tender.setBg_default(bg_button_tender);
                        bt_tender.setTextTender("重新出价");
                        isEnableTenderClick = true;
                        bt_tender.setEnabled(true);
                        if (productTender.getProduct_offers().size() > 1) {
                            arrow_reoffer.setVisibility(View.VISIBLE);
                            float fReOfferPrice = Float.parseFloat(productTender.getProduct_offers().get(1).getOffer_price().getFractional()) / 100;
                            String strReOfferPrice = String.format("%.2f", fReOfferPrice);
                            str_reoffer_offer_price = CustomApp.getInstance().getString(R.string.reoffer_price_1, strReOfferPrice);
                            //isReoffer = true;
                            reoffer_price.setText(str_reoffer_offer_price);
                            reoffer_price.setVisibility(View.VISIBLE);
                            if (!str_reoffer_offer_price.equals("")) {
                                arrow_end.setVisibility(View.GONE);
                                bt_tender.setVisibility(View.GONE);
                            }

                            bt_tender.setVisibility(View.VISIBLE);
                            arrow_end.setVisibility(View.VISIBLE);
                            bt_tender.setEnabled(false);
                            bg_button_tender = ContextCompat.getDrawable(CustomApp.getInstance(), R.drawable.red_btn);
                            bt_tender.setBg_default(bg_button_tender);
                            bt_tender.setTextTender("等待对方回应");
                            isEnableTenderClick = false;
                        }
                    }
                    if (offer_id != 0 && tender_id != 0 && product_tender_shipping_id != 0 && offerPriceIDLast != 0 && isEnableTenderClick) {
                        bt_tender.setEnabled(true);

                        final String finalStr_counter_offer_price = strCounterOfferPrice;
                        final long finalTender_id1 = tender_id;
                        final String finalRemarkCounterOffer = remarkCounterOffer;
                        final String finalOfferPrice = String.valueOf(Float.parseFloat(productTender.getProduct_offers().get(0).getOffer_price().getFractional()) / 100);
                        bt_tender.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (finalStr_counter_offer_price.equals("") == false) {
                                    DialogRebid dialog_bidshop = new DialogRebid(context, productID, finalTender_id1, isReoffer, finalStr_counter_offer_price, isOffered,
                                            finalOfferPrice, finalRemarkCounterOffer);
                                    dialog_bidshop.setCanceledOnTouchOutside(true);
                                    dialog_bidshop.f = f;
                                    RsProduct myProduct = rsProduct;
                                    if (myProduct != null) {
                                        dialog_bidshop.setMyProduct(myProduct);
                                    }
                                    dialog_bidshop.show();


                                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                                    layoutParams.copyFrom(dialog_bidshop.getWindow().getAttributes());
                                    int dialogWindowWidth = WindowManager.LayoutParams.MATCH_PARENT;
                                    int dialogWindowHeight = WindowManager.LayoutParams.WRAP_CONTENT;
                                    layoutParams.width = dialogWindowWidth;
                                    layoutParams.height = dialogWindowHeight;
                                    dialog_bidshop.getWindow().setAttributes(layoutParams);
                                } else {
                                    DialogBidShop dialog_bidshop = new DialogBidShop(context, productID, finalTender_id1, isReoffer, finalStr_counter_offer_price, isOffered);
                                    dialog_bidshop.setCanceledOnTouchOutside(true);
                                    RsProduct myProduct = rsProduct;
                                    if (myProduct != null) {
                                        dialog_bidshop.setMyProduct(myProduct);
                                    }
                                    dialog_bidshop.show();
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(dialog_bidshop.getWindow().getAttributes());
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


                                    dialog_bidshop.getWindow().setAttributes(lp);
                                   //dialog_bidshop.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                                }

                            }
                        });
                    }
                    if (productTender.isClosed()) {
                        if (isAccept) {

                            arrow_end.setVisibility(View.VISIBLE);
                            bt_tender.setVisibility(View.VISIBLE);
                            Drawable bg_button_tender = ContextCompat.getDrawable(CustomApp.getInstance(), R.drawable.violet_btn);
                            bt_tender.setBg_default(bg_button_tender);
                            float price = 0;
//                            OrderModel orderModelAccept = GetOrderAccept(rsProduct.getOrderModels());
//                            try {
//                                price = orderModelAccept.getAgreed_price_cents() / 100;
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            String strReOfferPrice = String.format("%.2f", price);
                            String strReOfferPrice = "";
                            ProductOffer productOfferAccept = GetProductTenderOfferAccept(productTender.getProduct_offers());
                            if (productOfferAccept != null) {
                                price = Float.parseFloat(productOfferAccept.getOffer_price().getFractional()) / 100;
                                strReOfferPrice = String.format("%.2f", price);
                            }
                            bt_tender.setEnabled(false);
                            bt_tender.setTextTender("买家已接受出价 ¥ " + strReOfferPrice);
                        }
                        if (CheckStateOffer(productTender).equals("rejected")) {
                            arrow_end.setVisibility(View.VISIBLE);
                            bt_tender.setEnabled(false);
                            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, ProductDetailAdapter.this.getContext().getResources().getDisplayMetrics());
                            bt_tender.setVisibility(View.VISIBLE);
                            arrow_end.setVisibility(View.VISIBLE);
                            Drawable bg_button_tender = ContextCompat.getDrawable(CustomApp.getInstance(), R.drawable.red_btn);
                            bt_tender.setBg_default(bg_button_tender);
                            bt_tender.setTextTender("买家已拒绝出价");
                        }
                    }
                    boolean isCheckOrder = false;
                    int idPositionTenderAccept = -1;
                    for (int i = 0; i < rsProduct.getProduct_tenders().size(); i++) {
                        for (int j = 0; j < rsProduct.getProduct_tenders().get(i).getProduct_offers().size(); j++) {
                            if (rsProduct.getProduct_tenders().get(i).getProduct_offers().get(j).getState().equals("accepted")) {
                                isCheckOrder = true;
                                idPositionTenderAccept = rsProduct.getProduct_tenders().get(i).getId();
                            }
                        }
                    }
                    if (isCheckOrder) {
                        if (productTender.getId() != idPositionTenderAccept && rsProduct.getOrders().size() > 0) {
                            if (!CheckStateOffer(productTender).equals("rejected")) {
                                arrow_end.setVisibility(View.GONE);
                                bt_tender.setVisibility(View.GONE);
                            }
                            try {
                                arrow_end.setVisibility(View.VISIBLE);
                                bt_tender.setVisibility(View.VISIBLE);
                                arrow_end.setVisibility(View.VISIBLE);
                                Drawable bg_button_tender = ContextCompat.getDrawable(CustomApp.getInstance(), R.drawable.red_btn);
                                bt_tender.setBg_default(bg_button_tender);

                                bt_tender.setTextTender(rsProduct.getCommon_user().getNickname() + " 已经接受了其他用户的出价");
//                            if (flagAccepted == true) {

//                            }
//                            else {
//                                arrow_end.setVisibility(View.VISIBLE);
//                                btn_post_offer.setVisibility(View.VISIBLE);
//                            }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                if (productTender.getCommon_user().getProfile_picture().getUrl() != null) {
                    String url_avt = productTender.getCommon_user().getProfile_picture().getUrl();
                    if (url_avt != null && !url_avt.equals("")) {
                        ImageLoader il = ImageLoader.getInstance();
                        il.displayImage(url_avt, img_offer, OPTION_DISPLAY_IMG);
                    }
                }
                offer_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String flag = "offer";
                        DialogDetailOffer dialogDetailOffer = new DialogDetailOffer(ProductDetailAdapter.this.context, productTender, flag);
                        dialogDetailOffer.show();
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        Window window = dialogDetailOffer.getWindow();
                        lp.copyFrom(window.getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        window.setAttributes(lp);
                    }
                });
                reoffer_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String flag = "reoffer";
                        DialogDetailOffer dialogDetailOffer = new DialogDetailOffer(ProductDetailAdapter.this.context, productTender, flag);
                        dialogDetailOffer.show();
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        Window window = dialogDetailOffer.getWindow();
                        lp.copyFrom(window.getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        window.setAttributes(lp);
                    }
                });

                counter_offer_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogDetailCounterOffer dialogDetailCounterOffer = new DialogDetailCounterOffer(ProductDetailAdapter.this.context, productTender);
                        dialogDetailCounterOffer.show();
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        Window window = dialogDetailCounterOffer.getWindow();
                        lp.copyFrom(window.getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        window.setAttributes(lp);
                    }
                });
            }
        }
        return viewProductTender;
    }


    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return day + "th";
        }
        switch (day % 10) {
            case 1:
                return day + "st";
            case 2:
                return day + "nd";
            case 3:
                return day + "rd";
            default:
                return day + "th";
        }
    }

    /**
     * Get confirm receive
     *
     * @params: id: identify order
     * @return:
     */
    public void GetConfirmReceive(int idConfirmReceive) {
        final ProgressDialog progressLoading = Utils.createProgressDialog(context);
        ApiServices api = ApiServices.getInstance();
        final WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<OrderModel>> call = ws.actionGetConfirmReceive(idConfirmReceive);
        call.enqueue(new Callback<ResponseMessage<OrderModel>>() {
            @Override
            public void onResponse(Call<ResponseMessage<OrderModel>> call, Response<ResponseMessage<OrderModel>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        OrderModel orderModel = response.body().getData();
                        if (orderModel.getBuyer_receive_status().equals(BUYER_RECEIVE_STATUS_RECEIVED)) {
                            if (f instanceof ProductDetailFragment) {
                                ProductDetailFragment productDetail = (ProductDetailFragment) f;
                                productDetail.setView(productDetail.getProduct_id());
                            }
                        }
                        progressLoading.dismiss();
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }

                        if (progressLoading != null) {
                            progressLoading.dismiss();
                        }
                    } else {
                        if (progressLoading != null) {
                            progressLoading.dismiss();
                        }
                        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<OrderModel>> call, Throwable t) {
                Utils.OnFailException(t);
            }
        });
    }

    /**
     * Get confirm receive
     *
     * @params: id: identify order
     * @return:
     */
    public void GetConfirmDelivery(int idConfirmReceive) {
        final ProgressDialog progressLoading = Utils.createProgressDialog(context);
        ApiServices api = ApiServices.getInstance();
        final WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<OrderModel>> call = ws.actionGetConfirmDelivery(idConfirmReceive);
        call.enqueue(new Callback<ResponseMessage<OrderModel>>() {
            @Override
            public void onResponse(Call<ResponseMessage<OrderModel>> call, Response<ResponseMessage<OrderModel>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        OrderModel orderModel = response.body().getData();
                        if (orderModel.getSeller_delivery_status().equals(SELLER_DELIVERY_STATUS_SENT)) {
                            if (f instanceof ProductDetailFragment) {
                                ProductDetailFragment productDetail = (ProductDetailFragment) f;
                                productDetail.setView(productDetail.getProduct_id());
                            }
                        }
                        progressLoading.dismiss();
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }

                        if (progressLoading != null) {
                            progressLoading.dismiss();
                        }
                    } else {
                        if (progressLoading != null) {
                            progressLoading.dismiss();
                        }
                        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<OrderModel>> call, Throwable t) {
                Utils.OnFailException(t);
            }
        });
    }

    /**
     * check state offer
     *
     * @params:
     * @return:
     */
    public String CheckStateOffer(ProductTenders productTenders) {
        return productTenders.getProduct_offers().get(productTenders.getProduct_offers().size() - 1).getState();
    }

    public int CountItem(ArrayList<Object> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof QuestionAnswers
                    || list.get(i) instanceof QuestionReplyModel
                    || list.get(i) instanceof ProductTenders) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get order confirm or prcessing
     *
     * @params:
     * @return:
     */
    private OrderModel GetOrderAccept(ArrayList<OrderModel> listOrder) {
        for (int i = 0; i < listOrder.size(); i++) {
            if (listOrder.get(i).getStatus().equals(STATUS_ORDER_CONFIRMED)
                    || listOrder.get(i).getStatus().equals(STATUS_ORDER_PROCESSING)) {
                return listOrder.get(i);
            }
        }
        return null;
    }

    /**
     * Get order confirm or prcessing
     *
     * @params:
     * @return:
     */
    private ProductOffer GetProductTenderOfferAccept(ArrayList<ProductOffer> productOffers) {
        for (int i = 0; i < productOffers.size(); i++) {
            if (productOffers.get(i).getState().equals(OFFER_ACCEPT)) {
                return productOffers.get(i);
            }
        }
        return null;
    }
}
