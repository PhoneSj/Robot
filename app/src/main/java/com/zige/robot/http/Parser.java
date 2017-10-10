package com.zige.robot.http;

import android.util.Log;

import com.alibaba.fastjson.TypeReference;
import com.zige.robot.bean.AnswerCode;
import com.zige.robot.bean.CBaseCode;
import com.zige.robot.bean.LoginCode;
import com.zige.robot.utils.ApplicationUtil;


/**
 * 功能：数据解析
 * <p>
 * Created by user on 2016/4/26.
 */
public class Parser {
    private static final String ParserError = "数据解析出错";




    public static AnswerCode getAnswer(String json) {
        AnswerCode result = new AnswerCode();

        try {
            result = ApplicationUtil.fromJson(json, new TypeReference<AnswerCode>(){}.getType());
        } catch (Exception e) {
            Log.i("ParkNewsEntry", ParserError);
        }
        return result;
    }

    public static CBaseCode getBaseCode(String json) {
        CBaseCode result = new CBaseCode();

        try {
            result = ApplicationUtil.fromJson(json, new TypeReference<CBaseCode>(){}.getType());
        } catch (Exception e) {
            Log.i("ParkNewsEntry", ParserError);
        }
        return result;
    }

    public static LoginCode getLoginCode(String json) {
        LoginCode result = new LoginCode();

        try {
            result = ApplicationUtil.fromJson(json, new TypeReference<LoginCode>(){}.getType());
        } catch (Exception e) {
            Log.i("ParkNewsEntry", ParserError);
        }
        return result;
    }

}
