package com.wo1haitao.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wo1haitao.R;
import com.wo1haitao.controls.CustomViewProviderItem;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class SearchFragment extends BaseFragment{
    ArrayList<String> listProvider;
    FlowLayout flListProvider;
    ChangeFragment getListenner(){
        if (this.getActivity() instanceof ChangeFragment) {
            return (ChangeFragment) this.getActivity();
        }
        return null;
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        flListProvider = (FlowLayout) view.findViewById(R.id.flListprovider);
        Utils.setActionBack(view,getActivity());
        ActionBarProject my_action_bar;
        my_action_bar = (ActionBarProject) view.findViewById(R.id.my_action_bar);
        my_action_bar.showSearch((new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        backPress();
                    }
                }), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getListenner().changeNewProduct();
                    }
                },null);

                fakeData();
        createViewProvider();
        LinearLayout btn_search = (LinearLayout)view.findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SearchFragment.this.getActivity() != null){
                    getListenner().changeProductList();
                }
            }
        });

        return view;
    }

    public interface ChangeFragment{
        void changeProductList();
        void changeNewProduct();

    }
    private void fakeData(){
        listProvider = new ArrayList<>();
        listProvider.add("NIKE");
        listProvider.add("封确");
        listProvider.add("钟认");
        listProvider.add("家具");
        listProvider.add("确认确认 认");
        listProvider.add("家具");
    }

    private void createViewProvider(){
        for (String text : listProvider){
            CustomViewProviderItem itemView = new CustomViewProviderItem(getActivity());
            itemView.setTextContent(text);
            itemView.setClickEvent(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getListenner().changeProductList();
                }
            });
            flListProvider.addView(itemView);
        }

    }


}
