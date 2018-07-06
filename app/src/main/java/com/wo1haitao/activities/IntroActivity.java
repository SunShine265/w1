package com.wo1haitao.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wo1haitao.R;
import com.wo1haitao.adapters.IntroViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by goodproductssoft on 4/12/17.
 */

public class IntroActivity extends BaseActivity {

    private ViewPager viewPager;
    Button next, skip;
    private ImageView[] dots;
    private LinearLayout dotsLayout;
    private int[] layouts, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        checkPermissions();

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        skip = (Button) findViewById(R.id.btn_register);
        next = (Button) findViewById(R.id.btn_next);

        layouts = new int[]{R.layout.activity_intro_screen_1, R.layout.activity_intro_screen_2, R.layout.activity_intro_screen_3};
        image = new int[]{R.drawable.splash_1_2x, R.drawable.splash_2_2x, R.drawable.splash_3_2x};


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try{
                    if (Build.VERSION.SDK_INT >= 21) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    }
                    View decorView = getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                }
                catch (Exception e){
                   Log.i("Error: ","error");
                }
                addBottomDots(0, IntroActivity.this);
                changeStatusBarColor();
                IntroViewPagerAdapter pagerAdapter = new IntroViewPagerAdapter(IntroActivity.this,layouts,image);
                viewPager.setAdapter(pagerAdapter);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        final int current_position = position;
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                addBottomDots(current_position, IntroActivity.this);
                            }
                        });

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        });



        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(IntroActivity.this, RegisterActivity.class);
                PackageManager packageManager = getPackageManager();
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i);
                }
                overridePendingTransition(R.anim.new_activity, R.anim.empty_ani);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(IntroActivity.this, LoginActivity.class);

                PackageManager packageManager = getPackageManager();
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i);
                    overridePendingTransition(R.anim.new_activity, R.anim.empty_ani);
                }
            }
        });
    }

    private void addBottomDots(final int position, final Activity activity) {
        if(activity == null){
            return;
        }
        dots = new ImageView[layouts.length];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(activity);
            ViewGroup.LayoutParams lo_param = new ViewGroup.LayoutParams(25, 25);
            dots[i].setLayoutParams(lo_param);
            dots[i].setPadding(5, 5, 5, 5);
            dots[i].setImageResource(R.drawable.dot_no_select);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            LinearLayout.LayoutParams lo_param = new LinearLayout.LayoutParams(35, 35);
            dots[position].setLayoutParams(lo_param);
            dots[position].setImageResource(R.drawable.dot_selected);
            //viewPager.startAnimation(AnimationUtils.loadAnimation(IntroActivity.this,R.anim.translate_type_5));
        }
    }





    public void changeStatusBarColor() {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Subribe pusher
     *
     * @param: notify_scope_name
     * @return:
     */

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
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            boolean checkNumPermission = false;
//            for (int i = 0, len = permissions.length; i < len; i++) {
//                String permission = permissions[i];
//                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                    // user rejected the permission
//                    boolean showRationale = false;
//
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                        showRationale = shouldShowRequestPermissionRationale( permission );
//                    }
//                    if (showRationale) {
//                        checkNumPermission = true;
//                    }
//                }
//            }
//            if(checkNumPermission == true){
//                checkPermissions();
//            }
        }
    }
}
