package com.wo1haitao.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.R;
import com.wo1haitao.views.ActionBarProject;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class HomeFragment extends BaseFragment{
    FragmentChangeBuyer getChangeBuyerListenner(){
        if (this.getActivity() instanceof FragmentChangeBuyer) {
            return (FragmentChangeBuyer) this.getActivity();
        }
        return null;
    };
    ActionBarProject my_action_bar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView iv_buyer = (ImageView) view.findViewById(R.id.iv_buyer);
        ImageView iv_purchase = (ImageView) view.findViewById(R.id.iv_purchase);

        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.image_wantobuy, iv_purchase);
        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.image_wanttosell, iv_buyer);

        my_action_bar = (ActionBarProject) view.findViewById(R.id.my_action_bar);
        my_action_bar.showLogo();
        Button btn_buyer = (Button)view.findViewById(R.id.btn_buyer);
        btn_buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeFragment.this.getActivity() != null){
                    getChangeBuyerListenner().changeFragmentBuyer();
                }
            }
        });
        Button btn_purchase = (Button)view.findViewById(R.id.btn_purchase);
        btn_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeFragment.this.getActivity() != null){
                    getChangeBuyerListenner().changeFragmentPurchase();
                }
            }
        });

        return view;
    }


//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        Handler UIHandler = new Handler();
//        UIHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(HomeFragment.this.getActivity(), PayDemoActivity.class));
//
////                Intent intent = new Intent(HomeFragment.this.getActivity(), H5PayDemoActivity.class);
////                Bundle extras = new Bundle();
//                /**
//                 * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
//                 * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
//                 * 商户可以根据自己的需求来实现
//                 */
////                String url = "https://mapi.alipay.com/gateway.do?service=create_direct_pay_by_user&_input_charset=utf-8&partner=2088521643782020&seller_id=2088521643782020&payment_type=1&return_url=http%3A%2F%2Fwww.wo1haitao.com%2F&notify_url=http%3A%2F%2Fwww.wo1haitao.com%2Forders%2F198%2Falipay_notify&out_trade_no=14JUL2017-A43F6-688&subject=Pd+1023+%2314JUL2017-A43F6-688&total_fee=5250.0&sign_type=MD5&sign=37a80304d689438f493c870d43710549";
////                // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
////                extras.putString("url", url);
////                intent.putExtras(extras);
////                startActivity(intent);
//
////                com.alipay.sdk.app.AuthTask authTask = new com.alipay.sdk.app.AuthTask(HomeFragment.this.getActivity());
////                java.util.Map<java.lang.String,java.lang.String> meauth = authTask.authV2("", true);
////
////
////                PayTask payTask = new PayTask (HomeFragment.this.getActivity());
////                String url = "partner=\"2088521643782020\"&seller_id=\"2088521643782020\"&out_trade_no=\"14JUL2017-A43F6-688\"&subject=\"Pd+1023+%2314JUL2017-A43F6-688\"&body=\"testtest\"&total_fee=\"5250.0\"&currency=\"USD\"&forex_biz=\"FP\"&notify_url=\"http://www.wo1haitao.com/orders/198/alipay_notify\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&sign=\"37a80304d689438f493c870d43710549\"&sign_type=\"MD5\"";
////                java.util.Map<java.lang.String,java.lang.String> message = payTask.payV2(url, true);
////                java.util.Map<java.lang.String,java.lang.String> error = message;
//            }
//        }, 2000);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public interface FragmentChangeBuyer{
        void changeFragmentBuyer();
        void changeFragmentPurchase();
    }


}
