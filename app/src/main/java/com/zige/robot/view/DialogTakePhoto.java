package com.zige.robot.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zige.robot.activity.LocalPhotoSelectorActivity;
import com.zige.robot.utils.PhotoUtil;
import com.zige.robot.utils.Util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.zige.robot.R;

/**
 * 类描述：选择图片dialog
 * 创建人：Shirley
 */
public class DialogTakePhoto extends Dialog implements OnClickListener {

    /**
     * 使用照相机获取的图片设置头像
     */
    public static final int REQUEST_PORTRAIT_BY_TAKE_PHOTO_CODE = 0X073;
    /**
     * 从相册中选择的图片设置头像
     */
    public static final int REQUEST_PORTRAIT_BY_PICK_PHOTO_CODE = 0X074;
    /**
     * 打开系统相册后悔来
     */
    public static final int REQUEST_PORTRAIT_BY_SYSTEM_ALBUM = 0X075;

    /**
     * 裁剪
     */
    public static final int REQUEST_PORTRAIT_CROP = 0X077;

    private Context mContext;

    private String mCurrentPhotoPath;// 拍照图片存储路径

    public DialogTakePhoto(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_take_photo);

        initView();
        Window dialogWindow = getWindow();
        // dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.getDecorView()
                .setBackgroundResource(R.drawable.public_close_icon);
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager m = ((Activity) mContext).getWindowManager();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//		p.height = (int) (d.getHeight() * 0.7); // 高度设置为屏幕的0.7
        p.width = (int) (Util.getScreenWidth(mContext) ); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    private void initView() {
        findViewById(R.id.btn_take_photo).setOnClickListener(this);
        findViewById(R.id.btn_search_photo).setOnClickListener(this);
        findViewById(R.id.btn_cancel_photo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                if (isIntentAvailable(mContext,
                        MediaStore.ACTION_IMAGE_CAPTURE)) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(mContext, "拍照",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_search_photo:
                pickPhoto();
                break;
            case R.id.btn_cancel_photo:
                hide();
                break;
            default:
                break;
        }
    }

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
     *
     * @param context The application's environment.
     * @param action  The Intent action to check for availability.
     * @return True if an Intent with the specified action can be sent and
     * responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 处理拍照事件
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = setUpPhotoFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, mCurrentPhotoPath);
        Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        ((Activity) mContext).startActivityForResult(takePictureIntent,
                REQUEST_PORTRAIT_BY_TAKE_PHOTO_CODE);
        dismiss();
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    /*
         * 创建图片文件，用于拍照图片存储
         */
    private File setUpPhotoFile() {
        File f = null;
        try {
            f = createImageFile();
            mCurrentPhotoPath = f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            mCurrentPhotoPath = null;
        }
        return f;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageFileName = PhotoUtil.JPEG_FILE_PREFIX + timeStamp + "_";
        String filePath = PhotoUtil.getPictureStorePath(mContext);
        File imageF = File.createTempFile(imageFileName, PhotoUtil.JPEG_FILE_SUFFIX,
                new File(filePath));
        return imageF;
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
//        LocalPhotoSelectorActivity.selectSingleImage((Activity) mContext, REQUEST_PORTRAIT_BY_PICK_PHOTO_CODE);
        dismiss();
        //打开系统相册
        Intent albumIntent = new Intent(Intent.ACTION_PICK);//打开系统的相册
        albumIntent.setType("image/*");
        ((Activity) mContext).startActivityForResult(albumIntent, REQUEST_PORTRAIT_BY_SYSTEM_ALBUM);
//        LocalPhotoSelectorActivity.selectMultipleImage((Activity) mContext,9, REQUEST_PORTRAIT_BY_PICK_PHOTO_CODE);
    }
}
