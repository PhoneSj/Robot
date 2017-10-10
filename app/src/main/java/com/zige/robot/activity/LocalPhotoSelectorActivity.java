package com.zige.robot.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zige.robot.adapter.LocalPhotoSelectorAdapter;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.AlbumPhotoEntity;
import com.zige.robot.utils.ToastUtils;
import com.zige.robot.view.DialogTakePhoto;
import com.zige.robot.view.ListImageDirPopupWindow;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.zige.robot.R;


/**
 * 类描述：本地照片选择页面
 */
public class LocalPhotoSelectorActivity extends BaseActivity implements ListImageDirPopupWindow.OnImageDirSelected, OnClickListener, LocalPhotoSelectorAdapter.OnLocalPhotoSelectListener {

    public static final String EXTRA_PHOTO_RESULT = "EXTRA_PHOTO_RESULT";

    public static final String EXTRA_MAX_SELECTED_COUNT = "EXTRA_MAX_SELECTED_COUNT";

    public static final String EXTRA_IS_MULTI = "EXTRA_IS_MULTI";

    /**
     * 最多选择图片的个数
     */
    public static int DEFAULT_MAX_SELECTED_COUNT = 6;

    private ProgressDialog mProgressDialog;

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;

    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;

    /**
     * 所有的图片路径
     */
    private List<String> mAllImgList = new ArrayList<>();

    private GridView mGirdView;

    private LocalPhotoSelectorAdapter mAdapter;

    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<AlbumPhotoEntity> mImageFloders = new ArrayList<>();

    private RelativeLayout mBottomLy;

    private TextView mChooseDir;

    private TextView mImageCount;

    private TextView tv_action;

    int totalCount = 0;

    private int mScreenHeight;

    private int mMaxSelectedCount = DEFAULT_MAX_SELECTED_COUNT;
    private boolean isMulti = false;

