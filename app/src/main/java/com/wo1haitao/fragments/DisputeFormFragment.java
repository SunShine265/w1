package com.wo1haitao.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.RsProduct;
import com.wo1haitao.controls.CustomEditextSoftkey;
import com.wo1haitao.controls.SquareImageView;
import com.wo1haitao.models.DisputeModel;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisputeFormFragment extends BaseFragment {

    Activity context;
    ActionBarProject my_action_bar;
    String paramWantToBuyID = "", paramReportedID = "";
    MyCallback listener;
    RsProduct rsProduct;
    String flag = "";
    View fragment_view;
    CustomEditextSoftkey reasonReport, detailsReport;
    Button btnPostReport;
    SquareImageView iv_product_img;
    TextView name_product, tv_category, tv_country, tv_message;
    TextView user_order;
    FrameLayout fl_message;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public RsProduct getRsProduct() {
        return rsProduct;
    }

    public void setRsProduct(RsProduct rsProduct) {
        this.rsProduct = rsProduct;
    }

    public DisputeFormFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (this.getActivity() instanceof MyCallback) {
            listener = (MyCallback) this.getActivity();
        }

        fragment_view = inflater.inflate(R.layout.fragment_dispute_form, container, false);
        fl_message = (FrameLayout) fragment_view.findViewById(R.id.fl_message);
        tv_message = (TextView) fragment_view.findViewById(R.id.tv_message);
        name_product = (TextView) fragment_view.findViewById(R.id.name_product);
        tv_category = (TextView) fragment_view.findViewById(R.id.tv_category);
        tv_country = (TextView) fragment_view.findViewById(R.id.tv_country);
        user_order = (TextView) fragment_view.findViewById(R.id.user_order);
        iv_product_img = (SquareImageView) fragment_view.findViewById(R.id.iv_product_img);
        btnPostReport = (Button) fragment_view.findViewById(R.id.btn_post_dispute);
        reasonReport = (CustomEditextSoftkey) fragment_view.findViewById(R.id.et_name_dispute);
        detailsReport = (CustomEditextSoftkey) fragment_view.findViewById(R.id.et_content_dispute);
        my_action_bar = (ActionBarProject) fragment_view.findViewById(R.id.my_action_bar);
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPress();
            }
        });
        btnPostReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DisputeFormFragment.this.rsProduct.getWant_to_buy_id() != null && DisputeFormFragment.this.rsProduct.getReported_id() != null) {
                    PostDispute(DisputeFormFragment.this.rsProduct.getWant_to_buy_id(), DisputeFormFragment.this.rsProduct.getReported_id(), reasonReport.getText().toString(), detailsReport.getText().toString());
                }
            }
        });
        my_action_bar.showTitle("投诉代购用户");
        try {
            name_product.setText(rsProduct.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> myListCategory = new ArrayList<>();
        HashMap<String, String> hashmapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
        if (hashmapCategory != null) {
            for (Map.Entry entry : hashmapCategory.entrySet()) {
                myListCategory.add(entry.getValue().toString());
            }
        }
        try {
            String strCategory = hashmapCategory.get(String.valueOf(rsProduct.getCategory_id()));
            tv_category.setText(strCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String countries = "";
            if (rsProduct.isNon_restricted_country() == true) {
                countries = getActivity().getString(R.string.txt_no_limited_country);
            } else {
                if (rsProduct.getCountries() != null && rsProduct.getCountries().size() > 0) {
                    for (int i = 0; i < rsProduct.getCountries().size(); i++) {
                        countries += rsProduct.getCountries().get(i).getName();
                        if (i < rsProduct.getCountries().size() - 1) {
                            countries += ", ";
                        }
                    }
                }
            }
            tv_country.setText(countries);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (rsProduct.getOrderModels() != null) {
                user_order.setText("用户名: " + rsProduct.getOrderModels().get(0).getOwner().getNickname().toString());
            } else {
                user_order.setText("用户名: " + rsProduct.getUserProfileMyPost().getNickname());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            imageLoader.displayImage(ApiServices.BASE_URI + rsProduct.getProduct_images().get(0).getProduct_image().getUrl(), iv_product_img);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fragment_view;
    }

    public interface MyCallback {

        void changeMyBids();

        void changeMyPost(String flag);

    }

    public void PostDispute(String wantTobuyID, String reportedID, String reasonToDispute, String details) {
        ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        try {
            paramWantToBuyID = URLEncoder.encode(wantTobuyID, "utf-8");
            paramReportedID = URLEncoder.encode(reportedID, "utf-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final ProgressDialog progressDialog = Utils.createProgressDialog(DisputeFormFragment.this.getActivity());
        Call<ResponseMessage<DisputeModel>> call = ws.actionPostDispute(paramWantToBuyID, paramWantToBuyID, paramReportedID, reasonToDispute, details);
        call.enqueue(new Callback<ResponseMessage<DisputeModel>>() {
            @Override
            public void onResponse(Call<ResponseMessage<DisputeModel>> call, Response<ResponseMessage<DisputeModel>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        DisputeModel disputeModel = response.body().getData();
                        progressDialog.dismiss();
                        if (listener != null) {
                            if (DisputeFormFragment.this.getFlag().equals("mybid")) {
                                listener.changeMyBids();
                            } else if (DisputeFormFragment.this.getFlag().equals("mypost")) {
                                listener.changeMyPost("dispute");
                            }
                        }
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ArrayList<String> list_error = responseMessage.getError_messages();
                            String strError = "";
                            if (list_error != null) {
                                for (String error_item : list_error) {
                                    if (strError.isEmpty() == true) {
                                        strError = "\u2022 " + error_item;
                                    } else {
                                        strError += "\n\u2022 " + error_item;
                                    }
                                }
                            }
                            if (!strError.equals("")) {
                                tv_message.setText(strError);
                                fl_message.setVisibility(View.VISIBLE);
                            }
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
            public void onFailure(Call<ResponseMessage<DisputeModel>> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }

}
