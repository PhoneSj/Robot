package com.zige.robot.greendao.util;


import android.util.Log;

import com.zige.robot.greendao.RobotRemindManager;
import com.zige.robot.greendao.entity.RobotRemind;
import com.zige.robot.greendao.gen.RobotRemindDao;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class RobotRemindUtil {
    private static RobotRemindUtil mInstance;
    public RobotRemindDao robotRemindDao;
    private String TAG = "RobotRemindUtil";

    public RobotRemindUtil() {
        robotRemindDao = RobotRemindManager.getInstance().getSession().getRobotRemindDao();
    }

    public static RobotRemindUtil getInstance() {
        if (mInstance == null) {
            mInstance = new RobotRemindUtil();
        }
        return mInstance;
    }

    //插入
    public void insert(RobotRemind robotRemind) {
        robotRemindDao.insert(robotRemind);
        Log.i(TAG, "数据添加成功：");
    }
    //修改
    public void update(RobotRemind robotRemind) {
        robotRemindDao.update(robotRemind);
        Log.i(TAG, "数据修改成功：");
    }
    public List<RobotRemind> query() {
        List<RobotRemind> robotRemindList = robotRemindDao.queryBuilder().orderDesc(RobotRemindDao.Properties.Change_time).list();
        Log.i(TAG, "数据查询成功：条数   "+robotRemindList.size());
        for (int i = 0; i < robotRemindList.size(); i++) {
            RobotRemind robot = robotRemindList.get(i);
            Log.i(TAG, "数据查询成功：" + robot.getId() + " " + robot.getTime() + " " + robot.getWeek() + " " + robot.getMember() + " " + robot.getContext());
        }
        return robotRemindList;
    }


    public RobotRemind queryKey(long key) {
        RobotRemind robotRemind = robotRemindDao.load(key);

        return robotRemind;
    }
    public void upDate(long key){
//        robotRemindDao.u(key);
    }
    public void delete(long key){
        robotRemindDao.deleteByKey(key);
    }
    public void deleteAll(){
        robotRemindDao.deleteAll();
    }
}
