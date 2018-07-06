package com.wo1haitao.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.adapters.UserReriewsAdapter;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 8/3/2017.
 */

public class UserReviews extends BaseFragment {
    ActionBarProject my_action_bar;
    UserProfile userProfile;
    UserReriewsAdapter userReriewsAdapter;
    ListView lv_product;
    View fragment_view;

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment_view = inflater.inflate(R.layout.fragment_user_reviews, container, false);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);

        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        lv_product = (ListView) fragment_view.findViewById(R.id.lv_reviews);
        GetUserReviews(userProfile.getId());
        return fragment_view;
    }

    public void GetUserReviews(int id){
        final ProgressDialog progressDialog = Utils.createProgressDialog(UserReviews.this.getActivity());
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<UserProfile>> call = ws.actionGetUserReview(id);
        call.enqueue(new Callback<ResponseMessage<UserProfile>>() {
            @Override
            public void onResponse(Call<ResponseMessage<UserProfile>> call, Response<ResponseMessage<UserProfile>> response) {
                try{
                    if(response.body() != null && response.isSuccessful()){
                        UserProfile userProfile = response.body().getData();
                        my_action_bar.showTitle(userProfile.getNickname() + "的评价");
                        userReriewsAdapter = new UserReriewsAdapter(getActivity(), userProfile.getReviews(), false);
                        lv_product.setAdapter(userReriewsAdapter);
                        lv_product.setEmptyView(fragment_view.findViewById(R.id.emptyElement));
                        progressDialog.dismiss();
                    }
                    else if(response.errorBody() != null){
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                    else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Utils.crashLog(e);
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }
}
