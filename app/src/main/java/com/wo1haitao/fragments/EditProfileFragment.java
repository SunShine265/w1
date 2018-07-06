package com.wo1haitao.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.R;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.CustomResponse;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.api.response.ResponsePacket;
import com.wo1haitao.api.response.UserProfile;
import com.wo1haitao.controls.CustomEditextSoftkey;
import com.wo1haitao.controls.MultiSpinner;
import com.wo1haitao.controls.RoundedImageView;
import com.wo1haitao.dialogs.DialogDropdown;
import com.wo1haitao.dialogs.DialogPermission;
import com.wo1haitao.models.ItemPicker;
import com.wo1haitao.utils.MyPreferences;
import com.wo1haitao.utils.Utils;
import com.wo1haitao.views.ActionBarProject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG_DEFAULT;

/**
 * A simple {@link Fragment} subclass.
 */


public class EditProfileFragment extends BaseFragment implements MultiSpinner.MultiSpinnerListener, SwipeRefreshLayout.OnRefreshListener {
    //Spinner sn_country;
    ArrayList<String> sp_list_country, sp_list_phone_code;
    String flag = "";
    ProgressDialog pd_dialog;
    CustomEditextSoftkey et_name, et_alipayname, et_lastname, et_firstname, et_mail, et_phone_number, edt_id;
    Button bt_to_main;
    TextView tv_message, text_avt, tv_check_fill, tv_check_fill_alipay;
    LinearLayout fl_message;
    ScrollView sv_main;
    RoundedImageView iv_avt;
    int RQ_GET_IMAGE = 0;
    boolean state_image = false;
    ActionBarProject my_action_bar;
    Spinner sp_choose_country, sp_choose_code_phone;
    List<Integer> list_country;
    UserProfile user;
    ProgressDialog processDialogUpdate;
    LinearLayout ln_check_fill, ln_check_fill_alipay, ln_alipay_section;
    ;
    View viewFragment;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText edt_choose_phonecode, edt_choose_country;
    ArrayList<ItemPicker> list_item_country, list_item_phonecode;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    MyCallBack getListener() {
        if (this.getActivity() instanceof MyCallBack) {
            return (MyCallBack) this.getActivity();
        }
        return null;
    }

