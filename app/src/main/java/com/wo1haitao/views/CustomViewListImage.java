package com.wo1haitao.views;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.CustomApp;
import com.wo1haitao.R;
import com.wo1haitao.activities.BaseActivity;
import com.wo1haitao.dialogs.DialogPermission;
import com.wo1haitao.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.wo1haitao.utils.Utils.getPickImageIntent;

/**
 * Created by user on 4/26/2017.
 */

public class CustomViewListImage extends LinearLayout {
    ArrayList<ImageView> list_image, list_btn_delete;
    ArrayList<ImageView> list_image_data;
    Map<Integer, Integer> list_destroy;
    Fragment my_fragment;
    final int REQUEST_ID_MULTIPLE_PERMISSIONS = 10001;
    public Fragment getMy_fragment() {
        return my_fragment;
    }

    public void setMy_fragment(Fragment my_fragment) {
        this.my_fragment = my_fragment;
    }

    public ArrayList<Integer> getList_IDImage() {
        return list_IDImage;
    }

    public void setList_IDImage(ArrayList<Integer> list_IDImage) {
        while (list_IDImage.size() < 3){
            list_IDImage.add(0);
        }
        this.list_IDImage = list_IDImage;
    }

    ArrayList<Integer> list_IDImage;

    public Map<Integer, Integer> getList_destroy() {
        return list_destroy;
    }

    public void setList_destroy(Map<Integer, Integer> list_destroy) {
        this.list_destroy = list_destroy;
    }

    ImageView current_image;
    int line;
    Context my_context;
    //private OnClickListener myClick;
    OnClickListener pick_image_click;
    public static final int REQUEST_IMAGE_CODE = 10009;

    MyCallBack listener;

    public void setListener(MyCallBack listener) {
        this.listener = listener;
    }

    public CustomViewListImage(Context context) {
        super(context);
        my_context = context;
        setOrientation(VERTICAL);
        initView();
    }

