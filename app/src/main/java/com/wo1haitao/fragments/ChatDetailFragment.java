package com.wo1haitao.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.adapters.ListChatDetail;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.CustomResponse;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.ResponsePacket;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.controls.CustomEditextSoftkey;
import com.wo1haitao.models.ItemChat;
import com.wo1haitao.models.MessageContainer;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.io.IOException;
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
 * A simple {@link Fragment} subclass.
 */
public class ChatDetailFragment extends BaseFragment {

    ActionBarProject my_action_bar;
    ImageView ivProduct, verifies_user, ivSend;
    TextView tvNumOfOffer, nameProduct, userName;
    public int messageContainerID = 0;
    CustomEditextSoftkey boxchat;
    MessageContainer messageContainer;
    View fragment_view, viewdate;
    ListView lstChat;
    ArrayList<ItemChat> lstItemChat;
    RatingBar ratingBar;
    ListChatDetail listChatDetail;
    int wantToByID = 0, tendererID = 0;
    LinearLayout content_detail_chat, header_chat_detail;

    public int getWantTobuyID() {
        return wantToByID;
    }

    public void setWantTobuyID(int wantTobuyID) {
        this.wantToByID = wantTobuyID;
    }

    public int getTendererID() {
        return tendererID;
    }

    public void setTendererID(int tendererID) {
        this.tendererID = tendererID;
    }

    public int getIdMessage() {
        return messageContainerID;
    }

