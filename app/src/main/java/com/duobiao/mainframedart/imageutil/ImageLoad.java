package com.duobiao.mainframedart.imageutil;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Author:Admin
 * Time:2019/8/1 15:28
 * 描述：
 */
public class ImageLoad {


    private Context mContext;
    private static ImageLoad mImageLoad;
    private ImageLoad(Context context){
        mContext = context;
    }
    public static ImageLoad with(Context context){
        mImageLoad = new ImageLoad(context);
        return mImageLoad;
    }
    public void loadNetUrlImage(String url, ImageView imageView){
        Glide.with(mContext).load(url).into(imageView);

    }
}
