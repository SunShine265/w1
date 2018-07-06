package com.wo1haitao.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.activities.VerifyActivity;
import com.wo1haitao.adapters.NotificationsAdapter;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.models.NotificationModel;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class NotificationFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    ActionBarProject my_action_bar;
    NotificationsAdapter product_adapter;
    ListView lv_notification;
    boolean isEndPage = false;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<NotificationModel> arrNotifications;
    View fragment_view;

    boolean is_fetching_data = false;
    final static String ACCOUNT_VERIFY_REJECTED = "noti_account_verification_rejected";
    final static String ACCOUNT_VERIFY_CLOSED = "noti_account_verification_closed";
    final static String INVOICE_REJECTED = "noti_invoice_rejected";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragment_view = inflater.inflate(R.layout.fragment_notification, container, false);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) fragment_view.findViewById(R.id.swipe_refresh_layout_grid);
        my_action_bar.showTitle(R.string.title_my_global);
        lv_notification = (ListView) fragment_view.findViewById(R.id.lv_notification);

        final int[] threshold = {1};
        GetUserNotifications(1);
        lv_notification.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                int count = lv_notification.getCount();
                if (scrollState == SCROLL_STATE_IDLE) {
//                    if (lv_notification.getLastVisiblePosition() >= count - 1) {
//                        // Execute LoadMoreDataTask
//                        if (isEndPage == false) {
//                            threshold[0]++;
//                            GetUserNotifications(threshold[0]);
//                        }
//                    }

                    View view = (View) lv_notification.getChildAt(lv_notification.getChildCount()-1);

                    // Calculate the scrolldiff
                    int diff = (view.getBottom()-(lv_notification.getHeight()+lv_notification.getScrollY()));

                    if(diff == 0 && is_fetching_data == false){
                        threshold[0]++;
                        swipeRefreshLayout.setEnabled(true);
                        GetUserNotifications(threshold[0]);
                    }
                    else if(listIsAtTop()){
                        swipeRefreshLayout.setEnabled(true);
                        swipeRefreshLayout.setRefreshing(true);
                        threshold[0] = 1;
                        GetUserNotifications(1);
                    }
                    else {
                        swipeRefreshLayout.setEnabled(false);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

//            @Override
//            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int i2) {
//                int topRowVerticalPosition = (lv_notification == null || lv_notification.getChildCount() == 0) ?
//                        0 : lv_notification.getChildAt(0).getTop();
//                if (firstVisibleItem == 0 && topRowVerticalPosition >= 0) {
//                    swipeRefreshLayout.setEnabled(true);
//                    threshold[0] = 1;
//
//                } else {
//                    swipeRefreshLayout.setEnabled(false);
//                }
//            }
        });

        swipeRefreshLayout.setOnRefreshListener(NotificationFragment.this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                threshold[0] = 1;
                GetUserNotifications(1);
            }
        });
        return fragment_view;
    }

    public boolean listIsAtTop()   {
        if(lv_notification.getChildCount() == 0) return true;
        return lv_notification.getChildAt(0).getTop() == 0;
    }

    /**
     * Get notifications form API
     *
     * @params:
     * @return:
     */
    public void GetUserNotifyHide() {
        is_fetching_data = true;
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<ArrayList<NotificationModel>>> call = ws.actionGetUserNotifications(1);
        call.enqueue(new Callback<ResponseMessage<ArrayList<NotificationModel>>>() {
            @Override
            public void onResponse(Call<ResponseMessage<ArrayList<NotificationModel>>> call, Response<ResponseMessage<ArrayList<NotificationModel>>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        ArrayList<NotificationModel> tempReponse = new ArrayList();
                        arrNotifications = response.body().getData();
                        if (arrNotifications == null) {
                            arrNotifications = new ArrayList<NotificationModel>();
                        }
                        product_adapter = new NotificationsAdapter(getActivity(), arrNotifications, false, R.layout.item_my_notification);
                        lv_notification.setAdapter(product_adapter);
                        lv_notification.setEmptyView(fragment_view.findViewById(R.id.emptyElement));
                        if (arrNotifications.size() > 0) {
                            UserProfile user = arrNotifications.get(0).getCommon_user();
                            if (getActivity() instanceof MainActivity) {
                                MainActivity main = (MainActivity) getActivity();
                                CustomApp.getInstance().setNum_of_notify(user.getUnread_notifications());
                                main.SetNotification(user.getUnread_notifications());
                            }
                        } else {
                            if (getActivity() instanceof MainActivity) {
                                MainActivity main = (MainActivity) getActivity();
                                CustomApp.getInstance().setNum_of_notify(0);
                                main.SetNotification(0);
                            }
                        }
                        lv_notification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    PostNotifications(arrNotifications.get(i));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                is_fetching_data = false;
            }

            @Override
            public void onFailure(Call<ResponseMessage<ArrayList<NotificationModel>>> call, Throwable t) {
                Utils.OnFailException(t);
                is_fetching_data = false;
            }
        });
    }

    public void GetUserNotifications(final int page) {
        is_fetching_data = true;
        swipeRefreshLayout.setRefreshing(true);
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<ArrayList<NotificationModel>>> call = ws.actionGetUserNotifications(page);
        call.enqueue(new Callback<ResponseMessage<ArrayList<NotificationModel>>>() {
            @Override
            public void onResponse(Call<ResponseMessage<ArrayList<NotificationModel>>> call, Response<ResponseMessage<ArrayList<NotificationModel>>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        ArrayList<NotificationModel> tempReponse = new ArrayList();
                        if (page == 1) {
                            arrNotifications = response.body().getData();
                            if (arrNotifications == null) {
                                arrNotifications = new ArrayList<NotificationModel>();
                            }
                            if (arrNotifications.size() > 0) {
                                UserProfile user = arrNotifications.get(0).getCommon_user();
                                if (getActivity() instanceof MainActivity) {
                                    MainActivity main = (MainActivity) getActivity();
                                    CustomApp.getInstance().setNum_of_notify(user.getUnread_notifications());
                                    main.SetNotification(user.getUnread_notifications());
                                }
                            } else {
                                if (getActivity() instanceof MainActivity) {
                                    MainActivity main = (MainActivity) getActivity();
                                    CustomApp.getInstance().setNum_of_notify(0);
                                    main.SetNotification(0);
                                }
                            }
                            product_adapter = new NotificationsAdapter(getActivity(), arrNotifications, false, R.layout.item_my_notification);
                            lv_notification.setAdapter(product_adapter);
                            lv_notification.setEmptyView(fragment_view.findViewById(R.id.emptyElement));
                        } else {
                            tempReponse = response.body().getData();
                            if (tempReponse.size() == 0) {
                                isEndPage = true;
                            } else {
                                for (int i = 0; i < tempReponse.size(); i++) {
                                    arrNotifications.add(tempReponse.get(i));
                                }
                            }
                            product_adapter.notifyDataSetChanged();
                        }
                        lv_notification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    PostNotifications(arrNotifications.get(i));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                is_fetching_data = false;
            }

            @Override
            public void onFailure(Call<ResponseMessage<ArrayList<NotificationModel>>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Utils.OnFailException(t);
                is_fetching_data = false;
                swipeRefreshLayout.setEnabled(false);
            }
        });
    }

    /**
     * Post notifications
     *
     * @params:
     * @return:
     */
    public void PostNotifications(final NotificationModel notificationModel) {
        final ProgressDialog progressDialog = Utils.createProgressDialog(NotificationFragment.this.getActivity());
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<NotificationModel>> call = ws.actionPostNotifications(notificationModel.getId());
        call.enqueue(new Callback<ResponseMessage<NotificationModel>>() {
            @Override
            public void onResponse(Call<ResponseMessage<NotificationModel>> call, Response<ResponseMessage<NotificationModel>> response) {
                try {
                    if (response.body() != null && response.body().isSuccess()) {
                        if (notificationModel.getNotice_type().equals(ACCOUNT_VERIFY_REJECTED)
                                || notificationModel.getNotice_type().equals(ACCOUNT_VERIFY_CLOSED)) {
                            Intent it = new Intent(NotificationFragment.this.getActivity(), VerifyActivity.class);
                            NotificationFragment.this.getActivity().startActivity(it);
                        } else if (notificationModel.getNotice_type().equals(INVOICE_REJECTED)
                                && notificationModel.getProduct_offer() != null && notificationModel.getProduct_offer().getOrder() != null) {
                            MainActivity mainActivity = (MainActivity) NotificationFragment.this.getActivity();
                            MyBidFragment fragment = new MyBidFragment();
                            fragment.setNotification(true);
                            fragment.setIdOrderNotification(notificationModel.getProduct_offer().getOrder().getId());
                            mainActivity.changeFragment(fragment, true);
                        } else {
                            MainActivity mainActivity = (MainActivity) NotificationFragment.this.getActivity();
                            CustomApp.getInstance().setNum_of_notify(notificationModel.getCommon_user().getUnread_notifications());
                            mainActivity.SetNotification(notificationModel.getCommon_user().getUnread_notifications());
                            mainActivity.changeDetailProduct(notificationModel.getProduct_listing().getId(), null);
                        }
                        progressDialog.dismiss();
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<NotificationModel>> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }

    @Override
    public void onRefresh() {
        GetUserNotifications(1);
    }
}
