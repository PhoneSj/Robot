package com.zige.robot.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import com.zige.robot.R;


/**
 * Created by Zhouztashin on 2015/7/16.
 */
public class ImageLoaderUtils {

    private static DisplayImageOptions.Builder createDefaultDisplayImageOptionsBuilder() {
        return new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .considerExifParams(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT);
    }

    // portrait image loading option.
    private static final DisplayImageOptions displayPortraitOption = createDefaultDisplayImageOptionsBuilder()
            .showImageOnFail(R.drawable.uvv_common_ic_loading_icon)
            .showImageOnLoading(R.drawable.uvv_common_ic_loading_icon)
            .showImageForEmptyUri(R.drawable.uvv_common_ic_loading_icon)
            .build();

    private static final DisplayImageOptions displayCircleSmallOption = createDefaultDisplayImageOptionsBuilder()
            .showImageOnFail(R.drawable.uvv_common_ic_loading_icon)
            .showImageOnLoading(R.drawable.uvv_common_ic_loading_icon)
            .showImageForEmptyUri(R.drawable.uvv_common_ic_loading_icon)
            .displayer(new RoundedBitmapDisplayer(360))
            .build();

    private static final DisplayImageOptions displayParkPicOption = createDefaultDisplayImageOptionsBuilder()
            .showImageOnLoading(R.drawable.uvv_common_ic_loading_icon)
            .showImageForEmptyUri(R.drawable.uvv_common_ic_loading_icon)
            .showImageOnFail(R.drawable.uvv_common_ic_loading_icon)
            .build();

    private static final DisplayImageOptions defaultOption = createDefaultDisplayImageOptionsBuilder()
            .showImageOnLoading(R.drawable.uvv_common_ic_loading_icon)
            .showImageForEmptyUri(R.drawable.uvv_common_ic_loading_icon)
            .showImageOnFail(R.drawable.uvv_common_ic_loading_icon)
            .build();

    private static final DisplayImageOptions defaultNullOption = createDefaultDisplayImageOptionsBuilder()
            .build();


    private static void initImageLoader(Context context) {

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .tasksProcessingOrder(QueueProcessingType.LIFO);
        //.writeDebugLogs();
        //	.denyCacheImageMultipleSizesInMemory()//对于使用同个URL，加载显示不同大小的图片只会在内存存放一个Bitmap的实例（默认是没有此设置的，所以会存在同个图片，多个Bitmap实例）
        //	.memoryCache(new WeakMemoryCache());//ImageLoader提供此参数进行设置，如果频繁遇到OOM，加上此设置可降低OOM的机率(但在Android4.0的平台已经不再适用)
        builder.imageDownloader(new AuthImageDownloader(context));//支持https
        ImageLoaderConfiguration config = builder.build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }


    private static ImageLoader getImageLoader(Context context) {
        if (!ImageLoader.getInstance().isInited()) {
            initImageLoader(context);
        }
        return ImageLoader.getInstance();
    }

    public static void clearCache(Context c) {
        getImageLoader(c).clearMemoryCache();
    }

