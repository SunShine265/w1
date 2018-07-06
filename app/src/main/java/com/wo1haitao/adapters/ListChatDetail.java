package com.wo1haitao.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wo1haitao.R;
import com.wo1haitao.models.ItemChat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by user on 7/10/2017.
 */

public class ListChatDetail extends BaseAdapter<ItemChat> {
    ArrayList<ItemChat> itemChats;

    public ListChatDetail(Activity context, ArrayList<ItemChat> itemChats) {
        super(context, 0, itemChats);
        this.itemChats = itemChats;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemLeft = ((LayoutInflater) ListChatDetail.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_chat_left, null, false);
        View itemRight = ((LayoutInflater) ListChatDetail.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_chat_right, null, false);
        View itemWaitting = ((LayoutInflater) ListChatDetail.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_chat_waitting, null, false);
        TextView content, date;
        // Get the data item for this position
        ItemChat itemChat = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_chat, parent, false);
        }

        if(itemChat.isFlag() == -1){
            content = (TextView) itemRight.findViewById(R.id.tv_content);
            date = (TextView) itemRight.findViewById(R.id.tv_date);
            content.setText(itemChat.getContent());
            if(itemChat.getCreated_at() != null){
                String formatDate = "";
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
                inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy - HH:mm");
                outputFormat.setTimeZone(TimeZone.getDefault());
                String myStringDate = itemChat.getCreated_at();

                Date myDate = null;
                try {
                    myDate = inputFormat.parse(myStringDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                formatDate = outputFormat.format(myDate);
                date.setText(formatDate);
            }

            return  itemRight;
        }
        else if(itemChat.isFlag() == 1) {
            content = (TextView) itemLeft.findViewById(R.id.tv_content);
            date = (TextView) itemLeft.findViewById(R.id.tv_date);
            content.setText(itemChat.getContent());
            if(itemChat.getCreated_at() != null){
                String formatDate = "";
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
                inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy - HH:mm");
                String myStringDate = itemChat.getCreated_at();

                Date myDate = null;
                try {
                    myDate = inputFormat.parse(myStringDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                formatDate = outputFormat.format(myDate);
                date.setText(formatDate);
            }

            return itemLeft;
        }
        else {
            content = (TextView) itemWaitting.findViewById(R.id.tv_content);
            content.setText(itemChat.getContent());
            return itemWaitting;
        }
    }

    public void add(ItemChat itemChat){
        itemChats.add(itemChat);
        notifyDataSetChanged();
    }
}
