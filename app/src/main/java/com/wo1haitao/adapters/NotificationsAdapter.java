package com.wo1haitao.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.controls.RoundedImageView;
import com.wo1haitao.models.NotificationModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG;
import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG_PRODUCT;

/**
 * Created by goodproductssoft on 4/20/17.
 */

public class NotificationsAdapter extends BaseAdapter<NotificationModel> {
    Boolean sortView;
    int layout_id;
    Activity context;
    int short_dimention = 0;
    final static String OFFER_RISED = "noti_offer_raised";
    final static String REOFFER_RISED = "noti_reoffer_raised";
    final static String OFFER_COUNTER = "noti_offer_counter";
    final static String OFFER_REJECTED = "noti_offer_rejected";
    final static String OFFER_ACCEEPTED = "noti_offer_accepted";
    final static String ORDER_CONFIRMED = "noti_order_confirmed";
    final static String INVOICE_ACCEPTED = "noti_invoice_accepted";
    final static String INVOICE_REJECTED = "noti_invoice_rejected";
    final static String ACCOUNT_VERIFY_REJECTED = "noti_account_verification_rejected";
    final static String ACCOUNT_VERIFY_CLOSED = "noti_account_verification_closed";
    final static String QUESTIONCREATED = "noti_question_created";
    final static String QUESTIONANSWERCREATED = "noti_question_answer_created";

    public NotificationsAdapter(Activity activity, ArrayList<NotificationModel> productModels, Boolean sortView, int layout_id) {
        super(activity, 0, productModels);
        this.sortView = sortView;
        this.layout_id = layout_id;
        this.context = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        NotificationModel notificationModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        final HolderViewNotification holderView;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(layout_id, parent, false);
            holderView = new HolderViewNotification();
            holderView.tv_update_at = (TextView) convertView.findViewById(R.id.tv_update_at);
            holderView.nameProduct = (TextView) convertView.findViewById(R.id.tv_name_product);
            holderView.nameUser = (TextView) convertView.findViewById(R.id.tv_name_user);
            holderView.imageProduct = (ImageView) convertView.findViewById(R.id.iv_product);
            holderView.imageUser = (ImageView) convertView.findViewById(R.id.iv_user);
            holderView.state = (TextView) convertView.findViewById(R.id.tv_state);
            holderView.iv_verifies_user = (ImageView) convertView.findViewById(R.id.iv_verifies_user);
            holderView.iv_user = (RoundedImageView) convertView.findViewById(R.id.iv_user);
            holderView.ln_state = (LinearLayout) convertView.findViewById(R.id.ln_state);
            holderView.tv_username_profile = (TextView) convertView.findViewById(R.id.tv_user_name_profile);

            convertView.setTag(holderView);
        }
        else{
            holderView = (HolderViewNotification) convertView.getTag();
            if(holderView == null || holderView.state == null){
                return convertView;
            }
        }


