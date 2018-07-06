package com.wo1haitao.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.adapters.ProductsEvaTab1Adapter;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.CustomResponse;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.ResponsePacket;
import com.wo1haitao.api.response.RsAddress;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.dialogs.CustomDialogClass;
import com.wo1haitao.dialogs.DialogCountoffer;
import com.wo1haitao.dialogs.DialogFeedback;
import com.wo1haitao.dialogs.DialogShipMethod;
import com.wo1haitao.fragments.AddressPayFragment;
import com.wo1haitao.fragments.AllEvaFragment;
import com.wo1haitao.fragments.BaseFragment;
import com.wo1haitao.fragments.BuyerFragment;
import com.wo1haitao.fragments.ChatDetailFragment;
import com.wo1haitao.fragments.ChatFragment;
import com.wo1haitao.fragments.DisputeFormFragment;
import com.wo1haitao.fragments.EditProfileFragment;
import com.wo1haitao.fragments.FeedbacksFragment;
import com.wo1haitao.fragments.GenFavoriteFragment;
import com.wo1haitao.fragments.HomeFragment;
import com.wo1haitao.fragments.MyBidFragment;
import com.wo1haitao.fragments.MyPostFragment;
import com.wo1haitao.fragments.NotificationFragment;
import com.wo1haitao.fragments.PaymentFragment;
import com.wo1haitao.fragments.ProductDetailFragment;
import com.wo1haitao.fragments.ProductEditFragment;
import com.wo1haitao.fragments.ProductListFragment;
import com.wo1haitao.fragments.ProfileFragment;
import com.wo1haitao.fragments.PurchaseSearchCountryFragment;
import com.wo1haitao.fragments.RequestPurchaseFragment;
import com.wo1haitao.fragments.SearchFragment;
import com.wo1haitao.fragments.WhenPurchaseFragment;
import com.wo1haitao.models.BillingAddressesModel;
import com.wo1haitao.models.CategoryModel;
import com.wo1haitao.models.CountryModel;
import com.wo1haitao.models.OrderModel;
import com.wo1haitao.models.ShippingMethods;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.BottomBarProject;

import java.util.ArrayList;
import java.util.HashMap;

