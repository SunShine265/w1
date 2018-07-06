package com.wo1haitao.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.R;

import java.util.ArrayList;

/**
 * Created by user on 5/19/2017.
 */

public class BaseAdapter<T> extends ArrayAdapter<T>{

    ImageLoader imageLoader;
    DisplayImageOptions options;
    public BaseAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<T> objects) {
        super(context, resource, objects);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.place_holder)
                .showImageOnFail(R.drawable.place_holder)
                .showImageOnLoading(R.drawable.place_holder).build();
    }
}
