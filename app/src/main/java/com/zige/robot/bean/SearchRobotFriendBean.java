package com.zige.robot.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 */

public class SearchRobotFriendBean {

    /**
     * code :
     * message :
     * searchList : [{"friendId":0,"friendName":"","addStatus":0}]
     */

    private String code;
    private String message;
    private List<SearchListBean> searchList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SearchListBean> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<SearchListBean> searchList) {
        this.searchList = searchList;
    }

    public static class SearchListBean {
        /**
         * friendId : 0
         * friendName :
         * addStatus : 0
         */

        private int friendId;
        private String friendName;
        private int addStatus;

        public int getFriendId() {
            return friendId;
        }

        public void setFriendId(int friendId) {
            this.friendId = friendId;
        }

        public String getFriendName() {
            return friendName;
        }

        public void setFriendName(String friendName) {
            this.friendName = friendName;
        }

        public int getAddStatus() {
            return addStatus;
        }

        public void setAddStatus(int addStatus) {
            this.addStatus = addStatus;
        }
    }
}
