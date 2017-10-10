package com.zige.robot.fsj.model.db;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Administrator on 2017/9/9.
 */

public class RealmHelper implements DBHelper {

    private static final String DB_NAME = "zige.realm";

    private Realm realm;

    @Inject
    public RealmHelper() {
        realm = Realm.getInstance(new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded()
                .name(DB_NAME)
                .build());
    }
}
