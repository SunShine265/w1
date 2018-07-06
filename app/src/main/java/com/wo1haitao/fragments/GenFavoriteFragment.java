package com.wo1haitao.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.adapters.ProductsGenFavAdapter;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.WishlistItemRs;
import com.wo1haitao.models.ProductModel;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GenFavoriteFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    ActionBarProject my_action_bar;
    ArrayList<WishlistItemRs> arrayOfProductModels;
    SwipeRefreshLayout swipeRefreshLayout;
    ProductsGenFavAdapter product_adapter;
    ListView lv_product;
    View fragment_view;

    GenFavoriteFragment.ChangeFragment getListener() {
        if (this.getActivity() instanceof GenFavoriteFragment.ChangeFragment) {
            return (GenFavoriteFragment.ChangeFragment) this.getActivity();
        }
        return null;
    }

    String flag = "Favourite";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment_view = inflater.inflate(R.layout.fragment_gen_favorite, container, false);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) fragment_view.findViewById(R.id.swipe_refresh_layout);
        my_action_bar.showTitle(R.string.title_my_gen_favorite); //
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getListener() != null) {
                    getListener().changeProfile();
                }
            }
        });
        lv_product = (ListView) fragment_view.findViewById(R.id.lv_product);
        lv_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (getListener() != null) {
                    if(arrayOfProductModels.get(position) != null && arrayOfProductModels.get(position).getProduct_listing() != null){
                        getListener().changeDetailProduct(arrayOfProductModels.get(position).getProduct_listing().getId(), flag);
                    }

                }
            }
        });
        lv_product.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int i2) {
                int topRowVerticalPosition = (lv_product == null || lv_product.getChildCount() == 0) ?
                        0 : lv_product.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
        GetDataFavorite();
        swipeRefreshLayout.setOnRefreshListener(GenFavoriteFragment.this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                GetDataFavorite();
            }
        });
        return fragment_view;
    }

    @Override
    public void onRefresh() {
        GetDataFavorite();
    }

    public interface ChangeFragment {
        void changeProfile();

        void changeDetailProduct(long product_id, String flag);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Get data favorite from API
     *
     * @params:
     * @return:
     */
    public void GetDataFavorite() {
        swipeRefreshLayout.setRefreshing(true);
        final ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<ArrayList<WishlistItemRs>>> call = ws.actionGetFavourite();
        call.enqueue(new Callback<ResponseMessage<ArrayList<WishlistItemRs>>>() {
            @Override
            public void onResponse(Call<ResponseMessage<ArrayList<WishlistItemRs>>> call, Response<ResponseMessage<ArrayList<WishlistItemRs>>> response) {
                try {
                    if (response.body() != null) {
                        ResponseMessage responseMessage = response.body();
                        if (responseMessage.isSuccess()) {
                            arrayOfProductModels = response.body().getData();
                            int i = 0;
                            while(i < arrayOfProductModels.size()){
                                if(arrayOfProductModels.get(i).getProduct_listing() == null || arrayOfProductModels.get(i).getProduct_listing().getId() < 1){
                                    arrayOfProductModels.remove(i);
                                    i--;
                                }
                                i++;
                            }
                            product_adapter = new ProductsGenFavAdapter(getActivity(), arrayOfProductModels, false, R.layout.item_gen_favorite);
                            lv_product.setAdapter(product_adapter);
                            lv_product.setEmptyView(fragment_view.findViewById(R.id.emptyElement));
                        }
                    } else if (response.errorBody() != null) {
                        String errorMessage = "";
                        try {
                            errorMessage = response.errorBody().string();
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(errorMessage, ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    Utils.crashLog(e);
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<ArrayList<WishlistItemRs>>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Utils.OnFailException(t);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
