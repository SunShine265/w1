package com.wo1haitao.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.wo1haitao.R;
import com.wo1haitao.adapters.ProductsMyBidstAdapter;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.dialogs.DialogInvoice;
import com.wo1haitao.models.ProductTenders;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBidFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    ActionBarProject my_action_bar;
    ArrayList<ProductTenders> arrayOfOffer;
    ProductsMyBidstAdapter product_adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView lv_product;
    boolean isEndPage = false;
    View fragment_view;
    boolean isNotification = false;
    int idOrderNotification = 0;
    boolean changeInvoice = false;
    boolean is_in_progress = false;
    public int getIdOrderNotification() {
        return idOrderNotification;
    }

    public void setIdOrderNotification(int idOrderNotification) {
        this.idOrderNotification = idOrderNotification;
    }

    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean notification) {
        isNotification = notification;
    }

    ActionListener getListionner() {
        if (this.getActivity() instanceof ActionListener) {
            return (ActionListener) this.getActivity();
        }
        return null;
    }

    String flag = "MyBidFragment";

    public MyBidFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        arrayOfOffer = new ArrayList<>();
        fragment_view = inflater.inflate(R.layout.fragment_my_bid, container, false);
        InputMethodManager imm = (InputMethodManager) MyBidFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(fragment_view.getWindowToken(), 0);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) fragment_view.findViewById(R.id.swipe_refresh_layout);
        my_action_bar.showTitle(R.string.title_my_bid);
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        lv_product = (ListView) fragment_view.findViewById(R.id.lv_product);
        lv_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (getListionner() != null && arrayOfOffer.get(position).getProduct_listing() != null) {
                    isEndPage = false;
                    getListionner().changeDetailProduct(arrayOfOffer.get(position).getProduct_listing().getId(), flag);
                }
            }
        });
        InitViewMyBid();
        return fragment_view;
    }

    public void InitViewMyBid() {
        final int[] threshold = {1};
        GetMyBid(1);
        lv_product.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                int count = lv_product.getCount();
                if (scrollState == SCROLL_STATE_IDLE) {
//                    if (lv_product.getLastVisiblePosition() >= count - 1) {
//                        // Execute LoadMoreDataTask
//                        if (isEndPage == false) {
//                            threshold[0]++;
//                            GetMyBid(threshold[0]);
//                        }
//                    }
                    View view = (View) lv_product.getChildAt(lv_product.getChildCount()-1);

                    // Calculate the scrolldiff
                    int diff = (view.getBottom()-(lv_product.getHeight()+lv_product.getScrollY()));
                    if(diff == 0){
                        if (isEndPage == false) {
                            threshold[0]++;
                            GetMyBid(threshold[0]);
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
                    isEndPage = false;
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(MyBidFragment.this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                GetMyBid(1);
            }
        });

    }

    public void GetMyBid(final int page) {
        swipeRefreshLayout.setRefreshing(true);
        is_in_progress = true;
        lv_product.setEnabled(false);
        final ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<ArrayList<ProductTenders>>> call = ws.actionGetOfferMe(page);
        call.enqueue(new Callback<ResponseMessage<ArrayList<ProductTenders>>>() {
            @Override
            public void onResponse(Call<ResponseMessage<ArrayList<ProductTenders>>> call, Response<ResponseMessage<ArrayList<ProductTenders>>> response) {
                try {
                    if (MyBidFragment.this.getActivity() != null) {
                        if (response.body() != null) {
                            ResponseMessage responseMessage = response.body();
                            if (responseMessage.isSuccess()) {
                                ArrayList<ProductTenders> tempReponse = new ArrayList();
                                if (page == 1) {
                                    isEndPage = false;
                                    arrayOfOffer = response.body().getData();
                                    for (int i = 0; i < arrayOfOffer.size(); i++) {
                                        if (arrayOfOffer.get(i).getProduct_listing() == null) {
                                            arrayOfOffer.remove(i);
                                        }
                                    }
                                    if (arrayOfOffer == null) {
                                        arrayOfOffer = new ArrayList<ProductTenders>();
                                    }
                                    product_adapter = new ProductsMyBidstAdapter(MyBidFragment.this.getActivity(), arrayOfOffer, MyBidFragment.this);
                                    lv_product.setAdapter(product_adapter);
                                    lv_product.setEmptyView(fragment_view.findViewById(R.id.emptyElement));

                                } else {
                                    tempReponse = response.body().getData();
                                    if (tempReponse.size() == 0) {
                                        isEndPage = true;
                                    } else {
                                        for (int i = 0; i < tempReponse.size(); i++) {
                                            arrayOfOffer.add(tempReponse.get(i));
                                        }
                                    }
                                    product_adapter.notifyDataSetChanged();
                                }
                                if (MyBidFragment.this.isNotification == true && changeInvoice == false) {
                                    changeInvoice = true;
                                    DialogInvoice dialogInvoice = new DialogInvoice(MyBidFragment.this.getActivity(), MyBidFragment.this.getIdOrderNotification(), MyBidFragment.this);


                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    Window window = dialogInvoice.getWindow();
                                    lp.copyFrom(window.getAttributes());
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                    dialogInvoice.show();
                                    dialogInvoice.getWindow().setAttributes(lp);
                                }

                            }
                        } else if (response.errorBody() != null) {
                            try {
                                ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                                ErrorMessage error = responseMessage.getErrors();
                                Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Crashlytics.logException(e);
                                Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.no_advesting, Toast.LENGTH_SHORT).show();
                        }

                    }
                    lv_product.setEnabled(true);
                    is_in_progress = false;
                    swipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<ArrayList<ProductTenders>>> call, Throwable t) {
                lv_product.setEnabled(true);
                is_in_progress = false;
                swipeRefreshLayout.setRefreshing(false);
                Utils.OnFailException(t);
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        if (getListionner() != null) {
            getListionner().changeProfile();
            return true;
        } else {
            return super.onBackPressed();
        }
    }

    @Override
    public void onRefresh() {
        if(is_in_progress == false){
            GetMyBid(1);
        }


    }

    public interface ActionListener {
        void changeProfile();

        void changeDetailProduct(long product_id, String flag);
    }

}
