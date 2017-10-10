package com.zige.robot.fsj.model.bean;

import java.util.List;

/**
 * Created by PhoneSj on 2017/9/22.
 */

public class AlbumBean {


    /**
     * hasNextPage : false
     * list : [{"photoAlbumId":12483578237838,"photoAlbumName":"相册名","introImages":[],"count":10}]
     */

    private boolean hasNextPage;
    private List<ListBean> list;

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * photoAlbumId : 12483578237838
         * photoAlbumName : 相册名
         * introImages : []
         * count : 10
         */

        private long photoAlbumId;
        private String photoAlbumName;
        private int count;
        private List<String> introImages;
        private boolean selected;

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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<String> getIntroImages() {
            return introImages;
        }

        public void setIntroImages(List<String> introImages) {
            this.introImages = introImages;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
