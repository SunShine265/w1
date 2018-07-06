package com.wo1haitao.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wo1haitao.R;
import com.wo1haitao.api.response.RsAddress;

import java.util.ArrayList;

/**
 * Created by user on 5/19/2017.
 */

public class BaseAdapterAddress extends ArrayAdapter<RsAddress>{

    ImageLoader imageLoader;
    DisplayImageOptions options;
    public BaseAdapterAddress(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<RsAddress> objects) {
        super(context, resource, objects);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.place_holder)
                .showImageOnFail(R.drawable.place_holder)
                .showImageOnLoading(R.drawable.place_holder).build();
    }
}