    private ListImageDirPopupWindow mListImageDirPopupWindow;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mProgressDialog.dismiss();
            // 为View绑定数据
            data2View();
            // 初始化展示文件夹的popupWindw
            initListDirPopupWindow();
        }
    };

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            ToastUtils.showToastShort(mContext, "选择文件出错");
            return;
        }
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new LocalPhotoSelectorAdapter(this, mAllImgList, isMulti);
        mAdapter.setAlbumSelectListener(this);
        mGirdView.setAdapter(mAdapter);
        mImageCount.setText(getString(R.string.company_piece_of, totalCount));
        if (isMulti) {
            tv_action.setText("完成(" + mAdapter.getSelectedImage().size() + ")");
        }
    }

    ;

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindow() {
        AlbumPhotoEntity entity = new AlbumPhotoEntity();
        entity.setCount(mAllImgList.size());
        entity.setName(getString(R.string.all_pic));
        entity.setFirstImagePath(mImageFloders.get(0).getFirstImagePath());
        mImageFloders.add(0, entity);
        mListImageDirPopupWindow = new ListImageDirPopupWindow(LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7), mImageFloders, LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }


    public static void selectSingleImage(Activity act, int requestCode) {
        Intent i = new Intent(act, LocalPhotoSelectorActivity.class);
        act.startActivityForResult(i, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        initData();
        initView();
        getImages();
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        mBottomLy.setOnClickListener(this);
    }


    private void initData() {
        if (getIntent().hasExtra(EXTRA_MAX_SELECTED_COUNT)) {
            mMaxSelectedCount = getIntent().getIntExtra(EXTRA_MAX_SELECTED_COUNT, mMaxSelectedCount);
        }
        if (getIntent().hasExtra(EXTRA_IS_MULTI)) {
            isMulti = getIntent().getBooleanExtra(EXTRA_IS_MULTI, isMulti);
        }
        if (mMaxSelectedCount == 1) {
            //single mode
            isMulti = false;
        }
    }

    /**
     * 初始化View
     */
    private void initView() {
        setTitleName("图片选择");
        mGirdView = (GridView) findViewById(R.id.id_gridView);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);
        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
        tv_action = (TextView) findViewById(R.id.tv_action);
        tv_action.setText("完成");
        tv_action.setVisibility(View.VISIBLE);
        tv_action.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13); //设置45PX
        tv_action.setTextColor(getResources().getColor(R.color.tv_464646));
        tv_action.setOnClickListener(this);
        if (!isMulti) {
            tv_action.setVisibility(View.GONE);
        } else {
            tv_action.setOnClickListener(this);
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ToastUtils.showToastShort(mContext, R.string.have_no_storage);
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, getString(R.string.common_loading));

        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = LocalPhotoSelectorActivity.this
                        .getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED + " desc");

                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    Log.e("TAG", path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    AlbumPhotoEntity imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new AlbumPhotoEntity();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    String[] pics = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            return isPic(filename);
                        }
                    });
                    if (pics == null) {
                        continue;
                    }
                    int picSize = pics.length;
                    totalCount += picSize;

                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);

                    List<String> list = Arrays.asList(parentFile.list((new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            return isPic(filename);
                        }
                    })));
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        mAllImgList.add(parentFile + "/" + list.get(i));
                    }
                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();

    }

    @Override
    public void selected(AlbumPhotoEntity floder) {
        if (floder.getName().equals(getString(R.string.all_pic))) {//选择所有图片
            mAdapter = new LocalPhotoSelectorAdapter(this, mAllImgList, isMulti);
            mAdapter.setAlbumSelectListener(this);
            mGirdView.setAdapter(mAdapter);
            // mAdapter.notifyDataSetChanged();
            mImageCount.setText(getString(R.string.company_piece_of, mAllImgList.size()));
            mChooseDir.setText(getString(R.string.all_pic));
            mListImageDirPopupWindow.dismiss();
            if (isMulti) {
                tv_action.setText(getString(R.string.done) + "(" + mAdapter.getSelectedImage().size() + ")");
            }
            return;
        }
        mImgDir = new File(floder.getDir());
        /*
      选中的图片路径
     */
        List<String> imgList = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return isPic(filename);
            }
        }));
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        List<String> list = new ArrayList<>();
        int size = imgList.size();
        for (int i = 0; i < size; i++) {
            list.add(mImgDir.getAbsolutePath() + "/" + imgList.get(i));
        }
        mAdapter = new LocalPhotoSelectorAdapter(this, list, isMulti);
        mAdapter.setAlbumSelectListener(this);
        mGirdView.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();
        mImageCount.setText(getString(R.string.company_piece_of, floder.getCount()));
        mChooseDir.setText(floder.getName());
        mListImageDirPopupWindow.dismiss();
        if (isMulti) {
            tv_action.setText(getString(R.string.done) + "(" + mAdapter.getSelectedImage().size() + ")");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_bottom_ly://选择相册的弹窗
                mListImageDirPopupWindow.setAnimationStyle(R.style.bottom_pop_window_anim_style);
                mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .9f;
                getWindow().setAttributes(lp);
                break;
            case R.id.rl_back_return: //返回
                finish();
                break;
        }
    }


    //是否是jpg或者png图片
    private boolean isPic(String filename) {
        return filename.endsWith(".jpg")
                || filename.endsWith(".png")
                || filename.endsWith(".jpeg");
    }


    @SuppressLint("StringFormatMatches")
    @Override
    public boolean isInterceptSelect(int countBeforeSelected, boolean isCheck) {
        if (isCheck) {
            //判断是否到达上限
            if (countBeforeSelected >= mMaxSelectedCount) {
                ToastUtils.showToastLong(this, getString(R.string.max_pic, mMaxSelectedCount));
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterSelect(int countAfterSelected, boolean isCheck) {
        // TODO Auto-generated method stub
        if (isMulti) {
            if (countAfterSelected > 0) {
                tv_action.setEnabled(true);
//                bt_preview.setEnabled(true);
            } else {
                tv_action.setEnabled(false);
//                bt_preview.setEnabled(false);
            }
            tv_action.setText(getString(R.string.done) + "(" + countAfterSelected + ")");
        } else {
            beginPortraitCrop(mAdapter.getSelectedImage().get(0));
        }
    }

    /**
     * 多张图片处理
     */
    private void done() {
        //过滤损坏的照片
        List<String> selectedPic = filterSelectedPic();
        if (selectedPic.size() == 0 && selectedPic.size() != mAdapter.getSelectedImage().size()) {
            ToastUtils.showToastLong(this, R.string.select_pic_error);
            return;
        }
        Intent i = new Intent();
        i.putStringArrayListExtra(EXTRA_PHOTO_RESULT, (ArrayList<String>) selectedPic);
        this.setResult(Activity.RESULT_OK, i);
        this.finish();
    }

    /**
     * 单张图片
     * param picPath
     */
    private void done(String picPath) {
        //过滤损坏的照片
        String selectedPic = filterSelectedPic(picPath);
        if (TextUtils.isEmpty(selectedPic)) {
            ToastUtils.showToastLong(this, R.string.select_pic_error);
            return;
        }
        ArrayList<String> list = new ArrayList<>();
        list.add(selectedPic);
        Intent i = new Intent();
        i.putExtra(EXTRA_PHOTO_RESULT, list);
        this.setResult(Activity.RESULT_OK, i);
        this.finish();
    }

    private List<String> filterSelectedPic() {
        List<String> result = new ArrayList<>();
        List<String> str = mAdapter.getSelectedImage();
        File f = null;
        try {
            if (str != null && str.size() > 0) {
                for (int i = 0; i < str.size(); i++) {
                    f = new File(str.get(i));
                    if (f.exists() && f.length() > 0) {
                        result.add(str.get(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    private String filterSelectedPic(String picPath) {
        String result = "";
        String str = picPath;
        File f;
        try {
            if (!TextUtils.isEmpty(str)) {
                f = new File(str);
                if (f.exists() && f.length() > 0) {
                    result = picPath;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    private void beginPortraitCrop(String imagePath) {
        Intent i = new Intent(this, SetPortraitActivity.class);
        i.putExtra(SetPortraitActivity.PATH, imagePath);
        startActivityForResult(i, DialogTakePhoto.REQUEST_PORTRAIT_CROP);

//        if (!TextUtils.isEmpty(imagePath) && new File(imagePath).exists()) {
//            done(imagePath);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == DialogTakePhoto.REQUEST_PORTRAIT_CROP) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    String path = extras.getString(SetPortraitActivity.PATH);
                    if (!TextUtils.isEmpty(path) && new File(path).exists()) {
                        done(path);
                    }
                }
            }
        }
    }

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_album_list;

    }
}
