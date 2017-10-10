package com.zige.robot.fsj.model.bean;

import java.util.List;

/**
 * Created by PhoneSj on 2017/9/26.
 */

public class PhotoListBean {

    /**
     * hasNextPage : true
     * list : [{"photoId":13530611330,"photoUrl":"qiniuPath","description":"picture"}]
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

        public enum UploadState {
            WAITING, UPLOADING, FINISH, FAIL
        }

        public static final int VIEW_TYPE_NORMAL = 0;
        public static final int VIEW_TYPE_UPLOAD = 1;

        /**
         * photoId : 13530611330
         * photoUrl : qiniuPath
         * description : picture
         */

        private long photoId;
        private String photoUrl;
        private String description;
        //下面几个本地增加属性
        private boolean selected;
        private int viewType;
        private UploadState state;
        private double percent;

        public long getPhotoId() {
            return photoId;
        }

        public void setPhotoId(long photoId) {
            this.photoId = photoId;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getViewType() {
            return viewType;
        }

        public void setViewType(int viewType) {
            this.viewType = viewType;
        }

        public UploadState getState() {
            return state;
        }

        public void setState(UploadState state) {
            this.state = state;
        }

        public double getPercent() {
            return percent;
        }

        public void setPercent(double percent) {
            this.percent = percent;
        }
    }
}
