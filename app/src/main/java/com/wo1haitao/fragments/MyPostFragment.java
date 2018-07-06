package com.wo1haitao.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.adapters.ProductsMyPostAdapter;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsProduct;
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
public class MyPostFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    ActionBarProject my_action_bar;
    ArrayList<RsProduct> arrayOfProducts;
    ProductsMyPostAdapter product_adapter;
    ListView lv_product;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean isEndPage = false;
    View fragment_view;

    ChangeFragment getListenner() {
        if (this.getActivity() instanceof ChangeFragment) {
            return (ChangeFragment) this.getActivity();
        }
        return null;
    }
    boolean is_in_progress = false;
    String flag = "MyPost";
    String changeFragmentTo = "";

    public String getChangeFragmentTo() {
        return changeFragmentTo;
    }

    public void setChangeFragmentTo(String changeFragmentTo) {
        this.changeFragmentTo = changeFragmentTo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment_view = inflater.inflate(R.layout.fragment_my_post, container, false);
        InputMethodManager imm = (InputMethodManager) MyPostFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(fragment_view.getWindowToken(), 0);
        swipeRefreshLayout = (SwipeRefreshLayout) fragment_view.findViewById(R.id.swipe_refresh_layout);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        my_action_bar.showTitle(R.string.title_my_post);
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyPostFragment.this.getChangeFragmentTo().equals("RequestPurchase") || MyPostFragment.this.getChangeFragmentTo().equals("payment")) {
                    getListenner().changeProfile();
                } else {
                    backPress();
                }
            }
        });
        arrayOfProducts = new ArrayList<>();
        lv_product = (ListView) fragment_view.findViewById(R.id.lv_product);
        lv_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                isEndPage = false;
                getListenner().changeDetailProduct(arrayOfProducts.get(position).getId(), flag);
            }
        });
        final int[] threshold = {1};
        GetDataMyPost(1);
        lv_product.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                int count = lv_product.getCount();
                if (scrollState == SCROLL_STATE_IDLE) {
//                    if (lv_product.getLastVisiblePosition() >= count - 1) {
//                        // Execute LoadMoreDataTask
//                        if (isEndPage == false) {
//                            threshold[0]++;
//                            GetDataMyPost(threshold[0]);
//                        }
//                    }
                    View view = (View) lv_product.getChildAt(lv_product.getChildCount()-1);

                    // Calculate the scrolldiff
                    int diff = (view.getBottom()-(lv_product.getHeight()+lv_product.getScrollY()));
                    if(diff == 0){
                        if (isEndPage == false) {
                            threshold[0]++;
                            GetDataMyPost(threshold[0]);
                        }
                    }
                }
            }


            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItem) {
                int topRowVerticalPosition = (lv_product == null || lv_product.getChildCount() == 0) ?
                        0 : lv_product.getChildAt(0).getTop();
                if (firstVisibleItem == 0 && topRowVerticalPosition >= 0) {
                    swipeRefreshLayout.setEnabled(true);
                    threshold[0] = 1;
                    if (totalItem == 10) {
                        isEndPage = false;
                    }
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(MyPostFragment.this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                GetDataMyPost(1);
            }
        });
        return fragment_view;
    }

    @Override
    public boolean onBackPressed() {
        if (getListenner() != null && ("RequestPurchase".equals(MyPostFragment.this.getChangeFragmentTo()) || "Payment".equals(MyPostFragment.this.getChangeFragmentTo()))) {
            getListenner().changeProfile();
            return true;
        } else {
            return super.onBackPressed();
        }
    }

    @Override
    public void onRefresh() {
        if(is_in_progress == false) {
            GetDataMyPost(1);
        }
    }

    public interface ChangeFragment {
        void changeDetailProduct(long product_id, String flag);

        void changeProfile();
    }

    public void GetDataMyPost(final int page) {
        is_in_progress = true;
        lv_product.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<ArrayList<RsProduct>>> call = ws.actionGetMyProducts(page);
        call.enqueue(new Callback<ResponseMessage<ArrayList<RsProduct>>>() {
            @Override
            public void onResponse(Call<ResponseMessage<ArrayList<RsProduct>>> call, Response<ResponseMessage<ArrayList<RsProduct>>> response) {
                try {
                    if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.body() != null && response.body().isSuccess()) {
                        ArrayList<RsProduct> tempReponse = new ArrayList();
                        if (page == 1) {
                            arrayOfProducts = response.body().getData();
                            product_adapter = new ProductsMyPostAdapter(getActivity(), R.layout.item_my_post, arrayOfProducts, MyPostFragment.this);
                            lv_product.setAdapter(product_adapter);
                            lv_product.setEmptyView(fragment_view.findViewById(R.id.emptyElement));
                            isEndPage = false;
                        } else {
                            tempReponse = response.body().getData();
                            if (tempReponse.size() == 0) {
                                isEndPage = true;
                            } else {
                                for (int i = 0; i < tempReponse.size(); i++) {
                                    arrayOfProducts.add(tempReponse.get(i));
                                }
                            }
                            product_adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                    is_in_progress = false;
                    lv_product.setEnabled(true);
                    swipeRefreshLayout.setRefreshing(false);

                } catch (Exception e) {
                    Utils.crashLog(e);
                }

            }

            @Override
            public void onFailure(Call<ResponseMessage<ArrayList<RsProduct>>> call, Throwable t) {
                lv_product.setEnabled(true);
                is_in_progress = false;
                swipeRefreshLayout.setRefreshing(false);
                Utils.OnFailException(t);
            }
        });
    }

}
