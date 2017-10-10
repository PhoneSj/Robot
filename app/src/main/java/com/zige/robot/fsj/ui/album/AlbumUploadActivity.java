package com.zige.robot.fsj.ui.album;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.zige.robot.R;
import com.zige.robot.fsj.base.StateActivity;
import com.zige.robot.fsj.model.bean.AlbumUploadBean;
import com.zige.robot.fsj.model.bean.QiniuToken;
import com.zige.robot.fsj.model.http.response.AlbumHttpResponse;
import com.zige.robot.fsj.model.param.PhotoParams;
import com.zige.robot.fsj.ui.album.adapter.UploadAdapter;
import com.zige.robot.fsj.ui.album.util.HttpConfig;
import com.zige.robot.fsj.ui.album.util.PickConfig;
import com.zige.robot.fsj.ui.album.util.PickUtils;
import com.zige.robot.fsj.util.LogUtil;
import com.zige.robot.fsj.util.RxUtil;
import com.zige.robot.fsj.util.SnackbarUtil;
import com.zige.robot.utils.SharedPreferencesUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Created by PhoneSj on 2017/9/25.
 */

public class AlbumUploadActivity extends StateActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rv_photo)
    RecyclerView rvPhoto;
    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;

    private List<String> uploadPaths;
    private List<AlbumUploadBean> uploadDatas;
    private UploadAdapter adapter;
    private long albumId;

    private ThreadPoolExecutor poolExecutor;

    @Override
    protected int getLayout() {
        return R.layout.activity_album_upload;
    }

    @Override
    protected void initEventAndData() {
        albumId = getIntent().getLongExtra(PickConfig.INTENT_ALBUM_ID, 0);
        setToolbar(toolBar, "上传图片");
        uploadPaths = getIntent().getStringArrayListExtra(PickConfig.INTENT_IMG_LIST_SELECT);
        GridLayoutManager layoutManager = new GridLayoutManager(this, UploadAdapter.SPAN_COUNT);
        rvPhoto.setLayoutManager(layoutManager);
        rvPhoto.addItemDecoration(new SpaceItemDecoration(PickUtils.getInstance(AlbumUploadActivity.this)
                                                                   .dp2px(PickConfig.ITEM_SPACE), UploadAdapter.SPAN_COUNT));
        rvPhoto.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        adapter = new UploadAdapter(AlbumUploadActivity.this, genarateDatas(uploadPaths));
        rvPhoto.setAdapter(adapter);
        //初始化线程池
        poolExecutor = new ThreadPoolExecutor(4, 16, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(64));
    }

    private List<AlbumUploadBean> genarateDatas(List<String> paths) {
        uploadDatas = new ArrayList<>();
        for (String path : paths) {
            AlbumUploadBean bean = new AlbumUploadBean();
            bean.setPath(path);
            uploadDatas.add(bean);
        }
        return uploadDatas;
    }

    /**
     * d第一步：从自己的服务器获取七牛token
     *
     * @param position
     */
    public void uploadPhoto(final int position) {
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final String key = UUID.randomUUID().toString() + ".png";//统一加png后缀名
                HttpConfig.getInstance()
                          .getApi()
                          .getQiniuToken(key)
                          .compose(RxUtil.<QiniuToken>rxSchedulerHelper())
                          .subscribe(new Consumer<QiniuToken>() {
                              @Override
                              public void accept(QiniuToken token) throws Exception {
                                  if (token.isSuccess()) {
                                      LogUtil.showI(AlbumUploadActivity.this.getClass()
                                                                            .getSimpleName() + "获取七牛token成功");
                                      uploadPhotoToQiniu(position, key, token.getQiniuToken());
                                  } else {
                                      LogUtil.showI(AlbumUploadActivity.this.getClass()
                                                                            .getSimpleName() + "获取七牛token失败");
                                      stateError("上传失败");
                                  }
                              }
                          }, new Consumer<Throwable>() {
                              @Override
                              public void accept(Throwable throwable) throws Exception {
                                  LogUtil.showI(AlbumUploadActivity.this.getClass()
                                                                        .getSimpleName() + "获取七牛token失败");
                                  stateError("上传失败");
                              }
                          });
            }
        });
    }

    /**
     * 第二步：将图片上传到七牛服务器
     */
    private void uploadPhotoToQiniu(final int position, final String key, final String token) {
        final String path = uploadDatas.get(position).getPath();
        Configuration.Builder configuration = new Configuration.Builder();
        configuration.retryMax(2);
        configuration.responseTimeout(7);
        final UploadManager uploadManager = new UploadManager(configuration.build());
        new Thread() {
            @Override
            public void run() {
                uploadManager.put(path, key, token, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            LogUtil.showI(AlbumUploadActivity.this.getClass()
                                                                  .getSimpleName() + "上传图片成功");
                            String url = "http://r.mento.ai/" + key;
                            uploadPhotoToServer(position, url);
                        } else {
                            stateError("上传失败");
                            LogUtil.showI(AlbumUploadActivity.this.getClass()
                                                                  .getSimpleName() + "上传图片到七牛失败");
                            uploadDatas.get(position).setState(AlbumUploadBean.UploadState.FAIL);
                            AlbumUploadActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyItemChanged(position);
                                }
                            });
                        }
                    }

                }, new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                        uploadDatas.get(position).setState(AlbumUploadBean.UploadState.UPLOADING);
                        uploadDatas.get(position).setPercent(percent);
                        AlbumUploadActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyItemChanged(position);
                            }
                        });
                    }
                }, null));
            }
        }.start();

    }

    /**
     * 第三步：将图片在七牛的url上传到自己的服务器
     */
    private void uploadPhotoToServer(final int position, String url) {
        PhotoParams params = new PhotoParams();
        params.setUsername(SharedPreferencesUtils.getHXAcount());
        params.setRobotDeviceId(SharedPreferencesUtils.getRobotIdFromSP());
        params.setPhotoAlbumId(albumId);
        params.setPhotoUrl(url);
        AlbumHttp.uploadPhoto(layoutRoot, params, new AlbumHttp.AlbumHttpCallback<AlbumHttpResponse, Throwable>() {
            @Override
            public void onSuccess(AlbumHttpResponse albumHttpResponse) {
                if (albumHttpResponse.isSuccess()) {
                    LogUtil.showI(AlbumUploadActivity.this.getClass()
                                                          .getSimpleName() + "上传图片url成功");
                    stateError("上传成功");
                    uploadDatas.get(position)
                               .setState(AlbumUploadBean.UploadState.FINISH);
                    adapter.notifyItemChanged(position);
                } else {
                    LogUtil.showI(AlbumUploadActivity.this.getClass()
                                                          .getSimpleName() + "上传图片url失败");
                    stateError("上传失败");
                    uploadDatas.get(position).setState(AlbumUploadBean.UploadState.FAIL);
                    adapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                LogUtil.showI(AlbumUploadActivity.this.getClass()
                                                      .getSimpleName() + "上传图片url失败");
                stateError("上传失败");
                uploadDatas.get(position).setState(AlbumUploadBean.UploadState.FAIL);
                adapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    protected void showErrorMsg(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            SnackbarUtil.showShort(layoutRoot, msg);
        }
    }
}
