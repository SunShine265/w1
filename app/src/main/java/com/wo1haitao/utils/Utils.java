package com.wo1haitao.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.wo1haitao.CustomApp;
import com.wo1haitao.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by user on 4/28/2017.
 */

public class Utils {
    public static final String TEMP_IMG = "temp_product_image_wo1haitao.jpg";
    public static final int RQ_CODE_GET_IMAGE = 1005;
    static Activity activity_this;
    public static final int SMALL_SIZE_SCREEN_DP = 1920;

    public static void setTitle(View view, int title_id) {
        TextView tv = (TextView) view.findViewById(R.id.mytext);
        tv.setText(title_id);
    }
    public static Point getLocationOnScreen(View view){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
    }
    public static void crashLog(Exception e) {
        Crashlytics.logException(e);
        Toast.makeText(CustomApp.getInstance(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
    }

    public static void OnFailException(Throwable t) {
        if (t instanceof java.net.UnknownHostException
                || t instanceof java.net.BindException
                || t instanceof java.net.ConnectException
                || t instanceof java.net.HttpRetryException
                || t instanceof java.net.MalformedURLException
                || t instanceof java.net.NoRouteToHostException
                || t instanceof java.net.PortUnreachableException
                || t instanceof java.net.ProtocolException
                || t instanceof java.net.UnknownServiceException
                || t instanceof java.net.URISyntaxException) {
            Toast.makeText(CustomApp.getInstance(), R.string.not_network, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CustomApp.getInstance(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
            Crashlytics.logException(t);
        }
    }

    public static int getRotationFromCamera(Context context, Uri imageFile) {
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

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
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

    public static void setActionBack(View view, final Activity activity) {
        ImageView iv_back = (ImageView) view.findViewById(R.id.ib_back_action);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
    }

    public static void setTextActionBar(View view, int title_id, final Activity activity) {
        TextView tv = (TextView) view.findViewById(R.id.mytext);
        tv.setText(title_id);
        ImageView iv_back = (ImageView) view.findViewById(R.id.ib_back_action);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
    }

    private int getImageOrientation(Context my_context) {
        CameraManager manager = (CameraManager) my_context.getSystemService(Context.CAMERA_SERVICE);
        int orientation = 0;
        String cameraId = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            try {
                cameraId = manager.getCameraIdList()[0];
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                orientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return orientation;
    }

    public static Intent getPickImageIntent(Activity context, String text) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File filePicture = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picture_name = IMAGE_CATURE_PRODUCT;
        File image_file = new File(filePicture, picture_name);
        Uri image_uri = Uri.fromFile(image_file);

        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);


        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1), text);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }

    public static Intent getTakePictureIntent(Activity context) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();


        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File filePicture = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picture_name = IMAGE_CATURE_PRODUCT;
        File image_file = new File(filePicture, picture_name);
        Uri image_uri = Uri.fromFile(image_file);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1), "");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }

    public static String IMAGE_CATURE_PRODUCT = "Wo1ImageProduct.jpg";


    public static String getStringChooseOption(Activity activity) {
        String packageName = activity.getPackageName();
        int resId = activity.getResources().getIdentifier("dialog_pick_image_choose_option", "string", packageName);
        return activity.getString(resId);
    }

    private static List<Intent> addIntentsToList(Activity context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }


    public static void getImageView(ImageView ivPicker, Intent data, Context context) {


        if (data != null && data.getData() != null) {
            Uri selectedImage = data.getData();

            int degree = Utils.getRotationFromGallery(context, selectedImage);

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
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
                int degree = Utils.getRotationFromCamera(context, image_uri);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), image_uri);
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

    public static void hideSoftKeyWhenClick(Activity activity) {
        View editText = activity.getCurrentFocus();
        if (editText != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static ProgressDialog getPGDialog(Activity context) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        //pd.setTitle(R.string.in_progress);
        pd.setCanceledOnTouchOutside(false);
        return pd;
    }

    public static ProgressDialog createProgressDialog(Activity context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        try {
            progressDialog.show();
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.setCanceledOnTouchOutside(false);
        } catch (WindowManager.BadTokenException e) {

        }

        return progressDialog;
    }

    public static MultipartBody.Part createStringPart(String name, String value) {
        try {

            MultipartBody.Part image_part = MultipartBody.Part.createFormData(name, value);
            return image_part;
        } catch (Exception e) {
            Crashlytics.logException(e.getCause());
        }
        return null;
    }

    public static MultipartBody.Part createImagePart(String name, String fileName, ImageView iv) {
        try {
            Bitmap bm = ((BitmapDrawable) iv.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            MultipartBody.Part image_part = MultipartBody.Part.createFormData(name, fileName, RequestBody.create(MediaType.parse("image/PNG"), bitmapdata));
            return image_part;
        } catch (Exception e) {
            Crashlytics.logException(e.getCause());
        }
        return null;
    }


    public static RequestBody createRequestBody(ImageView iv) {
        Bitmap bm = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), bitmapdata);
        return requestBody;
    }

    public static String getStringFromListCountry(Context context, int position) {
        List<String> product_country = Arrays.asList(context.getResources().getStringArray(R.array.list_country_product));
        String text = product_country.get(position);
        return text;
    }

    public static String getStringFromListCategory(Context context, int position) {
        String text = "";
        ArrayList<String> myListCategory = new ArrayList<>();
        HashMap<String, String> hashmapCategory = MyPreferences.GetDataFromMyPreferences(MyPreferences.KEY_GET_CATEGORY);
        if (hashmapCategory != null) {
            for (Map.Entry entry : hashmapCategory.entrySet()) {
                myListCategory.add(entry.getValue().toString());
            }
        }
        if (position < myListCategory.size() && position > 0) {
//            List<String> product_type = Arrays.asList(context.getResources().getStringArray(R.array.form_category_list));
            for (Map.Entry entry : hashmapCategory.entrySet()) {
                if (Integer.parseInt(entry.getKey().toString()) == position) {
                    text = entry.getValue().toString();
                    break;
                }
            }
//            text = myListCategory.get(position);
        }
        return text;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Temp_image_product", null);
        return Uri.parse(path);
    }

    public final static DisplayImageOptions OPTION_DISPLAY_IMG_DEFAULT = new DisplayImageOptions.Builder()
            //.cacheInMemory(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading(true)
            .cacheInMemory(true)
            .showImageForEmptyUri(R.drawable.avatar)
            .showImageOnFail(R.drawable.avatar)
            .showImageOnLoading(R.drawable.avatar).build();

    public final static DisplayImageOptions OPTION_DISPLAY_IMG = new DisplayImageOptions.Builder()
            //.cacheInMemory(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheOnDisk(true).resetViewBeforeLoading(true)
            .showImageForEmptyUri(R.drawable.avatar)
            .showImageOnFail(R.drawable.avatar)
            .showImageOnLoading(R.drawable.avatar).build();

    public final static DisplayImageOptions OPTION_DISPLAY_IMG_NORMAL = new DisplayImageOptions.Builder()
            //.cacheInMemory(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheOnDisk(true).resetViewBeforeLoading(false)
            .showImageForEmptyUri(R.drawable.place_holderdemo)
            .showImageOnFail(R.drawable.place_holderdemo)
            .showImageOnLoading(R.drawable.place_holderdemo).build();

    public final static DisplayImageOptions OPTION_DISPLAY_IMG_PRODUCT = new DisplayImageOptions.Builder()
            //.cacheInMemory(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheOnDisk(true).resetViewBeforeLoading(false)
            .showImageForEmptyUri(R.drawable.place_holderdemo)
            .showImageOnFail(R.drawable.place_holderdemo)
            .showImageOnLoading(R.drawable.place_holderdemo).build();
}
