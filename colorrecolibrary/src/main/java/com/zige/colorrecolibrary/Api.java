package com.zige.colorrecolibrary;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zige on 2017/8/7.
 */

public interface Api {

    public static final String HOST = "http://115.159.46.147:61/app/";

//    @POST("robot/getQiniuToken")
//    Observable<QiNiuToken> getQiNiuToken(@Query("fileKey") String fileKey, @Query("sign") String sign);
//
//
    @FormUrlEncoded
    @POST("zigerobot/updateUserInfo")
    Observable<DubeAddOrUpdatePersonResult> addUser(@Field("robotDeviceId") String robotDeviceId, @Field("username") String username,
                                                    @Field("age") String age, @Field("title") String title, @Field("sex") String sex,
                                                    @Field("personId") String personId, @Field("faceUrl") String faceUrl, @Field("faceInfo") String faceInfo);
//
//    @POST("zigerobot/getUserInfo")
//    Observable<DubePersonFromService> checkUser(@Query("robotDeviceId") String robotDeviceId, @Query("personId") String personId, @Query("sign") String sign);

    @POST("zigerobot/getUserInfo")
    Observable<DubePersonList> getAllUser(@Query("robotDeviceId") String robotDeviceId, @Query("sign") String sign);

    @POST("robot/getFaceInfoList")
    Observable<DubePersonList> getAllUserByUserId(@Query("robotDeviceId") String robotDeviceId, @Query("userId") String userId, @Query("phoneDeviceId") String phoneDeviceId);


    @FormUrlEncoded
    @POST("robot/delFaceInfo")
    Observable<DefResponse> delFace(@Field("robotDeviceId") String robotDeviceId, @Field("userId") String userId, @Field("personId") String personId, @Field("faceId") String faceId, @Field("phoneDeviceId") String phoneDeviceId);


//                            map.put("username", userInfo.getUsername());
//                            map.put("age", userInfo.getAge()+"");
//                            map.put("title", userInfo.getTitle());
//                            map.put("sex", userInfo.getSex()+"");
//                            map.put("personId", personId);
//                            map.put("faceUrl", path);
//                            map.put("faceInfo", FaceColorRec.getServiceKey(token));
//
//                            map.put("userId", AppContext.getInstance().getUserInfo().getUserId() + "");
//                            map.put("phoneDeviceId", SystemUtils.getDeviceKey());
//                            map.put("robotDeviceId", AppContext.getInstance().getUserInfo().getDeviceid());

    @FormUrlEncoded
    @POST("robot/updateUserFaceInfo")
    Observable<DefResponse> addFace(@Field("username") String username, @Field("age") String age, @Field("title") String title,
                                    @Field("sex") String sex, @Field("personId") String personId, @Field("faceUrl") String faceUrl,
                                    @Field("faceInfo") String faceInfo, @Field("userId") String userId, @Field("phoneDeviceId")String phoneDeviceId,
                                    @Field("robotDeviceId") String robotDeviceId);
   }
