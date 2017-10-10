package com.zige.robot.fsj.ui.album;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zige.robot.R;
import com.zige.robot.fsj.Constants;
import com.zige.robot.fsj.base.StateActivity;
import com.zige.robot.fsj.model.bean.PhotoListBean;
import com.zige.robot.fsj.ui.album.adapter.FileAdapter;
import com.zige.robot.fsj.ui.album.util.PickConfig;
import com.zige.robot.fsj.ui.album.util.PickUtils;
import com.zige.robot.fsj.util.LogUtil;
import com.zige.robot.fsj.util.SnackbarUtil;
import com.zige.robot.fsj.widget.EmptyRecyclerView;
import com.zige.robot.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by PhoneSj on 2017/9/27.
 */

public class AlbumFileActivity extends StateActivity {

    private static final int FIRST_PAGE = 1;

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rv_album)
    EmptyRecyclerView rvAlbum;
    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;
    @BindView(R.id.layout_empty)
    RelativeLayout layoutEmpty;

    private List<PhotoListBean.ListBean> imgDatas;
    private FileAdapter adapter;

    private long albumId;
    private String albumName;
    private int currentPage = FIRST_PAGE;
    private int pageSize = 40;
    private boolean isLoadingMore = false;
    private boolean hasNextPage;

    @Override
    protected int getLayout() {
        return R.layout.activity_album_file;
    }

    @Override
    protected void initEventAndData() {
        albumId = getIntent().getLongExtra(PickConfig.INTENT_ALBUM_ID, Constants.DEFAULT_SELECTED_ALBUM_ID);
        albumName = getIntent().getStringExtra(PickConfig.INTENT_ALBUM_NAME);
        setToolbar(toolBar, albumName);
        rvAlbum.setEmptyView(layoutEmpty);
        imgDatas = new ArrayList<>();
        adapter = new FileAdapter(AlbumFileActivity.this, imgDatas);
        rvAlbum.setLayoutManager(new GridLayoutManager(AlbumFileActivity.this, FileAdapter.SPAN_COUNT));
        rvAlbum.addItemDecoration(new SpaceItemDecoration(PickUtils.getInstance(AlbumFileActivity.this)
                                                                   .dp2px(PickConfig.ITEM_SPACE), FileAdapter.SPAN_COUNT));
        rvAlbum.setAdapter(adapter);
        adapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int positon) {
                Intent intent = new Intent(AlbumFileActivity.this, PreviewActivity.class);
                intent.putExtra(PickConfig.INTENT_ALBUM_ID, albumId);
                intent.putExtra(PickConfig.INTENT_PHOTO_POSITION, positon);
                startActivity(intent);
            }
        });
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
        getPhotoList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_file, menu);
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
            case R.id.menu_select: {
                Intent intent = new Intent(AlbumFileActivity.this, AlbumEditActivity.class);
                intent.putExtra(PickConfig.INTENT_ALBUM_ID, albumId);
                intent.putExtra(PickConfig.INTENT_ALBUM_NAME, albumName);
                startActivity(intent);
                break;
            }
        }
        return true;
    }

    private void getPhotoList() {
        stateLoading("正在加载");
        currentPage = FIRST_PAGE;
        String username = SharedPreferencesUtils.getHXAcount();
        String robotDeviceId = SharedPreferencesUtils.getRobotIdFromSP();
        AlbumHttp.fetchPhotoList(layoutRoot, currentPage, pageSize, username, robotDeviceId, albumId, new AlbumHttp.AlbumHttpCallback<PhotoListBean, Throwable>() {
            @Override
            public void onSuccess(PhotoListBean photoListBean) {
                LogUtil.showI("AlbumFileActivity后去图片列表成功");
                stateContent();
                hasNextPage = photoListBean.isHasNextPage();
                imgDatas.clear();
                imgDatas.addAll(photoListBean.getList());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Throwable throwable) {
                stateError("获取云相片失败");
                LogUtil.showI("AlbumFileActivity后去图片列表失败");
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

    @Override
    protected void stateContent() {
        super.stateContent();
        isLoadingMore = false;
    }

    @Override
    protected void showErrorMsg(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            SnackbarUtil.showShort(layoutRoot, msg);
        }
    }
}
