package com.zige.robot.fsj.model.param;

/**
 * Created by PhoneSj on 2017/9/25.
 */

public class AlbumUpdateParams {

    /**
     * username : 13583593593
     * robotDeviceId : rkdskjfskfskd
     * photoAlbumId : 000012
     * photoAlbumName : 新文件夹
     */

    private String username;
    private String robotDeviceId;
    private long photoAlbumId;
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

    public long getPhotoAlbumId() {
        return photoAlbumId;
    }

    public void setPhotoAlbumId(long photoAlbumId) {
        this.photoAlbumId = photoAlbumId;
    }

    public String getPhotoAlbumName() {
        return photoAlbumName;
    }

    public void setPhotoAlbumName(String photoAlbumName) {
        this.photoAlbumName = photoAlbumName;
    }
}
