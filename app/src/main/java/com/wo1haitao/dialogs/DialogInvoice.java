package com.wo1haitao.dialogs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.wo1haitao.R;
import com.wo1haitao.activities.BaseActivity;
import com.wo1haitao.api.ApiServices;
import com.wo1haitao.api.WebService;
import com.wo1haitao.api.response.ErrorMessage;
import com.wo1haitao.api.response.ResponseMessage;
import com.wo1haitao.controls.CustomEditextSoftkey;
import com.wo1haitao.fragments.MyBidFragment;
import com.wo1haitao.models.OrderModel;
import com.wo1haitao.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.wo1haitao.utils.Utils.getImageUri;
import static com.wo1haitao.utils.Utils.getPickImageIntent;

/**
 * Created by goodproductssoft on 4/18/17.
 */

public class DialogInvoice extends Dialog {
    public Activity activity;
    TextView tv_transaction_number, tv_post_invoice, tv_message, name_shipping_company;
    LinearLayout ln_post_invoice;
    ImageView iv_invoice_status, iv_back_action;
    CustomEditextSoftkey ed_tracking_number;
    ImageView bt_upload_picture_invoice;
    ImageView img_picture_invoice, img_picture_invoice_update, iv_delete_invoice1, iv_delete_invoice2;
    BaseActivity baseActivity;
    int idOrder;
    OrderModel orderModel = null;
    ImageLoader imageLoader;
    FrameLayout fl_message, fr_invoice_upload1, fr_invoice_upload2, fl_message_info;
    LinearLayout ln_form_view;
    boolean is_updated_invoice = false;
    static String STATE_VERIFY_OPEN = "inv_opened";
    static String STATE_VERIFY_REJECT = "inv_rejected";
    static String STATE_VERIFY_ACCEPT = "inv_accepted";

