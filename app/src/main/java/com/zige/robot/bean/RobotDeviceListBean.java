package com.zige.robot.bean;

import java.util.List;

/**
 * Created by ldw on 2017/8/28.
 */

public class RobotDeviceListBean {

    /**
     * code : 0000
     * message : 成功
     * robotList : [{"id":284,"icon":null,"robotDeviceId":"320d139be4c0ab81","deviceName":"哈哈","nickname":"爸爸"}]
     */

    private String code;
    private String message;
    private List<RobotListBean> robotList;

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

    public List<RobotListBean> getRobotList() {
        return robotList;
    }

    public void setRobotList(List<RobotListBean> robotList) {
        this.robotList = robotList;
    }

    public static class RobotListBean {
        /**
         * id : 284
         * icon : null
         * robotDeviceId : 320d139be4c0ab81
         * deviceName : 哈哈
         * nickname : 爸爸
         */

        private int id;
        private String icon;
        private String robotDeviceId;
        private String deviceName;
        private String nickname;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getRobotDeviceId() {
            return robotDeviceId;
        }

        public void setRobotDeviceId(String robotDeviceId) {
            this.robotDeviceId = robotDeviceId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
