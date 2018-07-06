package com.wo1haitao;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.pusher.android.PusherAndroid;
import com.wo1haitao.activities.MainActivity;
import com.wo1haitao.fragments.QRWebchatFragment;

import io.fabric.sdk.android.Fabric;

import static com.wo1haitao.utils.Utils.OPTION_DISPLAY_IMG_NORMAL;

/**
 * Created by goodproductssoft on 4/17/17.
 */

public class CustomApp extends Application {
    private static CustomApp instance = null;

    public final static CustomApp getInstance() {
        return instance;
    }

    public MainActivity mainActivity;
    PusherAndroid pusher;
    String notify_scope_name;
    int num_of_notify = 0;
    int num_of_chat = 0;
    private static Tracker sTracker;
    private static GoogleAnalytics sAnalytics;

    public boolean getIsOpen() {

        return currentActivity != null && !currentActivity.isDestroyed() && !currentActivity.isFinishing();
    }

    private Activity currentActivity = null;

    public Activity getCurrentActivity() {
        return currentActivity;
    }


    public int getNum_of_chat() {
        return num_of_chat;
    }

    public void setNum_of_chat(int num_of_chat) {
        this.num_of_chat = num_of_chat;
    }

    public int getNum_of_notify() {
        return num_of_notify;
    }

    public void setNum_of_notify(int num_of_notify) {
        this.num_of_notify = num_of_notify;
    }

    public String getNotify_scope_name() {
        return notify_scope_name;
    }

    public void setNotify_scope_name(String notify_scope_name) {
        this.notify_scope_name = notify_scope_name;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public PusherAndroid getPusher() {
        return pusher;
    }

    public void setPusher(PusherAndroid pusher) {
        this.pusher = pusher;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }



    public double getInch() {
        try{
            Display display = this.getCurrentActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getRealSize(size);
            int width = size.x;
            int height = size.y;

            DisplayMetrics dm = new DisplayMetrics();
            display.getMetrics(dm);
            double x = Math.pow(width / dm.xdpi, 2);
            double y = Math.pow(height / dm.ydpi, 2);
            double screenInches = Math.sqrt(x + y);
//        DecimalFormat twoDForm = new DecimalFormat("#.#");
//        screenInches = Double.valueOf(twoDForm.format(screenInches));
            String strScreenIches;
            strScreenIches = String.format("%.1f", screenInches);
            strScreenIches = strScreenIches.replace(".", "");
            double SalePotential = Double.parseDouble(strScreenIches.replace(",", ""));
            screenInches = SalePotential / 10;
            return screenInches;
        }
        catch (Exception e){
            return 0;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Fabric.with(this, new Answers(), new Crashlytics());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(OPTION_DISPLAY_IMG_NORMAL)
                .build();
        ImageLoader.getInstance().init(config);

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                currentActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                currentActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {

                currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
//                if(currentActivity == activity){
//                    currentActivity = null;
//                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (currentActivity == activity) {
                    currentActivity = null;
                    DiskCacheUtils.removeFromCache(QRWebchatFragment.Image_Link_QRCode, ImageLoader.getInstance().getDiskCache());
                    MemoryCacheUtils.removeFromCache(QRWebchatFragment.Image_Link_QRCode, ImageLoader.getInstance().getMemoryCache());
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                if (currentActivity == activity) {
                    Log.i("TAG","AA");
                }
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (currentActivity == activity) {
                    currentActivity = null;
                }
            }
        });

    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sAnalytics = GoogleAnalytics.getInstance(this);
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }
}
