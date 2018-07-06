package com.wo1haitao.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.CustomResponse;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.ResponsePacket;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.controls.RoundedImageView;
import com.wo1haitao.dialogs.PopupMenuCustom;
import com.wo1haitao.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG_DEFAULT;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class ProfileFragment extends BaseFragment {

    ActionListener getListener(){
        if (this.getActivity() instanceof ActionListener) {
            return (ActionListener) this.getActivity();
        }
        return null;
    };
    ImageView iv_vrf, iv_favorite, iv_address, iv_update_profile, iv_mypost, iv_bid, iv_all_eval, iv_fback, iv_inbox, is_verified, bg_profile;
    View.OnTouchListener changeColorTouch;
    ProgressDialog pd_dialog;
    TextView tv_name_us, num_of_reviews;
    String flag = "Profile";
    RatingBar ratingBar;
    UserProfile user;
    boolean isEnableUserSetting = false;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view_fragment = inflater.inflate(R.layout.fragment_profile, container, false);
        changeColorTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView iv = (ImageView) view;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    iv.setColorFilter(ContextCompat.getColor(getActivity(), R.color.black));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    iv.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                }
                return false;
            }
        };
        ProfileFragment.this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Utils.setTitle(view_fragment, R.string.title_profile);
        final ImageView iv_setting = (ImageView) view_fragment.findViewById(R.id.iv_setting);
        iv_setting.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        final LinearLayout ll_setting = (LinearLayout) view_fragment.findViewById(R.id.layout_setting);
        ll_setting.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        num_of_reviews = (TextView)view_fragment.findViewById(R.id.num_of_reviews);
        ratingBar = (RatingBar) view_fragment.findViewById(R.id.ratingBar);
        final PopupWindow popupWindow = PopupMenuCustom.getPopupWindowSettingProfile(getActivity(), iv_setting, ll_setting);
        final boolean[] check = {false};
        bg_profile = (ImageView) view_fragment.findViewById(R.id.bg_profile);
        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.profile_background_user, bg_profile);
        //profile_background_user
        ll_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEnableUserSetting = true){
                    if(check[0] == false) {
                        popupWindow.showAsDropDown(ll_setting, 0, 0);
                        iv_setting.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.profile_menu));
                        ll_setting.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.profile_menu));
                    }
                    else {
                        iv_setting.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                        ll_setting.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                        popupWindow.dismiss();
                    }
                    check[0] = !check[0];
                    TextView tv_version = (TextView) popupWindow.getContentView().findViewById(R.id.tvVersion);
                    try {
                        PackageInfo pInfo = CustomApp.getInstance().getPackageManager().getPackageInfo(CustomApp.getInstance().getPackageName(), 0);
                        String version = "v"+pInfo.versionName;
                        tv_version.setText(version);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    TextView tv_webchat = (TextView) popupWindow.getContentView().findViewById(R.id.tv_webchat);
                    tv_webchat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            MainActivity mainActivity = (MainActivity) ProfileFragment.this.getActivity();
                            QRWebchatFragment fragment = new QRWebchatFragment();
                            mainActivity.changeFragment(fragment, true);

                        }
                    });
                    TextView tvSetting = (TextView) popupWindow.getContentView().findViewById(R.id.tvTitle);
                    tvSetting.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            getListener().logOut();

                        }
                    });
                    TextView tvTerm = (TextView) popupWindow.getContentView().findViewById(R.id.tvTerm);
                    tvTerm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            MainActivity mainActivity = (MainActivity) ProfileFragment.this.getActivity();
                            InformationTermFragment fragment = new InformationTermFragment();
                            fragment.setFlag("Terms");
                            mainActivity.changeFragment(fragment, true);
                        }
                    });
                    TextView tvCondition = (TextView) popupWindow.getContentView().findViewById(R.id.tvCondition);
                    tvCondition.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            MainActivity mainActivity = (MainActivity) ProfileFragment.this.getActivity();
                            InformationTermFragment fragment = new InformationTermFragment();
                            fragment.setFlag("Conditions");
                            mainActivity.changeFragment(fragment, true);
                        }
                    });

                    TextView tv_change_password = (TextView) popupWindow.getContentView().findViewById(R.id.tv_change_password);
                    tv_change_password.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            MainActivity mainActivity = (MainActivity) ProfileFragment.this.getActivity();
                            ChangePasswordFragment fragment = new ChangePasswordFragment();

                            mainActivity.changeFragment(fragment, true);
                        }
                    });
                    TextView tv_change_alipay = (TextView) popupWindow.getContentView().findViewById(R.id.tv_change_alipay);
                    if(user != null){
                        if(user.getAlipay_id().isEmpty() || user.getAlipay_name().isEmpty()){
                            tv_change_alipay.setText("! 我的支付宝账户信息");
                        }
                        else{
                            tv_change_alipay.setText("我的支付宝账户信息");
                        }
                    }
                    tv_change_alipay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            MainActivity mainActivity = (MainActivity) ProfileFragment.this.getActivity();
                            ChangeAlipayFragment fragment = new ChangeAlipayFragment();
                            if(user != null){
                                fragment.alipay_name = user.getAlipay_name();
                                fragment.alipay_id = user.getAlipay_id();
                            }
                            mainActivity.changeFragment(fragment, true);
                        }
                    });
                }

            }
        });
        final RoundedImageView iv_avt = (RoundedImageView) view_fragment.findViewById(R.id.riv_avatar);
        is_verified = (ImageView) view_fragment.findViewById(R.id.is_verified);
        iv_vrf = (ImageView) view_fragment.findViewById(R.id.iv_ed_vrf);
        iv_vrf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getListener() != null && user != null) {
                    getListener().changeVerify();
                }
            }
        });
        iv_vrf.setOnTouchListener(changeColorTouch);
        iv_favorite = (ImageView) view_fragment.findViewById(R.id.iv_fav);
        iv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListener().changeFavorite();
            }
        });
        iv_favorite.setOnTouchListener(changeColorTouch);
        iv_address = (ImageView) view_fragment.findViewById(R.id.iv_address);
        iv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListener().changeAddress();
            }
        });
        iv_address.setOnTouchListener(changeColorTouch);
        iv_update_profile = (ImageView) view_fragment.findViewById(R.id.iv_ed_profile);
        iv_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListener().openEditProfile();
            }
        });
        iv_update_profile.setOnTouchListener(changeColorTouch);
        iv_mypost = (ImageView) view_fragment.findViewById(R.id.iv_mypost);
        iv_mypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListener().changeMyPost(flag);
            }
        });
        iv_mypost.setOnTouchListener(changeColorTouch);
        iv_bid = (ImageView) view_fragment.findViewById(R.id.iv_bid);
        iv_bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListener().changeMyBids();
            }
        });
        iv_bid.setOnTouchListener(changeColorTouch);
        iv_all_eval = (ImageView) view_fragment.findViewById(R.id.iv_all_eva);
        iv_all_eval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListener().changeAllEval();
            }
        });
        iv_all_eval.setOnTouchListener(changeColorTouch);
        iv_inbox = (ImageView) view_fragment.findViewById(R.id.iv_inbox);
        iv_inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListener().changeChat();
            }
        });
        iv_inbox.setOnTouchListener(changeColorTouch);
        pd_dialog = Utils.getPGDialog(getActivity());
        tv_name_us = (TextView) view_fragment.findViewById(R.id.tv_name_us);
        isEnableUserSetting = false;
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        ws.actionGetUser().enqueue(new Callback<ResponseMessage<UserProfile>>() {
            @Override
            public void onResponse(Call<ResponseMessage<UserProfile>> call, Response<ResponseMessage<UserProfile>> response) {
                try {
                    pd_dialog.dismiss();
                    isEnableUserSetting = true;
                    ResponsePacket response_packet = CustomResponse.getResponseMessage(response);
                    ResponseMessage responseMessage;
                    if (response_packet.isSet()) {
                        responseMessage = response_packet.getResponseMessage();
                        if (response_packet.isSuccess()) {
                            if (responseMessage.isSuccess()) {
                                user = (UserProfile) responseMessage.getData();
                                if(user.getVerification_state().equals("closed")){
                                    is_verified.setBackgroundResource(R.drawable.verified);
                                }
                                else {
                                    is_verified.setBackgroundResource(R.drawable.unverifyimg);
                                }
//                                iv_update_profile.setBackground(null);
//                                if(user.getAlipay_name() != null && user.getAlipay_id() != null) {
//                                    if (!user.getAlipay_id().toString().equals("") && !user.getAlipay_name().toString().equals("")) {
//                                        iv_update_profile.setImageResource(R.drawable.ic_p_profile_update);
//                                    } else {
//                                        iv_update_profile.setImageResource(R.drawable.profile_update_notify);
//                                    }
//                                }
//                                else {
//                                    iv_update_profile.setImageResource(R.drawable.profile_update_notify);
//                                }
                                float ratingStar= (float)(user.getAverage_rating());
                                int intpart = (int)ratingStar;
                                float decpart = ratingStar - intpart;
                                if(decpart != 0.0f)
                                {
                                    ratingStar = intpart + 0.5f;
                                }
                                ratingBar.setRating(ratingStar);

                                if(user.getNum_of_reviews() != null){
                                    num_of_reviews.setText("(" + user.getNum_of_reviews().toString() + ")");
                                }
                                tv_name_us.setText(user.getNickname());
                                if (user.getProfile_picture() != null ) {
                                    if(user.getProfile_picture().getUrl() != null) {
                                        String url_avt = user.getProfile_picture().getUrl();
                                        if (url_avt != null && !url_avt.equals("")) {
//                                            DiskCacheUtils.removeFromCache(url_avt, ImageLoader.getInstance().getDiskCache());
//                                            MemoryCacheUtils.removeFromCache(url_avt, ImageLoader.getInstance().getMemoryCache());
                                            ImageLoader il = ImageLoader.getInstance();
                                            il.displayImage(url_avt, iv_avt, OPTION_DISPLAY_IMG_DEFAULT);
                                        }
                                    }
                                }
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
                        getListener().logOut();
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                Utils.OnFailException(t);
                if(pd_dialog!= null) {
                    pd_dialog.dismiss();
                }
            }
        });
        iv_fback = (ImageView) view_fragment.findViewById(R.id.iv_fback);
        iv_fback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListener().changeFBack();
            }
        });
        iv_fback.setOnTouchListener(changeColorTouch);
        return view_fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    public interface ActionListener {
        void openEditProfile();

        void logOut();

        void changeVerify();

        void changeFavorite();

        void changeAddress();

        void changeMyPost(String flag);

        void changeMyBids();

        void changeAllEval();

        void changeFBack();

        void changeChat();
    }


}
