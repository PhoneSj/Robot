package com.zige.robot.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.zige.robot.App;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PhotoUtil {

    /**
     * 手机SD卡图片缓存
     */
    public static HashMap<String, SoftReference<Bitmap>> mPhoneAlbumCache = new HashMap<String, SoftReference<Bitmap>>();

    /**
     * 手机缩略图图片缓存
     */
    public static HashMap<String, SoftReference<Bitmap>> mPhoneThumbnailCache = new HashMap<String, SoftReference<Bitmap>>();

    /**
     * 文件路径
     */
    public static String FILE_PATH = "ParkBees_File";

    /**
     * 头像存储路径
     */
    public static String mPhotosPath = "ParkBees_Photo";

    public static final String JPEG_FILE_PREFIX = "IMG_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";

    /**
     * 图片保存路径
     */
    public static final String IMG_FILE_PATH = PhotoUtil.FILE_PATH + "/Camera/";

    public static HashMap<String, SoftReference<Bitmap>> mImageCache = new HashMap<String, SoftReference<Bitmap>>();

    /**
     * 将图片变为圆角
     *
     * @param bitmap 原Bitmap图片
     * @param pixels 图片圆角的弧度(单位:像素(px))
     * @return 带有圆角的图片(Bitmap 类型)
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    /**
     * 获取缩略图图片
     *
     * @param imagePath 图片的路径
     * @param width     图片的宽度
     * @param height    图片的高度
     * @return 缩略图图片
     */
    public static Bitmap getImageThumbnail(String imagePath, int width,
                                           int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * LOMO特效
     *
     * @param bitmap 原图片
     * @return LOMO特效图片
     */
    public static Bitmap lomoFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int dst[] = new int[width * height];
        bitmap.getPixels(dst, 0, width, 0, 0, width, height);

        int ratio = width > height ? height * 32768 / width : width * 32768
                / height;
        int cx = width >> 1;
        int cy = height >> 1;
        int max = cx * cx + cy * cy;
        int min = (int) (max * (1 - 0.8f));
        int diff = max - min;

        int ri, gi, bi;
        int dx, dy, distSq, v;

        int R, G, B;

        int value;
        int pos, pixColor;
        int newR, newG, newB;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pos = y * width + x;
                pixColor = dst[pos];
                R = Color.red(pixColor);
                G = Color.green(pixColor);
                B = Color.blue(pixColor);

                value = R < 128 ? R : 256 - R;
                newR = (value * value * value) / 64 / 256;
                newR = (R < 128 ? newR : 255 - newR);

                value = G < 128 ? G : 256 - G;
                newG = (value * value) / 128;
                newG = (G < 128 ? newG : 255 - newG);

                newB = B / 2 + 0x25;

                // ==========边缘黑暗==============//
                dx = cx - x;
                dy = cy - y;
                if (width > height)
                    dx = (dx * ratio) >> 15;
                else
                    dy = (dy * ratio) >> 15;

                distSq = dx * dx + dy * dy;
                if (distSq > min) {
                    v = ((max - distSq) << 8) / diff;
                    v *= v;

                    ri = (int) (newR * v) >> 16;
                    gi = (int) (newG * v) >> 16;
                    bi = (int) (newB * v) >> 16;

                    newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
                    newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
                    newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
                }
                // ==========边缘黑暗end==============//

                dst[pos] = Color.rgb(newR, newG, newB);
            }
        }

        Bitmap acrossFlushBitmap = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
        return acrossFlushBitmap;
    }

    /**
     * 旧时光特效
     *
     * @param bmp 原图片
     * @return 旧时光特效图片
     */
    public static Bitmap oldTimeFilter(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR,
                        newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 暖意特效
     *
     * @param bmp     原图片
     * @param centerX 光源横坐标
     * @param centerY 光源纵坐标
     * @return 暖意特效图片
     */
    public static Bitmap warmthFilter(Bitmap bmp, int centerX, int centerY) {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;
        int radius = Math.min(centerX, centerY);

        final float strength = 150F; // 光照强度 100~150
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                newR = pixR;
                newG = pixG;
                newB = pixB;

                // 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
                int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(
                        centerX - k, 2));
                if (distance < radius * radius) {
                    // 按照距离大小计算增加的光照值
                    int result = (int) (strength * (1.0 - Math.sqrt(distance)
                            / radius));
                    newR = pixR + result;
                    newG = pixG + result;
                    newB = pixB + result;
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


    /**
     * 获取边框图片
     *
     * @param context   上下文
     * @param frameName 边框名称
     * @param position  边框的类型
     * @return 边框图片
     */
    private static Bitmap decodeBitmap(Context context, String frameName,
                                       int position) {
        try {
            switch (position) {
                case 0:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/leftup.png"));
                case 1:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/left.png"));
                case 2:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/leftdown.png"));
                case 3:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/down.png"));
                case 4:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/rightdown.png"));
                case 5:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/right.png"));
                case 6:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/rightup.png"));
                case 7:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/up.png"));
                default:
                    return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加内边框
     *
     * @param bm    原图片
     * @param frame 内边框图片
     * @return 带有边框的图片
     */
    public static Bitmap addBigFrame(Bitmap bm, Bitmap frame) {
        Bitmap newBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(bm, 0, 0, paint);
        frame = Bitmap.createScaledBitmap(frame, bm.getWidth(), bm.getHeight(),
                true);
        canvas.drawBitmap(frame, 0, 0, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBitmap;

    }

    /**
     * 创建一个缩放的图片
     *
     * @param path 图片地址
     * @param w    图片宽度
     * @param h    图片高度
     * @return 缩放后的图片
     */
    public static Bitmap createLocalBitmap(String path, int w, int h) {
        Bitmap bitmap = null;
        String strId = String.valueOf(path);
        if (mImageCache.containsKey(strId)) {
            SoftReference<Bitmap> softReference = mImageCache.get(strId);
            bitmap = softReference.get();
            if (null != bitmap)
                return bitmap;
        }
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = (int) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            bitmap = BitmapFactory.decodeFile(path, newOpts);
            mImageCache.put(strId, new SoftReference<Bitmap>(bitmap));
        } catch (OutOfMemoryError e) {
            System.gc();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    /**
     * 上传头像先压缩。图片先按比例压缩，再按质量压缩
     *
     * @param filePath    文件路径
     * @param w           图片宽度
     * @param h           图片高度
     * @param isTakephoto
     * @return 缩放后的图片
     */
    @SuppressWarnings("resource")
    public static String getBitmapByFile(String filePath, int w, int h,
                                         boolean isTakephoto) {
        FileInputStream inputStream = null;
        BitmapFactory.Options opts = null;
        Bitmap bitmap = null;
        try {
            inputStream = new FileInputStream(filePath);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int count = -1;
            while ((count = inputStream.read(data)) != -1) {
                outStream.write(data, 0, count);
            }
            opts = new BitmapFactory.Options();
            // 如果图片大于1M先进行尺寸压缩
            if (outStream.toByteArray().length > 1024 * 1024) {
                opts.inJustDecodeBounds = true;
                // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
                BitmapFactory.decodeFile(filePath, opts);
                // 缩放的比例
                opts.inSampleSize = computeSampleSize(opts, h, w);
                // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
                // inJustDecodeBounds设为false表示把图片读进内存中
                opts.inJustDecodeBounds = false;
                opts.inDither = false;
                opts.inPreferredConfig = Config.ARGB_8888;
            }
            bitmap = compressImage(BitmapFactory.decodeFile(filePath, opts));
            int degrees = getExifOrientation(filePath);
            if (degrees != 0) {
                bitmap = rotateAndMirror(bitmap, degrees, false);
            }
        } catch (Exception e) {
        }
        return savePicToLocal(filePath, isTakephoto, bitmap);
    }

    private static int options = 100;

    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, options,
                baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options,
                    baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(
                baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, options, baos1);
        long a = baos1.toByteArray().length;
        return bitmap;
    }

    // 保存图片到本地
    public static String savePicToLocal(Bitmap bitmap) {
        String filePath = App.getInstance().getFilesDir().getPath().toString() + mPhotosPath + System.currentTimeMillis() + ".png";
        File file = new File(filePath);
        FileOutputStream fileOutputStream = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return filePath;
    }

    //保存压缩好的头像到本地
    private static String savePicToLocal(String filePath, boolean isTakephoto,
                                         Bitmap bitmap) {
        String path = null;
        FileOutputStream fileOutputStream = null;
        try {
            if (isTakephoto) {
                // 获取filepath的后缀，-5的原因是后缀可能是“jpng”
                path = filePath;
            } else {
                path = mPhotosPath + System.currentTimeMillis() + ".png";
            }
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, options,
                    fileOutputStream);
            options = 100;
        } catch (IOException e) {
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                } else {
                    return null;
                }
            } catch (IOException e) {
            }
        }
        return path;
    }


    public static BitmapDrawable getBigPic(Bitmap bitmap, Context context, float sacle) {
        if (bitmap != null) {
            Bitmap resizeBmp;
            try {
                Matrix matrix = new Matrix();
                float sx = (Util.getScreenWidth(context) * sacle) / (bitmap.getWidth() * 1.0f);
                float sy = (Util.getScreenWidth(context) * sacle) / (bitmap.getHeight() * 1.0f);
                if (sx < sy) {
                    matrix.postScale(sx, sx);
                } else {
                    matrix.postScale(sy, sy); // 长和宽放大缩小的比例
                }
                resizeBmp = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                BitmapDrawable bd = new BitmapDrawable(context.getResources(),
                        resizeBmp);
                return bd;
            } catch (OutOfMemoryError ex) {
                Log.e("Tag", "Got oom exception ", ex);
                return null;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @throws Exception
     * @功能：
     * @param：根据屏幕长度放大图片(图片比例大于1/3则不进行放大，以免图片变模糊)
     * @return：
     */

    public static BitmapDrawable getBigPic(Bitmap bitmap, Context context) {
        Bitmap resizeBmp;
        try {
            Matrix matrix = new Matrix();
            float sx = (Util.getScreenWidth(context) * 1.0f) / (bitmap.getWidth() * 1.0f);
            float sy =
                    (Util.getScreenWidth(context) * 1.0f) / (bitmap.getHeight() * 1.0f);
            if (sx < 3 || sy < 3) {
                if (sx < sy) {
                    matrix.postScale(sx, sx);
                } else {
                    matrix.postScale(sy, sy); // 长和宽放大缩小的比例
                }
                resizeBmp = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } else {
                //如果图片过小则不进行放大显示
                resizeBmp = bitmap;
            }
            BitmapDrawable bd = new BitmapDrawable(context.getResources(),
                    resizeBmp);
            return bd;
        } catch (OutOfMemoryError ex) {
            Log.e("Tag", "Got oom exception ", ex);
            return null;
        }
    }


    public static int computeSampleSize(BitmapFactory.Options options,
                                        int reqHeight, int reqWidth) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        /*
         * if (reqHeight <= 800 || reqWidth <= 480) { return 1; }
		 */
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // Rotates and/or mirrors the bitmap. If a new bitmap is created, the
    // original bitmap is recycled.
    public static Bitmap rotateAndMirror(Bitmap b, int degrees, boolean mirror) {
        if ((degrees != 0 || mirror) && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth() / 2,
                    (float) b.getHeight() / 2);
            if (mirror) {
                m.postScale(-1, 1);
                degrees = (degrees + 360) % 360;
                if (degrees == 0 || degrees == 180) {
                    m.postTranslate((float) b.getWidth(), 0);
                } else if (degrees == 90 || degrees == 270) {
                    m.postTranslate((float) b.getHeight(), 0);
                } else {
                    throw new IllegalArgumentException("Invalid degrees="
                            + degrees);
                }
            }

            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                        b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return b;
    }

    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    //把一个url的网络图片变成一个本地的BitMap
    public static Bitmap returnBitMap(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static final String CAMERA_DIR = "/parkBees/";

    /**
     * 获取相片中的存储路径
     *
     * @param context
     * @return
     */
    public static String getPictureStorePath(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //String path=context.getString(sdPathSettingId);
            File file = Environment.getExternalStorageDirectory();
            String pathFileName = file.getAbsoluteFile() + File.separator + CAMERA_DIR;
            File filePath = new File(pathFileName);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            String name = pathFileName + File.separator + ".nomedia";
            File fileNomedia = new File(name);
            if (fileNomedia.exists()) {
                fileNomedia.delete();
            }
            return pathFileName;
        } else {
            String pathFileName = context.getFilesDir().getAbsolutePath() + File.separator + CAMERA_DIR;
            File filePath = new File(pathFileName);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            String name = pathFileName + File.separator + ".nomedia";
            File fileNomedia = new File(name);
            if (fileNomedia.exists()) {
                fileNomedia.delete();
            }
            return pathFileName;
        }
    }

    public static File generatePicturePath() {
        try {
            File storageDir = getAlbumDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            return new File(storageDir, "IMG_" + timeStamp + ".jpg");
        } catch (Exception e) {
        }
        return null;
    }

    private static File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "albumDir");
            if (!storageDir.mkdirs()) {
                if (!storageDir.exists()) {
                    return null;
                }
            }
        } else {
        }

        return storageDir;
    }

    /**
     * 保存bitmap
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static boolean saveBitmaps(Context context, Bitmap bitmap, File newFile) {
        try {

            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(
                    newFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * 将图片加入相册中
	 */
    public static void galleryAddPic(Context context, String filePath) {
        Intent mediaScanIntent = new Intent(
                "android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(filePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 绘制纵向平铺的bitmap
     *
     * @param height 要绘制图片的高度
     * @param src    要重复绘制的单个子图片
     * @return
     */
    public static Bitmap createRepeaterY(int height, Bitmap src) {
        int count = (height + src.getHeight() - 1) / src.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (int idY = 0; idY < count; ++idY) {
            canvas.drawBitmap(src, 0, idY * src.getHeight(), null);
        }
        return bitmap;
    }

    /**
     * 绘制横向平铺的bitmap
     *
     * @param width 要绘制图片的宽度
     * @param src   要重复绘制的单个子图片
     * @return
     */
    public static Bitmap createRepeaterX(int width, Bitmap src) {
        int count = (width + src.getWidth() - 1) / src.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (int idx = 0; idx < count; ++idx) {
            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }

        return bitmap;
    }

    public static Bitmap getBitmapFromPath(String path, int w, int h) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        Bitmap b =BitmapFactory.decodeFile(path, opts);
        return Bitmap.createScaledBitmap(b, w, h, true);
    }
    /****************************************
     方法描述：压缩并保存图片
     @param  rawBitmap  需要压缩的图片
     @param  mFilePath  需要保存的目标路径
     @param  quality    图片质量
     @param  lastSize    目标大小(kb)
     ****************************************/
    public static void compressAndSaveBitmap(Bitmap rawBitmap,
                                             String mFilePath, int quality, long lastSize) {
        File saveFile = new File(mFilePath);
        if (saveFile.exists()) {
            saveFile.delete();
        }
        compressPic(rawBitmap, quality, saveFile);
        long picSize = saveFile.length() / 1024;

        while (picSize > lastSize) {
            compressPic(rawBitmap, quality -= 5, saveFile);
            picSize = saveFile.length() / 1024;
        }

//        NativeUtil.compressBitmap(rawBitmap, mFilePath, quality);

    }

    private static void compressPic(Bitmap rawBitmap, int quality, File saveFile) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
            if (fileOutputStream != null) {

                rawBitmap.compress(Bitmap.CompressFormat.JPEG, quality,
                        fileOutputStream);
                TagUtil.showLogDebug("图片大小：" + saveFile.length() / 1024);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /****************************************
     方法描述： 图片大小
     @param
     @return 单位kb
     ****************************************/
    private static long getBitmapSize(Bitmap bitmap) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
    }


}
