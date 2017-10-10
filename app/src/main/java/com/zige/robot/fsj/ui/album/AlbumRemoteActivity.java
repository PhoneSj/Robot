package com.zige.robot.fsj.ui.album;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zige.robot.R;
import com.zige.robot.fsj.base.StateActivity;
import com.zige.robot.fsj.model.bean.AlbumBean;
import com.zige.robot.fsj.model.http.response.AlbumHttpResponse;
import com.zige.robot.fsj.model.param.AlbumDeleteParams;
import com.zige.robot.fsj.model.param.AlbumUpdateParams;
import com.zige.robot.fsj.ui.album.adapter.RemoteAdapter;
import com.zige.robot.fsj.ui.album.util.PickConfig;
import com.zige.robot.fsj.ui.album.widget.SwipeListView;
import com.zige.robot.fsj.util.LogUtil;
import com.zige.robot.fsj.util.SnackbarUtil;
import com.zige.robot.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by PhoneSj on 2017/9/23.
 */

public class AlbumRemoteActivity extends StateActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rv_album)
    SwipeListView rvAlbum;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;

    private List<AlbumBean.ListBean> albumList;
    private RemoteAdapter adaper;

    @Override
    protected int getLayout() {
        return R.layout.activity_album_remote;
    }

    @Override
    protected void initEventAndData() {
        setToolbar(toolBar, "机器人共享相册");
        albumList = new ArrayList<>();
        adaper = new RemoteAdapter(AlbumRemoteActivity.this, albumList);
        rvAlbum.setAdapter(adaper);
        //点击
        rvAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(AlbumRemoteActivity.this, AlbumFileActivity.class);
                intent.putExtra(PickConfig.INTENT_ALBUM_ID, albumList.get(position)
                                                                     .getPhotoAlbumId());
                intent.putExtra(PickConfig.INTENT_ALBUM_NAME, albumList.get(position)
                                                                       .getPhotoAlbumName());
                startActivity(intent);
            }
        });
        //删除
        adaper.setOnItemDeleteListener(new RemoteAdapter.OnItemDeleteListener() {
            @Override
            public void onItemDelete(int position) {
                showDeleteDialog(albumList.get(position));
            }
        });
        //重命名
        adaper.setOnItemRenameListener(new RemoteAdapter.OnItemRenameListener() {
            @Override
            public void onItemRename(int position) {
                showRenameDialog(albumList.get(position));
            }
        });
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAlbum();
            }
        });
        getAlbum();
    }

    /**
     * 获取网络相册信息
     */
    private void getAlbum() {
        stateLoading("正在加载...");
        String username = SharedPreferencesUtils.getHXAcount();
        String robotDeviceId = SharedPreferencesUtils.getRobotIdFromSP();
        AlbumHttp.fetchAlubmList(layoutRoot, 0, Integer.MAX_VALUE, username, robotDeviceId, new AlbumHttp.AlbumHttpCallback<AlbumBean, Throwable>() {
            @Override
            public void onSuccess(AlbumBean albumBean) {
                stateContent();
                if (albumBean != null) {
                    albumList.clear();
                    albumList.addAll(albumBean.getList());
                    adaper.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                stateError("获取相册信息失败");
                LogUtil.showI(AlbumRemoteActivity.this.getClass()
                                                      .getSimpleName() + "获取相册信息失败");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album, menu);
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
            case R.id.menu_add: {
                Intent intent = new Intent(this, AlbumLocalActivity.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }

    private void showDeleteDialog(AlbumBean.ListBean bean) {
        final long albumId = bean.getPhotoAlbumId();
        int count = bean.getCount();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否删除")
               .setMessage("将会同时删除里面的" + count + "张照片")
               .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                   }
               }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                deleteAlbem(albumId);
            }
        }).create().show();
    }

    /**
     * 删除相册
     */
    private void deleteAlbem(long albumId) {
        AlbumDeleteParams params = new AlbumDeleteParams();
        params.setUsername(SharedPreferencesUtils.getHXAcount());
        params.setRobotDeviceId(SharedPreferencesUtils.getRobotIdFromSP());
        AlbumHttp.deleteAlbum(layoutRoot, params, albumId, new AlbumHttp.AlbumHttpCallback<AlbumHttpResponse, Throwable>() {
            @Override
            public void onSuccess(AlbumHttpResponse albumHttpResponse) {
                if (albumHttpResponse.getCode().equalsIgnoreCase("200") ||
                        albumHttpResponse.getMessage()
                                         .equalsIgnoreCase(AlbumHttpResponse.RESULT_SUCCESS)) {
                    LogUtil.showI("AlbumRemoteActivity删除相册文件夹成功");
                    stateError("删除成功");
                    //刷新
                    getAlbum();
                } else {
                    LogUtil.showI("AlbumRemoteActivity删除相册文件夹失败");
                    stateError("删除失败");
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                LogUtil.showI("AlbumRemoteActivity删除相册文件夹失败");
                stateError("删除失败");
            }
        });
    }

    private void showRenameDialog(final AlbumBean.ListBean bean) {
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑相册名称")
               .setView(view).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText etDialogValue = (EditText) view.findViewById(R.id.et_dialog_value);
                String newName = etDialogValue.getText().toString().trim();
                if (!TextUtils.isEmpty(newName)) {
                    dialogInterface.dismiss();
                    renameAlubm(bean, newName);
                } else {
                    showErrorMsg("相册名不能为空");
                }
            }
        }).create().show();
    }

    /**
     * 修改相册文件夹名称
     *
     * @param bean
     */
    private void renameAlubm(AlbumBean.ListBean bean, String newName) {
        String username = SharedPreferencesUtils.getHXAcount();
        String robotDeviceId = SharedPreferencesUtils.getRobotIdFromSP();
        long albumId = bean.getPhotoAlbumId();
        AlbumUpdateParams params = new AlbumUpdateParams();
        params.setUsername(username);
        params.setRobotDeviceId(robotDeviceId);
        params.setPhotoAlbumId(albumId);
        AlbumHttp.renameAlbum(layoutRoot, params, newName, new AlbumHttp.AlbumHttpCallback<AlbumHttpResponse, Throwable>() {
            @Override
            public void onSuccess(AlbumHttpResponse albumHttpResponse) {
                if (albumHttpResponse.getCode()
                                     .equalsIgnoreCase("200") || albumHttpResponse.getMessage()
                                                                                  .equalsIgnoreCase(AlbumHttpResponse.RESULT_SUCCESS)) {
                    LogUtil.showI("AlbumRemoteActivity修改相册文件夹成功");
                    stateContent("修改成功");
                    //刷新
                    getAlbum();
                } else {
                    LogUtil.showI("AlbumRemoteActivity修改相册文件夹失败");
                    stateError("修改失败");
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                LogUtil.showI("AlbumRemoteActivity修改相册文件夹失败");
                stateError("修改失败");
            }
        });
    }


    @Override
    protected void showContentMsg(String msg) {
        super.showContentMsg(msg);
        srlRefresh.setRefreshing(false);
    }

    @Override
    protected void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        srlRefresh.setRefreshing(false);
        if (!TextUtils.isEmpty(msg)) {
            SnackbarUtil.showShort(layoutRoot, msg);
        }
    }
}
