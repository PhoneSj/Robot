package com.zige.robot.http.rxhttp;/*
 *===============================================
 *
 * 文件名:${type_name}
 *
 * 描述: 
 *
 * 作者:
 *
 * 创建日期: ${date} ${time}
 *
 * 修改人:   金征
 *
 * 修改时间:  ${date} ${time} 
 *
 * 修改备注: 
 *
 * 版本:      v1.0 
 *
 *===============================================
 */

import com.zige.robot.App;
import com.zige.robot.BuildConfig;
import com.zige.robot.http.rxhttp.query.QueryCraeateLeaveMsgBean;
import com.zige.robot.http.rxhttp.query.QueryHomeWorkBean;
import com.zige.robot.http.rxhttp.query.QuerySubjectBean;
import com.zige.robot.http.rxhttp.query.QueryUserBean;
import com.zige.robot.http.rxhttp.reponse.BaseCode;
import com.zige.robot.http.rxhttp.reponse.HomeWorkDetails;
import com.zige.robot.http.rxhttp.reponse.HomeWorkListBean;
import com.zige.robot.http.rxhttp.reponse.SubjectIdListBean;
import com.zige.robot.utils.GsonUtils;
import com.zige.robot.utils.SystemUtils;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Datacenter {

    public static Datacenter datacenter;
    private static ApiService apiService;


    private Datacenter() {
    }

    public static synchronized Datacenter get() {
        if (datacenter == null) {
            datacenter = new Datacenter();
        }
        if (apiService == null) {
            apiService = ApiBox.getInstance().createService(ApiService.class, BuildConfig.BASE_URL);
        }
        return datacenter;
    }

    /**
     * 创建和修改科目(app)
     *
     * @param baseSubscriber
     */
    public void changeSubject(BaseSubscriber<BaseCode> baseSubscriber, QuerySubjectBean bean) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), GsonUtils.getObjectToJson(bean));
        Subscription subscribe = apiService.changeSubject(body).
                subscribeOn(Schedulers.io()).
                                                   observeOn(AndroidSchedulers.mainThread()).
                                                   subscribe(baseSubscriber);
    }

    /**
     * 删除科目(app)
     *
     * @param baseSubscriber
     */
    public void deleteSubject(BaseSubscriber<BaseCode> baseSubscriber, long subjectId, QueryUserBean bean) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), GsonUtils.getObjectToJson(bean));
        Subscription subscribe = apiService.deleteSubject(subjectId, body).
                subscribeOn(Schedulers.io()).
                                                   observeOn(AndroidSchedulers.mainThread()).
                                                   subscribe(baseSubscriber);
    }

    /**
     * 获取科目列表
     *
     * @param baseSubscriber
     * @param page
     * @param size
     */
    public void getSubjectList(BaseSubscriber<SubjectIdListBean> baseSubscriber, int page, int size) {

        Subscription subscribe = apiService.subjectList(page, size, App.getInstance().getPhone(),
                SystemUtils.getDeviceKey(), App.getInstance().getUserInfo().getDeviceid()).
                                                   subscribeOn(Schedulers.io()).
                                                   observeOn(AndroidSchedulers.mainThread()).
                                                   subscribe(baseSubscriber);
    }


    /**
     * 创建和修改家庭作业(app)
     *
     * @param baseSubscriber
     */
    public void chageHomeWork(BaseSubscriber<BaseCode> baseSubscriber, QueryHomeWorkBean bean) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), GsonUtils.getObjectToJson(bean));
        Subscription subscribe = apiService.chageHomeWork(body).
                subscribeOn(Schedulers.io()).
                                                   observeOn(AndroidSchedulers.mainThread()).
                                                   subscribe(baseSubscriber);
    }

    /**
     * 家庭作业列表(app)
     *
     * @param baseSubscriber
     */
    public void homeWorkList(BaseSubscriber<HomeWorkListBean> baseSubscriber, int page, int size, String username, String deviceId, String robotDeviceId, Long startTime, Long endTime, Long subjectId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", Integer.toString(page));
        map.put("size", Integer.toString(size));
        map.put("username", username);
        map.put("deviceId", deviceId);
        map.put("robotDeviceId", robotDeviceId);
        if (startTime != null)
            map.put("startTime", Long.toString(startTime));
        if (endTime != null)
            map.put("endTime", Long.toString(endTime));
        if (subjectId != null)
            map.put("subjectId", Long.toString(subjectId));
        apiService.homeWorkList(map).
                subscribeOn(Schedulers.io()).
                          observeOn(AndroidSchedulers.mainThread()).
                          subscribe(baseSubscriber);
    }

    /**
     * 删除作业(app)
     *
     * @param baseSubscriber
     */
    public void deleteHomeWork(BaseSubscriber<BaseCode> baseSubscriber, long homeworkId, QueryUserBean bean) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), GsonUtils.getObjectToJson(bean));
        Subscription subscribe = apiService.deleteHomeWork(homeworkId, body).
                subscribeOn(Schedulers.io()).
                                                   observeOn(AndroidSchedulers.mainThread()).
                                                   subscribe(baseSubscriber);
    }


    /**
     * 标记作业已读(app)
     *
     * @param baseSubscriber
     */
    public void leaveMsgReaded(BaseSubscriber<BaseCode> baseSubscriber, QueryUserBean bean) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), GsonUtils.getObjectToJson(bean));
        Subscription subscribe = apiService.leaveMsgReaded(body).
                subscribeOn(Schedulers.io()).
                                                   observeOn(AndroidSchedulers.mainThread()).
                                                   subscribe(baseSubscriber);
    }

    /**
     * 上传留言(app)
     *
     * @param baseSubscriber
     */
    public void createLeaveMsg(BaseSubscriber<BaseCode> baseSubscriber, QueryCraeateLeaveMsgBean bean) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), GsonUtils.getObjectToJson(bean));
        Subscription subscribe = apiService.createLeaveMsg(body).
                subscribeOn(Schedulers.io()).
                                                   observeOn(AndroidSchedulers.mainThread()).
                                                   subscribe(baseSubscriber);
    }


    /**
     * 获取作业详情
     *
     * @param baseSubscriber
     * @param homeworkId     作业id
     */
    public void homeWorkDetail(BaseSubscriber<HomeWorkDetails> baseSubscriber, long homeworkId) {

        Subscription subscribe = apiService.homeWorkDetail(homeworkId, App.getInstance().getPhone(),
                SystemUtils.getDeviceKey(), App.getInstance().getUserInfo().getDeviceid()).
                                                   subscribeOn(Schedulers.io()).
                                                   observeOn(AndroidSchedulers.mainThread()).
                                                   subscribe(baseSubscriber);
    }


}
