package com.zige.robot.fsj.model.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.hyphenate.chat.EMMessage;
import com.zige.robot.App;
import com.zige.robot.fsj.Constants;
import com.zige.robot.fsj.model.bean.CallInfoBean;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/9/9.
 */

public class MySPHelper implements SPHelper {


    private final SharedPreferences mSP;

    @Inject
    public MySPHelper() {
        mSP = App.getInstance().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public int fetchUsername() {
        return mSP.getInt(Constants.SP_USERNAME, 0);
    }

    @Override
    public String fetchConnectedRobot() {
        return mSP.getString(Constants.SP_ROBOT_ID, null);
    }

    @Override
    public void saveConsumeTime(int minute) {
        mSP.edit().putInt(Constants.SP_CONSUME_TIME, minute).apply();
    }

    @Override
    public int fetchConsumeTime() {
        return mSP.getInt(Constants.SP_CONSUME_TIME, 0);
    }

    @Override
    public void saveCallMessage(CallInfoBean bean) {
        mSP.edit().putString(Constants.SP_CALL_FROM, bean.getCallerId()).apply();
        mSP.edit().putString(Constants.SP_CALL_TO, bean.getCalledId()).apply();
        mSP.edit().putInt(Constants.SP_CONSUME_TIME, bean.getConsumeTime()).apply();
        mSP.edit().putString(Constants.SP_CALL_ID, bean.getId()).apply();
    }

    @Override
    public CallInfoBean getCallMessage() {
        CallInfoBean bean = new CallInfoBean();
        bean.setCallerId(mSP.getString(Constants.SP_CALL_FROM, null));
        bean.setCalledId(mSP.getString(Constants.SP_CALL_TO, null));
        bean.setConsumeTime(mSP.getInt(Constants.SP_CONSUME_TIME, 0));
        bean.setId(mSP.getString(Constants.IT_CALL_ID, null));
        return bean;
    }

    @Override
    public String fetchNickname() {
        return mSP.getString(Constants.SP_NICKNAME, null);
    }

    @Override
    public String fetchPhone() {
        return mSP.getString(Constants.SP_PHONE, null);
    }

    @Override
    public void saveHxAcount(String hxAcount) {
        mSP.edit().putString(Constants.SP_HX_ACOUNT, hxAcount).apply();
    }

    @Override
    public String fetchHXAcount() {
        return mSP.getString(Constants.SP_HX_ACOUNT, null);
    }

    @Override
    public void saveHxPassword(String hxPwd) {
        mSP.edit().putString(Constants.SP_HX_PASSWORD, hxPwd).apply();
    }

    @Override
    public String fetchHxPassword() {
        return mSP.getString(Constants.SP_HX_PASSWORD, null);
    }

    @Override
    public void saveHxRemoteAcount(String romoteAcount) {
        mSP.edit().putString(Constants.SP_REMOTE_HX_CONTACT, romoteAcount).apply();
    }

    @Override
    public String fetchHxRemoteAcount() {
        return mSP.getString(Constants.SP_REMOTE_HX_CONTACT, null);
    }

    @Override
    public void saveConnectedRobot(String cntRobot) {

    }


}