    /**
     * 显示头像
     * param str  url or local path,if it is a url ,the str must with the "http:// " or "https://" prefix.
     */
    public static void displayPortrait(Context context, ImageView imageView, String str) {

        if (TextUtils.isEmpty(str)) {
            ImageLoaderUtils.getImageLoader(context).displayImage("drawable://" + R.drawable.loading_wait, imageView, displayPortraitOption);
            return;
        }
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            str = "file://" + str;
        }
        ImageLoaderUtils.getImageLoader(context).displayImage(str, imageView, displayPortraitOption);
    }

    /**
     * 显示停车场图片
     * param str  url or local path,if it is a url ,the str must with the "http:// " or "https://" prefix.
     */
    public static void displayParkPic(Context context, ImageView imageView, String str, ImageLoadingListener imageLoadingListener) {

        if (TextUtils.isEmpty(str)) {
            ImageLoaderUtils.getImageLoader(context).displayImage("drawable://" + R.drawable.loading_wait, imageView, displayParkPicOption);
            return;
        }
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            str = "file://" + str;
        }
        ImageLoaderUtils.getImageLoader(context).displayImage(str, imageView, displayParkPicOption, imageLoadingListener);
    }

    /**
     * 显示停车场图片
     * param str  url or local path,if it is a url ,the str must with the "http:// " or "https://" prefix.
     */
    public static void displayParkPic(Context context, ImageView imageView, String str) {

        if (TextUtils.isEmpty(str)) {
            ImageLoaderUtils.getImageLoader(context).displayImage("drawable://" + R.drawable.loading_wait, imageView, displayParkPicOption);
            return;
        }
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            str = "file://" + str;
        }
        ImageLoaderUtils.getImageLoader(context).displayImage(str, imageView, displayParkPicOption);
    }

    public static void displayResizeSmalImage(Context context, ImageView imageView, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (!str.startsWith("http://") && !str.startsWith("https://")) {

            str = "file://" + str;

        }
        ImageSize is = new ImageSize(100, 100);
        ImageLoaderUtils.getImageLoader(context).displayImage(str, new ImageViewAware(imageView), defaultOption, is, null, null);
    }

    /**
     * 显示圆形图片
     */
    public static void displayCircleSmallImage(Context context, ImageView imageView, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            str = "file://" + str;
        }
        ImageLoaderUtils.getImageLoader(context).displayImage(str, imageView, displayCircleSmallOption);

    }

    /**
     * 显示圆形图片
     */
    public static void displayCircleSmallImage(Context context, ImageView imageView, String str, int defaultResId) {
        if (TextUtils.isEmpty(str)) {
            ImageLoaderUtils.getImageLoader(context).displayImage("drawable://" + defaultResId, imageView, displayCircleSmallOption);
            return;
        }
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            str = "file://" + str;
        }
        ImageLoaderUtils.getImageLoader(context).displayImage(str, imageView, displayCircleSmallOption);

    }


    public static void displayImage(Context context, ImageView imageView, String str, ImageLoadingListener imageLoadingListener) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            str = "file://" + str;
        }
        ImageLoaderUtils.getImageLoader(context).displayImage(str, imageView, defaultNullOption, imageLoadingListener);

    }

    public static void displayImage(Context context, ImageView imageView, String str, int defaultResId) {
        if (TextUtils.isEmpty(str)) {
            ImageLoaderUtils.getImageLoader(context).displayImage("drawable://" + defaultResId, imageView, defaultOption);
            return;
        }
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            str = "file://" + str;
        }
        if (defaultResId == R.drawable.loading_wait) {
            ImageLoaderUtils.getImageLoader(context).displayImage(str, imageView, displayPortraitOption);
            return;
        }
        if (defaultResId == R.drawable.loading_wait) {
            ImageLoaderUtils.getImageLoader(context).displayImage(str, imageView, displayParkPicOption);
        }
        ImageLoaderUtils.getImageLoader(context).displayImage(str, imageView, defaultOption);
    }

    public static void displayImage(Context context, ImageView imageView, String str) {
        if (TextUtils.isEmpty(str)) {
            ImageLoaderUtils.getImageLoader(context).displayImage("drawable://" + R.drawable.loading_wait, imageView, defaultOption);
            return;
        }
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            str = "file://" + str;
        }
        ImageLoaderUtils.getImageLoader(context).displayImage(str, imageView, defaultOption);
    }

    public static void displayImage(Context context, ImageView imageView, int resID) {
        if (resID <= 0) {
            return;
        }
        String resStr = "drawable://" + resID;
        ImageLoaderUtils.getImageLoader(context).displayImage(resStr, imageView, defaultOption);

    }

    public static void loadImage(Context context, String str, ImageLoadingListener imageLoadingListener) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            str = "file://" + str;
        }
        ImageLoaderUtils.getImageLoader(context).loadImage(str, defaultOption, imageLoadingListener);

    }

    public static void clearDiskCache(Context c) {
        ImageLoaderUtils.getImageLoader(c).clearDiskCache();
    }

    public static void clearMemoryCache(Context c) {
        ImageLoaderUtils.getImageLoader(c).clearMemoryCache();
    }
}
