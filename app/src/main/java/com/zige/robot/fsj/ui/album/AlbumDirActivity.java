package com.zige.robot.fsj.ui.album;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zige.robot.R;
import com.zige.robot.fsj.Constants;
import com.zige.robot.fsj.base.SimpleActivity;
import com.zige.robot.fsj.model.bean.AlbumBean;
import com.zige.robot.fsj.model.http.response.AlbumHttpResponse;
import com.zige.robot.fsj.model.param.AlbumNewParams;
import com.zige.robot.fsj.ui.album.adapter.DirAdapter;
import com.zige.robot.fsj.ui.album.util.PickConfig;
import com.zige.robot.fsj.util.LogUtil;
import com.zige.robot.fsj.util.ToastUtil;
import com.zige.robot.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by PhoneSj on 2017/9/25.
 */

public class AlbumDirActivity extends SimpleActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rv_dir)
    RecyclerView rvDir;
    @BindView(R.id.btn_new)
    Button btnNew;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;

    private DirAdapter adapter;
    private List<AlbumBean.ListBean> datas;

    private long selectedAlubmId;

    @Override
    protected int getLayout() {
        return R.layout.activity_album_dir;
    }

    @Override
    protected void initEventAndData() {
        selectedAlubmId = getIntent().getLongExtra(PickConfig.INTENT_SELECT_REMOTE_ALBUM_ID, Constants.DEFAULT_SELECTED_ALBUM_ID);
        setToolbar(toolBar, "选择相册");
        //初始化RecyclerView
        datas = new ArrayList<>();
        adapter = new DirAdapter(AlbumDirActivity.this, datas, 0);
        rvDir.setLayoutManager(new LinearLayoutManager(AlbumDirActivity.this));
        rvDir.addItemDecoration(new DividerItemDecoration(AlbumDirActivity.this, DividerItemDecoration.VERTICAL));
        rvDir.setAdapter(adapter);
        getAlbumInfo();
    }

    @OnClick({R.id.btn_new, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_new:
                showNewAlbumDialog();
                break;
            case R.id.btn_ok:
                confirmSelectAlbum();
                break;
        }
    }

    /**
     * 获取相册列表
     */
    private void getAlbumInfo() {
        String username = SharedPreferencesUtils.getHXAcount();
        String robotDeviceId = SharedPreferencesUtils.getRobotIdFromSP();
        AlbumHttp.fetchAlubmList(layoutRoot, 0, Integer.MAX_VALUE, username, robotDeviceId, new AlbumHttp.AlbumHttpCallback<AlbumBean, Throwable>() {
            @Override
            public void onSuccess(AlbumBean albumBean) {
                LogUtil.showI("AlbumDirActivity获取相册列表成功");
                ToastUtil.shortShow("AlbumDirActivity获取相册列表成功");
                datas.clear();
                datas.addAll(albumBean.getList());
                adapter.setSelectedAlbumId(selectedAlubmId);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Throwable throwable) {
                LogUtil.showI("AlbumDirActivity获取相册列表成失败");
                ToastUtil.shortShow("AlbumDirActivity获取相册列表失败");
            }
        });
    }

    private void confirmSelectAlbum() {
        int current = adapter.getCurrent();
        Intent intent = new Intent(this, AlbumUploadActivity.class);
        intent.putExtra(PickConfig.INTENT_SELECT_REMOTE_ALBUM_NAME, datas.get(current)
                                                                         .getPhotoAlbumName());
        intent.putExtra(PickConfig.INTENT_SELECT_REMOTE_ALBUM_ID, datas.get(current)
                                                                       .getPhotoAlbumId());
        LogUtil.showE(datas.get(current).getPhotoAlbumId() + "");
        setResult(PickConfig.RESULT_CODE_REMOTE_ALBUM, intent);
        finish();
    }

    private void showNewAlbumDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(lp);
        builder.setTitle("新建文件夹")
               .setView(editText)
               .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                   }
               })
               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       String filename = editText.getText().toString().trim();
                       if (!TextUtils.isDigitsOnly(filename)) {
                           newAlbum(filename);
                       }
                   }
               })
               .create()
               .show();

    }

    /**
     * 创建相册文件夹
     *
     * @param filename
     */

    private void newAlbum(String filename) {
        AlbumNewParams params = new AlbumNewParams();
        params.setUsername(SharedPreferencesUtils.getHXAcount());
        params.setRobotDeviceId(SharedPreferencesUtils.getRobotIdFromSP());
        params.setPhotoAlbumName(filename);
        AlbumHttp.createAlbum(layoutRoot, params, new AlbumHttp.AlbumHttpCallback<AlbumHttpResponse, Throwable>() {
            @Override
            public void onSuccess(AlbumHttpResponse albumHttpResponse) {
                if (albumHttpResponse.isSuccess()) {
                    LogUtil.showI("AlbumDirActivity创建相册文件夹成功");
                    ToastUtil.shortShow("AlbumDirActivity创建相册文件夹成功");
                    //重新获取相册列表，刷新ui
                    getAlbumInfo();
                } else {
                    LogUtil.showI("AlbumDirActivity创建相册文件夹失败");
                    ToastUtil.shortShow("AlbumDirActivity创建相册文件夹失败");
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                LogUtil.showI("AlbumDirActivity创建相册文件夹失败");
                ToastUtil.shortShow("AlbumDirActivity创建相册文件夹失败");
            }
        });
    }

}