import me.pushy.sdk.Pushy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements HomeFragment.FragmentChangeBuyer, BuyerFragment.ChangeFragment, ProfileFragment.ActionListener,
        SearchFragment.ChangeFragment, ProductListFragment.ChangeFragment, EditProfileFragment.MyCallBack, RequestPurchaseFragment.ChangeFragment,
        PurchaseSearchCountryFragment.MyCallback, ChatFragment.MyCallBack, AddressPayFragment.MyCallBack, DialogCountoffer.MyCallback,
        WhenPurchaseFragment.MyCallback, MyPostFragment.ChangeFragment, GenFavoriteFragment.ChangeFragment, MyBidFragment.ActionListener,
        ProductDetailFragment.ActionListener, DialogShipMethod.MyCallback, PaymentFragment.ActionListener, DialogFeedback.MyCallback,
        DisputeFormFragment.MyCallback, ProductsEvaTab1Adapter.ChangeFragment {
    SharedPreferences token_sh;
    FragmentTransaction frag_trans;
    FragmentManager frag_man;
    Boolean isBack = false;
    //    BottomBar bottomBar;
    BottomBarProject bottomBar;
    CustomDialogClass cdd;
    WindowManager.LayoutParams lp;
    int state_dialog = 0;
    boolean isOpened = false;
    boolean state_back = true;
    int bar_pos = 0;
    int this_bar = 0;
    int bottom_bar_height = 0;

    public int getBottom_bar_height() {
        return bottom_bar_height;
    }

    public void setBottom_bar_height(int bottom_bar_height) {
        this.bottom_bar_height = bottom_bar_height;
    }

    boolean isReSelectTab = false;
    UserProfile user;
    boolean isPressedHome = true, isPressedNotification = true, isPressedChat = true, isPressedProfile = true;
    CallBackKeyboard listenerKeyboard;
    private static Tracker sTracker;
    boolean isSetupCategory = false;
    ProgressDialog progressDialog;
    HashMap<String, String> mapCategory, mapCountry, mapShippingMethod;
    boolean isCategory = false, isCountry = false, isShipping = false;
    boolean isGetAllData = true;

    public CallBackKeyboard getListenerKeyboard() {
        return listenerKeyboard;
    }

    public void setListenerKeyboard(CallBackKeyboard listenerKeyboard) {
        this.listenerKeyboard = listenerKeyboard;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushy.listen(this);
        setContentView(R.layout.activity_main);

        //init analytics


        //init data

        progressDialog = Utils.createProgressDialog(MainActivity.this);
        GetCategory();
        GetCountry();
        GetShippingMethod();
        cdd = new CustomDialogClass(MainActivity.this);
        lp = new WindowManager.LayoutParams();
        lp.copyFrom(cdd.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.argb(150, 0, 0, 0)));
//        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar = (BottomBarProject) findViewById(R.id.bottomBar);
        GetUser(false);
        bottomBar.CheckOnClickTabHome(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(getFragmentManager().findFragmentById(R.id.contentContainer) instanceof HomeFragment)) {
                    ChangeBackgroundButtonHome();
                    Fragment newFragment = new HomeFragment();
                    changeFragment(newFragment, false);
                    bar_pos = 0;
                }
            }
        });
        bottomBar.CheckOnClickTabNotification(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(!(getFragmentManager().findFragmentById(R.id.contentContainer) instanceof NotificationFragment)) {
                this_bar = 1;
                ChangeBackgroundButtonNotification();
                GetUser(true);
                Fragment newFragment = new NotificationFragment();
                if (bar_pos > this_bar) {
                    changeFragment(newFragment, false);
                } else {
                    changeFragment(newFragment, true);
                }
                bar_pos = this_bar;
                //}
            }
        });
        bottomBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                MainActivity.this.bottom_bar_height = bottomBar.getHeight();
            }
        });
        bottomBar.CheckOnClickTabChat(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(getFragmentManager().findFragmentById(R.id.contentContainer) instanceof ChatFragment)) {
                    this_bar = 2;
                    ChangeBackgroundButtonChat();
                    Fragment newFragment = new ChatFragment();
                    if (bar_pos > this_bar) {
                        changeFragment(newFragment, false);
                    } else {
                        changeFragment(newFragment, true);
                    }
                    bar_pos = this_bar;
                }
            }
        });

        bottomBar.CheckOnClickTabProfile(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(getFragmentManager().findFragmentById(R.id.contentContainer) instanceof ProfileFragment)) {
                    ChangeBackgroundButtonProfile();
                    Fragment newFragment = new ProfileFragment();
                    changeFragment(newFragment, true);
                    bar_pos = 3;
                }
            }
        });
        if (bar_pos == 0) {
            if (state_dialog == 0) {
                if (getIntent().getStringExtra("setup-category") != null ) {
                    cdd.dismiss();
                    GetUser(false);
                } else {
//                    cdd.show();
                }
                cdd.getWindow().setAttributes(lp);
                state_dialog = 1;
            }
            ChangeBackgroundButtonHome();
            Fragment newFragment = new HomeFragment();
            changeFragment(newFragment, false);
            bar_pos = 0;
        }
        checkScreen();
    }

    @Override
    public void onKeyboardShow() {

        bottomBar.setVisibility(View.GONE);
        if(listenerKeyboard != null){
            listenerKeyboard.actionWhenShowKeyboard();
        }
    }

    @Override
    public void onKeyboardHide() {

        bottomBar.setVisibility(View.VISIBLE);
        if(listenerKeyboard != null){
            listenerKeyboard.actionWhenHideKeyboard();
        }
    }

    private void checkScreen() {
        if (getIntent().getExtras() == null)
            return;
        if (getIntent().getExtras().getString("screen") != null) {
            String screen = getIntent().getExtras().getString("screen");
            if (screen.equals("mail-activity")) {
                if (cdd.isShowing()) {
                    cdd.dismiss();
                    GetUser(false);
                }
//                Fragment newFragment = new EditProfileFragment();
//                changeFragment(newFragment, true);
                bar_pos = 0;
            }
        } else
            return;
    }


    @Override
    public void changeFragmentBuyer() {
        Fragment newFragment = new BuyerFragment();
        changeFragment(newFragment, true);
        bar_pos = 0;


    }