        WindowManager manager =  (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        final Point point = new Point();
        display.getSize(point);
        short_dimention = point.x > point.y ? point.y : point.x;
        holderView.tv_update_at.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


                float scale_by_screen = (float) 1;
                if(short_dimention < 800){
                    scale_by_screen = (float) 0.575;
                }
                else if (short_dimention < 1200){
                    scale_by_screen = (float) 0.8;
                }
//        float scale_by_screen_size = 1;
//        if(CustomApp.getInstance().getInch() >= 9){
//            scale_by_screen_size = (float) 0.8;
//        }
//        else if(CustomApp.getInstance().getInch() >= 6){
//            scale_by_screen_size = (float) 0.9;
//        }
                int new_size = (int) (scale_by_screen*270) + 1;
                ViewGroup.LayoutParams layoutParams = holderView.tv_update_at.getLayoutParams();
                layoutParams.width = new_size;
                holderView.tv_update_at.setLayoutParams(layoutParams);
            }
        });
        try {
            if (notificationModel.getNotice_type().equals(ACCOUNT_VERIFY_REJECTED)
                    || notificationModel.getNotice_type().equals(ACCOUNT_VERIFY_CLOSED)) {
                holderView.nameProduct.setText("帐户验证");
                holderView.nameUser.setText("");
                holderView.imageProduct.setImageResource(R.drawable.notifycation_app_con);
                holderView.imageUser.setImageResource(R.drawable.photo);
            } else {
                holderView.nameProduct.setText(notificationModel.getProduct_listing().getName());
                holderView.nameUser.setText("");
                if(notificationModel.getNotified_by().getNickname().isEmpty() == false){
                    holderView.tv_username_profile.setText(notificationModel.getNotified_by().getNickname());
                }
                else{
                    holderView.tv_username_profile.setText("客户服务");
                }
                imageLoader.displayImage(notificationModel.getNotified_by().getProfile_picture().getUrl(), holderView.imageUser, OPTION_DISPLAY_IMG);
                imageLoader.displayImage(ApiServices.BASE_URI + notificationModel.getProduct_listing().getProduct_images().get(0).getProduct_image().getUrl(), holderView.imageProduct, OPTION_DISPLAY_IMG_PRODUCT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String noticeType = "";
        try {
            noticeType = notificationModel.getNotice_type();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (noticeType != null && !noticeType.equals("")) {
            switch (noticeType) {
                case OFFER_RISED:
                    holderView.state.setText(R.string.offer_raised);
//                    state.setTextColor(ContextCompat.getColor(context, R.color.orange_color));
                    holderView.ln_state.setBackgroundResource(R.drawable.label_border_radius_notify_orange);
                    break;
                case REOFFER_RISED:
                    holderView.state.setText(R.string.reoffer_raised);
//                    state.setTextColor(ContextCompat.getColor(context, R.color.orange_color));
                    holderView.ln_state.setBackgroundResource(R.drawable.label_border_radius_notify_orange);
                    break;
                case OFFER_REJECTED:
                    holderView.state.setText(R.string.offer_rejected);
//                    state.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    holderView.ln_state.setBackgroundResource(R.drawable.label_border_radius_notify_red);
                    break;
                case OFFER_ACCEEPTED:
                    holderView.state.setText(R.string.offer_accepted);
//                    state.setTextColor(ContextCompat.getColor(context, R.color.green_color));
                    holderView.ln_state.setBackgroundResource(R.drawable.label_border_radius_notify_green);
                    break;
                case OFFER_COUNTER:
                    holderView.state.setText(R.string.offer_counter);
//                    state.setTextColor(ContextCompat.getColor(context, R.color.orange_color));
                    holderView.ln_state.setBackgroundResource(R.drawable.label_border_radius_notify_orange);
                    break;
                case ORDER_CONFIRMED:
                    holderView.state.setText(R.string.order_confirmed);
//                    state.setTextColor(ContextCompat.getColor(context, R.color.green_color));
                    holderView.ln_state.setBackgroundResource(R.drawable.label_border_radius_notify_green);
                    break;
                case INVOICE_REJECTED:
                    holderView.state.setText(R.string.invoice_rejected);
//                    state.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    holderView.ln_state.setBackgroundResource(R.drawable.label_border_radius_notify_red);
                    break;
                case INVOICE_ACCEPTED:
                    holderView.state.setText(R.string.invoice_accepted);
//                    state.setTextColor(ContextCompat.getColor(context, R.color.green_color));
                    holderView.ln_state.setBackgroundResource(R.drawable.label_border_radius_notify_green);
                    break;
                case ACCOUNT_VERIFY_REJECTED:
                    holderView.state.setText(R.string.account_verify_rejected);
//                    state.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    holderView.ln_state.setBackgroundResource(R.drawable.label_border_radius_notify_red);
                    break;
                case ACCOUNT_VERIFY_CLOSED:
                    holderView.state.setText(R.string.account_verify_closed);
//                    state.setTextColor(ContextCompat.getColor(context, R.color.green_color));
                    holderView.ln_state.setBackgroundResource(R.drawable.label_border_radius_notify_green);
                    break;
                case QUESTIONCREATED:
                    holderView.state.setText(R.string.question_answer);
//                    state.setTextColor(ContextCompat.getColor(context, R.color.orange_color));
                    holderView.ln_state.setBackgroundResource(R.drawable.label_border_radius_notify_green);
                    break;
                case QUESTIONANSWERCREATED:
                    holderView.state.setText(R.string.reply_question_answer);
//                    state.setTextColor(ContextCompat.getColor(context, R.color.orange_color));
                    holderView.ln_state.setBackgroundResource(R.drawable.label_border_radius_notify_orange);
                    break;
            }
        }

        try {
            holderView.iv_verifies_user.setBackground(null);
            if (notificationModel.getNotified_by().getVerification_state().equals("closed")) {
                holderView.iv_verifies_user.setImageDrawable(null);
                holderView.iv_verifies_user.setBackgroundResource(R.drawable.green_icon);
            } else {
                holderView.iv_verifies_user.setImageDrawable(null);
                holderView.iv_verifies_user.setBackgroundResource(R.drawable.yellow_icon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            String myStringCreate = "", myStringCurrent = "";
            myStringCreate = notificationModel.getCreated_at();
            myStringCurrent = notificationModel.getCurrent_time();

            Date myDateCreate = null, myDateCurrent = null;
            myDateCreate = inputFormat.parse(myStringCreate);
            myDateCurrent = inputFormat.parse(myStringCurrent);
//            formatDateCreate = outputFormat.format(myDateCreate);
//            formatDateCurrent = outputFormat.format(myDateCurrent);
//            myDateCreateFormat = outputFormat.parse(formatDateCreate);
//            myDateCurrentFormat = outputFormat.parse(formatDateCurrent);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(myDateCurrentFormat);
//            int hour = cal.get(Calendar.HOUR_OF_DAY);
//            long temp;
//            if(myDateCurrent.getHours() < myDateCreate.getHours()){
//                temp = myDateCurrent.getTime() + 24*60*60*1000;
//            }
//            else {
//                temp = myDateCurrent.getTime();
//            }
            long remainningTime = myDateCurrent.getTime() - myDateCreate.getTime();
            long hours, min, days;
            days = (int) (remainningTime / (1000 * 60 * 60 * 24));
            hours = (int) ((remainningTime - (Math.abs(1000 * 60 * 60 * 24 * days))) / (1000 * 60 * 60));
            min = (int) (remainningTime - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            if (min < 1) {
                min = 1;
            }
            String timing = "";
            if (min > 0) {
                timing = min + " 分钟前 ";
                if (hours > 0) {
                    timing = hours + " 个小时前 ";
                    if (days > 0) {
                        timing = days + " 天前 ";
                    }
                }
            }
            holderView.tv_update_at.setText(notificationModel.getCreated_at_in_words());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (notificationModel.getProduct_listing() != null) {
            int day;
            Date date = Calendar.getInstance().getTime();
            Date expert = notificationModel.getProduct_listing().getExpired_date();
            if (expert != null) {
                long restDatesinMillis = expert.getTime() - date.getTime();
                day = (int) (restDatesinMillis / (24 * 60 * 60 * 1000));
                if (day < 0) {
                    holderView.state.setText(CustomApp.getInstance().getString(R.string.date_limit_notification, Math.abs(day)));
                    holderView.ln_state.setBackgroundResource(R.drawable.label_border_radius_notify_orange);
                }
            }
        }
        return convertView;
    }
}

class HolderViewNotification {
    public TextView nameProduct, nameUser, state, tv_update_at, tv_username_profile;
    public ImageView imageProduct, imageUser, iv_verifies_user;
    public RoundedImageView iv_user;
    public LinearLayout ln_state;
}
