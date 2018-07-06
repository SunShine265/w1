package com.wo1haitao.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.adapters.ProductsGenChatAdapter;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.CustomResponse;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.InboxRs;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.ResponsePacket;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class ChatFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    ActionBarProject my_action_bar;
    ArrayList<InboxRs> arrayOfInbox;
    ProductsGenChatAdapter product_adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView lv_product;
    View fragment_view;

    MyCallBack getListener() {
        if (this.getActivity() instanceof MyCallBack) {
            return (MyCallBack) this.getActivity();
        }
        return null;
    }

    ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ChatFragment.this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        fragment_view = inflater.inflate(R.layout.fragment_chat, container, false);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) fragment_view.findViewById(R.id.swipe_refresh_layout);
        my_action_bar.showTitle(R.string.station_leter);
        arrayOfInbox = new ArrayList<>();
        product_adapter = new ProductsGenChatAdapter(getActivity(), arrayOfInbox, false, R.layout.item_gen_chat);
        lv_product = (ListView) fragment_view.findViewById(R.id.lv_product);
        lv_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    getListener().changeChatDetail(arrayOfInbox.get(i).getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        GetInbox();
        final int[] threshold = {1};
        lv_product.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
//                int count = lv_product.getCount();
//                if (scrollState == SCROLL_STATE_IDLE) {
//                    if (lv_product.getLastVisiblePosition() >= count - 1) {
//                        // Execute LoadMoreDataTask
//                        if (isEndPage == false) {
//                            threshold[0]++;
//                            GetInbox(threshold[0]);
//                        }
//                    }
//                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItem) {
                int topRowVerticalPosition = (lv_product == null || lv_product.getChildCount() == 0) ?
                        0 : lv_product.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(ChatFragment.this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                GetInbox();
            }
        });
        return fragment_view;
    }

    public void GetInbox() {
        swipeRefreshLayout.setRefreshing(true);
        final ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<ArrayList<InboxRs>>> call = ws.actionGetInbox();
        call.enqueue(new Callback<ResponseMessage<ArrayList<InboxRs>>>() {
            @Override
            public void onResponse(Call<ResponseMessage<ArrayList<InboxRs>>> call, Response<ResponseMessage<ArrayList<InboxRs>>> response) {
                try {
                    if (response.body() != null) {
                        ResponseMessage responseMessage = response.body();

                        if (responseMessage.isSuccess()) {
                            ArrayList<InboxRs> tempReponse = new ArrayList();
//                            if (page == 1) {
                            arrayOfInbox = response.body().getData();
                            //arrayOfInbox = SortMessage(arrayOfInbox);
                            product_adapter = new ProductsGenChatAdapter(getActivity(), arrayOfInbox, false, R.layout.item_gen_chat);
                            lv_product.setAdapter(product_adapter);
                            lv_product.setEmptyView(fragment_view.findViewById(R.id.emptyElement));
                            if (arrayOfInbox.size() > 0) {
                                UserProfile user;
                                if (arrayOfInbox.get(0).getPurchaser().getId() == MyPreferences.getID()) {
                                    user = arrayOfInbox.get(0).getPurchaser();
                                } else {
                                    user = arrayOfInbox.get(0).getTenderer();
                                }
                                if (getActivity() instanceof MainActivity) {
                                    MainActivity main = (MainActivity) getActivity();
                                    CustomApp.getInstance().setNum_of_chat(user.getNum_of_unread());
                                    main.SetChat(user.getNum_of_unread());
                                }
                            } else {
                                if (getActivity() instanceof MainActivity) {
                                    MainActivity main = (MainActivity) getActivity();
                                    CustomApp.getInstance().setNum_of_chat(0);
                                    main.SetChat(0);
                                }
                            }
//                                progressDialogChat.dismiss();
//                            } else {
//                                tempReponse = response.body().getData();
//                                if (tempReponse.size() == 0) {
//                                    isEndPage = true;
////                                    Toast.makeText(getActivity(), "End", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    for (int i = 0; i < tempReponse.size(); i++) {
//                                        arrayOfInbox.add(tempReponse.get(i));
//                                    }
//                                    //arrayOfInbox = SortMessage(arrayOfInbox);
//                                }
//                                product_adapter.notifyDataSetChanged();
////                                progressDialogChat.dismiss();
//
//                            }
                            GetProfile();
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
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<ArrayList<InboxRs>>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Utils.OnFailException(t);
            }
        });
    }

    /**
     * Compare 2 item Message follow date
     *
     * @params:
     * @return:
     */
    public static Comparator<InboxRs> CHAT_SORT = new Comparator<InboxRs>() {
        public int compare(InboxRs itemChat1, InboxRs itemChat2) {
            Date date1, date2;
            int result = 0;
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            String strDate1 = "", strDate2 = "";
            try {
                if (itemChat1.getMessages().size() > 0) {
                    strDate1 = itemChat1.getMessages().get(itemChat1.getMessages().size() - 1).getCreated_at();
                }
                if (itemChat2.getMessages().size() > 0) {
                    strDate2 = itemChat2.getMessages().get(itemChat2.getMessages().size() - 1).getCreated_at();
                }
                date1 = inputFormat.parse(strDate1);
                date2 = inputFormat.parse(strDate2);
                if (date1.after(date2)) {
                    result = -1;
                } else {
                    result = 1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return result;
        }
    };

    /**
     * Sort list message follow date
     */
    public ArrayList<InboxRs> SortMessage(ArrayList<InboxRs> list) {
        Collections.sort(list, CHAT_SORT);
        return list;
    }

    @Override
    public void onRefresh() {
        GetInbox();
    }

    public interface MyCallBack {
        public void changeChatDetail(int idMessage);
    }

    /**
     * Get profile
     *
     * @params;
     * @return:
     */

    public void GetProfile() {
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        ws.actionGetUser().enqueue(new Callback<ResponseMessage<UserProfile>>() {
            @Override
            public void onResponse(Call<ResponseMessage<UserProfile>> call, Response<ResponseMessage<UserProfile>> response) {
                try {
                    ResponsePacket response_packet = CustomResponse.getResponseMessage(response);
                    ResponseMessage responseMessage;
                    if (response_packet.isSet()) {
                        responseMessage = response_packet.getResponseMessage();
                        if (response_packet.isSuccess()) {
                            UserProfile userProfile = response.body().getData();
                            if (getActivity() instanceof MainActivity) {
                                MainActivity main = (MainActivity) getActivity();
                                CustomApp.getInstance().setNum_of_chat(userProfile.getNum_of_unread());
                                main.SetChat(userProfile.getNum_of_unread());
                            }
                        } else {
                            try {
                                ErrorMessage error = responseMessage.getErrors();
                                Toast.makeText(getActivity(), error.getStringError(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                    Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                Utils.OnFailException(t);
            }
        });
    }
}
