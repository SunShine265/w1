package com.wo1haitao.fragments;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.adapters.ProductListAdapter;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.dialogs.DialogDropdown;
import com.wo1haitao.models.CategoryModel;
import com.wo1haitao.models.ItemPicker;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class ProductListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    ListView listViewProduct;
    Boolean sortView = true;
    ProductListAdapter adapter;
    ProductListAdapter adapter_grid;
    ArrayList<RsProduct> arrayOfProduct, tempReponse;

    int state_listgird = 1;
    ImageView iv_list_to_gird;
    GridView girdViewProduct;
    boolean isEndPage = false;
    SwipeRefreshLayout swipeRefreshLayoutGrid, swipeRefreshLayoutList;

    ChangeFragment getListenner() {
        if (this.getActivity() instanceof ChangeFragment) {
            return (ChangeFragment) this.getActivity();
        }
        return null;
    }

    ;
    ActionBarProject my_action_bar;
    Integer categry_id = null, country_id = null;
    Spinner spinnerFilter;
    TextView txt_choose_category, txt_choose_country;
    ArrayList<String> myListCategory, myListCountry, list_filter;
    LinearLayout ll_filter, dialog_filter;
    String flag = "ProductList";
    String param_sort = "";
    String param_country = "";
    String param_category = "";
    Map<String, String> map_param_sort = new HashMap<>();
    CheckBox check_new_product, check_used_product;
    String purchaseSearchCountry = "";
    View view;
    EditText edt_choose_country, edt_choose_category;
    ArrayList<ItemPicker> list_item_country, list_item_category;

    public String getPurchaseSearchCountry() {
        return purchaseSearchCountry;
    }

    public void setPurchaseSearchCountry(String purchaseSearchCountry) {
        this.purchaseSearchCountry = purchaseSearchCountry;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_list, container, false);
        map_param_sort.put("最多评价", "reviews");
        map_param_sort.put("最新广告", "recent");
        myListCategory = new ArrayList<>();
        myListCountry = new ArrayList<>();
        arrayOfProduct = new ArrayList<>();
        tempReponse = new ArrayList<>();

        my_action_bar = (ActionBarProject) view.findViewById(R.id.my_action_bar);
        txt_choose_category = (TextView) view.findViewById(R.id.txt_choose_category);
        txt_choose_country = (TextView) view.findViewById(R.id.txt_choose_country);
        ll_filter = (LinearLayout) view.findViewById(R.id.view_filter);
        dialog_filter = (LinearLayout) view.findViewById(R.id.dialog_filter);
        spinnerFilter = (Spinner) view.findViewById(R.id.id_sp_filter);
        check_new_product = (CheckBox) view.findViewById(R.id.check_new_product);
        check_used_product = (CheckBox) view.findViewById(R.id.check_used_product);
        ImageView bt_new_product = (ImageView) view.findViewById(R.id.ivCammera);
        listViewProduct = (ListView) view.findViewById(R.id.list);
        girdViewProduct = (GridView) view.findViewById(R.id.gridview);
        LinearLayout btn_sort_view = (LinearLayout) view.findViewById(R.id.sortView);
        swipeRefreshLayoutGrid = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_grid);
        swipeRefreshLayoutList = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_list);
        edt_choose_country = (EditText) view.findViewById(R.id.edt_choose_country);
        edt_choose_category = (EditText) view.findViewById(R.id.edt_choose_category);

        HashMap<String, String> mapCountry = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_COUNTRY);
        list_item_country = new ArrayList<>();
        list_item_country.add(new ItemPicker("0","全部国家", false));
        for (Map.Entry entry : mapCountry.entrySet()){
            ItemPicker item_choose = new ItemPicker(entry.getKey().toString(), entry.getValue().toString(), false);
            list_item_country.add(item_choose);
        }

        HashMap<String, String> mapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
        list_item_category = new ArrayList<>();
        list_item_category.add(new ItemPicker("0","全部分类", false));
        for (Map.Entry entry : mapCategory.entrySet()){
            ItemPicker item_choose = new ItemPicker(entry.getKey().toString(), entry.getValue().toString(), false);
            list_item_category.add(item_choose);
        }

        edt_choose_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDropDownCountry();
            }
        });

        edt_choose_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDropDownCategory();
            }
        });

        my_action_bar.ChangeButtonImageSearch();

        my_action_bar.showSearch(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        }, null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category, country, namesearch;
                category = "";
                country = "";
                namesearch = "";
                check_used_product.setChecked(true);
                check_new_product.setChecked(true);

                if (!my_action_bar.getEdt_search().getText().toString().isEmpty()) {
                    namesearch = my_action_bar.getEdt_search().getText().toString();
                }
                if (txt_choose_category.getText() != null) {
                    if (txt_choose_category.getText().equals("全部分类")) {
                        category = "";
                    } else {
                        category = txt_choose_category.getText().toString();
                    }
                }

                if (txt_choose_country.getText() != null) {
                    if (txt_choose_country.getText().equals("全部国家")) {
                        country = "";
                    } else {
                        country = txt_choose_country.getText().toString();
                    }
                }

                getDataSearch(0, category, country, namesearch);
            }
        });
        Utils.setActionBack(view, getActivity());
        bt_new_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListenner().changeNewProduct();
            }
        });
        final HashMap<String, String> hashmapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
        myListCategory.add(0, "全部分类");
        txt_choose_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogCategory = new AlertDialog.Builder(getActivity());
                ListView lvCategory = new ListView(getActivity());
                if (hashmapCategory != null) {
                    if (hashmapCategory != null) {
                        for (Map.Entry entry : hashmapCategory.entrySet()) {
                            myListCategory.add(entry.getValue().toString());
                        }
                    }
                }
                lvCategory.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, myListCategory));
                dialogCategory.setView(lvCategory);
                final AlertDialog alertDialog = dialogCategory.create();
                alertDialog.show();
                lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        categry_id = i + 1;
                        txt_choose_category.setText(myListCategory.get(i).toString());
                        if (i > 0) {
                            param_category = txt_choose_category.getText().toString();
                        } else {
                            param_category = "";
                        }
                        alertDialog.dismiss();
                        getDataSearch(1, param_category, param_country, my_action_bar.getEdt_search().getText().toString());
                    }
                });
            }
        });

        ll_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog_filter.getVisibility() == View.GONE) {
                    dialog_filter.setVisibility(View.VISIBLE);
                } else {
                    dialog_filter.setVisibility(View.GONE);
                }
            }
        });
        //Filter
        list_filter = new ArrayList<String>(map_param_sort.keySet());
        final ArrayAdapter<String> adapter_Filter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list_filter) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                if (position == 0) {
                    tv.setTextColor(ContextCompat.getColor(CustomApp.getInstance(), R.color.grey_text));
                }
                return (View) tv;
            }
        };

        param_sort = map_param_sort.get(list_filter.get(0));
        adapter_Filter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter_Filter);
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    param_sort = map_param_sort.get(list_filter.get(i));
                    param_sort = URLEncoder.encode(param_sort, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                getDataSearch(1, param_category, param_country, my_action_bar.getEdt_search().getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        check_new_product.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (check_used_product.isChecked() == false && b == false) {
                    compoundButton.setChecked(true);
                }
                getDataSearch(1, param_category, param_country, my_action_bar.getEdt_search().getText().toString());
            }
        });

        check_used_product.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (check_new_product.isChecked() == false && b == false) {
                    compoundButton.setChecked(true);

                }
                getDataSearch(1, param_category, param_country, my_action_bar.getEdt_search().getText().toString());
            }
        });
        final HashMap<String, String> hashmapCountry = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_COUNTRY);
        myListCountry.add(0, "全部国家");
        txt_choose_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogCategory = new AlertDialog.Builder(getActivity());
                ListView lvCountry = new ListView(getActivity());
                if (hashmapCountry != null) {
                    for (Map.Entry entry : hashmapCountry.entrySet()) {
                        myListCountry.add(entry.getValue().toString());
                    }
                }
                lvCountry.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, myListCountry));
                dialogCategory.setView(lvCountry);
                final AlertDialog alertDialog = dialogCategory.create();
                alertDialog.show();
                lvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        country_id = i + 1;
                        txt_choose_country.setText(myListCountry.get(i).toString());
                        ProductListFragment.this.setPurchaseSearchCountry(myListCountry.get(i).toString());
                        if (i > 0) {
                            param_country = txt_choose_country.getText().toString();
                        } else {
                            param_country = "";
                        }
                        alertDialog.dismiss();
                        getDataSearch(1, param_category, param_country, my_action_bar.getEdt_search().getText().toString());
                    }
                });
            }
        });

        listViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                getListenner().changeDetailProduct(arrayOfProduct.get(position).getId(), flag);
            }
        });

        girdViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                getListenner().changeDetailProduct(arrayOfProduct.get(position).getId(), flag);

            }
        });
        final int[] threshold = {1};
        iv_list_to_gird = (ImageView) view.findViewById(R.id.iv_list_to_gird);
        iv_list_to_gird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threshold[0] = 1;
                isEndPage = false;
                if (state_listgird == 0 && adapter_grid != null) {
                    state_listgird = 1;
                    iv_list_to_gird.setImageResource(R.drawable.ic_show_list_pd);
                    listViewProduct.setVisibility(View.GONE);
                    swipeRefreshLayoutList.setVisibility(View.GONE);
                    swipeRefreshLayoutGrid.setVisibility(View.VISIBLE);
                    girdViewProduct.setVisibility(View.VISIBLE);
                    adapter_grid.notifyDataSetChanged();
                    getDataSearch(1, param_category, param_country, my_action_bar.getEdt_search().getText().toString());
                }
                else if(state_listgird == 1 && adapter != null) {
                    state_listgird = 0;
                    iv_list_to_gird.setImageResource(R.drawable.ic_show_grid_pd);
                    listViewProduct.setVisibility(View.VISIBLE);
                    girdViewProduct.setVisibility(View.GONE);
                    swipeRefreshLayoutList.setVisibility(View.VISIBLE);
                    swipeRefreshLayoutGrid.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    getDataSearch(1, param_category, param_country, my_action_bar.getEdt_search().getText().toString());
                }
            }
        });
        btn_sort_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortView = !sortView;
            }
        });
        if(!ProductListFragment.this.purchaseSearchCountry.equals("")){
            edt_choose_country.setText(this.getPurchaseSearchCountry().toString());
            param_country = edt_choose_country.getText().toString();
            getDataSearch(1, param_category, param_country, my_action_bar.getEdt_search().getText().toString());
        }
        else {
            getData(1);
        }
        listViewProduct.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                int count = listViewProduct.getCount();
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listViewProduct.getLastVisiblePosition() >= count - 1) {
                        // Execute LoadMoreDataTask
                        if(isEndPage == false) {
                            threshold[0]++;
                            getData(threshold[0]);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItem) {
                int topRowVerticalPosition = (listViewProduct == null || listViewProduct.getChildCount() == 0) ?
                        0 : listViewProduct.getChildAt(0).getTop();
                if(firstVisibleItem == 0 && topRowVerticalPosition >= 0) {
                    swipeRefreshLayoutList.setEnabled(true);
                    threshold[0] = 1;
                    if(totalItem == 10) {
                        isEndPage = false;
                    }
                }
                else {
                    swipeRefreshLayoutList.setEnabled(false);
                }
            }
        });
        girdViewProduct.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                int count = girdViewProduct.getCount();
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (girdViewProduct.getLastVisiblePosition() >= count - 1) {
                        // Execute LoadMoreDataTask
                        if(isEndPage == false) {
                            threshold[0]++;
                            getData(threshold[0]);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItem) {
                int topRowVerticalPosition = (girdViewProduct == null || girdViewProduct.getChildCount() == 0) ?
                        0 : girdViewProduct.getChildAt(0).getTop();
                if(firstVisibleItem == 0 && topRowVerticalPosition >= 0) {
                    swipeRefreshLayoutGrid.setEnabled(true);
                    threshold[0] = 1;
                    if(totalItem == 10) {
                        isEndPage = false;
                    }
                }
                else {
                    swipeRefreshLayoutGrid.setEnabled(false);
                }
            }
        });

        swipeRefreshLayoutGrid.setOnRefreshListener(ProductListFragment.this);
        swipeRefreshLayoutGrid.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayoutGrid.setRefreshing(true);
                getDataSearch(1, param_category, param_country, my_action_bar.getEdt_search().getText().toString());
            }
        });

        swipeRefreshLayoutList.setOnRefreshListener(ProductListFragment.this);
        swipeRefreshLayoutList.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayoutList.setRefreshing(true);
                getDataSearch(1, param_category, param_country, my_action_bar.getEdt_search().getText().toString());
            }
        });
        return view;
    }

    public ArrayList<CategoryModel> CategoryList() {
        ArrayList<CategoryModel> myCategory = new ArrayList<>();
        myCategory.add(new CategoryModel("电器", R.drawable.electronic));
        myCategory.add(new CategoryModel("家具", R.drawable.furniture));
        myCategory.add(new CategoryModel("服装", R.drawable.fashion));
        myCategory.add(new CategoryModel("首饰", R.drawable.jewellery));
        myCategory.add(new CategoryModel("健康食品", R.drawable.helthy_food_product));
        myCategory.add(new CategoryModel("手表", R.drawable.watch));
        myCategory.add(new CategoryModel("收藏品", R.drawable.others));
        myCategory.add(new CategoryModel("其它物品", R.drawable.collectable));
        myCategory.add(new CategoryModel("包包", R.drawable.bags));
        myCategory.add(new CategoryModel("运动用品", R.drawable.worout_equipment));
        return myCategory;
    }

    public interface ChangeFragment {
        void changeNewProduct();

        void changeDetailProduct(long product_id, String flag);
    }


    private void getDataSearch(int page, String category, String country, String namesearch) {
        if(swipeRefreshLayoutGrid.getVisibility() == View.VISIBLE) {
            swipeRefreshLayoutGrid.setRefreshing(true);
        }
        if(swipeRefreshLayoutList.getVisibility() == View.VISIBLE) {
            swipeRefreshLayoutList.setRefreshing(true);
        }
        final ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<ArrayList<RsProduct>>> call = ws.actionGetSearchProduct(page, category, country, namesearch,
                param_sort, check_new_product.isChecked(), check_used_product.isChecked());
        call.enqueue(new Callback<ResponseMessage<ArrayList<RsProduct>>>() {
            @Override
            public void onResponse(Call<ResponseMessage<ArrayList<RsProduct>>> call, Response<ResponseMessage<ArrayList<RsProduct>>> response) {
                try {
                    if (response.body() != null) {
                        ResponseMessage responseMessage = response.body();
                        if (responseMessage.isSuccess()) {
                            arrayOfProduct = response.body().getData();
                            ArrayList<RsProduct> list_product = new ArrayList<RsProduct>();
                            for (int i = 0; i < arrayOfProduct.size(); i++) {
                                if (arrayOfProduct.get(i).isClosed() == false) {
                                    list_product.add(arrayOfProduct.get(i));
                                }
                            }
                            arrayOfProduct = list_product;
                            adapter = new ProductListAdapter(ProductListFragment.this.getActivity(), arrayOfProduct, true, R.layout.listitemview_product, true);
                            adapter_grid = new ProductListAdapter(ProductListFragment.this.getActivity(), arrayOfProduct, true, R.layout.griditemview_product, false);
                            listViewProduct.setAdapter(adapter);
                            listViewProduct.setEmptyView(view.findViewById(R.id.emptyElementList));
                            girdViewProduct.setAdapter(adapter_grid);
                            listViewProduct.setEmptyView(view.findViewById(R.id.emptyElementGrid));

                            switch (state_listgird) {
                                case 0: {
                                    iv_list_to_gird.setImageResource(R.drawable.ic_show_grid_pd);
                                    listViewProduct.setVisibility(View.VISIBLE);
                                    girdViewProduct.setVisibility(View.GONE);
                                    swipeRefreshLayoutGrid.setVisibility(View.GONE);
                                }
                                break;
                                default: {
                                    iv_list_to_gird.setImageResource(R.drawable.ic_show_list_pd);
                                    listViewProduct.setVisibility(View.GONE);
                                    girdViewProduct.setVisibility(View.VISIBLE);
                                    swipeRefreshLayoutGrid.setVisibility(View.VISIBLE);
                                }
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
                    swipeRefreshLayoutGrid.setRefreshing(false);
                    swipeRefreshLayoutList.setRefreshing(false);
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<ArrayList<RsProduct>>> call, Throwable t) {
                swipeRefreshLayoutGrid.setRefreshing(false);
                swipeRefreshLayoutList.setRefreshing(false);
                Utils.OnFailException(t);
            }
        });
    }

    private void getData(final int page) {
        if(swipeRefreshLayoutGrid.getVisibility() == View.VISIBLE) {
            swipeRefreshLayoutGrid.setRefreshing(true);
        }
        if(swipeRefreshLayoutList.getVisibility() == View.VISIBLE) {
            swipeRefreshLayoutList.setRefreshing(true);
        }
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<ArrayList<RsProduct>>> call = ws.actionGetAllProduct(page);
        call.enqueue(new Callback<ResponseMessage<ArrayList<RsProduct>>>() {
            @Override
            public void onResponse(Call<ResponseMessage<ArrayList<RsProduct>>> call, Response<ResponseMessage<ArrayList<RsProduct>>> response) {
                try {
                    if (response.errorBody() != null) {
                        try {
                            if (response.isSuccessful()) {
                                ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                                ErrorMessage error = responseMessage.getErrors();
                                Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body() != null && response.body().isSuccess()) {
                        tempReponse = new ArrayList<RsProduct>();
                        if(page == 1) {
                            arrayOfProduct = response.body().getData();
                            ArrayList<RsProduct> list_product = new ArrayList<RsProduct>();
                            for (int i = 0; i < arrayOfProduct.size(); i++) {
                                if (arrayOfProduct.get(i).isClosed() == false) {
                                    list_product.add(arrayOfProduct.get(i));
                                }

                            }
                            arrayOfProduct = list_product;
                            adapter = new ProductListAdapter(ProductListFragment.this.getActivity(), arrayOfProduct, true, R.layout.listitemview_product, true);
                            adapter_grid = new ProductListAdapter(ProductListFragment.this.getActivity(), arrayOfProduct, true, R.layout.griditemview_product, false);
                            listViewProduct.setAdapter(adapter);
                            girdViewProduct.setAdapter(adapter_grid);
                            switch (state_listgird) {
                                case 0: {
                                    iv_list_to_gird.setImageResource(R.drawable.ic_show_grid_pd);
                                    listViewProduct.setVisibility(View.VISIBLE);
                                    girdViewProduct.setVisibility(View.GONE);
                                    swipeRefreshLayoutList.setVisibility(View.VISIBLE);
                                    swipeRefreshLayoutGrid.setVisibility(View.GONE);
                                }
                                break;
                                default: {
                                    iv_list_to_gird.setImageResource(R.drawable.ic_show_list_pd);
                                    listViewProduct.setVisibility(View.GONE);
                                    girdViewProduct.setVisibility(View.VISIBLE);
                                    swipeRefreshLayoutList.setVisibility(View.GONE);
                                    swipeRefreshLayoutGrid.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        else {
                            tempReponse = response.body().getData();
                            if(tempReponse.size() == 0){
                                isEndPage = true;
                            }
                            else {
                                for (int i = 0; i < tempReponse.size(); i++) {
                                    arrayOfProduct.add(tempReponse.get(i));
                                }
                            }
                            adapter.notifyDataSetChanged();
                            adapter_grid.notifyDataSetChanged();
                        }

                    } else {
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayoutGrid.setRefreshing(false);
                    swipeRefreshLayoutList.setRefreshing(false);
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<ArrayList<RsProduct>>> call, Throwable t) {
                swipeRefreshLayoutGrid.setRefreshing(false);
                swipeRefreshLayoutList.setRefreshing(false);
                Utils.OnFailException(t);
            }
        });
    }

    @Override
    public void onRefresh() {
        getDataSearch(1, param_category, param_country, my_action_bar.getEdt_search().getText().toString());
    }

    private void showDialogDropDownCountry(){

        DialogDropdown dropdown = new DialogDropdown(this.getActivity(), false, list_item_country, edt_choose_country);

        dropdown.setCanceledOnTouchOutside(true);
        dropdown.show();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dropdown.getWindow().getAttributes());

        layoutParams.width = size.x*4/5;
        layoutParams.height = size.y*4/5;
        dropdown.getWindow().setAttributes(layoutParams);
        dropdown.setOnDismissListener(listener);
    }

    private void showDialogDropDownCategory(){

        DialogDropdown dropdown = new DialogDropdown(this.getActivity(), false, list_item_category, edt_choose_category);

        dropdown.setCanceledOnTouchOutside(true);
        dropdown.show();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dropdown.getWindow().getAttributes());

        layoutParams.width = size.x*4/5;
        layoutParams.height = size.y*4/5;
        dropdown.getWindow().setAttributes(layoutParams);
        dropdown.setOnDismissListener(listener);
    }

    DialogInterface.OnDismissListener listener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            if(list_item_category.get(0).getChecked() == true){
                edt_choose_category.setText("");
            }
            if(list_item_country.get(0).getChecked() == true){
                edt_choose_country.setText("");
            }
            if(param_category.equals(edt_choose_category.getText().toString()) == false || param_country.equals(edt_choose_country.getText().toString()) == false){
                param_category = edt_choose_category.getText().toString();
                param_country = edt_choose_country.getText().toString();
                getDataSearch(1, param_category, param_country, my_action_bar.getEdt_search().getText().toString());
            }

        }
    };
}
