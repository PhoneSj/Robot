package com.zige.robot.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 * 机器人朋友bean
 */

public class RobotFriendListBean {

    /**
     * code :
     * message :
     * friendList : [{"friendId":0,"friendName":"","friendPhone":""}]
     */

    private String code;
    private String message;
    private List<FriendListBean> friendList;

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

    public List<FriendListBean> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<FriendListBean> friendList) {
        this.friendList = friendList;
    }

    public static class FriendListBean {
        /**
         * friendId : 0
         * friendName :
         * friendPhone :
         */

        private int friendId;
        private String friendName;
        private String friendPhone;

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

        public String getFriendPhone() {
            return friendPhone;
        }

        public void setFriendPhone(String friendPhone) {
            this.friendPhone = friendPhone;
        }
    }
}
