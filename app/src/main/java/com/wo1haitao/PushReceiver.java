package com.wo1haitao;

/**
 * Created by goodproductssoft on 10/3/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.wo1haitao.activities.SplashActivity;
import com.wo1haitao.fragments.ChatDetailFragment;
import com.wo1haitao.fragments.ChatFragment;
import com.wo1haitao.fragments.NotificationFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class PushReceiver extends BroadcastReceiver {
    String titleFromServer = "Wo1haitao";

    @Override
    public void onReceive(Context context, Intent intent) {
//        String notificationTitle = "Wo1haitao";
        String notificationText = "Test notification";

        // Attempt to extract the "message" property from the payload: {"message":"Hello World!"}
        if (intent.getStringExtra("message") != null) {
            notificationText = intent.getStringExtra("message");
        }

        // Prepare a notification with vibration, sound and lights
        // when pause or exit app
        if (CustomApp.getInstance() == null || CustomApp.getInstance().getIsOpen() == false) {
            if(intent.getStringExtra("data") != null){
                String data = intent.getStringExtra("data");
                try {
                    JSONObject obj = new JSONObject(data);
                    if (obj.isNull("title") == false) {
                        titleFromServer = obj.get("title").toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.notifycation_app_con)
                    .setContentTitle(titleFromServer)
                    .setContentText(notificationText)
                    .setLights(Color.RED, 1000, 1000)
                    .setVibrate(new long[]{0, 400, 250, 400})
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, SplashActivity.class), PendingIntent.FLAG_CANCEL_CURRENT));

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(149, builder.build());
        }
        // Running app
        else {
//            Toast.makeText(context, String.valueOf(CustomApp.getInstance().getIsOpen()), Toast.LENGTH_SHORT).show();
            if (intent.getStringExtra("data") != null) {
                actionWhenNotify(intent.getStringExtra("data"));
            }

        }
    }


    private void actionWhenNotify(String data) {
        int messageContainerID = 0;
        JSONObject obj = null;
        try {
            obj = new JSONObject(data);
            if (obj.get("message_container_id") != null) {
                messageContainerID = obj.getInt("message_container_id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (CustomApp.getInstance() != null && CustomApp.getInstance().mainActivity != null) {
            final int finalMessageContainerID = messageContainerID;
            CustomApp.getInstance().mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (CustomApp.getInstance().mainActivity.getFragmentManager().findFragmentById(R.id.contentContainer) instanceof NotificationFragment) {
                        if(finalMessageContainerID == 0){
                            NotificationFragment notiFragment = (NotificationFragment) CustomApp.getInstance().mainActivity.getFragmentManager().findFragmentById(R.id.contentContainer);
                            notiFragment.GetUserNotifyHide();
                        }
                        else{
                            CustomApp.getInstance().num_of_chat += 1;
                            CustomApp.getInstance().mainActivity.UpdateNumChat(CustomApp.getInstance().num_of_chat);
                        }

                    } else if (CustomApp.getInstance().mainActivity.getFragmentManager().findFragmentById(R.id.contentContainer) instanceof ChatFragment) {
                        if(finalMessageContainerID == 0){
                            CustomApp.getInstance().num_of_notify += 1;
                            CustomApp.getInstance().mainActivity.UpdateNumNotify(CustomApp.getInstance().num_of_notify);
                        }
                        else{
                            ChatFragment chatFragment = (ChatFragment) CustomApp.getInstance().mainActivity.getFragmentManager().findFragmentById(R.id.contentContainer);
                            chatFragment.GetInbox();
                        }

                    }
                    else if (CustomApp.getInstance().mainActivity.getFragmentManager().findFragmentById(R.id.contentContainer) instanceof ChatDetailFragment){
                        if (finalMessageContainerID != 0) {
                            ChatDetailFragment chatDetail = (ChatDetailFragment) CustomApp.getInstance().mainActivity.getFragmentManager().findFragmentById(R.id.contentContainer);
                            if (chatDetail.messageContainerID == 0 || chatDetail.messageContainerID == finalMessageContainerID) {
                                chatDetail.onRefeshChatDetail();
                            } else {
                                CustomApp.getInstance().num_of_chat += 1;
                                CustomApp.getInstance().mainActivity.UpdateNumChat(CustomApp.getInstance().num_of_chat);
                            }
                        }
                        else{
                            CustomApp.getInstance().num_of_notify += 1;
                            CustomApp.getInstance().mainActivity.UpdateNumNotify(CustomApp.getInstance().num_of_notify);
                        }
                    }
                    else{
                        if(finalMessageContainerID == 0){
                            CustomApp.getInstance().num_of_notify += 1;
                            CustomApp.getInstance().mainActivity.UpdateNumNotify(CustomApp.getInstance().num_of_notify);
                        }
                        else{
                            CustomApp.getInstance().num_of_chat += 1;
                            CustomApp.getInstance().mainActivity.UpdateNumChat(CustomApp.getInstance().num_of_chat);
                        }
                    }
                }
            });
        }
    }


}