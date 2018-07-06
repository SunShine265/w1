package com.wo1haitao.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.wo1haitao.R;
import com.wo1haitao.activities.SetupAccountProfile;
import com.wo1haitao.activities.SetupAccountVerify;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.CustomResponse;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.ResponsePacket;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.CustomViewCategoryItem;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class CustomDialogClass extends Dialog {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    FlowLayout flow_content;
    int count_select;
    ArrayList<Long> list_id_current;
    int imgLight,imgDark, textBlack,textWhite;
    HashMap<String, String> data_category;
    ArrayList<CustomViewCategoryItem> list_item_view;
    FrameLayout fl_error;
    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        count_select=0;
        list_id_current = new ArrayList<>(3);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_custom_view);
        yes = (Button) findViewById(R.id.btn_yes);
        int imgID = getContext().getResources().getIdentifier("button_drable_red", "drawable", getContext().getPackageName());
        imgLight = getContext().getResources().getIdentifier("rounded_button", "drawable", getContext().getPackageName());
        imgDark = getContext().getResources().getIdentifier("rounded_button_dark", "drawable", getContext().getPackageName());
        textBlack =  getContext().getResources().getColor(R.color.black);
        textWhite = getContext().getResources().getColor(R.color.white);
        yes.setBackgroundResource(imgID);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count_checked = 0;
                for(int i = 0; i< list_item_view.size(); i++){
                    if(list_item_view.get(i).getChecked() == true){
                        count_checked++;
                    }
                }
                if(count_checked < 3){
                    //show error text
                    fl_error.setVisibility(View.VISIBLE);
                }
                else{
                    fl_error.setVisibility(View.GONE);
                    PostCategory();
                }
            }
        });
        flow_content = (FlowLayout) findViewById(R.id.flow_content_category);
        fl_error = (FrameLayout) findViewById(R.id.fl_error);
        data_category = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
        list_item_view = new ArrayList<>();
        for(Map.Entry<String,String> item : data_category.entrySet()){
            CustomViewCategoryItem itemView = new CustomViewCategoryItem(this.c);
            itemView.setViewCheck();
            itemView.setId_category(item.getKey());
            itemView.setTextContent(item.getValue());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v instanceof CustomViewCategoryItem){
                        ((CustomViewCategoryItem) v).setChecked(!((CustomViewCategoryItem) v).getChecked());
                        ((CustomViewCategoryItem) v).setViewCheck();
                    }
                }
            });
            flow_content.addView(itemView);
            list_item_view.add(itemView);
        }
    }

    /**
     * Mehtod set image to select and unselect to my category
     * @param :
     * view- View
     * num-position of select
     * isPressed check selected and unselected
     * @return*/


    /**Method post my faverite category
     * @param
     * @return
     * */
    public void PostCategory(){

        HashMap<String,Integer> list_id_category = new HashMap<>();
        int index = 0;
        for(int i =0 ; i < list_item_view.size() ; i ++){
            if(Integer.valueOf(list_item_view.get(i).getId_category()) != null && list_item_view.get(i).getChecked() == true){
                String key = "category_ids["+index+"]";
                int value = Integer.valueOf(list_item_view.get(i).getId_category());
                list_id_category.put(key,value);
                index += 1;
            }
        }

        final ProgressDialog progressDialog = Utils.createProgressDialog(c);
        final ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage> call = ws.actionPostUserCategory(list_id_category);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if(CustomDialogClass.this.getContext() != null) {
                    if (response.body() != null) {
                        ResponseMessage responseMessage = response.body();
                        if (responseMessage.isSuccess()) {
                            progressDialog.dismiss();
                            dismiss();
                            GetUser();
                        }
                    } else if (response.errorBody() != null) {
//                        try {
//                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
//                            ErrorMessage error = responseMessage.getErrors();
//                            Toast.makeText(CustomDialogClass.this.getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
//                        } catch (Exception e) {
//                            Crashlytics.logException(e);
//                            Toast.makeText(CustomDialogClass.this.getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
//                        }
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
                        GetUser();
                        progressDialog.dismiss();
                        dismiss();
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(CustomDialogClass.this.getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
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

    /**
     * Get User
     * @params:
     * @return:
     * */
    protected void GetUser() {
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
                            if (responseMessage.isSuccess()) {
                                UserProfile user = (UserProfile) responseMessage.getData();
                                if(user.getSetup_account().getSetup_country() == false) {
                                    Intent it = new Intent(CustomDialogClass.this.c, SetupAccountProfile.class);
                                    PackageManager packageManager = CustomDialogClass.this.c.getPackageManager();
                                    if (it.resolveActivity(packageManager) != null) {
                                        CustomDialogClass.this.c.startActivity(it);
                                    }
                                }
                                else {
                                    if(user.getSetup_account().getSetup_verification() == false) {
                                        Intent it = new Intent(CustomDialogClass.this.c, SetupAccountVerify.class);
                                        PackageManager packageManager = CustomDialogClass.this.c.getPackageManager();
                                        if (it.resolveActivity(packageManager) != null) {
                                            CustomDialogClass.this.c.startActivity(it);
                                        }
                                    }
                                }
                            }
                        } else {
                            try {
                                ErrorMessage error = responseMessage.getErrors();
                                Toast.makeText(CustomDialogClass.this.getContext(), error.getStringError(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }
            @Override
            public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                Utils.OnFailException(t);
            }
        });
    }
}
