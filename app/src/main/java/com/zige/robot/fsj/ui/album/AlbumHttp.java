package com.zige.robot.fsj.ui.album;

import android.view.View;

import com.zige.robot.fsj.model.bean.AlbumBean;
import com.zige.robot.fsj.model.bean.PhotoListBean;
import com.zige.robot.fsj.model.http.response.AlbumHttpResponse;
import com.zige.robot.fsj.model.param.AlbumDeleteParams;
import com.zige.robot.fsj.model.param.PhotoMoveParams;
import com.zige.robot.fsj.model.param.AlbumNewParams;
import com.zige.robot.fsj.model.param.AlbumUpdateParams;
import com.zige.robot.fsj.model.param.PhotoDeleteBatchParams;
import com.zige.robot.fsj.model.param.PhotoDeleteParams;
import com.zige.robot.fsj.model.param.PhotoParams;
import com.zige.robot.fsj.ui.album.util.HttpConfig;
import com.zige.robot.fsj.util.MyNetworkUtil;
import com.zige.robot.fsj.util.RxUtil;

import io.reactivex.functions.Consumer;

/**
 * Created by PhoneSj on 2017/9/30.
 */

public class AlbumHttp {

    public interface AlbumHttpCallback<T, U> {

        void onSuccess(T t);

        void onFail(U u);
    }

    /**
     * 获取云相册列表
     *
     * @param page
     * @param size
     * @param username
     * @param robotDeviceId
     * @param callback
     */
    public static void fetchAlubmList(View layoutRoot, int page, int size, String username, String robotDeviceId, final AlbumHttpCallback<AlbumBean, Throwable> callback) {
        if (!MyNetworkUtil.checkNetwork(layoutRoot)) {
            //当前网络不可用
            return;
        }
        HttpConfig.getInstance()
                  .getApi()
                  .getAlbumInfo(page, size, username, robotDeviceId)
                  .compose(RxUtil.<AlbumHttpResponse<AlbumBean>>rxSchedulerHelper())
                  .compose(RxUtil.<AlbumBean>handleAlbumResult())
                  .subscribe(new Consumer<AlbumBean>() {
                      @Override
                      public void accept(final AlbumBean albumBean) throws Exception {
                          callback.onSuccess(albumBean);
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          callback.onFail(throwable);
                      }
                  });
    }

    /**
     * 创建云相册
     *
     * @param params
     * @param callback
     */
    public static void createAlbum(View layoutRoot, AlbumNewParams params, final AlbumHttpCallback<AlbumHttpResponse, Throwable> callback) {
        if (!MyNetworkUtil.checkNetwork(layoutRoot)) {
            //当前网络不可用
            return;
        }
        HttpConfig.getInstance()
                  .getApi()
                  .newAlbum(params)
                  .compose(RxUtil.<AlbumHttpResponse>rxSchedulerHelper())
                  .subscribe(new Consumer<AlbumHttpResponse>() {
                      @Override
                      public void accept(AlbumHttpResponse albumHttpResponse) throws Exception {
                          callback.onSuccess(albumHttpResponse);
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          callback.onFail(throwable);
                      }
                  });
    }

    /**
     * 删除云相册
     *
     * @param layoutRoot
     * @param params
     * @param albumId
     * @param callback
     */
    public static void deleteAlbum(View layoutRoot, AlbumDeleteParams params, long albumId, final AlbumHttpCallback<AlbumHttpResponse, Throwable> callback) {
        if (!MyNetworkUtil.checkNetwork(layoutRoot)) {
            //当前网络不可用
            return;
        }
        HttpConfig.getInstance()
                  .getApi()
                  .deleteAlbum(albumId, params)
                  .compose(RxUtil.<AlbumHttpResponse>rxSchedulerHelper())
                  .subscribe(new Consumer<AlbumHttpResponse>() {
                      @Override
                      public void accept(AlbumHttpResponse albumHttpResponse) throws Exception {
                          callback.onSuccess(albumHttpResponse);
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          callback.onFail(throwable);
                      }
                  });
    }

    /**
     * 重命名云相册
     *
     * @param params
     * @param newName  新名称
     * @param callback
     */
    public static void renameAlbum(View layoutRoot, AlbumUpdateParams params, String newName, final AlbumHttpCallback<AlbumHttpResponse, Throwable> callback) {
        params.setPhotoAlbumName(newName);
        if (!MyNetworkUtil.checkNetwork(layoutRoot)) {
            //当前网络不可用
            return;
        }
        HttpConfig.getInstance()
                  .getApi()
                  .updateAlbum(params)
                  .compose(RxUtil.<AlbumHttpResponse>rxSchedulerHelper())
                  .subscribe(new Consumer<AlbumHttpResponse>() {
                      @Override
                      public void accept(AlbumHttpResponse albumHttpResponse) throws Exception {
                          callback.onSuccess(albumHttpResponse);
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          callback.onFail(throwable);
                      }
                  });
    }

