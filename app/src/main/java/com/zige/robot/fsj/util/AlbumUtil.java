package com.zige.robot.fsj.util;

/**
 * Created by PhoneSj on 2017/9/30.
 */

public class AlbumUtil {

    public static String getThumbnailUrl(String url, int width, int height) {
        return url + "?imageView2/1/w/" + width + "/h/" + height + "/interlace/1";
    }
}