    boolean isNotification = false;

    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean notification) {
        isNotification = notification;
    }

    public MyBidFragment getMyBidFragment() {
        return myBidFragment;
    }

    public void setMyBidFragment(MyBidFragment myBidFragment) {
        this.myBidFragment = myBidFragment;
    }

    MyBidFragment myBidFragment;

    public DialogInvoice(Activity a, int idOrder, MyBidFragment f) {
        super(a);
        is_updated_invoice = false;
        // TODO Auto-generated constructor stub
        this.activity = a;
        this.idOrder = idOrder;
        this.myBidFragment = f;
        //
    }

    boolean isShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_invoice);
        imageLoader = ImageLoader.getInstance();
        ln_post_invoice = (LinearLayout) findViewById(R.id.ln_post_invoice);
        tv_transaction_number = (TextView) findViewById(R.id.tv_transaction_number);
        iv_invoice_status = (ImageView) findViewById(R.id.iv_invoice_status);
        tv_post_invoice = (TextView) findViewById(R.id.tv_post_invoice);
        ed_tracking_number = (CustomEditextSoftkey) findViewById(R.id.ed_tracking_number);
        bt_upload_picture_invoice = (ImageView) findViewById(R.id.bt_upload_picture_invoice);
        img_picture_invoice = (ImageView) findViewById(R.id.img_picture_invoice);
        img_picture_invoice_update = (ImageView) findViewById(R.id.img_picture_invoice_update);
        iv_delete_invoice1 = (ImageView) findViewById(R.id.iv_delete_invoice1);
        iv_delete_invoice2 = (ImageView) findViewById(R.id.iv_delete_invoice2);
        iv_back_action = (ImageView) findViewById(R.id.iv_back_action);
        tv_message = (TextView) findViewById(R.id.tv_message);
        name_shipping_company = (TextView) findViewById(R.id.name_shipping_company);
        fl_message = (FrameLayout) findViewById(R.id.fl_message);
        fr_invoice_upload1 = (FrameLayout) findViewById(R.id.fr_invoice_upload1);
        fr_invoice_upload2 = (FrameLayout) findViewById(R.id.fr_invoice_upload2);
        fl_message_info = (FrameLayout) findViewById(R.id.fl_message_info);
        ln_form_view = (LinearLayout) findViewById(R.id.ll_form_content);
        GetImage(img_picture_invoice);

        if (idOrder != 0) {
            GetInvoice(idOrder);
            //this.show();
        }
        tv_post_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_updated_invoice == false){
                    if (orderModel != null) {
                        if(img_picture_invoice_update.getDrawable() == null) {
                            PostInvoid(img_picture_invoice);
                        }
                        else {
                            PostInvoid(img_picture_invoice_update);
                        }
                    }
                }
                else{
                    if (DialogInvoice.this.getMyBidFragment() != null && DialogInvoice.this.getMyBidFragment() instanceof MyBidFragment) {
                        MyBidFragment fragment = (MyBidFragment) DialogInvoice.this.getMyBidFragment();
                        fragment.InitViewMyBid();
                    }
                    dismiss();
                }

            }
        });

        iv_back_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /**
     * Get picture invoice
     *
     * @params: InvoiceImageView
     * @return:
     */
    public void GetImage(final ImageView img_picture_invoice) {
        baseActivity = (BaseActivity) activity;
        baseActivity.setActivityCallback(new BaseActivity.ActivityCallback() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == 0) {
                    if (resultCode == RESULT_OK) {
                        if (img_picture_invoice != null) {
                            Bitmap bitmap = null;
                            if (data != null && data.getData() != null) {
                                Uri selectedImage = data.getData();
                                try {
                                    img_picture_invoice.setVisibility(View.VISIBLE);
                                    bt_upload_picture_invoice.setVisibility(View.GONE);
                                    bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImage);
                                    bitmap = getResizedBitmap(bitmap, 500);
                                    bitmap = rotate(bitmap, getRotationFromGallery(activity, selectedImage));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if(data == null){
                                    File filePicture = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                                    String picture_name = Utils.IMAGE_CATURE_PRODUCT;
                                    File image_file = new File(filePicture,picture_name);
                                    Uri image_uri =  Uri.fromFile(image_file);

                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), image_uri);
                                        bitmap = getResizedBitmap(bitmap, 500);
                                        bitmap = rotate(bitmap,getRotationFromCamera(activity,image_uri));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    Intent dt = data;
                                    Bundle extras = data.getExtras();
                                    if (extras != null) {
                                        Uri image_uri = (Uri) extras.get(MediaStore.EXTRA_OUTPUT);
                                        bitmap = (Bitmap) extras.get("data");
                                        bitmap = getResizedBitmap(bitmap, 500);
                                        Uri tempUri = getImageUri(baseActivity, bitmap);

                                    }
                                }
                            }
                            if(img_picture_invoice.getDrawable() != null){
//                                img_picture_invoice_update.setVisibility(View.VISIBLE);
                                img_picture_invoice_update.setImageBitmap(bitmap);
                                fr_invoice_upload2.setVisibility(View.VISIBLE);
                                iv_delete_invoice2.setVisibility(View.VISIBLE);
                                iv_delete_invoice1.setVisibility(View.GONE);
                            }
                            else {
                                iv_delete_invoice1.setVisibility(View.VISIBLE);
                                iv_delete_invoice2.setVisibility(View.GONE);
//                                img_picture_invoice.setVisibility(View.VISIBLE);
                                img_picture_invoice.setImageBitmap(bitmap);
                                fr_invoice_upload1.setVisibility(View.VISIBLE);
                            }
                            bt_upload_picture_invoice.setVisibility(View.GONE);
//                            img_picture_invoice.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    Intent it = getPickImageIntent(baseActivity, getStringChooseOption(baseActivity));
//                                    baseActivity.startActivityForResult(it, 0);
//                                }
//                            });
                        }
                    }
                }
            }
        });
        bt_upload_picture_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result;
                String mPermissions[] = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                List<String> listPermissionsNeeded = new ArrayList<>();

                for (String p : mPermissions) {
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(DialogInvoice.this.myBidFragment.getActivity(), p);
                    if(ContextCompat.checkSelfPermission(DialogInvoice.this.myBidFragment.getActivity(), p) == PackageManager.PERMISSION_DENIED){
                        if (ContextCompat.checkSelfPermission(DialogInvoice.this.myBidFragment.getActivity(), p) == PackageManager.PERMISSION_DENIED && !showRationale) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    DialogPermission dialogPermission = new DialogPermission(DialogInvoice.this.myBidFragment.getActivity());
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(dialogPermission.getWindow().getAttributes());
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                    dialogPermission.show();
                                }
                            });
                            return;
                        } else {
                            result = ContextCompat.checkSelfPermission(DialogInvoice.this.myBidFragment.getActivity(), p);
                            if (result != PackageManager.PERMISSION_GRANTED) {
                                listPermissionsNeeded.add(p);
                            }
                        }
                    }
                }

                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions(DialogInvoice.this.myBidFragment.getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1003);
                }
                else {
                    Intent it = getPickImageIntent(baseActivity, getStringChooseOption(baseActivity));
                    baseActivity.startActivityForResult(it, 0);
                }
            }
        });
        iv_delete_invoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_picture_invoice.setImageDrawable(null);
                fr_invoice_upload1.setVisibility(View.GONE);
                bt_upload_picture_invoice.setVisibility(View.VISIBLE);
            }
        });
        iv_delete_invoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_picture_invoice_update.setImageDrawable(null);
                fr_invoice_upload2.setVisibility(View.GONE);
                bt_upload_picture_invoice.setVisibility(View.VISIBLE);
            }
        });
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String getStringChooseOption(Activity activity) {
        String packageName = activity.getPackageName();
        int resId = activity.getResources().getIdentifier("dialog_pick_image_choose_option", "string", packageName);
        return activity.getString(resId);
    }

    private static int getRotationFromCamera(Context context, Uri imageFile) {
        int rotate = 0;
        try {

            context.getContentResolver().notifyChange(imageFile, null);
            ExifInterface exif = new ExifInterface(imageFile.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static int getRotationFromGallery(Context context, Uri imageUri) {
        int result = 0;
        String[] columns = {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int orientationColumnIndex = cursor.getColumnIndex(columns[0]);
                result = cursor.getInt(orientationColumnIndex);
            }
        } catch (Exception e) {
            //Do nothing
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }//End of try-catch block
        return result;
    }

    private void actionWhenWUpdated(){
        is_updated_invoice = true;
        tv_post_invoice.setText("关闭");
        ln_form_view.setVisibility(View.GONE);
        fl_message_info.setVisibility(View.VISIBLE);
    }

    private static Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bmOut = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            return bmOut;
        }
        return bm;
    }
    /**
     * Get invoice API
     *
     * @params: idOrder
     * @return:
     */
    public void GetInvoice(int id) {
        final ProgressDialog progressDialog = Utils.createProgressDialog(activity);
        final ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage<OrderModel>> callGetVoice = ws.actionGetInvoice(id);
        callGetVoice.enqueue(new Callback<ResponseMessage<OrderModel>>() {
            @Override
            public void onResponse(Call<ResponseMessage<OrderModel>> call, Response<ResponseMessage<OrderModel>> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {
                        orderModel = response.body().getData();
                        if (orderModel.getInvoice_status().equals(STATE_VERIFY_REJECT)
                                || !orderModel.isOrder_invoice_present()) {
                            img_picture_invoice.setImageDrawable(null);
//                            img_picture_invoice.setVisibility(View.GONE);
                            fr_invoice_upload1.setVisibility(View.GONE);
                            ed_tracking_number.setText(orderModel.getTracking_no());
                            name_shipping_company.setText(orderModel.getShipping_company_name());
                            iv_invoice_status.setImageResource(R.drawable.open_ivoice);
                            bt_upload_picture_invoice.setVisibility(View.VISIBLE);
                            ln_post_invoice.setVisibility(View.VISIBLE);
                        } else {
                            if (orderModel.getOrder_invoice().getUrl() != null) {
                                int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, DialogInvoice.this.getContext().getResources().getDisplayMetrics());
                                fr_invoice_upload1.getLayoutParams().height = px;
                                fr_invoice_upload1.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                                String url_avt = orderModel.getOrder_invoice().getUrl();
                                DiskCacheUtils.removeFromCache(url_avt, ImageLoader.getInstance().getDiskCache());
                                MemoryCacheUtils.removeFromCache(url_avt, ImageLoader.getInstance().getMemoryCache());
                                imageLoader.displayImage(url_avt, img_picture_invoice);
                            }
//                            img_picture_invoice.setVisibility(View.VISIBLE);
                            fr_invoice_upload1.setVisibility(View.VISIBLE);
//                            ln_post_invoice.setVisibility(View.GONE);
//                            bt_upload_picture_invoice.setVisibility(View.GONE);
                            ed_tracking_number.setText(orderModel.getTracking_no());
                            name_shipping_company.setText(orderModel.getShipping_company_name());
                            iv_invoice_status.setImageResource(R.drawable.accept_invoice);
                        }

                        tv_transaction_number.setText(orderModel.getOrder_no());
//                        tv_invoice_status.setText(orderModel.getInvoice_status());
                        progressDialog.dismiss();
                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
                            Toast.makeText(getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Crashlytics.logException(e);
                            Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getContext(), R.string.no_advesting, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Utils.crashLog(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage<OrderModel>> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utils.OnFailException(t);
            }
        });
    }

    /**
     * Create invoice
     *
     * @params:
     */

    public void PostInvoid(ImageView imageViewData) {
        MultipartBody.Part imageInvoice = Utils.createImagePart("order_invoice", "order_invoice" + ".png", imageViewData);
        String strakingNo = ed_tracking_number.getText().toString();
        String strNameShippingCompany = name_shipping_company.getText().toString();
        final ProgressDialog progressDialog = Utils.createProgressDialog(activity);
        final ApiServices api = ApiServices.getInstance();
        WebService ws = api.getRetrofit().create(WebService.class);
        Call<ResponseMessage> callPostInvoice;
        if(imageInvoice != null){
            callPostInvoice = ws.actionCreateInvoice(orderModel.getId(), strakingNo, imageInvoice, true, strNameShippingCompany);
        }
        else {
            callPostInvoice = ws.actionCreateInvoice(orderModel.getId(), strakingNo, true, strNameShippingCompany);
        }

        callPostInvoice.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {
                    if (response.body() != null && response.isSuccessful()) {

                        actionWhenWUpdated();
                        progressDialog.dismiss();

                    } else if (response.errorBody() != null) {
                        try {
                            ResponseMessage responseMessage = ApiServices.getGsonBuilder().create().fromJson(response.errorBody().string(), ResponseMessage.class);
                            ErrorMessage error = responseMessage.getErrors();
//                            Toast.makeText(getContext(), "Can't get data... " + error.getStringErrFormList(), Toast.LENGTH_SHORT).show();
                            String strError = "" ;
                            ArrayList<String> list_error = responseMessage.getError_messages();
                            if(list_error != null){
                                for(String error_item :list_error){
                                    if(strError.isEmpty() == true){
                                        strError = "请修改以下:\n "+error_item;
                                    }
                                    else{
                                        strError += "\n "+error_item;
                                    }
                                }
                            }
                            if(!strError.equals("")){
                                fl_message.setVisibility(View.VISIBLE);
                                tv_message.setText(strError);
                            }
                        } catch (Exception e) {
                            Crashlytics.logException(e);
                            Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
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
