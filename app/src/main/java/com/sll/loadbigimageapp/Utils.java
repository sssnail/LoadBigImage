package com.sll.loadbigimageapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Utils {


    private static ImageLoaderConfiguration configuration = null;

    public static void imageLoaderDisplayImage(Context context, String url, ImageView imageView){
        if(imageView == null) return;
        if(configuration == null){
            configuration =  ImageLoaderConfiguration.createDefault(context);
            ImageLoader.getInstance().init(configuration);
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageForEmptyUri(R.drawable.ic_launcher_foreground)
                .showImageOnFail(R.drawable.ic_launcher_foreground)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(360))
                .build();
        ImageLoader.getInstance().displayImage(url,imageView,options);


    }
}
