package com.wo1haitao.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wo1haitao.CustomApp;
import com.wo1haitao.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends AppCompatActivity {

    ActivityCallback callback;
    final int REQUEST_ID_MULTIPLE_PERMISSIONS = 10001;
    public static int keyboard_height = 0;

    public void setActivityCallback(ActivityCallback callback) {

        this.callback = callback;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    private static boolean checkedPermissions = false;

    @Override
    protected void onResume() {
        super.onResume();

//        if(!checkedPermissions) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                        if (checkPermissions()) {
//                            checkedPermissions = true;
//                            onRequestPermissionsGranted();
//                        }
//                    }
//            }, 0);
//        }
    }

    private  boolean checkPermissions() {
        int result;
        String[] mPermissions = new String[0];
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            mPermissions = packageInfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p : mPermissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
                break;
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    protected void onRequestPermissionsGranted(){
    }

    protected void onRequestPermissionsDenied(){
        checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                String message = "";

                for (int i = 0; i < permissions.length; i++){
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        if(!message.isEmpty()){
                            message += "\n";
                        }
                        message += (permissions[i] + " is denied!");
                    }
                }

                if (message.isEmpty()) {
                    onRequestPermissionsGranted();
                } else {
                    Toast.makeText(CustomApp.getInstance(), message, Toast.LENGTH_LONG);
                    onRequestPermissionsDenied();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onActivityResultFragment(int requestCode, int resultCode, Intent data) {
       onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        final View contentView = this.findViewById(android.R.id.content);
        this.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    BaseActivity.keyboard_height = keypadHeight;
                    onKeyboardShow();
                }
                else {
                    onKeyboardHide();
                }
            }
        });
    }

    public void changeFragment(Fragment fragment, boolean isUp) {
    }

    public void onKeyboardShow(){

    }

    public void onKeyboardHide(){

    }


    protected void getActionbar(int title) {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_custom_text_title);
        ImageView img_back_action = (ImageView) findViewById(R.id.ib_back_action);
        TextView titleBar = (TextView) findViewById(R.id.mytext);
        titleBar.setText(title);
        img_back_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.empty_ani,R.anim.old_activity);
    }

    protected void customStartActivity(Intent it){
        PackageManager packageManager = getPackageManager();
        if (it.resolveActivity(packageManager) != null) {
            startActivity(it);
            overridePendingTransition(R.anim.new_activity, R.anim.empty_ani);
        }
    }

    public interface ActivityCallback {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

}