//    @Override
//    public void HideBottombar(){
//        bottomBar.setVisibility(View.GONE);
//    }

    @Override
    public void changeFragmentPurchase() {
        Fragment newFragment = new WhenPurchaseFragment();
        changeFragment(newFragment, true);
    }


    @Override
    public void openEditProfile() {
        Fragment newFragment = new EditProfileFragment();
        changeFragment(newFragment, true);
        bar_pos = 3;
    }

    @Override
    public void logOut() {
        final ProgressDialog pd_dialog = Utils.createProgressDialog(MainActivity.this);
        final String deviceToken = MyPreferences.getDeviceToken();
        final ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage> call = ws.actionUnRegisterDevice(deviceToken);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if(response.code() != 200){
                    pd_dialog.dismiss();
                    Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }
                else{
                    MyPreferences.setDeviceToken("");
                    final ApiServices services = ApiServices.getInstance();
                    WebService api = services.getRetrofit().create(WebService.class);
                    Call<ResponseMessage<UserProfile>> call1 = api.actionLogout();
                    call1.enqueue(new Callback<ResponseMessage<UserProfile>>() {
                        @Override
                        public void onResponse(Call<ResponseMessage<UserProfile>> call, Response<ResponseMessage<UserProfile>> response) {
                            try {
                                pd_dialog.dismiss();
                                ResponsePacket response_packet = CustomResponse.getResponseMessage(response);
                                ResponseMessage responseMessage;
                                if (response_packet.isSet()) {
                                    responseMessage = response_packet.getResponseMessage();
                                    if (response_packet.isSuccess()) {
                                        if (responseMessage.isSuccess()) {
//                                CustomApp.getInstance().getPusher().nativePusher().unsubscribe(MyPreferences.getKeySubscribeNative());
                                            MyPreferences.clearToken();
//                                MyPreferences.setKeySubscribeNative("");
//                                Toast.makeText(MainActivity.this, "Logout Success!", Toast.LENGTH_SHORT).show();
                                            Intent it = new Intent(MainActivity.this, IntroActivity.class);
                                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            customStartActivity(it);
                                            finish();
                                        }
                                    } else {
                                        try {
                                            ErrorMessage error = responseMessage.getErrors();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(MainActivity.this, IntroActivity.class);
                                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    customStartActivity(it);
                                    finish();
                                }
                            } catch (Exception e) {
                                Utils.crashLog(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                            pd_dialog.dismiss();
                            Utils.OnFailException(t);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                pd_dialog.dismiss();
                Utils.OnFailException(t);
            }
        });

    }


    @Override
    public void changeVerify() {
        Intent it = new Intent(MainActivity.this, VerifyActivity.class);
        customStartActivity(it);
    }

    @Override
    public void changeSetupVerify() {
        Intent it = new Intent(MainActivity.this, SetupAccountVerify.class);
        customStartActivity(it);
    }

    @Override
    public void changeFavorite() {
//        bottomBar.selectTabWithId(R.id.tab_fav);
        GenFavoriteFragment newFragment = new GenFavoriteFragment();
        changeFragment(newFragment, true);
    }

    @Override
    public void changeAddress() {
        AddressPayFragment newFragment = new AddressPayFragment();

        changeFragment(newFragment, true);
    }

    @Override
    public void changeAcceptOffer(OrderModel orderModel, String flagAccept) {
        AddressPayFragment newFragment = new AddressPayFragment();
        newFragment.setAcceptOrtherModel(orderModel);
        newFragment.setFlag(flagAccept);
        changeFragment(newFragment, true);
    }

    @Override
    public void changeRequestPurchase(RsProduct product, String flag) {
        RequestPurchaseFragment newFragment = new RequestPurchaseFragment();
        newFragment.setProductToUpdate(product);
        newFragment.setFlag(flag);
        changeFragment(newFragment, true);
    }

    public void changeRequestPurchaseRepost(RsProduct product) {
        RequestPurchaseFragment newFragment = new RequestPurchaseFragment();
        newFragment.setProductToRepost(product);
        changeFragment(newFragment, true);
    }


    @Override
    public void changeMyPost(String flag) {
        MyPostFragment newFragment = new MyPostFragment();
        newFragment.setChangeFragmentTo(flag);
        changeFragment(newFragment, true);
    }

    @Override
    public void changeDupRequestPurchase(RsProduct product, String flag) {
        RequestPurchaseFragment newFragment = new RequestPurchaseFragment();
        newFragment.setProductToDup(product);
        newFragment.setFlag(flag);
        changeFragment(newFragment, true);
    }

    @Override
    public void changeMyBids() {
        Fragment newFragment = new MyBidFragment();
        changeFragment(newFragment, true);
    }

    @Override
    public void changeAllEval() {
        Fragment newFragment = new AllEvaFragment();
        changeFragment(newFragment, true);
    }

    @Override
    public void changeFBack() {
        Fragment newFragment = new FeedbacksFragment();
        changeFragment(newFragment, true);
    }

    @Override
    public void changeChat() {
        isPressedHome = false;
        isPressedNotification = false;
        isPressedChat = true;
        isPressedProfile = false;
        bottomBar.ChangeBackgroundButtonChat(isPressedChat);
        Fragment newFragment = new ChatFragment();
        changeFragment(newFragment, true);
    }


//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().findFragmentById(R.id.contentContainer) instanceof HomeFragment
                || getFragmentManager().findFragmentById(R.id.contentContainer) instanceof NotificationFragment
                || getFragmentManager().findFragmentById(R.id.contentContainer) instanceof ChatFragment
                || getFragmentManager().findFragmentById(R.id.contentContainer) instanceof ProfileFragment) {
            if (isBack) {
                finishAffinity();
                return;
            }
            this.isBack = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBack = false;
                }
            }, 2000);
        } else {
            if (getFragmentManager().findFragmentById(R.id.contentContainer) instanceof BaseFragment) {
                boolean isBack = ((BaseFragment) getFragmentManager().findFragmentById(R.id.contentContainer)).onBackPressed();
                if (!isBack) {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void changeFragment(Fragment fragment, boolean isUp) {
//        super.changeFragment(fragment, isUp);

        frag_trans = getFragmentManager().beginTransaction();
//        if (isUp) {
//            frag_trans.setCustomAnimations(R.animator.up_new_fragment, R.animator.up_old_fragment, R.animator.down_new_fragment, R.animator.down_old_fragment);
//        } else {
//            frag_trans.setCustomAnimations(R.animator.down_new_fragment, R.animator.down_old_fragment);
//        }
        frag_trans.replace(R.id.contentContainer, fragment);
        frag_trans.addToBackStack(fragment.getClass().getName());
        frag_trans.commit();
    }

    @Override
    public void changeProductList() {
        Fragment newFragment = new ProductListFragment();
        changeFragment(newFragment, true);
    }

    @Override
    public void changeNewProduct() {
        Fragment newFragment = new ProductEditFragment();
        changeFragment(newFragment, true);
    }

    @Override
    public void changeDetailProduct(long product_id, String flag) {
        ProductDetailFragment newFragment = new ProductDetailFragment();
        newFragment.setProduct_id(product_id);
        newFragment.setFlag(flag);
        changeFragment(newFragment, true);
    }


    @Override
    public void changeMain() {
//        cdd.show();
        cdd.getWindow().setAttributes(lp);
        state_dialog = 1;
        Fragment newFragment = new HomeFragment();
        changeFragment(newFragment, true);
    }

    @Override
    public void changeRequestPurchase() {
        Fragment newFragment = new RequestPurchaseFragment();
        changeFragment(newFragment, true);
        bar_pos = 0;
    }
//
//    @Override
//    public void changDetailProduct() {
//        position++;
//        Fragment newFragment = new ProductDetailFragment();
//        changeFragment(newFragment, true);
//    }

    @Override
    public void changeSearch() {
        Fragment newFragment = new SearchFragment();
        changeFragment(newFragment, true);
    }

    @Override
    public void changeChatDetail(int idMessage) {
        ChatDetailFragment newFragment = new ChatDetailFragment();
        newFragment.setIdMessage(idMessage);
        changeFragment(newFragment, true);
    }

    @Override
    public void onChangeCommitPayment(OrderModel orderModel, ArrayList<RsAddress> shippingAddresses, BillingAddressesModel billingAddressesModel, Boolean isShipandBill) {
        PaymentFragment newFragment = new PaymentFragment();
        newFragment.setShippingAddresses(shippingAddresses);
        newFragment.setBillingAddressesModel(billingAddressesModel);
        newFragment.setPaymentOrder(orderModel);
        newFragment.setShipandBill(isShipandBill);
        changeFragment(newFragment, true);
    }

    @Override
    public void changeSearchCountry() {
        Fragment newFragment = new PurchaseSearchCountryFragment();
        changeFragment(newFragment, true);
    }

    @Override
    public void changeProfile() {
        Fragment newFragment = new ProfileFragment();
        changeFragment(newFragment, true);
    }

    public void ChangeBackgroundButtonHome() {
        isPressedHome = true;
        isPressedNotification = false;
        isPressedChat = false;
        isPressedProfile = false;
        bottomBar.ChangeBackgroundButtonHome(isPressedHome);
    }

    public void ChangeBackgroundButtonNotification() {
        isPressedHome = false;
        isPressedNotification = true;
        isPressedChat = false;
        isPressedProfile = false;
        bottomBar.ChangeBackgroundButtonNotification(isPressedNotification);
    }

    public void ChangeBackgroundButtonChat() {
        isPressedHome = false;
        isPressedNotification = false;
        isPressedChat = true;
        isPressedProfile = false;
        bottomBar.ChangeBackgroundButtonChat(isPressedChat);
    }

    public void ChangeBackgroundButtonProfile() {
        isPressedHome = false;
        isPressedNotification = false;
        isPressedChat = false;
        isPressedProfile = true;
        bottomBar.ChangeBackgroundButtonProfile(isPressedProfile);
    }

    /**
     * Get User
     *
     * @params:
     * @return:
     */
    protected void GetUser(final boolean isNotification) {
//        pd_dialog = Utils.getPGDialog(MainActivity.this);
//        pd_dialog.show();
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        ws.actionGetUser().enqueue(new Callback<ResponseMessage<UserProfile>>() {
            @Override
            public void onResponse(Call<ResponseMessage<UserProfile>> call, Response<ResponseMessage<UserProfile>> response) {
                try {
//                    pd_dialog.dismiss();
                    ResponsePacket response_packet = CustomResponse.getResponseMessage(response);
                    ResponseMessage responseMessage;
                    if (response_packet.isSet()) {
                        responseMessage = response_packet.getResponseMessage();
                        if (response_packet.isSuccess()) {
                            if (responseMessage.isSuccess()) {
                                user = (UserProfile) responseMessage.getData();
                                CustomApp.getInstance().setNum_of_notify(user.getUnread_notifications());
                                CustomApp.getInstance().setNum_of_chat(user.getNum_of_unread());
                                SetNotification(user.getUnread_notifications());
                                SetChat(user.getNum_of_unread());
//                                if(isNotification == true){
//                                    SetNotification(0);
//                                }
//                                if (isNotification == false) {
//                                    if (user.getSetup_account() != null  && getIntent().getStringExtra("finish_setup_account") == null) {
//                                        if (user.getSetup_account().getSetup_country() == false) {
//                                            Intent it = new Intent(MainActivity.this, SetupAccountProfile.class);
//                                            startActivity(it);
//                                        }
////                                    else {
////                                        if (user.getSetup_account().getSetup_verification() == false) {
////                                            Intent it = new Intent(MainActivity.this, SetupAccountVerify.class);
////                                            startActivity(it);
////                                        }
////                                    }
//                                    }
//                                }
                            }
                        } else {
                            try {
                                ErrorMessage error = responseMessage.getErrors();
                                Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
//                pd_dialog.dismiss();
                Utils.OnFailException(t);
            }
        });
    }

    /**
     * Set notification
     *
     * @params:
     * @return:
     */
    public void SetNotification(int num) {
        bottomBar.SetNumberNotification(num);
    }

    /**
     * Set num chat unread
     *
     * @params:
     * @return:
     */
    public void SetChat(int num) {
        bottomBar.SetNumberChat(num);
    }

    @Override
    protected void onResume() {

        super.onResume();
        CustomApp application = (CustomApp) getApplication();
        sTracker = application.getDefaultTracker();
        sTracker.setScreenName("Main Screen");
        sTracker.send(new HitBuilders.ScreenViewBuilder().build());
        CustomApp.getInstance().mainActivity = this;

    }

    @Override
    protected void onStop() {
        CustomApp.getInstance().mainActivity = null;
        super.onStop();

    }



    public void UpdateNumNotify(int num_of_notify) {
        SetNotification(num_of_notify);
    }

    public void UpdateNumChat(int numChat){
        SetChat(numChat);
    }


    public interface CallBackKeyboard {
        void actionWhenHideKeyboard();
        void actionWhenShowKeyboard();
    }

    /**
     * Get list category from server
     *
     * @param:
     * @return: Hashmap
     */


    public void GetCategory() {
        ArrayList<CategoryModel> listCategory = new ArrayList<>();
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        Call<ArrayList<Object>> getCategory = ws.actionGetCategory();
        getCategory.enqueue(new Callback<ArrayList<Object>>() {
            @Override
            public void onResponse(Call<ArrayList<Object>> call, Response<ArrayList<Object>> response) {
                if (response.body() != null && response.isSuccessful()) {

                    ArrayList<Object> listCategory = response.body();
//                    for(int i = 0; i < listCategory.size(); i++){
//                        mapCategory.put(String.valueOf(listCategory.get(i).getId()), listCategory.get(i).getName());
//                    }
                    MyPreferences.SetDataFromServer(listCategory,MyPreferences.KEY_GET_CATEGORY);
//                    MyPreferences.GetDataFromMyPreferences("category");
                } else if (response.errorBody() != null) {
                    try {
                        isGetAllData = false;
                        ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                        ErrorMessage error = responseMessage.getErrors();
                        Toast.makeText(getBaseContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Crashlytics.logException(e);
//                        Toast.makeText(getBaseContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
//                        GetDataFail();
                    }
                } else {
                    isGetAllData = false;
//                    Toast.makeText(getBaseContext(), R.string.no_advesting, Toast.LENGTH_SHORT).show();
//                    GetDataFail();
                }
                isCategory = true;
                if (checkComplete()) {
                    actionWhenCompleteGetData();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Object>> call, Throwable t) {
                progressDialog.dismiss();
//                Toast.makeText(getBaseContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
//                GetDataFail();
                isCategory = true;
                isGetAllData = false;
                if (checkComplete()) {
                    actionWhenCompleteGetData();
                }
            }
        });
    }

    /**
     * Get list country from server
     *
     * @param:
     * @return: Hashmap
     */

    public void GetCountry() {
        ArrayList<CountryModel> listCountry = new ArrayList<>();
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        final Call<ArrayList<Object>> getCoutry = ws.actionGetCountry();
        getCoutry.enqueue(new Callback<ArrayList<Object>>() {
            @Override
            public void onResponse(Call<ArrayList<Object>> call, Response<ArrayList<Object>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    ArrayList<Object> listcountry = response.body();
                    MyPreferences.SetDataFromServer(listcountry, MyPreferences.KEY_GET_COUNTRY);
                } else if (response.errorBody() != null) {
                    try {
                        isGetAllData = false;
                        ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                        ErrorMessage error = responseMessage.getErrors();
                        Toast.makeText(getBaseContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Crashlytics.logException(e);
//                        Toast.makeText(getBaseContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
//                        GetDataFail();
                    }
                } else {
                    isGetAllData = false;
//                    Toast.makeText(getBaseContext(), R.string.no_advesting, Toast.LENGTH_SHORT).show();
//                    GetDataFail();
                }
                isCountry = true;
                if (checkComplete()) {
                    actionWhenCompleteGetData();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Object>> call, Throwable t) {
                progressDialog.dismiss();
                isCountry = true;
                isGetAllData = false;
                if (checkComplete()) {
                    actionWhenCompleteGetData();
                }
//                Toast.makeText(getBaseContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
//                GetDataFail();
            }
        });
    }

    /**
     * Get list shipping method from server
     *
     * @param:
     * @return: Hashmap
     */
    public void GetShippingMethod() {
        ArrayList<ShippingMethods> listShippingMethod = new ArrayList<>();
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        final Call<ArrayList<Object>> getShippingMethod = ws.actionGetShippingMethod();
        getShippingMethod.enqueue(new Callback<ArrayList<Object>>() {
            @Override
            public void onResponse(Call<ArrayList<Object>> call, Response<ArrayList<Object>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    ArrayList<Object> listShippingMethod = response.body();
                    MyPreferences.SetDataFromServer(listShippingMethod, MyPreferences.KEY_GET_SHIPPING_METHOD);
                } else if (response.errorBody() != null) {
                    try {
                        isGetAllData = false;
                        ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                        ErrorMessage error = responseMessage.getErrors();
                        Toast.makeText(getBaseContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Crashlytics.logException(e);
//                        Toast.makeText(getBaseContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
//                        GetDataFail();
                    }
                } else {
                    isGetAllData = false;
//                    GetDataFail();
//                    Toast.makeText(getBaseContext(), R.string.no_advesting, Toast.LENGTH_SHORT).show();
                }
                isShipping = true;
                if (checkComplete()) {
                    actionWhenCompleteGetData();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Object>> call, Throwable t) {
                progressDialog.dismiss();
                isShipping = true;
                isGetAllData = false;
                if (checkComplete()) {
                    actionWhenCompleteGetData();
                }
//                Toast.makeText(getBaseContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
//                GetDataFail();
            }
        });
    }

    private Boolean checkComplete() {
        return (isShipping == true && isCountry == true && isCategory == true);
    }

    private void actionWhenCompleteGetData() {
        progressDialog.dismiss();
        if (isGetAllData == false) {
            GetDataFail();
//            Toast.makeText(getBaseContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
//            Intent it = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(it);
//            overridePendingTransition(R.anim.new_activity, R.anim.empty_ani);
//            MyPreferences.clearToken();
        }
//        else {
//            Intent it = new Intent(MainActivity.this, MainActivity.class);
//            it.putExtra("setup-category", user.getSetup_account().getSetup_category());
//            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            customStartActivity(it);
//        }
    }

    private void GetDataFail() {
        Intent it = new Intent(MainActivity.this, IntroActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        customStartActivity(it);
        finish();
        MyPreferences.clearToken();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i("TAG","ABC");
    }
}
