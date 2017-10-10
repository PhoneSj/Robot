package com.baidufacerecog;


import com.baidufacerecog.Bean.RecogAddUserEntity;
import com.baidufacerecog.Bean.RecogResultEntity;



/**
 * 说明：
 * 创建者： kim
 * 创建日期：2017/7/26:11:23
 */


public interface BaiduFaceInterface {

    public rx.Observable<RecogResultEntity> checkByPatch(final String imagePath);//根据人脸图片路径检测


    //path:图片路径
    public rx.Observable<RecogAddUserEntity> addSet(String group, String path);//添加用户


    public void updateFace(String group,String path, final String pid); //更新用户人脸

    }
