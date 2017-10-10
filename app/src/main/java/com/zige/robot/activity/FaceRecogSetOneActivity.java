package com.zige.robot.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zige.colorrecolibrary.DefResponse;
import com.zige.colorrecolibrary.DubePerson;
import com.zige.colorrecolibrary.DubePersonList;
import com.zige.colorrecolibrary.FaceColorRec;
import com.zige.colorrecolibrary.RetrofitApi;
import com.zige.robot.App;
import com.zige.robot.HttpLink;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.PhotoUtil;
import com.zige.robot.utils.QiniuUtil;
import com.zige.robot.utils.SystemUtils;
import com.zige.robot.utils.TagUtil;
import com.zige.robot.utils.ToastUtils;
import com.zige.robot.view.DialogTakePhoto;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.zige.robot.utils.ToastUtils.showToast;
import static com.zige.robot.view.DialogTakePhoto.REQUEST_PORTRAIT_BY_PICK_PHOTO_CODE;
import static com.zige.robot.view.DialogTakePhoto.REQUEST_PORTRAIT_BY_SYSTEM_ALBUM;
import static com.zige.robot.view.DialogTakePhoto.REQUEST_PORTRAIT_BY_TAKE_PHOTO_CODE;
import static com.zige.robot.view.DialogTakePhoto.REQUEST_PORTRAIT_CROP;

/*************************************
 功能： 人脸识别设置
 创建者：金征
 创建日期：${DATE}
 *************************************/
public class FaceRecogSetOneActivity extends BaseActivity implements View.OnClickListener {
    public static final String QINIU_URL = "qiniuurl";
    public static final String BAIDU_PERSONID = "BaiduPersonId";

    private static final int REQUEST_PORTRAIT_BY_FINAL = 0x0011;
    @BindView(R.id.tv_action)
    public TextView tv_action;
    @BindView(R.id.tv_to_pic)
    public TextView tv_to_pic;
    @BindView(R.id.btn_face_next)
    public Button btn_face_next;
    @BindView(R.id.iv_photo_show)
    public ImageView iv_photo_show;


    private DialogTakePhoto mDialogTakePhoto;
    private String currentPicPatch;//以获取的图片路径

    //裁剪 文件存放路径
    private File mFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "temp.jpg");
    private FaceColorRec faceColorRec;
    private String personId = "", token = "";
    private boolean addPerson = true;
    private DubePersonList.UserInfoListBean userInfo;
    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_facerecog_set_one;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("人脸识别");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        tv_action.setText("历史");
