package com.zige.robot.greendao;


import com.zige.robot.App;
import com.zige.robot.greendao.gen.DaoMaster;
import com.zige.robot.greendao.gen.DaoSession;

/**
 * Created by Steven on 16/12/20.
 */

public class GreenDaoManager {
    private static GreenDaoManager mInstance;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static final String DB_NAME ="zige_db";

    private GreenDaoManager() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(App.getInstance(),
                DB_NAME, null);
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            mInstance = new GreenDaoManager();
        }

        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }


}
