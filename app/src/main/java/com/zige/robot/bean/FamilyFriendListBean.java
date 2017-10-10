package com.zige.robot.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */

public class FamilyFriendListBean {

    /**
     * code : 0000
     * message : 成功
     * familyList : [{"username":"13800138001","userId":1245,"nickname":"爸爸"}]
     */

    private String code;
    private String message;
    private List<FamilyListBean> familyList;

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

    public List<FamilyListBean> getFamilyList() {
        return familyList;
    }

    public void setFamilyList(List<FamilyListBean> familyList) {
        this.familyList = familyList;
    }

    public static class FamilyListBean {
        /**
         * username : 13800138001
         * userId : 1245
         * nickname : 爸爸
         */

        private String username;
        private int userId;
        private String nickname;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
