package com.wo1haitao.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.wo1haitao.R;

/**
 * Created by goodproductssoft on 12/5/17.
 */

public class IntroViewPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private boolean firstLoad = true;
    private Activity activity;
    private int[] layouts;
    private int[] images;
    public IntroViewPagerAdapter(Activity activity, int[] layouts, int[] images){
        this.activity = activity;
        this.layouts = layouts;
        this.images = images;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(layouts[position], container, false);
        final ImageView iv = (ImageView) v.findViewById(R.id.logo);
        if (position == 0 && firstLoad) {
            firstLoad = false;
//                iv.setImageResource(image[position]);
            ImageLoader.getInstance().displayImage("drawable://" + images[position], iv, new DisplayImageOptions.Builder()
                    //.cacheInMemory(true)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .cacheOnDisk(true).resetViewBeforeLoading(false)
                    .showImageOnLoading(R.drawable.splash_1_1x).build());

        } else {
            ImageLoader.getInstance().displayImage("drawable://" + images[position], iv);
        }
        container.addView(v);
        return v;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View v = (View) object;
        container.removeView(v);
    }
}