    public CustomViewListImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        my_context = context;
        setOrientation(VERTICAL);
        initView();
    }

    public void getView(ArrayList<ImageView> list_image_data, OnClickListener my_click) {
        this.list_image_data = list_image_data;
        if (list_image_data.size() > 0) {
            removeAllViews();
            list_image = new ArrayList<>();
            initView();
            for (int i = 0; i < list_image_data.size(); i++) {
                list_image.get(i).setImageDrawable(list_image_data.get(i).getDrawable());
                if ((i + 1) % 3 == 0 && i > 0) {
                    initView();
                }
            }
            setMyClick(my_click, true);
        }
    }

    public void setViewFromUrls(ArrayList<String> list_url_image) {
//        while (list_image.size() < list_url_image.size()) {
//            addViewLine();
//        }

        for (int i = 0; i < list_url_image.size(); i++) {
            if(i == 3 ) {
                break;
            }
            ImageLoader.getInstance().displayImage(list_url_image.get(i), list_image.get(i));
            list_image_data.add(list_image.get(i));
            list_btn_delete.get(i).setVisibility(VISIBLE);
        }

    }

    public void initView() {
        removeAllViews();
        list_IDImage = new ArrayList<>();
        list_destroy = new HashMap<>();
        list_image = new ArrayList<>();
        list_image_data = new ArrayList<>();
        line = 0;
        if (CustomApp.getInstance().getCurrentActivity() instanceof BaseActivity) {
            final BaseActivity baseActivity = (BaseActivity)CustomApp.getInstance().getCurrentActivity();
            baseActivity.setActivityCallback(new BaseActivity.ActivityCallback() {
                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    if (current_image != null && resultCode == RESULT_OK) {
                        getImageView(current_image, data);
                        if (!check_iv_exist(current_image)) {
                            list_image_data.add(current_image);
                            for (int i = 0; i < list_image.size(); i++) {
                                if (current_image == list_image.get(i)) {
                                    list_btn_delete.get(i).setVisibility(VISIBLE);
                                }
                            }
                        }
                    }
                }
            });
            pick_image_click = new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int result;
                    String mPermissions[] = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                    List<String> listPermissionsNeeded = new ArrayList<>();

                    for (String p : mPermissions) {
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(CustomViewListImage.this.my_fragment.getActivity(), p);
                        if(ContextCompat.checkSelfPermission(CustomViewListImage.this.my_fragment.getActivity(), p) == PackageManager.PERMISSION_DENIED){
                            if (ContextCompat.checkSelfPermission(CustomViewListImage.this.my_fragment.getActivity(), p) == PackageManager.PERMISSION_DENIED && !showRationale) {
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        DialogPermission dialogPermission = new DialogPermission(CustomViewListImage.this.my_fragment.getActivity());
                                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                        lp.copyFrom(dialogPermission.getWindow().getAttributes());
                                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                        dialogPermission.show();
                                    }
                                });
                                return;
                            } else {
                                result = ContextCompat.checkSelfPermission(CustomViewListImage.this.my_fragment.getActivity(), p);
                                if (result != PackageManager.PERMISSION_GRANTED) {
                                    listPermissionsNeeded.add(p);
                                }
                            }
                        }
                    }

                    if (!listPermissionsNeeded.isEmpty()) {
                        ActivityCompat.requestPermissions(CustomViewListImage.this.my_fragment.getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1003);
                    }
                    else {
                        current_image = (ImageView) view;
                        Intent it = getPickImageIntent(baseActivity, getStringChooseOption(baseActivity));
                        if (my_fragment != null) {
                            my_fragment.startActivityForResult(it, REQUEST_IMAGE_CODE);
                        } else {
                            baseActivity.startActivityForResult(it, REQUEST_IMAGE_CODE);
                        }
                    }
                }
            };
        }
        addViewLine();
        setMyClick(pick_image_click, true);
    }

    private  boolean checkPermissions() {
        int result;
        String mPermissions[] = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p : mPermissions) {
            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(CustomViewListImage.this.my_fragment.getActivity(), p);
            if(!showRationale){
                DialogPermission dialogPermission = new DialogPermission(CustomViewListImage.this.my_fragment.getActivity());
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialogPermission.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialogPermission.show();
                return true;
            }
            else {
                result = ContextCompat.checkSelfPermission(CustomViewListImage.this.my_fragment.getActivity(), p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(CustomViewListImage.this.my_fragment.getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void setViewOnResult(Intent data){
        if (current_image != null) {
            getImageView(current_image, data);
            if (!check_iv_exist(current_image)) {
                list_image_data.add(current_image);
                for (int i = 0; i < list_image.size(); i++) {
                    if (current_image == list_image.get(i)) {
                        list_btn_delete.get(i).setVisibility(VISIBLE);
                    }
                }
            }
        }
    }

    private void addViewLine() {
        list_btn_delete = new ArrayList<>();

        View view = inflate(getContext(), R.layout.view_custom_three_image, null);
        final ImageView iv_1 = (ImageView) view.findViewById(R.id.imageview_picker1);
        final ImageView iv_2 = (ImageView) view.findViewById(R.id.imageview_picker2);
        final ImageView iv_3 = (ImageView) view.findViewById(R.id.imageview_picker3);
        final ImageView iv_delete1 = (ImageView) view.findViewById(R.id.iv_delete1);
        final ImageView iv_delete2 = (ImageView) view.findViewById(R.id.iv_delete2);
        final ImageView iv_delete3 = (ImageView) view.findViewById(R.id.iv_delete3);
        iv_delete1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_1.setImageResource(R.drawable.plus_with_bg);
                for (int i = 0; i < list_image_data.size(); i++) {
                    if (iv_1 == list_image_data.get(i)) {
                        iv_delete1.setVisibility(GONE);
                        list_image_data.remove(i);
                        if (list_IDImage.size() > 0 && list_IDImage.get(0) != 0) {
                            int key = 0;
                            int value = list_IDImage.get(0);
                            list_IDImage.set(0, 0);
                            list_destroy.put(key, value);
                        }
                        break;
                    }
                }
            }
        });
        iv_delete2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_2.setImageResource(R.drawable.plus_with_bg);
                for (int i = 0; i < list_image_data.size(); i++) {
                    if (iv_2 == list_image_data.get(i)) {
                        iv_delete2.setVisibility(GONE);
                        list_image_data.remove(i);
                        if (list_IDImage.size() > 0 && list_IDImage.get(1) != 0) {
                            int key = 1;
                            int value = list_IDImage.get(1);
                            list_IDImage.set(1, 0);
                            list_destroy.put(key, value);
                        }
                        break;
                    }
                }
            }
        });
        iv_delete3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_3.setImageResource(R.drawable.plus_with_bg);
                for (int i = 0; i < list_image_data.size(); i++) {
                    if (iv_3 == list_image_data.get(i)) {
                        iv_delete3.setVisibility(GONE);
                        list_image_data.remove(i);
                        if (list_IDImage.size() > 0 && list_IDImage.get(2) != 0) {
                            int key = 2;
                            int value = list_IDImage.get(2);
                            list_IDImage.set(2, 0);
                            list_destroy.put(key, value);
                        }
                        break;
                    }
                }
            }
        });
        list_btn_delete.add(iv_delete1);
        list_btn_delete.add(iv_delete2);
        list_btn_delete.add(iv_delete3);
        list_image.add(iv_1);
        list_image.add(iv_2);
        list_image.add(iv_3);
        addView(view);
    }

    public  void HineButtonDelete(){
        for(int i = 0; i < list_btn_delete.size(); i++){
            list_btn_delete.get(i).setVisibility(GONE);
            list_image.get(i).setEnabled(false);
        }
    }

    public void setMyClick(OnClickListener myClick, boolean isAll) {
        int count = -1;
        if (isAll == true) {
            count = 0;
        } else {
            count = list_image.size() - 3;
        }
        for (int i = count; i < list_image.size(); i++) {
            list_image.get(i).setOnClickListener(myClick);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (list_image.get(0) != null) {
            for (ImageView image : list_image) {
                image.getLayoutParams().width = w / 3;
                image.getLayoutParams().height = w / 3;
            }
        }
    }

    public void setList_image(ArrayList<ImageView> list_image) {
        this.list_image = list_image;
    }

    public static String getStringChooseOption(Activity activity) {
        String packageName = activity.getPackageName();
        int resId = activity.getResources().getIdentifier("dialog_pick_image_choose_option", "string", packageName);
        return activity.getString(resId);
    }

    public Intent getImageClick(final View view, final Activity activity) {
        current_image = (ImageView) view;
        Intent it = getPickImageIntent(activity, getStringChooseOption(activity));
        return it;
    }


    public void getActivityRs(Intent data, OnClickListener myClick) {
        if (current_image != null) {
            getImageView(current_image, data);
            if (!check_iv_exist(current_image)) {
                list_image_data.add(current_image);

                if (list_image_data.size() % 3 == 0 && list_image_data.size() != 0) {
                    initView();
                    setMyClick(myClick, false);
                }
            }
        }
    }

    boolean check_iv_exist(ImageView iv) {
        for (int i = 0; i < list_image_data.size(); i++) {
            if (iv == list_image_data.get(i))
                return true;
        }
        return false;
    }


    public void getImageView(ImageView ivPicker, Intent data) {


        if (data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String path = getRealPathFromURI(selectedImage);

            int degree = Utils.getRotationFromGallery(my_context, selectedImage);

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(my_context.getContentResolver(), selectedImage);
                bitmap = Utils.getResizedBitmap(bitmap, 500);
                if (degree != 0) {
                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();

                    // Setting pre rotate
                    Matrix mtx = new Matrix();
                    mtx.preRotate(degree);

                    // Rotating Bitmap & convert to ARGB_8888, required by tess
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);

                }
                ivPicker.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File filePicture = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String picture_name = Utils.IMAGE_CATURE_PRODUCT;
            File image_file = new File(filePicture, picture_name);
            Uri image_uri = Uri.fromFile(image_file);
            if(image_file == null){
                return;
            }

            try {
                BitmapFactory.Options bounds = new BitmapFactory.Options();
                bounds.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(image_file.getAbsolutePath(), bounds);
                int degree = Utils.getRotationFromCamera(this.my_context, image_uri);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), image_uri);
                if (degree != 0) {
                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();

                    // Setting pre rotate
                    Matrix mtx = new Matrix();
                    mtx.preRotate(degree);
                    // Rotating Bitmap & convert to ARGB_8888, required by tess
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);

                }

                bitmap = Utils.getResizedBitmap(bitmap, 500);
                ivPicker.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public Map<String, RequestBody> getRequestBodyData() {
        Map<String, RequestBody> map = new HashMap<>();
        for (int i = 0; i < list_image_data.size(); i++) {
            String key = "product_images_attributes[" + i + "][product_image]";
            ImageView iv = list_image_data.get(i);
            RequestBody value = Utils.createRequestBody(iv);

            key = key + "\"; filename=product_images" + i + ".png\"";

            map.put(key, value);
        }
        return map;
    }


    public LinkedHashMap<String, MultipartBody.Part> getBitmapData(Activity activity) {
        LinkedHashMap<String, MultipartBody.Part> map = new LinkedHashMap<>();
        ArrayList<ImageView> list_image_data_sort = new ArrayList<>();

        for (int i = 0; i < list_image.size(); i++) {
            if (check_iv_exist(list_image.get(i))) {
                list_image_data_sort.add(list_image.get(i));
            }
        }
        for (int i = 0; i < list_image_data_sort.size(); i++) {
            String key = "product_images_attributes[" + i + "][product_image]";
            ImageView iv = list_image_data_sort.get(i);
            MultipartBody.Part value = Utils.createImagePart(key, key, iv);
            map.put(key, value);
        }
        return map;
    }

    public List<MultipartBody.Part> getListBitmapData(){
        List<MultipartBody.Part> map = new ArrayList<>();
        ArrayList<ImageView> list_image_data_sort = new ArrayList<>();

        for(int i = 0; i < list_image.size(); i++){
            if(check_iv_exist(list_image.get(i))){
                list_image_data_sort.add(list_image.get(i));
            }
        }
        for(int i = 0; i< list_image_data_sort.size(); i++ ){
            String key = "product_images_attributes["+i+"][product_image]";
            ImageView iv =list_image_data_sort.get(i);
            MultipartBody.Part value = Utils.createImagePart(key, key, iv);
            map.add(value);
        }
        return map;
    }

    public List<MultipartBody.Part> getListBitmapDataDelete() {
        List<MultipartBody.Part> map = new ArrayList<>();
        ArrayList<ImageView> list_image_data_sort = new ArrayList<>();

        for (int i = 0; i < list_image.size(); i++) {
            if (check_iv_exist(list_image.get(i))) {
                list_image_data_sort.add(list_image.get(i));
            }
        }
        int index = 0;
        for (Map.Entry t : list_destroy.entrySet()) {
            String key1 = "product_images_attributes[" + index + "][id]";
            String value1 = t.getValue().toString();
            String key2 = "product_images_attributes[" + index + "][_destroy]";
            String value2 = "1";
            MultipartBody.Part valueDestroy = Utils.createStringPart(key1, value1);
            MultipartBody.Part valueDestroy2 = Utils.createStringPart(key2, value2);
            map.add(valueDestroy);
            map.add(valueDestroy2);
            index++;
        }
        for (int i = 0; i < list_image.size(); i++) {
            if (check_iv_exist(list_image.get(i))) {
                if( list_IDImage.get(i) != 0){
                    String key1 = "product_images_attributes[" + index + "][id]";
                    String value1 = list_IDImage.get(i).toString();
                    MultipartBody.Part valueDestroy = Utils.createStringPart(key1, value1);
                    map.add(valueDestroy);
                }
                String key = "product_images_attributes[" + index + "][product_image]";
                ImageView iv = list_image.get(i);
                MultipartBody.Part value = Utils.createImagePart(key, key, iv);
                map.add(value);

                index++;
            }
        }
//        for (int i = 0; i < list_image_data_sort.size(); i++) {
//            String key = "product_images_attributes[" + i + "][product_image]";
//            ImageView iv = list_image_data_sort.get(i);
//            MultipartBody.Part value = Utils.createImagePart(key, key, iv);
//            map.add(value);
//        }
        return map;
    }

    public ArrayList<ImageView> getList_image_data() {
        return list_image_data;
    }

    public void setList_image_data(ArrayList<ImageView> list_image_data) {
        this.list_image_data = list_image_data;
    }

    public interface MyCallBack {
        void onCreateNewLine();
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = this.my_context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }




}
