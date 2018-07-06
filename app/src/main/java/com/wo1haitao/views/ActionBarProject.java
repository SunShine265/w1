package com.wo1haitao.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wo1haitao.R;

/**
 * Created by user on 5/3/2017.
 */

public class ActionBarProject extends FrameLayout {
    static ImageView iv_back_action;
    static ImageView iv_logo_top;
    static ImageView iv_setting;
    static ImageView iv_report;
    ImageView ic_menu_search;
    ImageView iv_back;
    ImageView iv_cam;
    ImageView iv_search, ivDeleteSearch;
    TextView tv_mytext, btnSaveProduct;
    FrameLayout fr_save, flSaveProduct_text, actionbar, fr_back_action;
    LinearLayout ll_search_bar, ll_product, ll_report;
    EditText edt_search;
    Context context;


    public ActionBarProject(@NonNull Context context) {
        super(context);
        this.context = context;
        init(context, null);

    }

    public ActionBarProject(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public ActionBarProject(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.action_bar_project, this, true);
        actionbar = (FrameLayout) findViewById(R.id.id_actionbar);
        iv_back_action = (ImageView) findViewById(R.id.ib_back_action);
        iv_logo_top = (ImageView) findViewById(R.id.logoTopBar);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);
        tv_mytext = (TextView) findViewById(R.id.mytext);
        fr_save = (FrameLayout) findViewById(R.id.flSaveProduct);
        ll_search_bar = (LinearLayout) findViewById(R.id.ll_search_bar);
        ll_product = (LinearLayout) findViewById(R.id.ll_product);
        ll_report = (LinearLayout) findViewById(R.id.ll_report);
        iv_back = (ImageView) ll_search_bar.findViewById(R.id.iv_back);
        iv_cam = (ImageView) ll_search_bar.findViewById(R.id.ivCammera);
        iv_search = (ImageView) ll_search_bar.findViewById(R.id.ivSearch);
        ivDeleteSearch = (ImageView) ll_search_bar.findViewById(R.id.ivDeleteSearch);
        edt_search = (EditText) ll_search_bar.findViewById(R.id.edtSearch);
        flSaveProduct_text = (FrameLayout) findViewById(R.id.flSaveProduct_text);
        btnSaveProduct = (TextView) findViewById(R.id.btnSaveProduct);
        iv_report = (ImageView) findViewById(R.id.iv_dispute);
    }

    public void showReport() {
        ll_report.setVisibility(VISIBLE);
    }

    public void showTitle(int resString) {
        tv_mytext.setVisibility(VISIBLE);
        tv_mytext.setText(resString);
    }

    public void showTitle(String resString) {
        tv_mytext.setVisibility(VISIBLE);
        tv_mytext.setText(resString);
    }

    public void showSettingBt(OnClickListener setting_click) {
        iv_setting.setVisibility(VISIBLE);
        iv_setting.setOnClickListener(setting_click);
    }

    public void showBack(OnClickListener onClickListener) {
        iv_back_action.setVisibility(VISIBLE);
        iv_back_action.setOnClickListener(onClickListener);
    }

    public void HideBack(){
        iv_back_action.setVisibility(GONE);
    }

    public void showLLProduct() {
        ll_product.setVisibility(VISIBLE);
    }

    public EditText getEdt_search() {
        return edt_search;
    }

    public void setOnSearchClick(OnClickListener searchClick) {
        iv_search.setOnClickListener(searchClick);
    }

    public void showLogo() {
        iv_logo_top.setVisibility(VISIBLE);
    }

    public void showSaveBt() {
        fr_save.setVisibility(VISIBLE);
    }

    public void showSearch(OnClickListener back_click, OnClickListener cam_click, OnClickListener search_click) {
        ll_search_bar.setVisibility(VISIBLE);
        iv_back.setOnClickListener(back_click);
        iv_cam.setOnClickListener(cam_click);
        iv_search.setOnClickListener(search_click);
    }

    public void getLogoBarBack(OnClickListener back_click) {
        iv_logo_top.setVisibility(VISIBLE);
        iv_back_action.setVisibility(VISIBLE);
        iv_back_action.setOnClickListener(back_click);
    }

    public void showLLProduct_text(OnClickListener click) {
        flSaveProduct_text.setVisibility(VISIBLE);
        flSaveProduct_text.setOnClickListener(click);
    }

    public void ShowProductTitleHeader() {
        btnSaveProduct.setVisibility(VISIBLE);
        btnSaveProduct.setText("出价");
    }

    public void setTextSaler(Boolean isVisible, View.OnClickListener click_event) {
        if (isVisible) {
            btnSaveProduct.setText(null);
            flSaveProduct_text.setVisibility(VISIBLE);
            flSaveProduct_text.setBackground(null);
            flSaveProduct_text.setEnabled(false);
            flSaveProduct_text.setPadding(0, 0, 45, 0);
            WindowManager manager =  (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int short_dimention = point.x > point.y ? point.y : point.x;
            int fix_size = 78;
            float scale_by_screen = (float) 1;
            if(short_dimention < 900){
                scale_by_screen = (float) 0.6;
            }
            else if (short_dimention < 1200){
                scale_by_screen = (float) 0.85;
            }
            fix_size = (int) (fix_size*scale_by_screen);
            ViewGroup.LayoutParams layoutParams = btnSaveProduct.getLayoutParams();
            layoutParams.width = fix_size;
            layoutParams.height = fix_size;

            btnSaveProduct.setBackgroundResource(R.drawable.ic_edit_white);
            btnSaveProduct.setLayoutParams(layoutParams);
            btnSaveProduct.setOnClickListener(click_event);
        } else {
            flSaveProduct_text.setVisibility(GONE);
        }

    }

    public void changeProductTextReoffer() {
        btnSaveProduct.setVisibility(VISIBLE);
        btnSaveProduct.setText("重新出价");
    }

    public void HideProductTitleHeader() {
        btnSaveProduct.setVisibility(GONE);
    }

    public void HideProductText() {
        flSaveProduct_text.setVisibility(GONE);
    }

    public void hideViewSetting() {
        iv_setting.setVisibility(GONE);
    }

    public void ShowDispute() {
        iv_setting.setImageBitmap(null);
        iv_setting.setVisibility(VISIBLE);
        iv_setting.setBackgroundResource(R.drawable.report_user);
    }

    public void ChangeButtonImageSearch() {

        edt_search.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager inputMethodManager = (InputMethodManager) ActionBarProject.this.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edt_search.getWindowToken(), 0);
                    edt_search.clearFocus();
                    return true;
                }
                return false;
            }
        });

        edt_search.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    iv_search.setVisibility(GONE);
                    ivDeleteSearch.setVisibility(VISIBLE);

                } else {
                    iv_search.setVisibility(VISIBLE);
                    ivDeleteSearch.setVisibility(GONE);

                }
            }
        });
        ivDeleteSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search.setText("");
            }
        });


    }

    public void DowSizeHeightActionbar(int height) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        actionbar.getLayoutParams().height = px;
    }
}
