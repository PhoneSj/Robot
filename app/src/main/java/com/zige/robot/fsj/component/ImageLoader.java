package com.zige.robot.fsj.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zige.robot.App;
import com.zige.robot.R;

/**
 * Created by Phone on 2017/7/17.
 * 图片加载工具类
 */

public class ImageLoader {

    public static void load(Context context, String url, int holdImg, int erroeImg, ImageView iv) {
//        if (!App.getAppComponent().getMySPHelper().getNoImageState()) {
        //crossFade() 图片显示、不显示启用渐变过程
        //diskCacheStrategy(DiskCacheStrategy.SOURCE) 磁盘缓存策略：仅缓存原图，不缓存转换过的图片
        Glide
                .with(context)
                .load(url)
                .placeholder(holdImg)
                .error(erroeImg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv);
//        }
    }

    public static void load(Context context, String url, ImageView iv) {
//        if (!App.getAppComponent().getMySPHelper().getNoImageState()) {
        //crossFade() 图片显示、不显示启用渐变过程
        //diskCacheStrategy(DiskCacheStrategy.SOURCE) 磁盘缓存策略：仅缓存原图，不缓存转换过的图片
        Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.photo_nor)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv);
//        }
    }


//    public static void load(Context context, String url, Transformation<Bitmap> transformation, ImageView iv) {
//        if (!App.getAppComponent().getMySPHelper().getNoImageState()) {
//            Glide.with(context).load(url).bitmapTransform(transformation).into(iv);
//        }
//    }
}
