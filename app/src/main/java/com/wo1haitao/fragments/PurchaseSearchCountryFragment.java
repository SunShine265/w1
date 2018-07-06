package com.wo1haitao.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.wo1haitao.R;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.views.ActionBarProject;
import com.wo1haitao.views.CustomViewAreaItem;

import java.util.ArrayList;
import java.util.Arrays;


public class PurchaseSearchCountryFragment extends BaseFragment implements View.OnClickListener {


    ArrayList<String> list_country;
    int fix_width=0;
    LinearLayout lo_list_country,ll_general;
    LinearLayout line_layout=null;
    int id_bn_is_click =-1;
    Button bt_clicked=null;
    Button btAme,btAsi,btEro,btAus, btAll;//, btOtherArea, btMiddleEast;
    MyCallback getListener(){
        if(getActivity() instanceof MyCallback){
            return (MyCallback) getActivity();
        }
        return null;
    };
    ActionBarProject my_action_bar;

    int resIdColor,resIdColorNormal,resIdBackground, resIdBackgroundNormal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment_view = inflater.inflate(R.layout.fragment_purchase, container, false);
        my_action_bar  = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        my_action_bar.showTitle("请选择您的地区");
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPress();
            }
        });
        lo_list_country = (LinearLayout) fragment_view.findViewById(R.id.lo_list_country);
        btAme = (Button) fragment_view.findViewById(R.id.btAme);
        btAsi = (Button) fragment_view.findViewById(R.id.btAsi);
        btEro = (Button) fragment_view.findViewById(R.id.btEro);
        btAus = (Button) fragment_view.findViewById(R.id.btAus);
        btAll = (Button) fragment_view.findViewById(R.id.btAll);
//        btOtherArea = (Button) fragment_view.findViewById(R.id.btOtherArea);
//        btMiddleEast = (Button) fragment_view.findViewById(R.id.btMiddleEast);
        ll_general = (LinearLayout) fragment_view.findViewById(R.id.ll_general);
        InputMethodManager imm = (InputMethodManager)PurchaseSearchCountryFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(fragment_view.getWindowToken(), 0);
        ll_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearMenuClick();
            }
        });
        SetDataAmerican();
        String packageName = getActivity().getPackageName();
        resIdColor = getResources().getColor(R.color.white);
        resIdColorNormal = btAme.getCurrentTextColor();
        resIdBackground =  getResources().getIdentifier("rounded_button_dark", "drawable", packageName);
        resIdBackgroundNormal = getResources().getIdentifier("rounded_button", "drawable", packageName);
        btAme.setOnClickListener(this);
        btAsi.setOnClickListener(this);
        btEro.setOnClickListener(this);
        btAus.setOnClickListener(this);
//        btOtherArea.setOnClickListener(this);
        btAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getListener() != null) {
                    clearMenuClick();
                    getListener().changeProductList();
                }
            }
        });

//        btOtherArea.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(getListener() != null) {
//                    clearMenuClick();
//                    MainActivity mainActivity = (MainActivity) PurchaseSearchCountryFragment.this.getActivity();
//                    ProductListFragment fragment = new ProductListFragment();
//                    fragment.setPurchaseSearchCountry("其他地区");
//                    mainActivity.changeFragment(fragment, true);
//                }
//            }
//        });

//        btMiddleEast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(getListener() != null) {
//                    clearMenuClick();
//                    MainActivity mainActivity = (MainActivity) PurchaseSearchCountryFragment.this.getActivity();
//                    ProductListFragment fragment = new ProductListFragment();
//                    fragment.setPurchaseSearchCountry("中东地区");
//                    mainActivity.changeFragment(fragment, true);
//                }
//            }
//        });

        return fragment_view;
    }

    private void clearMenuClick() {
        if(bt_clicked!=null){
            bt_clicked.setTextColor(resIdColorNormal);
            bt_clicked.setBackgroundResource(resIdBackgroundNormal);
            lo_list_country.removeAllViews();
            if(line_layout!=null)
                line_layout.removeAllViews();
            bt_clicked=null;
            return;
        }
    }

    private void SetDataAmerican(){
        list_country = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.america_group)));
    }

    private void SetDataAsia(){
        list_country = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.asia_group)));
    }

    private void SetDataEuro(){
        list_country = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.euro_group)));
    }

    private void SetDataAutralia(){
        list_country = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.autralia_group)));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btAme:
                SetDataAmerican();
                break;
            case R.id.btAsi:
                SetDataAsia();
                break;
            case R.id.btEro:
                SetDataEuro();
                break;
            case R.id.btAus:
                SetDataAutralia();
                break;
            default:
                list_country = null;
                break;
        }
        Button thisView = (Button) view;
        if(bt_clicked!=null && bt_clicked.getId() == view.getId()){
            bt_clicked.setTextColor(resIdColorNormal);
            bt_clicked.setBackgroundResource(resIdBackgroundNormal);
            lo_list_country.removeAllViews();
            if(line_layout!=null)
                line_layout.removeAllViews();
            bt_clicked=null;
            return;
        }
        if(bt_clicked!=null){
            bt_clicked.setTextColor(resIdColorNormal);
            bt_clicked.setBackgroundResource(resIdBackgroundNormal);
        }

        bt_clicked = (Button) view;
        id_bn_is_click = view.getId();
        thisView.setTextColor(resIdColor);
        thisView.setBackgroundResource(resIdBackground);
        if (line_layout!=null){
            lo_list_country.removeAllViews();
            line_layout.removeAllViews();
        }
        int position =0;
        LinearLayout.LayoutParams param_item = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        if(list_country!=null) {
            for (final String text : list_country) {
                if (line_layout == null || position > 3) {
                    if (position > 3) {
                        lo_list_country.addView(line_layout);
                    }
                    line_layout = new LinearLayout(getActivity());
                    line_layout.setOrientation(LinearLayout.HORIZONTAL);
                    position = 0;
                }
                CustomViewAreaItem view_item = new CustomViewAreaItem(getActivity());
                view_item.setLayoutParams(param_item);
                view_item.setTextContent(text);
                view_item.setFontSize(13);
                view_item.setTextColor(resIdColor);
                view_item.setClickBt(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(getListener() != null) {
                            clearMenuClick();
                            MainActivity mainActivity = (MainActivity) PurchaseSearchCountryFragment.this.getActivity();
                            ProductListFragment fragment = new ProductListFragment();
                            fragment.setPurchaseSearchCountry(text);
                            mainActivity.changeFragment(fragment, true);
//                        getListener().changeProductList();
                        }
                    }
                });
//                view_item.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        clearMenuClick();
//                        getListener().changeProductList();
//                    }
//                });
                line_layout.addView(view_item);
                position++;
            }

            if (position < 4) {
                for (int i = position; i < 4; i++) {
                    FrameLayout view_item = new FrameLayout(getActivity());
                    view_item.setLayoutParams(param_item);
                    line_layout.addView(view_item);

                }
            }
            lo_list_country.addView(line_layout);
        }
    }

    public interface MyCallback{
        void changeSearch();
        void changeProductList();
    }


    private View.OnClickListener OnSearchClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            clearMenuClick();
            getListener().changeProductList();
        }
    };
}





