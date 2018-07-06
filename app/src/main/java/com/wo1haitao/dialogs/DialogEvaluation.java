package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsMyReview;
import com.wo1haitao.api.response.RsReviewAwaitting;
import com.wo1haitao.controls.CustomEditextSoftkey;
import com.wo1haitao.controls.CustomRatingBar;
import com.wo1haitao.fragments.AllEvaFragment;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class DialogEvaluation extends Dialog {

    public Activity c;
    Fragment f;
    WindowManager.LayoutParams lp;
    ImageView iv_back_action;
    RsReviewAwaitting rsReviewAwaitting;

    public DialogEvaluation(Activity a, RsReviewAwaitting rsReviewAwaitting) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.rsReviewAwaitting = rsReviewAwaitting;
        lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
    }

    public Fragment getF() {
        return f;
    }

    public void setF(Fragment f) {
        this.f = f;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final View viewTabHost = ((LayoutInflater) DialogEvaluation.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.fragment_all_eva, null, false);
        setContentView(R.layout.layout_dialog_evaluation);
        Button btn_to_main = (Button) findViewById(R.id.btn_to_main);
        final long idUser = MyPreferences.getID();
        final LinearLayout ln_rating_bar, ln_rating_bar2, ln_rating_bar3;
        TextView title_dialog_evaluation;
        title_dialog_evaluation =(TextView) findViewById(R.id.title_dialog_evaluation);
        ln_rating_bar = (LinearLayout) findViewById(R.id.ln_rating_bar);
        ln_rating_bar2 = (LinearLayout) findViewById(R.id.ln_rating_bar2);
        ln_rating_bar3 = (LinearLayout) findViewById(R.id.ln_rating_bar3);
        if(idUser == rsReviewAwaitting.getCommon_user().getId()) {
            title_dialog_evaluation.setText(R.string.title_dialog_evaluation_my_bid);
            ln_rating_bar.setVisibility(View.VISIBLE);
            ln_rating_bar2.setVisibility(View.VISIBLE);
            ln_rating_bar3.setVisibility(View.VISIBLE);
        }
        else {
            title_dialog_evaluation.setText(R.string.title_dialog_evaluation);
            ln_rating_bar.setVisibility(View.GONE);
            ln_rating_bar2.setVisibility(View.GONE);
            ln_rating_bar3.setVisibility(View.VISIBLE);
        }
        btn_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiServices apiServices = ApiServices.getInstance();
                WebService ws = apiServices.getRetrofit().create(WebService.class);
                final ProgressDialog processDialog = Utils.createProgressDialog(c);
//                RatingBar ratingBar1, ratingBar2, ratingBar3;
                CustomRatingBar ratingBar1, ratingBar2, ratingBar3;
                String contentRating1 = "", contentRating2 = "", contentRating3 = "", reviewMssege = "";
                CustomEditextSoftkey reviewMassege;
                int idProduct;


                ratingBar1 = (CustomRatingBar) findViewById(R.id.ratingBar);
                ratingBar2 = (CustomRatingBar) findViewById(R.id.ratingBar2);
                ratingBar3 = (CustomRatingBar) findViewById(R.id.ratingBar3);
                reviewMassege = (CustomEditextSoftkey)findViewById(R.id.id_review_message);
                reviewMssege = reviewMassege.getText().toString();
                String finalReviewMssege = reviewMssege;
                idProduct = (int)rsReviewAwaitting.getId();

                if(idUser == rsReviewAwaitting.getCommon_user().getId()){
                    if(ratingBar1.getNumRating() != 0){
                        contentRating1 = String.valueOf(ratingBar1.getNumRating());
                    }
                    if(ratingBar2.getNumRating() != 0){
                        contentRating2 = String.valueOf((ratingBar2.getNumRating()));
                    }
                    if(ratingBar3.getNumRating() != 0){
                        contentRating3 = String.valueOf((ratingBar3.getNumRating()));
                    }
                    if(!contentRating1.equals("") && !contentRating2.equals("") && !contentRating3.equals("")) {
                        Call<ResponseMessage<RsMyReview>> call = ws.actionPostReview(idProduct, contentRating1, contentRating2, contentRating3, finalReviewMssege, idProduct);
                        call.enqueue(new Callback<ResponseMessage<RsMyReview>>() {
                            @Override
                            public void onResponse(Call<ResponseMessage<RsMyReview>> call, Response<ResponseMessage<RsMyReview>> response) {
                                try {
                                    if (response.body() != null) {
                                        if (response.body().isSuccess()) {
                                            RsMyReview rsMyReview = response.body().getData();
                                            processDialog.dismiss();
                                            if (f instanceof AllEvaFragment) {
                                                AllEvaFragment allEvaFragment = (AllEvaFragment) f;
                                                allEvaFragment.onRefeshTap();
                                            }
                                            dismiss();
                                        }
                                    } else if (response.errorBody() != null) {
                                        try {
                                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                                            ErrorMessage error = responseMessage.getErrors();
                                            Toast.makeText(getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                        }
                                        if (processDialog != null) {
                                            processDialog.dismiss();
                                        }
                                    } else {
                                        if (processDialog != null) {
                                            processDialog.dismiss();
                                        }
                                        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Utils.crashLog(e);
                                    if (processDialog != null) {
                                        processDialog.dismiss();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseMessage<RsMyReview>> call, Throwable t) {
                                if (processDialog != null) {
                                    processDialog.dismiss();
                                }
                                dismiss();
                                Utils.OnFailException(t);
                            }
                        });
                    }
                }
                else {
                    if(ratingBar1.getNumRating() != 0){
                        contentRating1 = String.valueOf((ratingBar1.getNumRating()));
                    }
                    if(!contentRating1.equals("")) {
                        Call<ResponseMessage<RsMyReview>> call = ws.actionPostReview(idProduct, contentRating1, finalReviewMssege, idProduct);
                        call.enqueue(new Callback<ResponseMessage<RsMyReview>>() {
                            @Override
                            public void onResponse(Call<ResponseMessage<RsMyReview>> call, Response<ResponseMessage<RsMyReview>> response) {
                                try {
                                    if (response.body() != null) {
                                        if (response.body().isSuccess()) {
                                            RsMyReview rsMyReview = response.body().getData();
                                            processDialog.dismiss();
                                            if (f instanceof AllEvaFragment) {
                                                AllEvaFragment allEvaFragment = (AllEvaFragment) f;
                                                allEvaFragment.onRefeshTap();
                                            }
                                            dismiss();
                                        }
                                    } else if (response.errorBody() != null) {
                                        try {
                                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                                            ErrorMessage error = responseMessage.getErrors();
                                            Toast.makeText(getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                        }
                                        if (processDialog != null) {
                                            processDialog.dismiss();
                                        }
                                    } else {
                                        if (processDialog != null) {
                                            processDialog.dismiss();
                                        }
                                        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Utils.crashLog(e);
                                    if (processDialog != null) {
                                        processDialog.dismiss();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseMessage<RsMyReview>> call, Throwable t) {
                                if (processDialog != null) {
                                    processDialog.dismiss();
                                }
                                dismiss();
                                Utils.OnFailException(t);
                            }
                        });
                    }
                }
                processDialog.dismiss();
            }
        });
        iv_back_action = (ImageView) findViewById(R.id.iv_back);
        iv_back_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
