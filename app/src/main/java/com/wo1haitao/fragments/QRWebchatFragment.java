package com.wo1haitao.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.R;
import com.wo1haitao.views.ActionBarProject;

/**
 * A simple {@link Fragment} subclass.
 */
public class QRWebchatFragment extends BaseFragment {
    ActionBarProject my_action_bar;
    ImageView iv_qrcode;
    public static final String Image_Link_QRCode = "https://www.wo1haitao.com/images/wechat-qr-code.jpg";
    public QRWebchatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment_view = inflater.inflate(R.layout.fragment_qrwebchat, container, false);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        iv_qrcode = (ImageView) fragment_view.findViewById(R.id.iv_Qrwebchat);
        my_action_bar.showTitle("微信联络");
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPress();
            }
        });
        ImageLoader.getInstance().displayImage(Image_Link_QRCode,iv_qrcode);
        return fragment_view;
    }

}
