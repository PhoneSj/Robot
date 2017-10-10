package com.baidufacerecog.Bean;

import java.util.ArrayList;

/**
 * 说明：识别结果
 * 创建者： kim
 * 创建日期：2017/7/24:19:02
 */


public class RecogResultEntity {
    public int result_num;//人脸个数
    public String error_code;
    public ArrayList<Data> result;


    public static class Data {

        public double face_probability;//可信度


    }
}
