package com.zige.robot.fsj.model.bean;

/**
 * Created by PhoneSj on 2017/9/22.
 */

public class PictureUploadBean {

    /**
     * username : 13530611330   手机号 | 可选
     * robotDeviceId : rId      机器人设备号
     * photoAlbumId : 100       相册id
     * description : desp       相片描述 | 可选
     * photoUrl : url           相片url,七牛url
     */

    private String username;
    private String robotDeviceId;
    private int photoAlbumId;
    private String description;
    private String photoUrl;

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

    public int getPhotoAlbumId() {
        return photoAlbumId;
    }

    public void setPhotoAlbumId(int photoAlbumId) {
        this.photoAlbumId = photoAlbumId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
