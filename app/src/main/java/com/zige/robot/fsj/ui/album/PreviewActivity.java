package com.zige.robot.fsj.ui.album;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.fsj.base.StateActivity;
import com.zige.robot.fsj.model.bean.PhotoListBean;
import com.zige.robot.fsj.model.http.response.AlbumHttpResponse;
import com.zige.robot.fsj.model.param.PhotoDeleteParams;
import com.zige.robot.fsj.ui.album.adapter.PreviewAdapter;
import com.zige.robot.fsj.ui.album.util.ImgUtils;
import com.zige.robot.fsj.ui.album.util.PickConfig;
import com.zige.robot.fsj.util.LogUtil;
import com.zige.robot.fsj.util.SnackbarUtil;
import com.zige.robot.fsj.util.ToastUtil;
import com.zige.robot.utils.SharedPreferencesUtils;
import com.zige.robot.utils.permission.PermissionChecker;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by PhoneSj on 2017/9/27.
 */

public class PreviewActivity extends StateActivity {

    @BindView(R.id.vp_preview)
    ViewPager vpPreview;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_current_page)
    TextView tvCurrentPage;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_move)
    TextView tvMove;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.layout_root)
    RelativeLayout layoutRoot;

    private List<PhotoListBean.ListBean> imgDatas;
    private PreviewAdapter adapter;

    private int current;
    private boolean mIsHidden;

    protected PermissionChecker mPermissionChecker;

    static final String[] PERMISSIONS_SAVE = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_photo_preview;
    }

    @Override
    protected void initEventAndData() {
        long albumId = getIntent().getLongExtra(PickConfig.INTENT_ALBUM_ID, 0);
        current = getIntent().getIntExtra(PickConfig.INTENT_PHOTO_POSITION, 0);
        getPhotos(albumId);
        mPermissionChecker = new PermissionChecker(this);
    }

    @OnClick({R.id.tv_back, R.id.tv_delete, R.id.tv_move, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_delete:
                showDeletePhotoDialog();
                break;
            case R.id.tv_move:
                movePhoto();
                break;
            case R.id.tv_save:
                savePhoto();
                break;
        }
    }

    private void savePhoto() {
        //检查权限
        if (mPermissionChecker.isLackPermissions(PERMISSIONS_SAVE)) {
            mPermissionChecker.requestPermissions();
            stateError("缺少存储权限");
        } else {
            new Thread() {
                @Override
                public void run() {
                    Bitmap bitmap = ImgUtils.getHttpBitmap(imgDatas.get(current).getPhotoUrl());
                    ImgUtils.saveImageToGallery(PreviewActivity.this, bitmap);
                }
            }.start();
        }
    }

    private void movePhoto() {
        stateError("暂无此接口");
    }

    private void showDeletePhotoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否删除本张图片?");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                deletePhoto();
            }
        }).create().show();

    }

    private void deletePhoto() {
        long photoId = imgDatas.get(vpPreview.getCurrentItem()).getPhotoId();
        PhotoDeleteParams params = new PhotoDeleteParams();
        params.setUsername(SharedPreferencesUtils.getHXAcount());
        params.setRobotDeviceId(SharedPreferencesUtils.getRobotIdFromSP());
        AlbumHttp.deleteSinglePhoto(layoutRoot, params, photoId, new AlbumHttp.AlbumHttpCallback<AlbumHttpResponse, Throwable>() {
            @Override
            public void onSuccess(AlbumHttpResponse albumHttpResponse) {
                if (albumHttpResponse.isSuccess()) {
                    PreviewActivity.this.stateError("删除成功");
                    imgDatas.remove(current);
                    adapter.notifyDataSetChanged();
                } else {
                    PreviewActivity.this.stateError("删除失败");
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                PreviewActivity.this.stateError("删除失败");
            }
        });
    }

    private void getPhotos(final long albumId) {
        String username = SharedPreferencesUtils.getHXAcount();
        String robotDeviceId = SharedPreferencesUtils.getRobotIdFromSP();
        AlbumHttp.fetchPhotoList(layoutRoot, 0, Integer.MAX_VALUE, username, robotDeviceId, albumId, new AlbumHttp.AlbumHttpCallback<PhotoListBean, Throwable>() {
            @Override
            public void onSuccess(PhotoListBean photoListBean) {
                if (photoListBean != null && photoListBean.getList() != null) {
                    ToastUtil.shortShow("PreviewActivity获取相片集合成功");
                    LogUtil.showI("PreviewActivity获取相片集合成功");
                    imgDatas = photoListBean.getList();
                    adapter = new PreviewAdapter(PreviewActivity.this, imgDatas);
                    vpPreview.setAdapter(adapter);
                    vpPreview.setCurrentItem(current);//显示第current个
                    tvCurrentPage.setText((current + 1) + "/" + imgDatas.size());
                    vpPreview.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            current = position;
                            tvCurrentPage.setText((current + 1) + "/" + imgDatas.size());
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    //点击ViewPager控制显示隐藏
                    vpPreview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideOrShowToolbar();
                        }
                    });
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                ToastUtil.shortShow("PreviewActivity获取相片集合失败");
                LogUtil.showI("PreviewActivity获取相片集合失败");
            }
        });
    }

    /**
     * 显示隐藏功能选项
     */
    public void hideOrShowToolbar() {
        rlTop.animate()
             .translationY(mIsHidden ? 0 : -rlTop.getHeight())
             .setInterpolator(new DecelerateInterpolator(2))
             .start();
        tvSave.animate()
              .translationY(mIsHidden ? 0 : tvSave.getHeight())
              .setInterpolator(new DecelerateInterpolator(2))
              .start();
        mIsHidden = !mIsHidden;
    }

    @Override
    protected void showErrorMsg(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            SnackbarUtil.showShort(layoutRoot, msg);
        }
    }
}
