package com.zige.robot.fsj.model.param;

import java.util.List;

/**
 * Created by PhoneSj on 2017/10/10.
 */

public class PhotoMoveParams {

    private String username;
    private String robotDeviceId;
    private List<Long> photoIds;
    private long toPhotoAlbumId;

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

    public List<Long> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(List<Long> photoIds) {
        this.photoIds = photoIds;
    }

    public long getToPhotoAlbumId() {
        return toPhotoAlbumId;
    }

    public void setToPhotoAlbumId(long toPhotoAlbumId) {
        this.toPhotoAlbumId = toPhotoAlbumId;
    }
}
