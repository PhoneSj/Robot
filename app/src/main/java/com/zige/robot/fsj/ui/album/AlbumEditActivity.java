package com.zige.robot.fsj.ui.album;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zige.robot.R;
import com.zige.robot.fsj.Constants;
import com.zige.robot.fsj.base.StateActivity;
import com.zige.robot.fsj.model.bean.PhotoListBean;
import com.zige.robot.fsj.model.http.response.AlbumHttpResponse;
import com.zige.robot.fsj.model.param.PhotoDeleteBatchParams;
import com.zige.robot.fsj.model.param.PhotoMoveParams;
import com.zige.robot.fsj.model.param.PhotoParams;
import com.zige.robot.fsj.ui.album.adapter.PhotoEditAdapter;
import com.zige.robot.fsj.ui.album.util.PickConfig;
import com.zige.robot.fsj.ui.album.util.PickUtils;
import com.zige.robot.fsj.util.SnackbarUtil;
import com.zige.robot.fsj.widget.EmptyRecyclerView;
import com.zige.robot.http.Parser;
import com.zige.robot.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by PhoneSj on 2017/9/26.
 */

public class AlbumEditActivity extends StateActivity {

    private static final int FIRST_PAGE = 1;

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rv_album)
    EmptyRecyclerView rvAlbum;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;
    @BindView(R.id.layout_empty)
    RelativeLayout layoutEmpty;

    private List<PhotoListBean.ListBean> imgDatas;
    private PhotoEditAdapter adapter;

    private long albumId = Constants.DEFAULT_SELECTED_ALBUM_ID;
    private long selectedAlbumId = Constants.DEFAULT_SELECTED_ALBUM_ID;
    private String albumName;
    private int currentPage = FIRST_PAGE;
    private int pageSize = 50;
    private boolean isLoadingMore = false;
    private boolean hasNextPage;

    @Override
    protected int getLayout() {
        return R.layout.activity_album_edit;
    }

    @Override
    protected void initEventAndData() {
        albumId = getIntent().getLongExtra(PickConfig.INTENT_ALBUM_ID, 0);
        albumName = getIntent().getStringExtra(PickConfig.INTENT_ALBUM_NAME);
        setToolbar(toolBar, albumName);
        imgDatas = new ArrayList<>();
        adapter = new PhotoEditAdapter(AlbumEditActivity.this, imgDatas, PhotoEditAdapter.Type.remote);
        rvAlbum.setLayoutManager(new GridLayoutManager(AlbumEditActivity.this, PhotoEditAdapter.SPAN_COUNT));
        rvAlbum.addItemDecoration(new SpaceItemDecoration(PickUtils.getInstance(AlbumEditActivity.this)
                                                                   .dp2px(PickConfig.ITEM_SPACE), PhotoEditAdapter.SPAN_COUNT));
        rvAlbum.setAdapter(adapter);
        rvAlbum.setEmptyView(layoutEmpty);
        rvAlbum.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int spanCount = ((GridLayoutManager) rvAlbum.getLayoutManager()).getSpanCount();
                int lastVisibleItem = ((GridLayoutManager) rvAlbum.getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = rvAlbum.getLayoutManager().getItemCount();
                if (lastVisibleItem >= totalItemCount - 2 * spanCount && dy > 0) {  //还剩2行个Item时加载更多
                    if (!isLoadingMore && hasNextPage) {
                        isLoadingMore = true;
                        getMorePhotoList();
                    }
                }
            }
        });
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPhotoList();
            }
        });
        getPhotoList();
    }

    @OnClick({R.id.btn_delete, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                showDeleteDialog();
                break;
            case R.id.btn_cancel:
                doMovePhotos();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.menu_cancel: {
                adapter.clearSelected();
                break;
            }
        }
        return true;
    }

    private void getPhotoList() {
        currentPage = FIRST_PAGE;
        stateLoading("正在加载");
        String username = SharedPreferencesUtils.getHXAcount();
        String robotDeviceId = SharedPreferencesUtils.getRobotIdFromSP();
        AlbumHttp.fetchPhotoList(layoutRoot, currentPage, pageSize, username, robotDeviceId, albumId, new AlbumHttp.AlbumHttpCallback<PhotoListBean, Throwable>() {
            @Override
            public void onSuccess(PhotoListBean photoListBean) {
                stateContent();
                if (photoListBean != null && photoListBean.getList() != null) {
                    hasNextPage = photoListBean.isHasNextPage();
                    imgDatas.clear();
                    imgDatas.addAll(photoListBean.getList());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                stateError("加载失败");
            }
        });
    }

    private void getMorePhotoList() {
        String username = SharedPreferencesUtils.getHXAcount();
        String robotDeviceId = SharedPreferencesUtils.getRobotIdFromSP();
        AlbumHttp.fetchPhotoList(layoutRoot, ++currentPage, pageSize, username, robotDeviceId, albumId, new AlbumHttp.AlbumHttpCallback<PhotoListBean, Throwable>() {
            @Override
            public void onSuccess(PhotoListBean photoListBean) {
                stateContent();
                if (photoListBean != null && photoListBean.getList() != null) {
                    hasNextPage = photoListBean.isHasNextPage();
                    int start = imgDatas.size();
                    imgDatas.addAll(photoListBean.getList());
                    adapter.notifyItemRangeChanged(start, photoListBean.getList().size());
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                stateError("加载失败");
            }
        });
    }

    /**
     * 是否有选择图片
     *
     * @return
     */
    private boolean hasSelectPhotos() {
        if (adapter == null || adapter.getSelectPosList() == null || adapter.getSelectPosList()
                                                                            .size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 移动图片
     */
    private void doMovePhotos() {
        Intent intent = new Intent(AlbumEditActivity.this, AlbumDirActivity.class);
        intent.putExtra(PickConfig.INTENT_SELECT_REMOTE_ALBUM_ID, selectedAlbumId);
        startActivityForResult(intent, PickConfig.REQUEST_CODE_REMOTE_DIR);
    }

    /**
     * 删除图片
     */
    private void showDeleteDialog() {
        if (!hasSelectPhotos()) {
            return;
        }
        int count = adapter.getSelectPosList().size();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否删除")
               .setMessage("选中的" + count + "张照片")
               .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                   }
               })
               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                       deletePhotos();
                   }
               })
               .create().show();

    }

    private void deletePhotos() {
        stateLoading("正在删除");
        PhotoDeleteBatchParams params = new PhotoDeleteBatchParams();
        params.setPhotoIds(adapter.getSelectPhotoIds());
        params.setUsername(SharedPreferencesUtils.getHXAcount());
        params.setRobotDeviceId(SharedPreferencesUtils.getRobotIdFromSP());
        AlbumHttp.deleteBatchPhoto(layoutRoot, params, new AlbumHttp.AlbumHttpCallback<AlbumHttpResponse, Throwable>() {
            @Override
            public void onSuccess(AlbumHttpResponse albumHttpResponse) {
                if (albumHttpResponse.getCode()
                                     .equals("200") || albumHttpResponse.getMessage()
                                                                        .equalsIgnoreCase(AlbumHttpResponse.RESULT_SUCCESS)) {
                    stateContent("删除成功");
                    //刷新
                    getPhotoList();
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                stateError("删除失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PickConfig.REQUEST_CODE_REMOTE_DIR) {
            selectedAlbumId = data.getLongExtra(PickConfig.INTENT_SELECT_REMOTE_ALBUM_ID, Constants.DEFAULT_SELECTED_ALBUM_ID);
            movePhotos();
        }
    }

    private void movePhotos() {
        PhotoMoveParams params = new PhotoMoveParams();
        params.setUsername(SharedPreferencesUtils.getHXAcount());
        params.setRobotDeviceId(SharedPreferencesUtils.getRobotIdFromSP());
        params.setToPhotoAlbumId(albumId);
        params.setPhotoIds(adapter.getSelectPhotoIds());
        AlbumHttp.movePhoto(layoutRoot, params, new AlbumHttp.AlbumHttpCallback<AlbumHttpResponse, Throwable>() {
            @Override
            public void onSuccess(AlbumHttpResponse albumHttpResponse) {
                if (albumHttpResponse.isSuccess()) {
                    stateContent("移动成功");
                } else {
                    stateError("移动失败");
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                stateError("移动失败");
            }
        });
    }

    @Override
    protected void stateContent(String msg) {
        super.stateContent(msg);
        isLoadingMore = false;
        srlRefresh.setRefreshing(false);
        if (!TextUtils.isEmpty(msg)) {
            SnackbarUtil.showShort(layoutRoot, msg);
        }
    }

    @Override
    protected void showErrorMsg(String msg) {
        super.stateError(msg);
        srlRefresh.setRefreshing(false);
        if (!TextUtils.isEmpty(msg)) {
            SnackbarUtil.showShort(layoutRoot, msg);
        }
    }
}
