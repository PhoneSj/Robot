package com.zige.robot.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */

public class UsableRolesBean {

    /**
     * code : 1
     * message :
     * userDeviceList : [{"username":"","nickname":"","user_id":"","userrole":"","usericon":"","child_nickname":"","child_sex":"","child_yearold":"","deviceid":"","admin":""}]
     */

    private String code;
    private String message;
    private List<UserDeviceListBean> userDeviceList;

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

    public List<UserDeviceListBean> getUserDeviceList() {
        return userDeviceList;
    }

    public void setUserDeviceList(List<UserDeviceListBean> userDeviceList) {
        this.userDeviceList = userDeviceList;
    }

    public static class UserDeviceListBean {
        /**
         * username :
         * nickname :
         * user_id :
         * userrole :
         * usericon :
         * child_nickname :
         * child_sex :
         * child_yearold :
         * deviceid :
         * admin :
         */

        private String username;
        private String nickname;
        private String user_id;
        private String userrole;
        private String usericon;
        private String child_nickname;
        private String child_sex;
        private String child_yearold;
        private String deviceid;
        private String admin;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUserrole() {
            return userrole;
        }

        public void setUserrole(String userrole) {
            this.userrole = userrole;
        }

        public String getUsericon() {
            return usericon;
        }

        public void setUsericon(String usericon) {
            this.usericon = usericon;
        }

        public String getChild_nickname() {
            return child_nickname;
        }

        public void setChild_nickname(String child_nickname) {
            this.child_nickname = child_nickname;
        }

        public String getChild_sex() {
            return child_sex;
        }

        public void setChild_sex(String child_sex) {
            this.child_sex = child_sex;
        }

        public String getChild_yearold() {
            return child_yearold;
        }

        public void setChild_yearold(String child_yearold) {
            this.child_yearold = child_yearold;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getAdmin() {
            return admin;
        }

        public void setAdmin(String admin) {
            this.admin = admin;
        }
    }
}
