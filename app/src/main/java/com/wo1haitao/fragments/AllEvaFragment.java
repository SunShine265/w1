package com.wo1haitao.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.adapters.ProductsEvaTab1Adapter;
import com.wo1haitao.adapters.ProductsEvaTab2Adapter;
import com.wo1haitao.adapters.ProductsEvaTab3Adapter;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsMyReview;
import com.wo1haitao.api.response.RsReview;
import com.wo1haitao.api.response.RsReviewAwaitting;
import com.wo1haitao.api.response.RsReviewToMe;
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
public class AllEvaFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    ActionBarProject my_action_bar;
    //    ArrayList<ProductModel> arrayOfProductModels, arrayOfProductModels2;
    ArrayList<RsMyReview> arrayOfMyReview;
    ArrayList<RsReviewToMe> arrayOfReviewToMe;
    ArrayList<RsReviewAwaitting> arrayOfReviewAwaitting;
    ProductsEvaTab1Adapter product_adapter_tab1;
    ProductsEvaTab2Adapter product_adapter_tab2;
    ProductsEvaTab3Adapter product_adapter_tab3;
    ListView lv_product_evaluation_tab_1, lv_product_evaluation_tab_2, lv_product_evaluation_tab_3;
    TabHost my_tabhost;
    RsReview rsReview;
    String title_tap1, title_tap2, title_tap3;
    SwipeRefreshLayout swipeRefreshLayout;
    View fragment_view;

    public AllEvaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment_view = inflater.inflate(R.layout.fragment_all_eva, container, false);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) fragment_view.findViewById(R.id.swipe_refresh_layout);
        my_action_bar.showTitle(R.string.title_all_evaluation);
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        my_action_bar.DowSizeHeightActionbar(48);
        my_tabhost = (TabHost) fragment_view.findViewById(R.id.tabhost_my);
        my_tabhost.setup();

        title_tap1 = getString(R.string.tab_not_yet_cmt);
        title_tap2 = getString(R.string.tab_cmt_rc);
        title_tap3 = getString(R.string.tab_given_cmt);
        setupTab(new TextView(getActivity()), title_tap1, R.id.tab1);
        setupTab(new TextView(getActivity()), title_tap2, R.id.tab2);
        setupTab(new TextView(getActivity()), title_tap3, R.id.tab3);
        lv_product_evaluation_tab_1 = (ListView) fragment_view.findViewById(R.id.id_evaluation_1);
        lv_product_evaluation_tab_2 = (ListView) fragment_view.findViewById(R.id.id_evaluation_2);
        lv_product_evaluation_tab_3 = (ListView) fragment_view.findViewById(R.id.id_evaluation_3);
        arrayOfMyReview = new ArrayList<>();
        arrayOfReviewToMe = new ArrayList<>();
        arrayOfReviewAwaitting = new ArrayList<>();
        GetDataEvaluation();
        lv_product_evaluation_tab_1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int i2) {
                int topRowVerticalPosition = (lv_product_evaluation_tab_1 == null || lv_product_evaluation_tab_1.getChildCount() == 0) ?
                        0 : lv_product_evaluation_tab_1.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
        lv_product_evaluation_tab_2.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int i2) {
                int topRowVerticalPosition = (lv_product_evaluation_tab_2 == null || lv_product_evaluation_tab_2.getChildCount() == 0) ?
                        0 : lv_product_evaluation_tab_2.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
        lv_product_evaluation_tab_3.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int i2) {
                int topRowVerticalPosition = (lv_product_evaluation_tab_3 == null || lv_product_evaluation_tab_3.getChildCount() == 0) ?
                        0 : lv_product_evaluation_tab_3.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(AllEvaFragment.this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                GetDataEvaluation();
            }
        });
        return fragment_view;
    }

    private void setupTab(final View view, final String tag, int resCt) {
        View tabview = createTabView(my_tabhost.getContext(), tag);
        TabHost.TabSpec setContent = my_tabhost.newTabSpec(tag).setIndicator(tabview);
        setContent.setContent(resCt);
        my_tabhost.addTab(setContent);
    }

    public void onRefeshTap() {
        my_tabhost.setCurrentTab(0);
        GetDataEvaluation();
    }

    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabs_host_top_bg, null);
        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        tv.setText(text);
        return view;
    }

    /**
     * Get data reviews from API
     *
     * @return
     * @params
     */
    private void GetDataEvaluation() {
        swipeRefreshLayout.setRefreshing(true);
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        Call<ResponseMessage<RsReview>> callReview = ws.actionGetReview();
        callReview.enqueue(new Callback<ResponseMessage<RsReview>>() {
            @Override
            public void onResponse(Call<ResponseMessage<RsReview>> call, Response<ResponseMessage<RsReview>> response) {
                try {
                    if (response.body() != null) {
                        if (response.body().isSuccess()) {
                            rsReview = response.body().getData();
                            arrayOfReviewAwaitting = rsReview.getReviews_awaiting();
                            arrayOfReviewToMe = rsReview.getReviews_as_buyer();
                            arrayOfMyReview = rsReview.getReviews_as_seller();
                            product_adapter_tab1 = new ProductsEvaTab1Adapter(getActivity(), arrayOfReviewAwaitting, false, AllEvaFragment.this);
                            product_adapter_tab2 = new ProductsEvaTab2Adapter(getActivity(), arrayOfReviewToMe, false);
                            product_adapter_tab3 = new ProductsEvaTab3Adapter(getActivity(), arrayOfMyReview, false);
                            if (arrayOfReviewAwaitting != null) {
                                lv_product_evaluation_tab_1.setAdapter(product_adapter_tab1);
                                lv_product_evaluation_tab_1.setEmptyView(fragment_view.findViewById(R.id.emptyElement1));
                            }
                            if (arrayOfReviewToMe != null) {
                                lv_product_evaluation_tab_2.setAdapter(product_adapter_tab2);
                                lv_product_evaluation_tab_2.setEmptyView(fragment_view.findViewById(R.id.emptyElement2));
                            }
                            if (arrayOfMyReview != null) {
                                lv_product_evaluation_tab_3.setAdapter(product_adapter_tab3);
                                lv_product_evaluation_tab_3.setEmptyView(fragment_view.findViewById(R.id.emptyElement3));
                            }

                        }
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
            public void onFailure(Call<ResponseMessage<RsReview>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Utils.OnFailException(t);
            }
        });
    }

    @Override
    public void onRefresh() {
        GetDataEvaluation();
    }
}
