package com.zige.robot.fsj.model.http.api;

import com.zige.robot.BuildConfig;
import com.zige.robot.fsj.model.bean.CallComsumeRecordBean;
import com.zige.robot.fsj.model.bean.CallInfoBean;
import com.zige.robot.fsj.model.bean.CallRemainTimeBean;
import com.zige.robot.fsj.model.bean.PayRecordBean;
import com.zige.robot.fsj.model.bean.PayResultBean;
import com.zige.robot.fsj.model.bean.UserBean;
import com.zige.robot.fsj.model.http.response.CallHttpResponse;
import com.zige.robot.fsj.model.http.response.ZigeHttpResponse;
import com.zige.robot.fsj.model.param.AlbumNewParams;
import com.zige.robot.fsj.model.param.AlbumUpdateParams;
import com.zige.robot.fsj.model.param.AlbumDeleteParams;
import com.zige.robot.fsj.model.param.LoginParams;
import com.zige.robot.fsj.model.param.PhotoDeleteBatchParams;
import com.zige.robot.fsj.model.param.PhotoDeleteParams;
import com.zige.robot.fsj.model.param.PhotoMoveParams;
import com.zige.robot.fsj.model.param.PhotoParams;
import com.zige.robot.fsj.model.bean.AlbumBean;
import com.zige.robot.fsj.model.bean.PhotoListBean;
import com.zige.robot.fsj.model.bean.QiniuToken;
import com.zige.robot.fsj.model.http.response.AlbumHttpResponse;
import com.zige.robot.fsj.model.param.RegisterParams;
import com.zige.robot.fsj.model.param.ResetPwdParams;
import com.zige.robot.fsj.model.param.SmsCodeParams;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by PhoneSj on 2017/9/26.
 */

public interface ApiService {

    String HOST = BuildConfig.BASE_URL;
    String APK_DOWNLOAD_URL = BuildConfig.APK_DOWNLOAD_URL;//apk下载地址

    /***=====================================登录api===========================================*/

    @Headers("Content-Type: application/json")
    @POST("app/robot/login")
    Flowable<ZigeHttpResponse<UserBean>> login(@Body LoginParams params);

    @Headers("Content-Type: application/json")
    @POST("app/robot/register")
    Flowable<ZigeHttpResponse<UserBean>> register(@Body RegisterParams params);

    @Headers("Content-Type: application/json")
    @POST("app/robot/smsGenerate")
    Flowable<ZigeHttpResponse> requestSendSmsCode(@Body SmsCodeParams params);


    @Headers("Content-Type: application/json")
    @POST("app/robot/pwdReset")
    Flowable<ZigeHttpResponse> resetPassword(@Body ResetPwdParams params);


    /**===================================相册api==========================================**/

    /**
     * 获取云相册列表
     *
     * @param page
     * @param size
     * @param username
     * @param robotDeviceId
     * @return
     */
    @GET("api/cloud/photo/album/list")
    Flowable<AlbumHttpResponse<AlbumBean>> getAlbumInfo(@Query("page") int page, @Query("size") int size, @Query("username") String username, @Query("robotDeviceId") String robotDeviceId);

    /**
     * 修改云相册
     *
     * @param params
     * @return
     */
    @Headers("Content-Type: application/json")
    @POST("api/cloud/photo/album")
    Flowable<AlbumHttpResponse> updateAlbum(@Body AlbumUpdateParams params);

    @Headers("Content-Type: application/json")
    @POST("api/cloud/photo/album")
    Flowable<AlbumHttpResponse> newAlbum(@Body AlbumNewParams params);

    @Headers("Content-Type: application/json")
    @POST("api/cloud/photo/album/{photoAlbumId}/delete")
    Flowable<AlbumHttpResponse> deleteAlbum(@Path("photoAlbumId") long photoAlbumId, @Body AlbumDeleteParams params);

    @Headers("Content-Type: application/json")
    @GET("api/cloud/photo/list")
    Flowable<AlbumHttpResponse<PhotoListBean>> getPhotoList(@Query("page") int page, @Query("size") int size, @Query("username") String username, @Query("robotDeviceId") String robotDeviceId, @Query("photoAlbumId") long photoAlbumId);

    @Headers("Content-Type: application/json")
    @POST("api/cloud/photo")
    Flowable<AlbumHttpResponse> uploadPhoto(@Body PhotoParams params);

    @Headers("Content-Type: application/json")
    @POST("app/robot/getQiniuToken")
    Flowable<QiniuToken> getQiniuToken(@Query("fileKey") String fileKey);

    @Headers("Content-Type: application/json")
    @POST("api/cloud/photo/batch/delete")
    Flowable<AlbumHttpResponse> deletePhotos(@Body PhotoDeleteBatchParams params);

    @Headers("Content-Type: application/json")
    @POST("api/cloud/photo/{photoId}/delete")
    Flowable<AlbumHttpResponse> deletePhoto(@Path("photoId") long photoId, @Body PhotoDeleteParams params);

    @Headers("Content-Type: application/json")
    @POST("api/cloud/photo/batch/move")
    Flowable<AlbumHttpResponse> movePhotos(@Body PhotoMoveParams params);

    /**=======================================视频通话api======================================**/

    /**
     * 可用时长查询
     *
     * @param robotId 机器人id
     * @return
     */
    @GET("api/807/videoCall/charge/remain/{robotId}")
    Flowable<CallHttpResponse<CallRemainTimeBean>> getCallRemainTime(@Path("robotId") String robotId);

    /**
     * 消费时长提交
     *
     * @return
     */
    @Headers("Content-Type: application/json")
    @POST("api/807/videoCall/charge/record")
    Flowable<CallHttpResponse<CallRemainTimeBean>> commitCallComsumeTime(@Body CallInfoBean bean);

    /**
     * 通话消费记录查询
     *
     * @param robotId 机器人id
     * @param page    查询的页码
     * @param size    每页的数目
     * @return
     */
    @GET("videoCall/comsume")
    Flowable<CallHttpResponse<CallComsumeRecordBean>> getComsumeRecordList(@Query("robotId") String robotId, @Query("page") int page, @Query("size") int size);

    /**
     * 通话充值记录查询
     *
     * @param robotId 机器人id
     * @param page    查询的页码
     * @param size    每页的数目
     * @return
     */
    @GET("api/807/videoCall/charge/records/{robotId}")
    Flowable<CallHttpResponse<PayRecordBean>> getPayRecordList(@Path("robotId") String robotId, @Query("page") int page, @Query("size") int size);

    /**
     * 通话充值结果查询
     *
     * @param orderNo
     * @return
     */
    @GET("api/807/videoCall/order/{orderNo}")
    Flowable<CallHttpResponse<PayResultBean>> getPayResult(@Path("orderNo") String orderNo);
}
