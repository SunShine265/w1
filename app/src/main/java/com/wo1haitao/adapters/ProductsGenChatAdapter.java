package com.wo1haitao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.response.InboxRs;
import com.wo1haitao.api.response.MessageRs;
import com.wo1haitao.controls.RoundedImageView;
import com.wo1haitao.utils.MyPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG;
import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG_PRODUCT;

/**
 * Created by goodproductssoft on 4/20/17.
 */

public class ProductsGenChatAdapter extends BaseAdapter<InboxRs> {
    Boolean sortView;
    int layout_id;
    Context context;
    //ArrayList<OfferMeModel> chat;
    ArrayList<InboxRs> chat;

    public ProductsGenChatAdapter(Context context, ArrayList<InboxRs> offerMeModels, Boolean sortView, int layout_id) {
        super(context, 0, offerMeModels);
        this.sortView = sortView;
        this.layout_id = layout_id;
        this.context = context;
        chat = offerMeModels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MessageRs ms;
        HolderGenChat holderView;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(layout_id, parent, false);
            holderView = new HolderGenChat();

            holderView.tv_last_time = (TextView) convertView.findViewById(R.id.tv_last_time);
            holderView.tvLastChat = (TextView) convertView.findViewById(R.id.tvLastChat);
            holderView.image_user = (RoundedImageView) convertView.findViewById(R.id.image_user);
            holderView.iv_product = (ImageView) convertView.findViewById(R.id.iv_product);
            holderView.verifies_user = (ImageView) convertView.findViewById(R.id.verifies_user);
            holderView.name_product = (TextView) convertView.findViewById(R.id.name_product);
            holderView.name_user = (TextView) convertView.findViewById(R.id.name_user);

            convertView.setTag(holderView);
        }
        else{
            holderView = (HolderGenChat) convertView.getTag();
            if(holderView == null || holderView.tv_last_time == null){
                return convertView;
            }
        }
        // OfferMeModel offerMeModel = getItem(position);
        InboxRs inbox = chat.get(position);
        // Check if an existing view is being reused, otherwise inflate the view




//        String formatDate = "";
//        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
//        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
//        try {
//            String myStringDate = inbox.getMessages().get(inbox.getMessages().size() - 1).getCreated_at();
//            Date myDate = null;
//            myDate = inputFormat.parse(myStringDate);
//            formatDate = outputFormat.format(myDate);
//            tv_last_time.setText(formatDate);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        String formatDateCreate = "", formatDateCurret = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
//        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy - HH:mm");
        try {
            String myStringCreate = "", myStringCurrent="";
            myStringCreate = inbox.getMessages().get(0).getCreated_at();
            myStringCurrent = inbox.getMessages().get(0).getCurrent_time();

            Date myDateCreate = null, myDateCurrent = null, myDateCreateFormat = null, myDateCurrentFormat = null;
            myDateCreate = inputFormat.parse(myStringCreate);
            myDateCurrent = inputFormat.parse(myStringCurrent);

            long remainningTime = myDateCurrent.getTime() - myDateCreate.getTime();
            int hours = 0, min = 0, days = 0;
            days = (int) (remainningTime / (1000*60*60*24));
            hours = (int) ((remainningTime - (1000*60*60*24*days)) / (1000*60*60));
            min = (int) (remainningTime - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            if(min < 1){
                min = 1;
            }
            String timing = "";
            if(min > 0){
                timing = min + " 分钟前 ";
                if(hours > 0){
                    timing = hours + " 个小时前 ";
                    if(days > 0)
                    {
                        timing = days + " 天前 ";
                    }
                }
            }
            holderView.tv_last_time.setText(inbox.getMessages().get(0).getCreated_at_in_words());
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            imageLoader.displayImage(inbox.getTenderer().getProfile_picture().getUrl(),  holderView.image_user, OPTION_DISPLAY_IMG);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            imageLoader.displayImage(ApiServices.BASE_URI + inbox.getProduct_listing().getProduct_images().get(0).getProduct_image().getUrl(),  holderView.iv_product, OPTION_DISPLAY_IMG_PRODUCT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (inbox.getProduct_listing() != null) {
                holderView.name_product.setText(inbox.getProduct_listing().getName());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(inbox.getPurchaser().getId() != MyPreferences.getID()) {
                holderView.name_user.setText(inbox.getPurchaser().getNickname());
                holderView.verifies_user.setBackground(null);
                if (inbox.getPurchaser().getVerification_state().equals("closed")) {
                    holderView.verifies_user.setBackgroundResource(R.drawable.green_icon);
                } else {
                    holderView. verifies_user.setBackgroundResource(R.drawable.yellow_icon);
                }
            }
            else {
                holderView.name_user.setText(inbox.getTenderer().getNickname());
                holderView.verifies_user.setBackground(null);
                if (inbox.getTenderer().getVerification_state().equals("closed")) {
                    holderView.verifies_user.setBackgroundResource(R.drawable.green_icon);
                } else {
                    holderView.verifies_user.setBackgroundResource(R.drawable.yellow_icon);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

//        try{
//            try {
//                verifies_user.setBackground(null);
//                if (inbox.getTenderer().getVerification_state().equals("closed")) {
//                    verifies_user.setBackgroundResource(R.drawable.green_icon);
//                } else {
//                    verifies_user.setBackgroundResource(R.drawable.yellow_icon);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        try {
            if (inbox.getMessages() != null && inbox.getMessages().size() > 0) {
                ms = inbox.getMessages().get(0);//inbox.getMessages().size() - 1
                holderView.tvLastChat.setText(ms.getContent());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}

class HolderGenChat {
    public TextView name_product, name_user;
    public TextView tvLastChat, tv_last_time;
    public ImageView iv_product, verifies_user;
    public RoundedImageView image_user;
}