    final static String PortalCodes = "[\n" +
            "        [\n" +
            "            \"(AD) +376\",\n" +
            "            \"AD\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AE) +971\",\n" +
            "            \"AE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AF) +93\",\n" +
            "            \"AF\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AG) +1\",\n" +
            "            \"AG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AI) +1\",\n" +
            "            \"AI\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AL) +355\",\n" +
            "            \"AL\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AM) +374\",\n" +
            "            \"AM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AO) +244\",\n" +
            "            \"AO\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AQ) +672\",\n" +
            "            \"AQ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AR) +54\",\n" +
            "            \"AR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AS) +1\",\n" +
            "            \"AS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AT) +43\",\n" +
            "            \"AT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AU) +61\",\n" +
            "            \"AU\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AW) +297\",\n" +
            "            \"AW\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AX) +358\",\n" +
            "            \"AX\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(AZ) +994\",\n" +
            "            \"AZ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BA) +387\",\n" +
            "            \"BA\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BB) +1\",\n" +
            "            \"BB\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BD) +880\",\n" +
            "            \"BD\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BE) +32\",\n" +
            "            \"BE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BF) +226\",\n" +
            "            \"BF\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BG) +359\",\n" +
            "            \"BG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BH) +973\",\n" +
            "            \"BH\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BI) +257\",\n" +
            "            \"BI\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BJ) +229\",\n" +
            "            \"BJ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BL) +590\",\n" +
            "            \"BL\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BM) +1\",\n" +
            "            \"BM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BN) +673\",\n" +
            "            \"BN\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BO) +591\",\n" +
            "            \"BO\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BQ) +599\",\n" +
            "            \"BQ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BR) +55\",\n" +
            "            \"BR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BS) +1\",\n" +
            "            \"BS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BT) +975\",\n" +
            "            \"BT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BV) +47\",\n" +
            "            \"BV\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BW) +267\",\n" +
            "            \"BW\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BY) +375\",\n" +
            "            \"BY\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(BZ) +501\",\n" +
            "            \"BZ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CA) +1\",\n" +
            "            \"CA\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CC) +61\",\n" +
            "            \"CC\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CD) +243\",\n" +
            "            \"CD\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CF) +236\",\n" +
            "            \"CF\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CG) +242\",\n" +
            "            \"CG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CH) +41\",\n" +
            "            \"CH\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CI) +225\",\n" +
            "            \"CI\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CK) +682\",\n" +
            "            \"CK\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CL) +56\",\n" +
            "            \"CL\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CM) +237\",\n" +
            "            \"CM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CN) +86\",\n" +
            "            \"CN\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CO) +57\",\n" +
            "            \"CO\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CR) +506\",\n" +
            "            \"CR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CU) +53\",\n" +
            "            \"CU\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CV) +238\",\n" +
            "            \"CV\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CW) +599\",\n" +
            "            \"CW\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CX) +61\",\n" +
            "            \"CX\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CY) +357\",\n" +
            "            \"CY\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(CZ) +420\",\n" +
            "            \"CZ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(DE) +49\",\n" +
            "            \"DE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(DJ) +253\",\n" +
            "            \"DJ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(DK) +45\",\n" +
            "            \"DK\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(DM) +1\",\n" +
            "            \"DM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(DO) +1\",\n" +
            "            \"DO\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(DZ) +213\",\n" +
            "            \"DZ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(EC) +593\",\n" +
            "            \"EC\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(EE) +372\",\n" +
            "            \"EE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(EG) +20\",\n" +
            "            \"EG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(EH) +212\",\n" +
            "            \"EH\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(ER) +291\",\n" +
            "            \"ER\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(ES) +34\",\n" +
            "            \"ES\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(ET) +251\",\n" +
            "            \"ET\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(FI) +358\",\n" +
            "            \"FI\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(FJ) +679\",\n" +
            "            \"FJ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(FK) +500\",\n" +
            "            \"FK\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(FM) +691\",\n" +
            "            \"FM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(FO) +298\",\n" +
            "            \"FO\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(FR) +33\",\n" +
            "            \"FR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GA) +241\",\n" +
            "            \"GA\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GB) +44\",\n" +
            "            \"GB\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GD) +1\",\n" +
            "            \"GD\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GE) +995\",\n" +
            "            \"GE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GF) +594\",\n" +
            "            \"GF\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GG) +44\",\n" +
            "            \"GG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GH) +233\",\n" +
            "            \"GH\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GI) +350\",\n" +
            "            \"GI\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GL) +299\",\n" +
            "            \"GL\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GM) +220\",\n" +
            "            \"GM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GN) +224\",\n" +
            "            \"GN\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GP) +590\",\n" +
            "            \"GP\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GQ) +240\",\n" +
            "            \"GQ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GR) +30\",\n" +
            "            \"GR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GS) +500\",\n" +
            "            \"GS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GT) +502\",\n" +
            "            \"GT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GU) +1\",\n" +
            "            \"GU\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GW) +245\",\n" +
            "            \"GW\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(GY) +592\",\n" +
            "            \"GY\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(HK) +852\",\n" +
            "            \"HK\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(HM) +\",\n" +
            "            \"HM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(HN) +504\",\n" +
            "            \"HN\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(HR) +385\",\n" +
            "            \"HR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(HT) +509\",\n" +
            "            \"HT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(HU) +36\",\n" +
            "            \"HU\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(ID) +62\",\n" +
            "            \"ID\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(IE) +353\",\n" +
            "            \"IE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(IL) +972\",\n" +
            "            \"IL\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(IM) +44\",\n" +
            "            \"IM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(IN) +91\",\n" +
            "            \"IN\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(IO) +246\",\n" +
            "            \"IO\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(IQ) +964\",\n" +
            "            \"IQ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(IR) +98\",\n" +
            "            \"IR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(IS) +354\",\n" +
            "            \"IS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(IT) +39\",\n" +
            "            \"IT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(JE) +44\",\n" +
            "            \"JE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(JM) +1\",\n" +
            "            \"JM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(JO) +962\",\n" +
            "            \"JO\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(JP) +81\",\n" +
            "            \"JP\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(KE) +254\",\n" +
            "            \"KE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(KG) +996\",\n" +
            "            \"KG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(KH) +855\",\n" +
            "            \"KH\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(KI) +686\",\n" +
            "            \"KI\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(KM) +269\",\n" +
            "            \"KM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(KN) +1\",\n" +
            "            \"KN\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(KP) +850\",\n" +
            "            \"KP\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(KR) +82\",\n" +
            "            \"KR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(KW) +965\",\n" +
            "            \"KW\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(KY) +1\",\n" +
            "            \"KY\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(KZ) +7\",\n" +
            "            \"KZ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(LA) +856\",\n" +
            "            \"LA\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(LB) +961\",\n" +
            "            \"LB\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(LC) +1\",\n" +
            "            \"LC\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(LI) +423\",\n" +
            "            \"LI\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(LK) +94\",\n" +
            "            \"LK\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(LR) +231\",\n" +
            "            \"LR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(LS) +266\",\n" +
            "            \"LS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(LT) +370\",\n" +
            "            \"LT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(LU) +352\",\n" +
            "            \"LU\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(LV) +371\",\n" +
            "            \"LV\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(LY) +218\",\n" +
            "            \"LY\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MA) +212\",\n" +
            "            \"MA\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MC) +377\",\n" +
            "            \"MC\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MD) +373\",\n" +
            "            \"MD\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(ME) +382\",\n" +
            "            \"ME\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MF) +590\",\n" +
            "            \"MF\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MG) +261\",\n" +
            "            \"MG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MH) +692\",\n" +
            "            \"MH\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MK) +389\",\n" +
            "            \"MK\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(ML) +223\",\n" +
            "            \"ML\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MM) +95\",\n" +
            "            \"MM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MN) +976\",\n" +
            "            \"MN\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MO) +853\",\n" +
            "            \"MO\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MP) +1\",\n" +
            "            \"MP\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MQ) +596\",\n" +
            "            \"MQ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MR) +222\",\n" +
            "            \"MR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MS) +1\",\n" +
            "            \"MS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MT) +356\",\n" +
            "            \"MT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MU) +230\",\n" +
            "            \"MU\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MV) +960\",\n" +
            "            \"MV\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MW) +265\",\n" +
            "            \"MW\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MX) +52\",\n" +
            "            \"MX\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MY) +60\",\n" +
            "            \"MY\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(MZ) +258\",\n" +
            "            \"MZ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(NA) +264\",\n" +
            "            \"NA\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(NC) +687\",\n" +
            "            \"NC\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(NE) +227\",\n" +
            "            \"NE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(NF) +672\",\n" +
            "            \"NF\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(NG) +234\",\n" +
            "            \"NG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(NI) +505\",\n" +
            "            \"NI\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(NL) +31\",\n" +
            "            \"NL\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(NO) +47\",\n" +
            "            \"NO\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(NP) +977\",\n" +
            "            \"NP\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(NR) +674\",\n" +
            "            \"NR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(NU) +683\",\n" +
            "            \"NU\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(NZ) +64\",\n" +
            "            \"NZ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(OM) +968\",\n" +
            "            \"OM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PA) +507\",\n" +
            "            \"PA\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PE) +51\",\n" +
            "            \"PE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PF) +689\",\n" +
            "            \"PF\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PG) +675\",\n" +
            "            \"PG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PH) +63\",\n" +
            "            \"PH\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PK) +92\",\n" +
            "            \"PK\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PL) +48\",\n" +
            "            \"PL\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PM) +508\",\n" +
            "            \"PM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PN) +\",\n" +
            "            \"PN\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PR) +1\",\n" +
            "            \"PR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PS) +970\",\n" +
            "            \"PS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PT) +351\",\n" +
            "            \"PT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PW) +680\",\n" +
            "            \"PW\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(PY) +595\",\n" +
            "            \"PY\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(QA) +974\",\n" +
            "            \"QA\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(RE) +262\",\n" +
            "            \"RE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(RO) +40\",\n" +
            "            \"RO\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(RS) +381\",\n" +
            "            \"RS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(RU) +7\",\n" +
            "            \"RU\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(RW) +250\",\n" +
            "            \"RW\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SA) +966\",\n" +
            "            \"SA\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SB) +677\",\n" +
            "            \"SB\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SC) +248\",\n" +
            "            \"SC\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SD) +249\",\n" +
            "            \"SD\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SE) +46\",\n" +
            "            \"SE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SG) +65\",\n" +
            "            \"SG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SH) +290\",\n" +
            "            \"SH\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SI) +386\",\n" +
            "            \"SI\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SJ) +47\",\n" +
            "            \"SJ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SK) +421\",\n" +
            "            \"SK\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SL) +232\",\n" +
            "            \"SL\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SM) +378\",\n" +
            "            \"SM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SN) +221\",\n" +
            "            \"SN\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SO) +252\",\n" +
            "            \"SO\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SR) +597\",\n" +
            "            \"SR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SS) +211\",\n" +
            "            \"SS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(ST) +239\",\n" +
            "            \"ST\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SV) +503\",\n" +
            "            \"SV\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SX) +1\",\n" +
            "            \"SX\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SY) +963\",\n" +
            "            \"SY\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(SZ) +268\",\n" +
            "            \"SZ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TC) +1\",\n" +
            "            \"TC\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TD) +235\",\n" +
            "            \"TD\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TF) +262\",\n" +
            "            \"TF\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TG) +228\",\n" +
            "            \"TG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TH) +66\",\n" +
            "            \"TH\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TJ) +992\",\n" +
            "            \"TJ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TK) +690\",\n" +
            "            \"TK\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TL) +670\",\n" +
            "            \"TL\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TM) +993\",\n" +
            "            \"TM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TN) +216\",\n" +
            "            \"TN\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TO) +676\",\n" +
            "            \"TO\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TR) +90\",\n" +
            "            \"TR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TT) +1\",\n" +
            "            \"TT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TV) +688\",\n" +
            "            \"TV\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TW) +886\",\n" +
            "            \"TW\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(TZ) +255\",\n" +
            "            \"TZ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(UA) +380\",\n" +
            "            \"UA\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(UG) +256\",\n" +
            "            \"UG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(UM) +\",\n" +
            "            \"UM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(US) +1\",\n" +
            "            \"US\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(UY) +598\",\n" +
            "            \"UY\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(UZ) +998\",\n" +
            "            \"UZ\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(VA) +39\",\n" +
            "            \"VA\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(VC) +1\",\n" +
            "            \"VC\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(VE) +58\",\n" +
            "            \"VE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(VG) +1\",\n" +
            "            \"VG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(VI) +1\",\n" +
            "            \"VI\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(VN) +84\",\n" +
            "            \"VN\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(VU) +678\",\n" +
            "            \"VU\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(WF) +681\",\n" +
            "            \"WF\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(WS) +685\",\n" +
            "            \"WS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(YE) +967\",\n" +
            "            \"YE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(YT) +262\",\n" +
            "            \"YT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(ZA) +27\",\n" +
            "            \"ZA\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(ZM) +260\",\n" +
            "            \"ZM\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"(ZW) +263\",\n" +
            "            \"ZW\"\n" +
            "        ]\n" +
            "    ]\n";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        viewFragment = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        list_country = new ArrayList<>();
        my_action_bar = (ActionBarProject) viewFragment.findViewById(R.id.my_action_bar);
        my_action_bar.showLogo();
        my_action_bar.showBack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        et_name = (CustomEditextSoftkey) viewFragment.findViewById(R.id.et_nickname);
        et_lastname = (CustomEditextSoftkey) viewFragment.findViewById(R.id.et_lastname);
        et_firstname = (CustomEditextSoftkey) viewFragment.findViewById(R.id.et_firstname);
        et_mail = (CustomEditextSoftkey) viewFragment.findViewById(R.id.et_mail);
        et_phone_number = (CustomEditextSoftkey) viewFragment.findViewById(R.id.et_phone_number);
        edt_id = (CustomEditextSoftkey) viewFragment.findViewById(R.id.edt_id);
        et_alipayname = (CustomEditextSoftkey) viewFragment.findViewById(R.id.edt_name_alipay);
        ln_alipay_section = (LinearLayout) viewFragment.findViewById(R.id.ln_alipay_section);
        ln_check_fill = (LinearLayout) viewFragment.findViewById(R.id.ln_check_fill);
        tv_check_fill = (TextView) viewFragment.findViewById(R.id.tv_check_fill);
        ln_check_fill_alipay = (LinearLayout) viewFragment.findViewById(R.id.ln_check_fill_alipay);
        tv_check_fill_alipay = (TextView) viewFragment.findViewById(R.id.tv_check_fill_alipay);
        swipeRefreshLayout = (SwipeRefreshLayout) viewFragment.findViewById(R.id.swipe_refresh_layout);

