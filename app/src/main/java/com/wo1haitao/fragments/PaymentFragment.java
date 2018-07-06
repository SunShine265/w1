package com.wo1haitao.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.crashlytics.android.Crashlytics;
import com.wo1haitao.R;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.alipay.PayResult;
import com.wo1haitao.alipay.SignUtils;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsAddress;
import com.wo1haitao.api.response.RsOrder;
import com.wo1haitao.controls.CustomTextViewFixSize;
import com.wo1haitao.dialogs.DialogInformAlipay;
import com.wo1haitao.models.BillingAddressesModel;
import com.wo1haitao.models.OrderModel;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends BaseFragment {
    ActionBarProject my_action_bar;
    OrderModel paymentOrder;
    ArrayList<RsAddress> shippingAddresses;
    BillingAddressesModel billingAddressesModel;
    TextView id_total_amount, id_offer_price, id_service_charge, id_shipping_cost, tvCommisionChargesPercent;
    LinearLayout ln_discount_offer_price;
    RadioButton pay_mehthod1;
    ScrollView activity_main;
    int paymentMethod = -1;
    final int SDK_PAY_FLAG = 1;
    String flag = "Payment";
    RsOrder rsOrder;
    CustomTextViewFixSize id_discount_offer_price;

    public Boolean getShipandBill() {
        return isShipandBill;
    }

    public void setShipandBill(Boolean shipandBill) {
        isShipandBill = shipandBill;
    }

    RsAddress primaryShipAddress;
    Boolean isShipandBill  = false;

    ActionListener getActionListener() {
        if (this.getActivity() instanceof ActionListener) {
            return (ActionListener) this.getActivity();
        }
        return null;
    }

    ;

    public OrderModel getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(OrderModel paymentOrder) {
        this.paymentOrder = paymentOrder;
    }

    public ArrayList<RsAddress> getShippingAddresses() {
        return shippingAddresses;
    }

    public void setShippingAddresses(ArrayList<RsAddress> shippingAddresses) {
        this.shippingAddresses = shippingAddresses;
        if(shippingAddresses != null && shippingAddresses.size() > 0){
            for (RsAddress address : shippingAddresses){
                if(address.isPrimary_address() == true){
                    primaryShipAddress = address;
                    break;
                }
            }
        }
    }

    public BillingAddressesModel getBillingAddressesModel() {
        return billingAddressesModel;
    }

    public void setBillingAddressesModel(BillingAddressesModel billingAddressesModel) {
        this.billingAddressesModel = billingAddressesModel;
    }

    Handler UIHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        UIHandler = new Handler();
        Button btn_to_main;
        id_total_amount = (TextView) view.findViewById(R.id.id_total_amount);
        id_offer_price = (TextView) view.findViewById(R.id.id_offer_price);
        ln_discount_offer_price = (LinearLayout) view.findViewById(R.id.ln_discount_offer_price);
        id_discount_offer_price = (CustomTextViewFixSize)view.findViewById(R.id.id_discount_offer_price);
        id_service_charge = (TextView) view.findViewById((R.id.id_service_charge));
        id_shipping_cost = (TextView) view.findViewById(R.id.id_shipping_cost);
        btn_to_main = (Button) view.findViewById(R.id.btn_to_main);
        pay_mehthod1 = (RadioButton) view.findViewById(R.id.pay_mehthod1);
        tvCommisionChargesPercent = (TextView) view.findViewById(R.id.tv_commission_charges_percent);
        activity_main = (ScrollView) view.findViewById(R.id.activity_main);

        try {
            float totalAmount = paymentOrder.getAgreed_price_cents() / 100;
            String strTotalAmount = String.format("%.2f", totalAmount);
            id_total_amount.setText("¥" + strTotalAmount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            float fOfferPrice = Float.parseFloat(paymentOrder.getProduct_offer().getOffer_price().getFractional()) / 100;
            float discountAmountCents = Float.parseFloat(paymentOrder.getDiscount_amount_cents()) / 100;
            float discountPurchaseLimitCents = Float.parseFloat(paymentOrder.getDiscount_purchase_limit_cents()) / 100;
            String strOfferPrice = String.format("%.2f", fOfferPrice);
            id_offer_price.setText("¥" + strOfferPrice);
            String strdisscountAmountCents = String.format("%.2f", discountAmountCents);
            if(fOfferPrice >= discountPurchaseLimitCents){
                id_discount_offer_price.setText("- ¥" + strdisscountAmountCents);
            }
            else {
                id_discount_offer_price.setText("- ¥ " + strdisscountAmountCents);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            float fServiceCharges = paymentOrder.getProduct_offer().getCommission_charges();
            String strServiceCharges = String.format("%.2f", fServiceCharges);
            id_service_charge.setText("¥" + strServiceCharges);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String strCommisionChargesPercentFormat = PaymentFragment.this.getActivity().getString(R.string.commission_charges_percent, paymentOrder.getCommission_charges_percent());
            tvCommisionChargesPercent.setText(strCommisionChargesPercentFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            float fShippingCost = Float.parseFloat(paymentOrder.getProduct_tender_shipping().getShipping_cost().getFractional()) / 100;
            String strShippingCost = String.format("%.2f", fShippingCost);
            id_shipping_cost.setText("¥" + strShippingCost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        my_action_bar = (ActionBarProject) view.findViewById(R.id.my_action_bar);
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        my_action_bar.showTitle(R.string.title_address_payment);
        btn_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostOrderProduct();
            }
        });
        pay_mehthod1.setChecked(true);
        paymentMethod = 0;
        return view;
    }

    public interface ActionListener {
        void changeMyPost(String flag);
    }

    public void PostOrderProduct() {
        final ProgressDialog progressPayment = Utils.createProgressDialog(PaymentFragment.this.getActivity());
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        String country = "", state = "", city = "", address_1 = "", address_2 = "", postal_code = "",
                billing_country = "", billing_state = "", billing_city = "", billing_address_1 = "",
                billing_address_2 = "", billing_postal_code = "", payment_method = "";

        if (billingAddressesModel != null) {
            if (billingAddressesModel.getAddress_1() != null) {
                billing_address_1 = billingAddressesModel.getAddress_1();
            }
            if (billingAddressesModel.getAddress_2() != null) {
                billing_address_2 = billingAddressesModel.getAddress_2();
            }
            if (billingAddressesModel.getCountry() != null) {
                billing_country = billingAddressesModel.getCountry();
            }
            if (billingAddressesModel.getState() != null) {
                billing_state = billingAddressesModel.getState();
            }
            if (billingAddressesModel.getPostal_code() != null) {
                billing_postal_code = billingAddressesModel.getPostal_code();
            }
            if (billingAddressesModel.getCity() != null) {
                billing_city = billingAddressesModel.getCity();
            }
        }
        if(isShipandBill == true){
            country = billing_country;
            state = billing_state;
            city = billing_city;
            address_1 =  billing_address_1;
            address_2 =  billing_address_2;
            postal_code = billing_postal_code;
        }
        else{
            if(primaryShipAddress != null){
                country = primaryShipAddress.getCountry();
                state = primaryShipAddress.getState();
                city = primaryShipAddress.getCity();
                address_1 = primaryShipAddress.getAddress_1();
                address_2 = primaryShipAddress.getAddress_2();
                postal_code = primaryShipAddress.getPostal_code();
            }
            else{
                country = billing_country;
                state = billing_state;
                city = billing_city;
                address_1 =  billing_address_1;
                address_2 =  billing_address_2;
                postal_code = billing_postal_code;
            }
        }

        final String paymentMethodTemp = payment_method;
        Call<RsOrder> call = ws.actionPostOrders(address_1, address_2,
                country, state, postal_code, city, billing_address_1, billing_address_2,
                billing_country, billing_state, billing_postal_code, billing_city,
                paymentOrder.getProduct_tender_shipping_id(), paymentOrder.getProduct_offer_id(),
                paymentMethod);
        call.enqueue(new Callback<RsOrder>() {
            @Override
            public void onResponse(Call<RsOrder> call, Response<RsOrder> response) {
                try {
                    if (response.body() != null) {
                        rsOrder = response.body();
                        if (paymentMethod == 0) {
                            Pay(rsOrder);
                        } else {
                            getActionListener().changeMyPost(flag);
                        }
                        progressPayment.dismiss();
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }

                        if (progressPayment != null) {
                            progressPayment.dismiss();
                        } else {
                            if (progressPayment != null) {
                                progressPayment.dismiss();
                            }
                            Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    if (progressPayment != null) {
                        progressPayment.dismiss();
                    }
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<RsOrder> call, Throwable t) {
                Utils.OnFailException(t);
                if (progressPayment != null) {
                    progressPayment.dismiss();
                }
            }
        });
    }

    AlertDialog alertDialog = null;

    public void Pay(final RsOrder rsOrder) {
        //url1 = url1.replace("\"", "");
//        String url = "https://mapi.alipay.com/gateway.do?" + url1;
//        Uri uri = Uri.parse(url);
//        String service, partner, seller_id, payment_type, notify_url, out_trade_no, subject, sign_type, total_fee;
//        service = uri.getQueryParameter("service");
//        partner = uri.getQueryParameter("partner");
//        seller_id = uri.getQueryParameter("seller_id");
//        payment_type = uri.getQueryParameter("payment_type");
//        notify_url = uri.getQueryParameter("notify_url");
//        out_trade_no = uri.getQueryParameter("out_trade_no");
//        subject = uri.getQueryParameter("subject");
//        sign_type = uri.getQueryParameter("sign_type");
//        total_fee = uri.getQueryParameter("total_fee");
//        String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAN0yqPkLXlnhM+2H/57aHsYHaHXazr9pFQun907TMvmbR04wHChVsKVgGUF1hC0FN9hfeYT5v2SXg1WJSg2tSgk7F29SpsF0I36oSLCIszxdu7ClO7c22mxEVuCjmYpJdqb6XweAZzv4Is661jXP4PdrCTHRdVTU5zR9xUByiLSVAgMBAAECgYEAhznORRonHylm9oKaygEsqQGkYdBXbnsOS6busLi6xA+iovEUdbAVIrTCG9t854z2HAgaISoRUKyztJoOtJfI1wJaQU+XL+U3JIh4jmNx/k5UzJijfvfpT7Cv3ueMtqyAGBJrkLvXjiS7O5ylaCGuB0Qz711bWGkRrVoosPM3N6ECQQD8hVQUgnHEVHZYtvFqfcoq2g/onPbSqyjdrRu35a7PvgDAZx69Mr/XggGNTgT3jJn7+2XmiGkHM1fd1Ob/3uAdAkEA4D7aE3ZgXG/PQqlm3VbE/+4MvNl8xhjqOkByBOY2ZFfWKhlRziLEPSSAh16xEJ79WgY9iti+guLRAMravGrs2QJBAOmKWYeaWKNNxiIoF7/4VDgrcpkcSf3uRB44UjFSn8kLnWBUPo6WV+x1FQBdjqRviZ4NFGIP+KqrJnFHzNgJhVUCQFzCAukMDV4PLfeQJSmna8PFz2UKva8fvTutTryyEYu+PauaX5laDjyQbc4RIEMU0Q29CRX3BA8WDYg7YPGRdTkCQQCG+pjU2FB17ZLuKRlKEdtXNV6zQFTmFc1TKhlsDTtCkWs/xwkoCfZKstuV3Uc5J4BNJDkQOGm38pDRPcUDUh2/";
//        //String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI7Xh680MTnfYDxwyzSqKMhKWed3UP64YMaDkHgBtgCk3odFjoMcDTDumZqeiC1hVICVFR94blrq1ZhxQnFS+UqhXkPbyzuiPZQVBGaZ42y8brGuFiGJVjlo7Sf6GMc14a0d+bLD0C13faGK7PV0YQ7A1xaIIn/fn/Uk4qpOWEhhAgMBAAECgYBO2ZkT1RrLWIxWMOlrY/bpQWnJhSrXwU3ip2OLa15dkqUoRPQ7WbPKbBusp5CChHTSGfm0CpXYaEOKSBMmXWgwuz767/EEpaJfep3OPmKGD+kGPdA3qDYfTMCGRXSX1J47kjLa9XQX6iBmUMAzvTrLS9lZlXPKDhmU6bNZdODH1QJBAO1I/TIcjq7Fd0lwmXigbCs52suZP6/JEVpy6dSVPbZubCnJY3JbG044TN/2Ve4PzMNhmt367k46COpN6Oh1gAMCQQCaG6S2+NL9HTPD2RYmLKF8mXwcYD20WH2qsEGSVZwZma1CzbiwgY0MCUyXue0T/RabkU+oj0v679qy5AtkuULLAkEAzuliwJveX9CZYFTrvyBEsrzUac3Ml0DB/RlPhaxOEBLiBt4x9bo0aVT21CU+cUUdzRIDtaXmwBgjRg2CF5K+eQJAHAZW5+dMBzeeSElcG8kV/OC0jzx5PCizgazX39KttoIZ3gInSgHlMoEmapknIfFugQ/l2pNkj9e6f7m00LZYDQJBAOBCfnJmeppAA07ws2FzfvR880+rYc1gI95BSKK4ZLMoO2w+4k/NQ15K6MqlqwFNzTJq0HBf1MryUcuuASx0sQs=";
//
//        // 支付宝公钥
////        final String RSA_PUBLIC = "";
////        if (TextUtils.isEmpty(partner) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(seller_id)) {
////            alertDialog = new AlertDialog.Builder(PaymentFragment.this.getActivity()).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
////                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                        public void onClick(DialogInterface dialoginterface, int i) {
////                            //
////                            alertDialog.dismiss();
////                        }
////                    }).show();
////            return;
////        }
////
////        String subject = "测试的商品";
//        String body = "该测试商品的详细描述";
//
//        // 签约合作者身份ID
//        String orderInfo = "partner=" + "\"" + partner + "\"";
//
//        // 签约卖家支付宝账号
//        orderInfo += "&seller_id=" + "\"" + seller_id + "\"";
//
//        // 商户网站唯一订单号
//        orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";
//
//        // 商品名称
//        orderInfo += "&subject=" + "\"" + subject + "\"";
//
//        // 商品详情
//        orderInfo += "&body=" + "\"" + body + "\"";
//
//        // 商品金额
//        orderInfo += "&total_fee=" + "\"" + total_fee + "\"";
//
//        // 服务器异步通知页面路径
//        orderInfo += "&notify_url=" + "\"" + notify_url + "\"";
//
//        // 服务接口名称， 固定值
//        orderInfo += "&service=\"mobile.securitypay.pay\"";
//
//        // 支付类型， 固定值
//        orderInfo += "&payment_type=" + "\"" + payment_type + "\"";
//
//        // 参数编码， 固定值
//        orderInfo += "&_input_charset=\"utf-8\"";
//
//        // 设置未付款交易的超时时间
//        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
//        // 取值范围：1m～15d。
//        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
//        // 该参数数值不接受小数点，如1.5h，可转换为90m。
//        orderInfo += "&it_b_pay=\"30m\"";
//
//        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
//        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
//
//        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//        orderInfo += "&return_url=\"m.alipay.com\"";
//        //global pay special parameters
////        orderInfo += "&currency=\"USD\"";
////        orderInfo += "&forex_biz=\"FP\"";
//
//
//        /**
//         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
//         */
//
//        String sign = sign(orderInfo, RSA_PRIVATE);
//        try {
//            /**
//             * 仅需对sign 做URL编码
//             */
//            sign = URLEncoder.encode(sign, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//
//
//        /**
//         * 完整的符合支付宝参数规范的订单信息
//         */
//        sign = uri.getQueryParameter("sign");
        //final String payInfo = "app_id=2017062907598134&biz_content={\"product_code\":\"QUICK_MSECURITY_PAY\",\"out_trade_no\":\"22AUG2017-B5250-874\",\"subject\":\"ticket #22AUG2017-B5250-874\",\"total_fee\":1041.0,\"body\":\"ticket\"}&charset=utf-8&method=alipay.trade.app.pay&notify_url=http://192.168.0.169:3000/orders/428/alipay_notify&sign_type=RSA&timestamp=2017-08-22 11:46:55&version=1.0&sign=0QYwQuAd13jgvPyVzD2l56WQDl3TBMiFKJCJhVIepoQqg4Nm+IYYHh9HbJyBfut5p06bYE+llLqpIHZ/JPnp4bxJngDirP61ikO0KSykP1u01Unw0tgfD5lOlUepCtIdDExoM19UN6rbUx9w3h6MMNE5wKN/fN9ear8VAiwx0Wc=";//orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PaymentFragment.this.getActivity());

                // 调用支付接口，获取支付结果
                try {
//                    url1 = URLDecoder.decode(url1, "UTF-8");
                    String result = alipay.pay(rsOrder.getAlipay_url(), true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    Crashlytics.logException(e);
                }
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResultStatus();// 同步返回需要验证的信息
                    if (!payResult.getResult().equals("")) {
                        resultInfo += ", " + payResult.getResult();
                    }
                    if (!payResult.getMemo().equals("")) {
                        resultInfo += ", " + payResult.getMemo();
                    }
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PaymentFragment.this.getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                        UIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ConfirmPayment(rsOrder.getOrder().getId());
                                MainActivity mainActivity = (MainActivity) PaymentFragment.this.getActivity();
                                DetailOrderFragment fragment = new DetailOrderFragment();
                                fragment.setFlag("offer_order");
                                fragment.setIdOrder(rsOrder.getOrder().getId());
                                mainActivity.changeFragment(fragment, true);
                            }
                        });
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
//                            Toast.makeText(PaymentFragment.this.getActivity(), "支付结果确认中", Toast.LENGTH_SHORT).show();
                            UIHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ConfirmPayment(rsOrder.getOrder().getId());
                                    MainActivity mainActivity = (MainActivity) PaymentFragment.this.getActivity();
                                    DetailOrderFragment fragment = new DetailOrderFragment();
                                    fragment.setFlag("offer_order");
                                    fragment.setIdOrder(rsOrder.getOrder().getId());
                                    mainActivity.changeFragment(fragment, true);
                                }
                            });
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误

                            String content = "支付失败, 请您重新支付！";
                            DialogInformAlipay dialogInformAlipay = new DialogInformAlipay(getActivity(), content);
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialogInformAlipay.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            dialogInformAlipay.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogInformAlipay.show();
//                            Toast.makeText(PaymentFragment.this.getActivity(), "支付失败" + "\n" + resultInfo
//                                    , Toast.LENGTH_SHORT).show();
//                            UIHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    getActionListener().changeMyPost(flag);
//                                }
//                            });
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content, String RSA_PRIVATE) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * Confirm payment
     *
     * @params:
     * @return:
     */
    private void ConfirmPayment(int id) {
        ApiServices apiServices = ApiServices.getInstance();
        WebService ws = apiServices.getRetrofit().create(WebService.class);
        final ProgressDialog progressDialog = Utils.createProgressDialog(PaymentFragment.this.getActivity());
        Call<ResponseMessage> call = ws.actionConfirmPayment(id);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {
                    if (response.code() == 200) {
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
                    Utils.crashLog(e);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }

}
