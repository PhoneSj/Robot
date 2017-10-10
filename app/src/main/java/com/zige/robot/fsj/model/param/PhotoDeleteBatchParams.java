package com.zige.robot.fsj.model.param;

import java.util.List;

/**
 * Created by PhoneSj on 2017/9/27.
 */

public class PhotoDeleteBatchParams {

    private List<Long> photoIds;
    private String username;
    private String robotDeviceId;

    public List<Long> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(List<Long> photoIds) {
        this.photoIds = photoIds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRobotDeviceId() {
        return robotDeviceId;
    }

    public void setRobotDeviceId(String robotDeviceId) {
        this.robotDeviceId = robotDeviceId;
    }
}
