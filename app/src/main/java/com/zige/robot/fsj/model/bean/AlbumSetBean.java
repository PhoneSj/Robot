package com.zige.robot.fsj.model.bean;

import java.util.List;

/**
 * Created by PhoneSj on 2017/9/22.
 * 相册集合
 */

public class AlbumSetBean {

    private boolean hasNextPage;
    private List<AlbumSingleBean> list;

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<AlbumSingleBean> getList() {
        return list;
    }

    public void setList(List<AlbumSingleBean> list) {
        this.list = list;
    }

    /**
     * 单个相册
     */
    public static class AlbumSingleBean {
        private String photoAlbumId;
        private String photoAlbumName;
        private String firstPhotoUrl;
        private int count;

        public String getPhotoAlbumId() {
            return photoAlbumId;
        }

        public void setPhotoAlbumId(String photoAlbumId) {
            this.photoAlbumId = photoAlbumId;
        }

        public String getPhotoAlbumName() {
            return photoAlbumName;
        }

        public void setPhotoAlbumName(String photoAlbumName) {
            this.photoAlbumName = photoAlbumName;
        }

        public String getFirstPhotoUrl() {
            return firstPhotoUrl;
        }

        public void setFirstPhotoUrl(String firstPhotoUrl) {
            this.firstPhotoUrl = firstPhotoUrl;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