        sp_list_country = new ArrayList<>();
        ImageView img_back_action = (ImageView) viewFragment.findViewById(R.id.ib_back_action);
        img_back_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPress();
            }
        });
        sp_choose_country = (Spinner) viewFragment.findViewById(R.id.sp_choose_country);
        sp_choose_code_phone = (Spinner) viewFragment.findViewById(R.id.sp_choose_code_phone);
        ArrayList<ArrayList<String>> portal_codes = new ArrayList<ArrayList<String>>();
        portal_codes = ApiServices.getGsonBuilder().create().fromJson(PortalCodes, portal_codes.getClass());
        sp_list_phone_code = new ArrayList<>();

        if (portal_codes != null) {
            for (int i = 0; i < portal_codes.size(); i++) {
                sp_list_phone_code.add(portal_codes.get(i).get(0));
            }
        }
        HashMap<String, String> hashmapCountry = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_COUNTRY);
        if (hashmapCountry != null) {
            for (Map.Entry entry : hashmapCountry.entrySet()) {
                sp_list_country.add(entry.getValue().toString());
            }
        }

        edt_choose_phonecode = (EditText) viewFragment.findViewById(R.id.edt_choose_phonecode);
        list_item_phonecode = new ArrayList<>();
        if (portal_codes != null) {
            for (int i = 0; i < portal_codes.size(); i++) {
                if(portal_codes.get(i).size() == 2){
                    list_item_phonecode.add(new ItemPicker(portal_codes.get(i).get(1),portal_codes.get(i).get(0), false));
                }
            }
        }
        edt_choose_phonecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDropDownPhoneCode();
            }
        });
        edt_choose_phonecode.setText("(CN) +86");

        HashMap<String, String> mapCountry = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_COUNTRY);
        edt_choose_country = (EditText) viewFragment.findViewById(R.id.edt_choose_country);
        list_item_country = new ArrayList<>();
        for (Map.Entry entry : mapCountry.entrySet()){
            ItemPicker item_choose = new ItemPicker(entry.getKey().toString(), entry.getValue().toString(), false);
            list_item_country.add(item_choose);
        }
        edt_choose_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDropDownCountry();
            }
        });

        sp_list_country.add(0, "当所在国家");
        final ArrayAdapter<String> adapter_country = new ArrayAdapter<String>(getActivity(), R.layout.list_item_spinner_custom_size, sp_list_country) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY);
                        tv.setEnabled(false);
                    } else {
                        tv.setTextColor(Color.BLACK);
                        tv.setEnabled(true);
                    }
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY);
                        tv.setEnabled(false);
                    } else {
                        tv.setTextColor(Color.BLACK);
                        tv.setEnabled(true);
                    }
                }
                return view;
            }
        };

        final ArrayAdapter<String> adapter_postcode = new ArrayAdapter<String>(EditProfileFragment.this.getActivity(), R.layout.list_item_spinner_custom_size, sp_list_phone_code) {
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    tv.setTextColor(Color.BLACK);
                    tv.setEnabled(true);
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    tv.setTextColor(Color.BLACK);
                    tv.setEnabled(true);
                }
                return view;
            }
        };

        adapter_postcode.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        sp_choose_code_phone.setAdapter(adapter_postcode);
        for (int i = 0; i < sp_list_phone_code.size(); i++) {
            if (sp_list_phone_code.get(i).contains("(CN)")) {
                sp_choose_code_phone.setSelection(i);
                break;
            }
        }

        adapter_country.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        sp_choose_country.setAdapter(adapter_country);
        tv_message = (TextView) viewFragment.findViewById(R.id.tv_message);
        fl_message = (LinearLayout) viewFragment.findViewById(R.id.fl_message);
        sv_main = (ScrollView) viewFragment.findViewById(R.id.sv_main);
        sv_main.setVisibility(View.GONE);
        bt_to_main = (Button) viewFragment.findViewById(R.id.btn_to_main);


        bt_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) EditProfileFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                int pos_country = 0;
                for(ItemPicker itemPicker :list_item_country){
                    if(itemPicker.getChecked() == true){
                        pos_country = Integer.parseInt(itemPicker.getId());
                        break;
                    }
                }
                if (pos_country > 0) {
                    MultipartBody.Part image_part = getImage_Part();
                    UpdateProfile(image_part, pos_country);
                }
            }
        });

        iv_avt = (RoundedImageView) viewFragment.findViewById(R.id.iv_avt);
        iv_avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result;
                String mPermissions[] = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                List<String> listPermissionsNeeded = new ArrayList<>();

                for (String p : mPermissions) {
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(EditProfileFragment.this.getActivity(), p);
                    if(ContextCompat.checkSelfPermission(EditProfileFragment.this.getActivity(), p) == PackageManager.PERMISSION_DENIED){
                        if (ContextCompat.checkSelfPermission(EditProfileFragment.this.getActivity(), p) == PackageManager.PERMISSION_DENIED && !showRationale) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    DialogPermission dialogPermission = new DialogPermission(EditProfileFragment.this.getActivity());
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(dialogPermission.getWindow().getAttributes());
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                    dialogPermission.show();
                                }
                            });
                            return;
                        } else {
                            result = ContextCompat.checkSelfPermission(EditProfileFragment.this.getActivity(), p);
                            if (result != PackageManager.PERMISSION_GRANTED) {
                                listPermissionsNeeded.add(p);
                            }
                        }
                    }
                }

                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions(EditProfileFragment.this.getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1003);
                }
                else {
                    Intent it = Utils.getPickImageIntent(getActivity(), Utils.getStringChooseOption(getActivity()));
                    startActivityForResult(it, RQ_GET_IMAGE);
                }
            }
        });
        GetUser();
        swipeRefreshLayout.setOnRefreshListener(EditProfileFragment.this);

        return viewFragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

    }

    protected void GetUser() {
        pd_dialog = Utils.getPGDialog(getActivity());
//        pd_dialog.show();
        swipeRefreshLayout.setRefreshing(true);
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
                                user = (UserProfile) responseMessage.getData();
                                if (user.getNickname() != null) {
                                    et_name.setText(user.getNickname());
                                }
                                if (user.getFirst_name() != null) {
                                    et_firstname.setText(user.getFirst_name());
                                }
                                if (user.getLast_name() != null) {
                                    et_lastname.setText(user.getLast_name());
                                }
                                if (user.getEmail() != null) {
                                    et_mail.setText(user.getEmail());
                                }
                                if (user.getPhone_number() != null) {
                                    et_phone_number.setText(user.getPhone_number());
                                }
                                if (user.getAlipay_id() != null) {
                                    edt_id.setText(user.getAlipay_id());
                                }
                                if (user.getAlipay_name() != null) {
                                    et_alipayname.setText(user.getAlipay_name());
                                }
                                Boolean isSelected = false;
                                for(ItemPicker itemPicker : list_item_country){
                                    if(String.valueOf(user.getCountry_id()).equals(itemPicker.getId()) == true){
                                        edt_choose_country.setText(itemPicker.getName());
                                        itemPicker.setChecked(true);
                                        isSelected = true;
                                        break;
                                    }
                                }

                                if (isSelected == false) {
                                    for(ItemPicker itemPicker : list_item_country){
                                        if(itemPicker.getName().contains("中国") == true){
                                            edt_choose_country.setText(itemPicker.getName());
                                            itemPicker.setChecked(true);
                                            break;
                                        }
                                    }
                                }
                                LayoutInflater inflater = (LayoutInflater) EditProfileFragment.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View editProfileView = inflater.inflate(R.layout.fragment_edit_profile, null);
                                text_avt = (TextView) editProfileView.findViewById(R.id.text_avt);
                                if (user.getProfile_picture() != null) {
                                    String url_avt = user.getProfile_picture().getUrl();

                                    if (url_avt != null && !url_avt.equals("")) {

                                        ImageLoader il = ImageLoader.getInstance();
                                        il.displayImage(url_avt, iv_avt, OPTION_DISPLAY_IMG_DEFAULT);
                                    }
                                    text_avt.setVisibility(View.GONE);
                                } else {
                                    text_avt.setVisibility(View.VISIBLE);
                                }
                                sv_main.setVisibility(View.VISIBLE);
                            }
                        } else {
                            try {
                                ErrorMessage error = responseMessage.getErrors();
                                Toast.makeText(getActivity(), error.getStringError(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Crashlytics.logException(e);
                            }
                        }
                    } else {
                        getListener().logOut();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(false);
                } catch (Exception e) {
                    Utils.crashLog(e);
                    Crashlytics.logException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                Utils.OnFailException(t);
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    /**
     * Update profile
     *
     * @params:
     * @return;
     */
    public void UpdateProfile(MultipartBody.Part image_part, final int pos_country) {
        ApiServices services = ApiServices.getInstance();
        WebService api = services.getRetrofit().create(WebService.class);
        int country_to_post = 0;

        Call<ResponseMessage<UserProfile>> call;
        processDialogUpdate = Utils.createProgressDialog(EditProfileFragment.this.getActivity());
        if (image_part == null) {
            call = api.actionUpdateProfile(et_firstname.getText().toString(), et_lastname.getText().toString(), et_name.getText().toString(),
                    et_mail.getText().toString(), pos_country, et_phone_number.getText().toString());
        } else {
            call = api.actionUpdateProfile(et_firstname.getText().toString(), et_lastname.getText().toString(), et_name.getText().toString(),
                    et_mail.getText().toString(), pos_country, image_part, et_phone_number.getText().toString());
        }
        call.enqueue(new Callback<ResponseMessage<UserProfile>>() {
            @Override
            public void onResponse(Call<ResponseMessage<UserProfile>> call, Response<ResponseMessage<UserProfile>> response) {
                try {
                    ResponsePacket response_packet = CustomResponse.getResponseMessage(response);
                    ResponseMessage responseMessage;
                    if (response_packet.isSet()) {
                        responseMessage = response_packet.getResponseMessage();
                        if (response_packet.isSuccess()) {
                            if (responseMessage.isSuccess()) {
                                fl_message.setVisibility(View.GONE);
                                UserProfile user = (UserProfile) responseMessage.getData();
//                                    PostCountry(pos_country);
                               // UpdateAlipay(true, edt_id.getText().toString(), et_alipayname.getText().toString());
                                Headers header = response.headers();
                                MyPreferences.setUID(header.get(MyPreferences.UID_FIELD));
                                MyPreferences.setToken(header.get(MyPreferences.TOKEN_FIELD));
                                MyPreferences.setClient(header.get(MyPreferences.CLIENT_FIELD));
                                MyPreferences.setExpiryField(header.get(MyPreferences.EXPIRY_FIELD));
                                backPress();
                            }
                        } else {
                            ErrorMessage error = responseMessage.getErrors();
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
                                tv_check_fill.setText(strError);
                                ln_check_fill.setVisibility(View.VISIBLE);
                            }


                        }
                        processDialogUpdate.dismiss();
                    } else {
                        processDialogUpdate.dismiss();
                        backPress();
                    }
                } catch (Exception e) {
                    if (pd_dialog != null) {
                        processDialogUpdate.dismiss();
                    }
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<UserProfile>> call, Throwable t) {
                if (processDialogUpdate != null) {
                    processDialogUpdate.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
//        }
    }

    /**
     * Update alipay
     *
     * @params:
     * @return:
     */
    public void UpdateAlipay(boolean checkDetails, String alipayID, String alipayName) {
        ApiServices services = ApiServices.getInstance();
        WebService api = services.getRetrofit().create(WebService.class);
        Call<ResponseMessage> callUpdateAlipay = api.actionUpdateAlipay(checkDetails, alipayID, alipayName);
        callUpdateAlipay.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getActivity(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Utils.OnFailException(t);
            }
        });
    }

    @Override
    public void onItemsSelected(boolean[] selected, View view) {
        if (sp_choose_country.getSelectedItem().toString().equals("")) {
            //hintSpinner1.init();
        }
    }

    @Override
    public void onRefresh() {

    }

    public interface MyCallBack {
        void changeMain();

        void changeSetupVerify();

        void logOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RQ_GET_IMAGE) {
            if (resultCode == RESULT_OK) {
                Utils.getImageView(iv_avt, data, getActivity());
                state_image = true;
            }
        }
    }

    private MultipartBody.Part getImage_Part() {
        MultipartBody.Part image_part = null;
        try {
            Bitmap bm = ((BitmapDrawable) iv_avt.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            image_part = MultipartBody.Part.createFormData("profile_picture", "profile_picture.png", RequestBody.create(MediaType.parse("image/*"), bitmapdata));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image_part;
    }

    private void showDialogDropDownCountry(){

        DialogDropdown dropdown = new DialogDropdown(this.getActivity(), false, list_item_country, edt_choose_country);

        dropdown.setCanceledOnTouchOutside(true);
        dropdown.show();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dropdown.getWindow().getAttributes());

        layoutParams.width = size.x*4/5;
        layoutParams.height = size.y*4/5;
        dropdown.getWindow().setAttributes(layoutParams);
    }

    private void showDialogDropDownPhoneCode(){

        DialogDropdown dropdown = new DialogDropdown(this.getActivity(), false, list_item_phonecode, edt_choose_phonecode);

        dropdown.setCanceledOnTouchOutside(true);
        dropdown.show();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dropdown.getWindow().getAttributes());

        layoutParams.width = size.x*4/5;
        layoutParams.height = size.y*4/5;
        dropdown.getWindow().setAttributes(layoutParams);
    }

}
