package com.wo1haitao.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.R;

import java.util.List;

/**
 * Created by user on 5/4/2017.
 */

public class ProductImagePagerAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    List<String> mResources;

    public ProductImagePagerAdapter(Context context, List<String> url_img) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        Collections.reverse(url_img);
        mResources = url_img;
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_image_product, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        String url = mResources.get(position);
        ImageLoader.getInstance().displayImage(url,imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