    public void setIdMessage(int idMessage) {
        this.messageContainerID = idMessage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragment_view = inflater.inflate(R.layout.fragment_chat_detail, container, false);
        ratingBar = (RatingBar) fragment_view.findViewById(R.id.ratingBar);
        lstChat = (ListView) fragment_view.findViewById(R.id.lst_chat);
        boxchat = (CustomEditextSoftkey) fragment_view.findViewById(R.id.box_chat);
        ivProduct = (ImageView) fragment_view.findViewById(R.id.iv_product);
        tvNumOfOffer = (TextView) fragment_view.findViewById(R.id.tv_number_of_offer);
        nameProduct = (TextView) fragment_view.findViewById(R.id.tv_name_product);
        verifies_user = (ImageView) fragment_view.findViewById(R.id.verifies_user);
        ivSend = (ImageView) fragment_view.findViewById(R.id.ivSendMess);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        userName = (TextView) fragment_view.findViewById(R.id.tv_user_name);
        content_detail_chat = (LinearLayout) fragment_view.findViewById(R.id.content_detail_chat);
        header_chat_detail = (LinearLayout) fragment_view.findViewById(R.id.header_chat_detail);
        my_action_bar.showTitle(R.string.station_leter);
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        if (messageContainerID != 0) {
            GetChatDetail();
        } else {
            GetNewChatDetail(wantToByID, tendererID);
        }
        header_chat_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wantToByID != 0) {
                    MainActivity mainActivity = (MainActivity) ChatDetailFragment.this.getActivity();
                    mainActivity.changeDetailProduct(wantToByID, "chatDetail");
                }
            }
        });
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) ChatDetailFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                int flag = 0;
                ScrollListViewBottom();
                String content = boxchat.getText().toString();
                content = content.trim();
                if (!content.isEmpty()) {
                    String create = "";
                    int senderChat = MyPreferences.getID();
                    ItemChat itemNewChat = new ItemChat(content, create, senderChat, flag);
                    lstItemChat.add(itemNewChat);
                    ListChatDetail listChatDetail = new ListChatDetail(ChatDetailFragment.this.getActivity(), lstItemChat);
                    lstChat.setAdapter(listChatDetail);

                    ApiServices apiServices = ApiServices.getInstance();
                    WebService webService = apiServices.getRetrofit().create(WebService.class);
                    long id = 0, messageContainerTenderID = 0, messageContainerPurchaserID = 0;
                    String messagerContent = "";
                    if (messageContainer != null) {
                        messageContainerID = messageContainer.getId();
                        if (messageContainer.getProduct_listing() != null) {
                            id = messageContainer.getProduct_listing().getId();
                            wantToByID = messageContainer.getProduct_listing().getId();
                        }
                        if (!boxchat.getText().toString().equals("")) {
                            messagerContent = content;
                        }
                        if (messageContainer.getTenderer() != null) {
                            tendererID = messageContainer.getTenderer().getId();
                            messageContainerTenderID = messageContainer.getTenderer().getId();
                        }
                        if (messageContainer.getPurchaser() != null) {
                            messageContainerPurchaserID = messageContainer.getPurchaser().getId();
                        }
                    }
                    boxchat.setText("");
                    if (tendererID != 0 && wantToByID != 0
                            && !messagerContent.equals("") && messageContainerTenderID != 0 && messageContainerPurchaserID != 0) {
                        Call<ResponseMessage> call;
                        if (messageContainerID != 0) {
                            call = webService.actionPostMessage(wantToByID, messageContainerID, tendererID, wantToByID, messagerContent, messageContainerTenderID, messageContainerPurchaserID);
                        } else {
                            call = webService.actionPostMessage(wantToByID, tendererID, wantToByID, messagerContent, messageContainerTenderID, messageContainerPurchaserID);
                        }
                        call.enqueue(new Callback<ResponseMessage>() {
                            @Override
                            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                try {
                                    if (response.body() != null && response.body().isSuccess()) {
                                        if (messageContainerID != 0) {
                                            GetChatDetail();
                                        } else {
                                            GetNewChatDetail(wantToByID, tendererID);
                                        }
                                        ScrollListViewBottom();
                                    } else if (response.errorBody() != null) {
                                        try {
                                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                                            ErrorMessage error = responseMessage.getErrors();
                                        } catch (Exception e) {
                                            Crashlytics.logException(e);
                                        }
                                    } else {
                                    }
                                } catch (Exception e) {
                                    Utils.crashLog(e);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                            }
                        });
                    }
                }
            }
        });

        return fragment_view;
    }

    /**
     * Get content chat from API
     *
     * @params:
     * @return:
     */
    public void GetChatDetail() {
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<MessageContainer>> call = ws.actionGetMessage(messageContainerID);
        call.enqueue(new Callback<ResponseMessage<MessageContainer>>() {
            @Override
            public void onResponse(Call<ResponseMessage<MessageContainer>> call, Response<ResponseMessage<MessageContainer>> response) {
                try {
                    if (response.body() != null) {
                        if (response.body().isSuccess()) {
                            messageContainer = new MessageContainer();
                            messageContainer = response.body().getData();
                            ChatDetailFragment.this.setWantTobuyID(messageContainer.getProduct_listing().getId());
                            UserProfile userProfile = new UserProfile();
                            if (messageContainer.getPurchaser().getId() != MyPreferences.getID()) {
                                userProfile = messageContainer.getPurchaser();
                            } else {
                                userProfile = messageContainer.getTenderer();
                            }
//                            pusherChannel = messageContainer.getPusher_channel();
                            messageContainerID = messageContainer.getId();
                            if (messageContainer.getProduct_listing().getProduct_images() != null && messageContainer.getProduct_listing().getProduct_images().size() > 0) {
                                if (messageContainer.getProduct_listing().getProduct_images().get(0).getProduct_image() != null
                                        && !messageContainer.getProduct_listing().getProduct_images().get(0).getProduct_image().getUrl().equals(""))
                                    imageLoader.displayImage(ApiServices.BASE_URI + messageContainer.getProduct_listing().getProduct_images().get(0).getProduct_image().getUrl(), ivProduct);
                            }
                            String strNumOffer = "("+userProfile.getNum_of_reviews()+")";
                            tvNumOfOffer.setText(strNumOffer);
                            if (messageContainer.getProduct_listing() != null) {
                                nameProduct.setText(messageContainer.getProduct_listing().getName());
                            }

                            try {
                                userName.setText(userProfile.getNickname());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                if (userProfile.getVerification_state().equals("closed")) {
                                    verifies_user.setImageDrawable(null);
                                    verifies_user.setBackgroundResource(R.drawable.verified);
                                } else {
                                    verifies_user.setImageDrawable(null);
                                    verifies_user.setBackgroundResource(R.drawable.unverifyimg);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (userProfile != null) {
                                float ratingStar = (float) (userProfile.getAverage_rating());
                                int intpart = (int) ratingStar;
                                float decpart = ratingStar - intpart;
                                if (decpart != 0.0f) {
                                    ratingStar = intpart + 0.5f;
                                }
                                ratingBar.setRating(ratingStar);
                            }
                            lstItemChat = new ArrayList<ItemChat>();
                            int flag = 1;
                            String conent = "", created = "";
                            int sender;
                            for (int i = 0; i < messageContainer.getMessages().size(); i++) {
                                flag = (messageContainer.getMessages().get(i).getSender().getId() != MyPreferences.getID()) ? 1 : -1;
                                conent = messageContainer.getMessages().get(i).getContent();
                                created = messageContainer.getMessages().get(i).getCreated_at();
                                sender = messageContainer.getMessages().get(i).getSender().getId();
                                lstItemChat.add(new ItemChat(conent, created, sender, flag));
                            }
                            lstItemChat = SortMessage(lstItemChat);
                            listChatDetail = new ListChatDetail(ChatDetailFragment.this.getActivity(), lstItemChat);
//                            lstChat.setStackFromBottom(true);
                            lstChat.setAdapter(listChatDetail);
                            ScrollListViewBottom();
                            content_detail_chat.setVisibility(View.VISIBLE);
//                            GetProfile();
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
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<MessageContainer>> call, Throwable t) {
                Utils.OnFailException(t);
            }
        });
    }

    /**
     * Get form chat detail
     */
    public void GetFormChatDetail() {
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<MessageContainer>> call = ws.actionGetMessage(messageContainerID);
        call.enqueue(new Callback<ResponseMessage<MessageContainer>>() {
            @Override
            public void onResponse(Call<ResponseMessage<MessageContainer>> call, Response<ResponseMessage<MessageContainer>> response) {
                try {
                    if (response.body() != null) {
                        if (response.body().isSuccess()) {
                            messageContainer = new MessageContainer();
                            messageContainer = response.body().getData();
//                            pusherChannel = messageContainer.getPusher_channel();
                            messageContainerID = messageContainer.getId();
                            lstItemChat = new ArrayList<ItemChat>();
                            int flag = 1;
                            String conent = "", created = "";
                            int sender;
                            for (int i = 0; i < messageContainer.getMessages().size(); i++) {
                                flag = (messageContainer.getMessages().get(i).getSender().getId() != MyPreferences.getID()) ? 1 : -1;
                                conent = messageContainer.getMessages().get(i).getContent();
                                created = messageContainer.getMessages().get(i).getCreated_at();
                                sender = messageContainer.getMessages().get(i).getSender().getId();
                                lstItemChat.add(new ItemChat(conent, created, sender, flag));
                            }
                            lstItemChat = SortMessage(lstItemChat);
                            listChatDetail = new ListChatDetail(ChatDetailFragment.this.getActivity(), lstItemChat);
//                            lstChat.setStackFromBottom(true);
                            lstChat.setAdapter(listChatDetail);
                            ScrollListViewBottom();
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
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<MessageContainer>> call, Throwable t) {
                Utils.OnFailException(t);
            }
        });
    }

    /**
     * Get content chat for first from API
     *
     * @params: wanttobuyID
     * tenderID
     * @return:
     */
    public void GetNewChatDetail(int wanttobuyID, int tenererID) {
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        Call<ResponseMessage<MessageContainer>> callDeleteProduct = ws.actionGetMessager(wanttobuyID, tenererID);
        callDeleteProduct.enqueue(new Callback<ResponseMessage<MessageContainer>>() {
            @Override
            public void onResponse(Call<ResponseMessage<MessageContainer>> call, Response<ResponseMessage<MessageContainer>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        messageContainer = new MessageContainer();
                        messageContainer = response.body().getData();
                        messageContainerID = messageContainer.getId();
                        UserProfile userProfile = new UserProfile();
                        if (messageContainer.getPurchaser().getId() != MyPreferences.getID()) {
                            userProfile = messageContainer.getPurchaser();
                        } else {
                            userProfile = messageContainer.getTenderer();
                        }
                        if (messageContainer.getProduct_listing().getProduct_images() != null && messageContainer.getProduct_listing().getProduct_images().size() > 0) {
                            if (messageContainer.getProduct_listing().getProduct_images().get(0).getProduct_image() != null
                                    && !messageContainer.getProduct_listing().getProduct_images().get(0).getProduct_image().getUrl().equals(""))
                                imageLoader.displayImage(ApiServices.BASE_URI + messageContainer.getProduct_listing().getProduct_images().get(0).getProduct_image().getUrl(), ivProduct);
                        }
                        String strNumOffer = "("+userProfile.getNum_of_reviews()+")";
                        tvNumOfOffer.setText(strNumOffer);
                        if (messageContainer.getProduct_listing() != null) {
                            nameProduct.setText(messageContainer.getProduct_listing().getName());
                        }

                        try {
                            userName.setText(userProfile.getNickname());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            if (userProfile.getVerification_state().equals("closed")) {
                                verifies_user.setImageDrawable(null);
                                verifies_user.setBackgroundResource(R.drawable.verified);
                            } else {
                                verifies_user.setImageDrawable(null);
                                verifies_user.setBackgroundResource(R.drawable.unverifyimg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (userProfile != null) {
                            ratingBar.setRating(userProfile.getAverage_rating());
                        }
                        lstItemChat = new ArrayList<ItemChat>();
                        int flag = 1;
                        String conent = "", createed = "";
                        int sender;
                        for (int i = 0; i < messageContainer.getMessages().size(); i++) {
                            flag = (messageContainer.getMessages().get(i).getSender().getId() != MyPreferences.getID()) ? 1 : -1;
                            conent = messageContainer.getMessages().get(i).getContent();
                            createed = messageContainer.getMessages().get(i).getCreated_at();
                            sender = messageContainer.getMessages().get(i).getSender().getId();
                            lstItemChat.add(new ItemChat(conent, createed, sender, flag));
                        }
                        lstItemChat = SortMessage(lstItemChat);
                        listChatDetail = new ListChatDetail(ChatDetailFragment.this.getActivity(), lstItemChat);
//                        lstChat.setStackFromBottom(true);
                        lstChat.setAdapter(listChatDetail);
                        ScrollListViewBottom();
                        content_detail_chat.setVisibility(View.VISIBLE);
//                        GetProfile();
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(ChatDetailFragment.this.getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(ChatDetailFragment.this.getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<MessageContainer>> call, Throwable t) {
                Utils.OnFailException(t);
            }
        });
    }

    /**
     * Get form chat detail
     *
     * @params: wanttobuyID
     * tenderID
     * @return:
     */
    public void GetNewChatFormDetail(int wanttobuyID, int tenererID) {
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        Call<ResponseMessage<MessageContainer>> callDeleteProduct = ws.actionGetMessager(wanttobuyID, tenererID);
        callDeleteProduct.enqueue(new Callback<ResponseMessage<MessageContainer>>() {
            @Override
            public void onResponse(Call<ResponseMessage<MessageContainer>> call, Response<ResponseMessage<MessageContainer>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        messageContainer = new MessageContainer();
                        messageContainer = response.body().getData();
                        messageContainerID = messageContainer.getId();
                        lstItemChat = new ArrayList<ItemChat>();
                        int flag = 1;
                        String conent = "", createed = "";
                        int sender;
                        for (int i = 0; i < messageContainer.getMessages().size(); i++) {
                            flag = (messageContainer.getMessages().get(i).getSender().getId() != MyPreferences.getID()) ? 1 : -1;
                            conent = messageContainer.getMessages().get(i).getContent();
                            createed = messageContainer.getMessages().get(i).getCreated_at();
                            sender = messageContainer.getMessages().get(i).getSender().getId();
                            lstItemChat.add(new ItemChat(conent, createed, sender, flag));
                        }
                        lstItemChat = SortMessage(lstItemChat);
                        listChatDetail = new ListChatDetail(ChatDetailFragment.this.getActivity(), lstItemChat);
                        lstChat.setAdapter(listChatDetail);
                        ScrollListViewBottom();
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(ChatDetailFragment.this.getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(ChatDetailFragment.this.getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<MessageContainer>> call, Throwable t) {
                Utils.OnFailException(t);
            }
        });
    }

    /**
     * Scroll listiview to bottom list chat
     */
    public void ScrollListViewBottom() {
        lstChat.post(new Runnable() {
            public void run() {
                lstChat.setSelection(lstChat.getCount() - 1);
            }
        });
    }

    public void onRefeshChatDetail() {
        if (messageContainerID != 0) {
            GetFormChatDetail();
        } else if (wantToByID != 0 && tendererID != 0) {
            GetNewChatFormDetail(wantToByID, tendererID);
        }
    }

    /**
     * Get chat from channel
     * @params:
     * @return:
     * */
//    public void GetChatFromChanel() {
//        HttpAuthorizer authorizer = new HttpAuthorizer("https://www.wo1haitao.com/api/v1/pusher/auth");
//        PusherAndroidOptions options = new PusherAndroidOptions();
//        options.setCluster("ap1");
//        HashMap<String, String> authHeader = new HashMap<>();
//        authHeader.put("Uid", MyPreferences.getUID());
//        authHeader.put("Access-Token", MyPreferences.getToken());
//        authHeader.put("Client", MyPreferences.getClient());
//        authHeader.put("Expiry", MyPreferences.getExpiryField());
//        authorizer.setHeaders(authHeader);
//        options.setAuthorizer(authorizer);
//
//        final PusherAndroid pusher = new PusherAndroid("da3d71cab30eb462c7f4", options);
//        pusher.connect(new ConnectionEventListener() {
//            @Override
//            public void onConnectionStateChange(ConnectionStateChange connectionStateChange) {
//                if (connectionStateChange.getCurrentState() == ConnectionState.CONNECTED) {
//                    String channel = "private-" + pusherChannel;
//                    pusher.subscribePrivate(channel, new PresenceChannelEventListener() {
//                        @Override
//                        public void onUsersInformationReceived(String s, Set<User> set) {
//                        }
//
//                        @Override
//                        public void userSubscribed(String s, User user) {
//
//                        }
//
//                        @Override
//                        public void userUnsubscribed(String s, User user) {
//
//                        }
//
//                        @Override
//                        public void onAuthenticationFailure(String s, Exception e) {
//
//                        }
//
//                        @Override
//                        public void onSubscriptionSucceeded(String s) {
//                            MyPreferences.setKeySubscribe(pusherChannel);
//                        }
//
//                        @Override
//                        public void onEvent(String s, String s1, String s2) {
//                            if (messageContainerID != 0) {
//                                GetFormChatDetail();
//                            } else {
//                                GetNewChatFormDetail(wantToByID, tendererID);
//                            }
//                        }
//                    }, "send_message");
//                }
//            }
//            @Override
//            public void onError(String s, String s1, Exception e) {
//                String s3 = "1";
//            }
//        });
//    }

    /**
     * Compare 2 item Message follow date
     *
     * @params:
     * @return:
     */
    public static  Comparator<ItemChat> CHAT_SORT = new Comparator<ItemChat>() {
        public int compare(ItemChat itemChat1, ItemChat itemChat2) {
            Date date1, date2;
            int result = 0;
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            String strDate1, strDate2;
            strDate1 = itemChat1.getCreated_at();
            strDate2 = itemChat2.getCreated_at();
            try {
                date1 = inputFormat.parse(strDate1);
                date2 = inputFormat.parse(strDate2);
                if (date1.after(date2)) {
                    result = 1;
                } else {
                    result = -1;
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
    public ArrayList<ItemChat> SortMessage(ArrayList<ItemChat> list) {
        Collections.sort(list, CHAT_SORT);
        return list;
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
