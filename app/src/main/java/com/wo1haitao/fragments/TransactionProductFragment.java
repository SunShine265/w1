package com.wo1haitao.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wo1haitao.R;
import com.wo1haitao.adapters.ProductsGenChatAdapter;
import com.wo1haitao.models.OfferMeModel;
import com.wo1haitao.views.ActionBarProject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionProductFragment extends BaseFragment {

    ActionBarProject my_action_bar;
    ArrayList<OfferMeModel> arrayOfProductModels;
    ProductsGenChatAdapter product_adapter;
    ListView lv_product;
    public TransactionProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment_view = inflater.inflate(R.layout.fragment_transaction_product, container, false);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        my_action_bar.showLogo();
        my_action_bar.showLLProduct();
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        fakeData();
       // product_adapter = new ProductsGenChatAdapter(getActivity(), arrayOfProductModels,false,R.layout.item_trans_product);
        lv_product = (ListView) fragment_view.findViewById(R.id.lv_product);
        lv_product.setAdapter(product_adapter);
        return fragment_view;
    }
    private void fakeData(){
//        ProductModel productModel1 = new ProductModel("不限", "http://www.golfsuperstore.com.au/image/cache/data/2013-xxio-forged-iron500x500-500x500.jpg", "运动用品", "不限", 0, false);
//        ProductModel productModel2 = new ProductModel("XXIO Iron", "http://www.golfsuperstore.com.au/image/cache/data/2013-xxio-forged-iron500x500-500x500.jpg", "运动用品", "不限", 2, true);
//        ProductModel productModel3 = new ProductModel("用品", "http://www.golfsuperstore.com.au/image/cache/data/2013-xxio-forged-iron500x500-500x500.jpg", "运动用品", "不限", 0, true);
//        ProductModel productModel4 = new ProductModel("运动用", "http://www.golfsuperstore.com.au/image/cache/data/2013-xxio-forged-iron500x500-500x500.jpg", "运动用品", "sss4", 2,false);
//        ProductModel productModel5 = new ProductModel("运动用", "http://www.golfsuperstore.com.au/image/cache/data/2013-xxio-forged-iron500x500-500x500.jpg", "运动用品", "不限", 1,false);
//        arrayOfProductModels = new ArrayList<>();
//        arrayOfProductModels.add(productModel1);
//        arrayOfProductModels.add(productModel2);
//        arrayOfProductModels.add(productModel3);
//        arrayOfProductModels.add(productModel4);
//        arrayOfProductModels.add(productModel5);
    }

}
