package com.zige.robot.fsj.model.param;

/**
 * Created by PhoneSj on 2017/9/27.
 * 创建相册文件夹时的网络请求参数
 */

public class AlbumNewParams {

    private String username;
    private String robotDeviceId;
    private String photoAlbumName;

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

    public String getPhotoAlbumName() {
        return photoAlbumName;
    }

    public void setPhotoAlbumName(String photoAlbumName) {
        this.photoAlbumName = photoAlbumName;
    }
}