//        tv_action.setVisibility(View.VISIBLE);
//        tv_action.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13); //设置45PX
//        tv_action.setTextColor(getResources().getColor(R.color.tv_464646));
//        tv_action.setOnClickListener(this);
        btn_face_next.setOnClickListener(this);
        iv_photo_show.setOnClickListener(this);
        mDialogTakePhoto = new DialogTakePhoto(this);
        String robotId = App.getInstance().getUserInfo().getDeviceid();
        String uid = App.getInstance().getUserInfo().getUserId()+"";
        String deviceKey = SystemUtils.getDeviceKey();
        faceColorRec = new FaceColorRec(App.getInstance().getUserInfo().getDeviceid(), uid, deviceKey, HttpLink.BASE_URL);
        if (getIntent().getExtras()!=null){
            addPerson = getIntent().getExtras().getBoolean("addPerson");
            userInfo = getIntent().getExtras().getParcelable("userInfo");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PORTRAIT_BY_TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            //拍照后返回
            Intent i = new Intent(this, SetPortraitActivity.class);
            i.putExtra(SetPortraitActivity.PATH, mDialogTakePhoto.getCurrentPhotoPath());
            startActivityForResult(i, REQUEST_PORTRAIT_CROP);
        } else if (requestCode == REQUEST_PORTRAIT_BY_PICK_PHOTO_CODE) {
            //相册选择并裁剪后返回
            if (resultCode != RESULT_OK){
                return;
            }
            showByCrop(data);
        } else if (requestCode == REQUEST_PORTRAIT_BY_SYSTEM_ALBUM) {
            //打开系统相册后回来
            if (resultCode != RESULT_OK || data == null || data.getData() == null){
                return;
            }
            // 调用相册选择返回的图片
            //调用图片库在这里返回结果
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            Intent i = new Intent(this, SetPortraitActivity.class);
            i.putExtra(SetPortraitActivity.PATH, imagePath);
            startActivityForResult(i, REQUEST_PORTRAIT_CROP);



//            //调用裁剪方法(注意，这里没有判断是否为空，应用中应该判断)
//            ContentValues contentValues = new ContentValues(1);
//            contentValues.put(MediaStore.Images.Media.DATA, mFile.getAbsolutePath());
//            Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
//            cropImageUri(data.getData(), iv_photo_show.getWidth(), iv_photo_show.getHeight(),
//                    REQUEST_PORTRAIT_BY_FINAL, uri);
        } else if (requestCode == REQUEST_PORTRAIT_CROP) {   //裁剪后回来
            if (resultCode != RESULT_OK){
                return;
            }
            Bundle extras = data.getExtras();
            if (extras != null) {
                String mPath = extras.getString(SetPortraitActivity.PATH);
                if (!TextUtils.isEmpty(mPath) && new File(mPath).exists()) {
                    recogPic(mPath, PhotoUtil.getBitmapFromPath(mPath, iv_photo_show.getWidth(), iv_photo_show.getHeight()));
                }
            }

        } else if (requestCode == REQUEST_PORTRAIT_BY_FINAL) {   //裁剪后返回
            if (data != null && data.getExtras() != null) {
                Bitmap bitmap = data.getExtras().getParcelable("data");
                String mPath = mFile.getAbsolutePath();
                recogPic(mPath, bitmap);
            }

        }
    }


    @SuppressWarnings("deprecation")
    private String getGalleryImgPath(Uri photoUri) {
        // 这里开始的第二部分，获取图片的路径：

        String[] proj = {MediaStore.Images.Media.DATA};

        // 好像是android多媒体数据库的封装接口，具体的看Android文档
        Cursor cursor = this.managedQuery(photoUri, proj, null,
                null, null);

        // 按我个人理解 这个是获得用户选择的图片的索引值
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        // 将光标移至开头 ，这个很重要，不小心很容易引起越界
        cursor.moveToFirst();

        // 最后根据索引值获取图片路径
        String path = cursor.getString(column_index);

        return path;
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode, Uri path) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //是否裁剪
        intent.putExtra("crop", "true");
        //设置xy的裁剪比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //该参数设定为你的imageView的大小
        intent.putExtra("outputX", iv_photo_show.getWidth() / 2);
        intent.putExtra("outputY", iv_photo_show.getWidth() / 2);

        //是否缩放
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        //输入图片的Uri，指定以后，可以在这个uri获得图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, path);
        //是否返回图片数据，可以不用，直接用uri就可以了
        intent.putExtra("return-data", true);
        //设置输入图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //是否关闭面部识别
        intent.putExtra("noFaceDetection", true); // no face detection
        //启动
        startActivityForResult(intent, requestCode);
    }

    //从裁剪页面返回后的处理
    private void showByCrop(Intent data) {
        if (data == null) {
            return;
        }
        ArrayList<String> picList = data.getStringArrayListExtra(
                LocalPhotoSelectorActivity.EXTRA_PHOTO_RESULT);
        if (picList == null || picList.size() == 0) {
            ToastUtils.showToastLong(this, R.string.select_pic_error);
            return;
        }
        final String picPath = picList.get(0);
        if (TextUtils.isEmpty(picPath)) {
            ToastUtils.showToastLong(this, R.string.select_pic_error);
            return;
        }

        recogPic(picPath, PhotoUtil.getBitmapFromPath(picPath, iv_photo_show.getWidth(), iv_photo_show.getHeight()));
    }


    //识别图片    picPath  图片路径
    private void recogPic(String path, Bitmap bitmap) {
        final Dialog loadingDialg = DialogUtils.createLoadingDialog(this, "正在识别,请稍候...", false);
        loadingDialg.setCanceledOnTouchOutside(false);
        loadingDialg.show();
        /****************************************************************/
        //林知礼
        //2017年08月22日
        //检测是否有人脸在图像中
        byte[] bgra = faceColorRec.getPixelsBGRA(bitmap);
        boolean has = faceColorRec.hasFace(bgra, bitmap.getWidth(), bitmap.getHeight());
        loadingDialg.dismiss();
        if (has) {
            currentPicPatch = path;
            showPic(bitmap);
            DubePerson dubePerson = faceColorRec.getPersonByARGB(bitmap.getWidth(), bitmap.getHeight(), bgra);
            if ("-1".equals(dubePerson.getPersonId())) {
                //unknow
                personId = "";
                token = faceColorRec.getFaceLandmark(bgra, bitmap.getWidth(), bitmap.getHeight());
            } else {
                //know
                personId = dubePerson.getPersonId();
                token = faceColorRec.getFaceLandmark(bgra, bitmap.getWidth(), bitmap.getHeight());
            }
            if (!addPerson){
                personId = userInfo.getPersonId()+"";
                boolean match = faceColorRec.isMatch(token, userInfo);
                if (!match){
                    currentPicPatch = "";
                    showToast("抱歉,图片里的人物和"+userInfo.getUsername() +"似乎不是同一个人,换一张试试吧");
                    iv_photo_show.setBackgroundDrawable(null);
                    tv_to_pic.setVisibility(View.VISIBLE);//隐藏选择图片按钮
                }
            }
        } else {
            currentPicPatch = "";
            showToast("抱歉,无法识别图片里的人物,换一张试试吧");
            iv_photo_show.setBackgroundDrawable(null);
            tv_to_pic.setVisibility(View.VISIBLE);//隐藏选择图片按钮
        }
        /****************************************************************/

        //获取所有人列表
        //遍历所有人找相似度最高的人
        //如果相似度超过阈值,则认为是同一个人
        //否则创建新的人



    }

    //显示当前图片
    private void showPic(Bitmap bitmap) {
//        Resources resources = getBaseContext().getResources();
//        Drawable imageDrawable = resources.getDrawable(R.drawable.background_image); //图片在drawable目录下
//        mImageView.setBackgroundDrawable(imageDrawable);
//        iv_photo_show.setImageBitmap(bitmap);
        iv_photo_show.setBackgroundDrawable(new BitmapDrawable(bitmap));
        tv_to_pic.setVisibility(View.GONE);//隐藏选择图片按钮
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_action:   //历史
                break;
            case R.id.btn_face_next: //下一步
                if (TextUtils.isEmpty(currentPicPatch)) {
                    showToast("请先选择图片");
                    break;
                }
                QiniuUtil.getQiNiuToken(this, currentPicPatch, new QiniuUtil.UploadCallBack() {

                    @Override
                    public void isOk(String path, String pathName, String t) {
                        if (!addPerson) {

                            RetrofitApi.getInstance(HttpLink.BASE_URL).getApi().addFace(userInfo.getUsername(),
                                    userInfo.getAge() + "", userInfo.getTitle(), userInfo.getSex() + "",
                                    userInfo.getPersonId() + "", path, FaceColorRec.getServiceKey(token), App.getInstance().getUserInfo().getUserId() + "",
                                    SystemUtils.getDeviceKey(), App.getInstance().getUserInfo().getDeviceid()).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new io.reactivex.Observer<DefResponse>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(DefResponse value) {
                                    if ("0000".equals(value.getCode())) {
                                        showToast("添加成功");
                                        finish();
                                        TagUtil.showLogDebug("提交成功" + value.getCode() + "\n" + value.getMessage());
                                    } else {
                                        showToast("提交失败" + value.getMessage());
                                    }

                                }

                                @Override
                                public void onError(Throwable e) {
                                    showToast(e.getMessage());
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                            return;
                        }
                        Intent intent = new Intent(FaceRecogSetOneActivity.this, FaceRecogSetTwoActivity.class);
                        intent.putExtra(QINIU_URL, path);
                        intent.putExtra(BAIDU_PERSONID, personId);
                        intent.putExtra("token", token);
                        startActivityForResult(intent, RESULT_OK);
                        finish();
                        //addBaiduRecogUser(path);
                    }

                    @Override
                    public void isFail() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("抱歉，图片上传失败，换一张图片试试吧");
                            }
                        });
                    }
                });

                break;
            case R.id.rl_back_return: //返回
                finish();
                break;
            case R.id.iv_photo_show://上传头像dialog
                mDialogTakePhoto.show();
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
