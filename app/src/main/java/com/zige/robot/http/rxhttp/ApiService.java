package com.zige.robot.http.rxhttp;

import com.zige.robot.http.rxhttp.reponse.HomeWorkDetails;
import com.zige.robot.http.rxhttp.reponse.HomeWorkListBean;
import com.zige.robot.http.rxhttp.reponse.SubjectIdListBean;
import com.zige.robot.http.rxhttp.reponse.BaseCode;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;


/**
 * Created by Administrator on 2017/8/1.
 */

public interface ApiService {


    //创建和修改科目(app)
    @POST("/homework/mobile/api/subject")
    Observable<BaseCode> changeSubject(@Body RequestBody body);

    //科目列表(app)　
    @GET("/homework/mobile/api/subject/list")
    Observable<SubjectIdListBean> subjectList(@Query("page") int page, @Query("size") int size, @Query("username")
             String username, @Query("deviceId") String deviceId, @Query("robotDeviceId") String robotDeviceId);

    //删除科目(app)
    @POST("/homework/mobile/api/subject/{subjectId}/delete")
    Observable<BaseCode> deleteSubject(@Path("subjectId") long subjectId , @Body RequestBody body);

    //创建和修改家庭作业(app)
    @POST("/homework/mobile/api/homework")
    Observable<BaseCode> chageHomeWork(@Body RequestBody body);


    //家庭作业列表list(app)
    @GET("/homework/mobile/api/homework/list")
    Observable<HomeWorkListBean> homeWorkList(@QueryMap Map<String, String> map);


    //获取家庭作业detail(app)
    @GET("/homework/mobile/api/homework/{homeworkId}")
    Observable<HomeWorkDetails> homeWorkDetail(@Path("homeworkId") long homeworkId, @Query("username") String username, @Query("deviceId") String deviceId,
                                               @Query("robotDeviceId") String robotDeviceId);
    //删除家庭作业(app)
    @POST("/homework/mobile/api/homework/{homeworkId}/delete")
    Observable<BaseCode> deleteHomeWork(@Path("homeworkId") long homeworkId , @Body RequestBody body);

    //留言标记已读(app)
    @POST("/homework/mobile/api/leavemessage/update")
    Observable<BaseCode> leaveMsgReaded( @Body RequestBody body);

   //上传留言(app)
    @POST("/homework/mobile/api/leavemessage")
    Observable<BaseCode> createLeaveMsg( @Body RequestBody body);


}
