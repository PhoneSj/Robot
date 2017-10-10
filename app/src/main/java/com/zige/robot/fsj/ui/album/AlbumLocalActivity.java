package com.zige.robot.fsj.ui.album;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.zige.robot.R;
import com.zige.robot.fsj.Constants;
import com.zige.robot.fsj.base.SimpleActivity;
import com.zige.robot.fsj.model.bean.AlbumBean;
import com.zige.robot.fsj.model.bean.PhotoListBean;
import com.zige.robot.fsj.ui.album.adapter.PhotoEditAdapter;
import com.zige.robot.fsj.ui.album.model.GroupImage;
import com.zige.robot.fsj.ui.album.util.PickConfig;
import com.zige.robot.fsj.ui.album.util.PickPhotoHelper;
import com.zige.robot.fsj.ui.album.util.PickPhotoListener;
import com.zige.robot.fsj.ui.album.util.PickPreferences;
import com.zige.robot.fsj.ui.album.util.PickUtils;
import com.zige.robot.fsj.util.LogUtil;
import com.zige.robot.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by PhoneSj on 2017/9/23.
 */

public class AlbumLocalActivity extends SimpleActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rv_photo)
    RecyclerView rvPhoto;
    @BindView(R.id.tv_path)
    TextView tvPath;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;

    private List<PhotoListBean.ListBean> imgDatas = new ArrayList<>();
    private PhotoEditAdapter adapter;
    private RequestManager manager;

    private long albumId;

    @Override
    protected int getLayout() {
        return R.layout.activity_album_local;
    }

    @Override
    protected void initEventAndData() {
        setToolbar(toolBar, "选择照片");
        getAlbumInfo();
        manager = Glide.with(mContext);
        rvPhoto.setItemAnimator(new DefaultItemAnimator());
        rvPhoto.setLayoutManager(new GridLayoutManager(this, PhotoEditAdapter.SPAN_COUNT));
        rvPhoto.addItemDecoration(new SpaceItemDecoration(PickUtils.getInstance(this)
                                                                   .dp2px(PickConfig.ITEM_SPACE), PhotoEditAdapter.SPAN_COUNT));
        rvPhoto.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > PickConfig.SCROLL_THRESHOLD) {
                    manager.pauseRequests();
                } else {
                    manager.resumeRequests();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    manager.resumeRequests();
                }
            }
        });

        PickPhotoHelper helper = new PickPhotoHelper(this, new PickPhotoListener() {
            @Override
            public void pickSuccess() {
                GroupImage groupImage = PickPreferences.getInstance(AlbumLocalActivity.this)
                                                       .getListImage();
                imgDatas = generateDatas(groupImage.mGroupMap.get(PickConfig.ALL_PHOTOS));
                if (imgDatas == null) {
                    Log.d("PickPhotoView", "Image is Empty");
                } else {
                    Log.d("All photos size:", String.valueOf(imgDatas.size()));
                }
                if (imgDatas != null && !imgDatas.isEmpty()) {
                    adapter = new PhotoEditAdapter(AlbumLocalActivity.this, imgDatas, PhotoEditAdapter.Type.local);
                    rvPhoto.setAdapter(adapter);
                }
            }
        });
        helper.getImages(false);//false表示不搜索gif图片
    }

    private List<PhotoListBean.ListBean> generateDatas(ArrayList<String> imgPath) {
        for (String path : imgPath) {
            PhotoListBean.ListBean bean = new PhotoListBean.ListBean();
            bean.setPhotoUrl(path);
            imgDatas.add(bean);
        }
        return imgDatas;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_local, menu);
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
            case R.id.action_cancel: {
                adapter.clearSelected();
                break;
            }
        }
        return true;
    }

    private void getAlbumInfo() {
        String username = SharedPreferencesUtils.getHXAcount();
        String robotDeviceId = SharedPreferencesUtils.getRobotIdFromSP();
        AlbumHttp.fetchAlubmList(layoutRoot, 0, Integer.MAX_VALUE, username, robotDeviceId, new AlbumHttp.AlbumHttpCallback<AlbumBean, Throwable>() {
            @Override
            public void onSuccess(AlbumBean albumBean) {
                //默认all相册
                albumId = albumBean.getList().get(0).getPhotoAlbumId();
                tvPath.setText(albumBean.getList().get(0).getPhotoAlbumName());
            }

            @Override
            public void onFail(Throwable throwable) {
                // TODO: 2017/9/30
            }
        });
    }

    @OnClick({R.id.tv_path, R.id.btn_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_path:
                jumpToDir();
                break;
            case R.id.btn_upload:
                jumpToUpload();
                break;
        }
    }


    private void jumpToDir() {
        Intent intent = new Intent(this, AlbumDirActivity.class);
        intent.putExtra(PickConfig.INTENT_SELECT_REMOTE_ALBUM_ID, albumId);
        startActivityForResult(intent, PickConfig.REQUEST_CODE_REMOTE_ALBUM);
    }

    public void jumpToUpload() {
        if (adapter == null) {
            return;
        }
        if (!adapter.getSelectPhotoIds().isEmpty()) {
            Intent intent = new Intent(this, AlbumUploadActivity.class);
//            intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, pickGridAdapter.getSelectPath());
            intent.putStringArrayListExtra(PickConfig.INTENT_IMG_LIST_SELECT, adapter.getSelectPaths());
//            setResult(PickConfig.PICK_PHOTO_DATA, intent);
            intent.putExtra(PickConfig.INTENT_ALBUM_ID, albumId);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PickConfig.RESULT_CODE_REMOTE_ALBUM) {
            String albumName = data.getStringExtra(PickConfig.INTENT_SELECT_REMOTE_ALBUM_NAME);
            long tempAlbumId = data.getLongExtra(PickConfig.INTENT_SELECT_REMOTE_ALBUM_ID, Constants.DEFAULT_SELECTED_ALBUM_ID);
            albumId = tempAlbumId;
            tvPath.setText(albumName);
        }
    }
}