    /**
     * 获取指定云相册中的云相片列表
     *
     * @param page
     * @param size
     * @param username
     * @param robotDeviceId
     * @param albumId
     * @param callback
     */
    public static void fetchPhotoList(View layoutRoot, int page, int size, String username, String robotDeviceId, long albumId, final AlbumHttpCallback<PhotoListBean, Throwable> callback) {
        if (!MyNetworkUtil.checkNetwork(layoutRoot)) {
            //当前网络不可用
            return;
        }
        HttpConfig.getInstance()
                  .getApi()
                  .getPhotoList(page, size, username, robotDeviceId, albumId)
                  .compose(RxUtil.<AlbumHttpResponse<PhotoListBean>>rxSchedulerHelper())
                  .compose(RxUtil.<PhotoListBean>handleAlbumResult())
                  .subscribe(new Consumer<PhotoListBean>() {
                      @Override
                      public void accept(PhotoListBean photoListBean) throws Exception {
                          callback.onSuccess(photoListBean);
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          callback.onFail(throwable);
                      }
                  });
    }

    /**
     * 批量删除云相片
     *
     * @param params
     * @param callback
     */
    public static void deleteBatchPhoto(View layoutRoot, PhotoDeleteBatchParams params, final AlbumHttpCallback<AlbumHttpResponse, Throwable> callback) {
        if (!MyNetworkUtil.checkNetwork(layoutRoot)) {
            //当前网络不可用
            return;
        }
        HttpConfig.getInstance()
                  .getApi()
                  .deletePhotos(params)
                  .compose(RxUtil.<AlbumHttpResponse>rxSchedulerHelper())
                  .subscribe(new Consumer<AlbumHttpResponse>() {
                      @Override
                      public void accept(AlbumHttpResponse albumHttpResponse) throws Exception {
                          callback.onSuccess(albumHttpResponse);
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          callback.onFail(throwable);
                      }
                  });
    }

    /**
     * 删除单张云相片
     *
     * @param params
     * @param photoId
     * @param callback
     */
    public static void deleteSinglePhoto(View layoutRoot, PhotoDeleteParams params, long photoId, final AlbumHttpCallback<AlbumHttpResponse, Throwable> callback) {
        if (!MyNetworkUtil.checkNetwork(layoutRoot)) {
            //当前网络不可用
            return;
        }
        HttpConfig.getInstance()
                  .getApi()
                  .deletePhoto(photoId, params)
                  .compose(RxUtil.<AlbumHttpResponse>rxSchedulerHelper())
                  .subscribe(new Consumer<AlbumHttpResponse>() {
                      @Override
                      public void accept(AlbumHttpResponse albumHttpResponse) throws Exception {
                          callback.onSuccess(albumHttpResponse);
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          callback.onFail(throwable);
                      }
                  });
    }

    /**
     * 上传云相片的位置
     *
     * @param params
     * @param callback
     */
    public static void uploadPhoto(View layoutRoot, PhotoParams params, final AlbumHttpCallback<AlbumHttpResponse, Throwable> callback) {
        if (!MyNetworkUtil.checkNetwork(layoutRoot)) {
            //当前网络不可用
            return;
        }
        HttpConfig.getInstance()
                  .getApi()
                  .uploadPhoto(params)
                  .compose(RxUtil.<AlbumHttpResponse>rxSchedulerHelper())
                  .subscribe(new Consumer<AlbumHttpResponse>() {
                      @Override
                      public void accept(AlbumHttpResponse albumHttpResponse) throws Exception {
                          callback.onSuccess(albumHttpResponse);
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          callback.onFail(throwable);
                      }
                  });
    }

    public static void movePhoto(View layoutRoot, PhotoMoveParams params, final AlbumHttpCallback<AlbumHttpResponse, Throwable> callback) {
        if (!MyNetworkUtil.checkNetwork(layoutRoot)) {
            //当前网络不可用
            return;
        }
        HttpConfig.getInstance()
                  .getApi()
                  .movePhotos(params)
                  .compose(RxUtil.<AlbumHttpResponse>rxSchedulerHelper())
                  .subscribe(new Consumer<AlbumHttpResponse>() {
                      @Override
                      public void accept(AlbumHttpResponse albumHttpResponse) throws Exception {
                          callback.onSuccess(albumHttpResponse);
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          callback.onFail(throwable);
                      }
                  });
    }
}
