package com.wo1haitao.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.adapters.ProductsFeedBacks;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsDisputes;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbacksFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    ActionBarProject my_action_bar;
    ArrayList<RsDisputes> arrayOfProductModels;
    ProductsFeedBacks product_adapter;
    ListView lv_product_evaluation;
    SwipeRefreshLayout swipeRefreshLayout;
    View fragment_view;
    String flag = "feedback";

    public FeedbacksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment_view = inflater.inflate(R.layout.fragment_my_feedback, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) fragment_view.findViewById(R.id.swipe_refresh_layout);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        my_action_bar.showTitle(R.string.title_feedback);
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        lv_product_evaluation = (ListView) fragment_view.findViewById(R.id.id_feedback);
        lv_product_evaluation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MainActivity mainActivity = (MainActivity) FeedbacksFragment.this.getActivity();
                mainActivity.changeDetailProduct(arrayOfProductModels.get(position).getProduct_listing().getId(), flag);
            }
        });
        lv_product_evaluation.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int i2) {
                int topRowVerticalPosition = (lv_product_evaluation == null || lv_product_evaluation.getChildCount() == 0) ?
                        0 : lv_product_evaluation.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
        GetDispute();
        swipeRefreshLayout.setOnRefreshListener(FeedbacksFragment.this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                GetDispute();
            }
        });
        return fragment_view;
    }

    public void GetDispute() {
        swipeRefreshLayout.setRefreshing(true);
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        Call<ResponseMessage<ArrayList<RsDisputes>>> callDisputes = ws.actionGetDisputes();
        callDisputes.enqueue(new Callback<ResponseMessage<ArrayList<RsDisputes>>>() {
            @Override
            public void onResponse(Call<ResponseMessage<ArrayList<RsDisputes>>> call, Response<ResponseMessage<ArrayList<RsDisputes>>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        arrayOfProductModels = response.body().getData();
                        product_adapter = new ProductsFeedBacks(getActivity(), arrayOfProductModels, false);
                        lv_product_evaluation.setAdapter(product_adapter);
                        lv_product_evaluation.setEmptyView(fragment_view.findViewById(R.id.emptyElement));

                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<ArrayList<RsDisputes>>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Utils.OnFailException(t);
            }
        });
    }

    @Override
    public void onRefresh() {
        GetDispute();
    }
}
