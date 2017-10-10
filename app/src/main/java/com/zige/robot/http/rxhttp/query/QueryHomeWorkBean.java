package com.zige.robot.http.rxhttp.query;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 * 科目列表请求json
 */

public class QueryHomeWorkBean {

    /**
     * content : haohaoxuexi
     * deviceId : ewrqr
     * endTime : 11111111111
     * homeworkId : 111111111111111111
     * isDone : false
     * leaveMessages : [{"duration":18011111,"messageContent":"httpurl","messageType":2}]
     * robotDeviceId : eqwrqrwqq
     * startTime : 11111111111111
     * subjectId : 1111111111111111
     * username : 177777799
     */

    private String content;
    private String deviceId;
    private long endTime;
    private Long homeworkId;
    private boolean isDone;
    private String robotDeviceId;
    private long startTime;
    private long subjectId;
    private String username;
    private List<LeaveMessagesBean> leaveMessages;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Long getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(Long homeworkId) {
        this.homeworkId = homeworkId;
    }

    public boolean isIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public String getRobotDeviceId() {
        return robotDeviceId;
    }

    public void setRobotDeviceId(String robotDeviceId) {
        this.robotDeviceId = robotDeviceId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<LeaveMessagesBean> getLeaveMessages() {
        return leaveMessages;
    }

    public void setLeaveMessages(List<LeaveMessagesBean> leaveMessages) {
        this.leaveMessages = leaveMessages;
    }

    public static class LeaveMessagesBean {
        /**
         * duration : 18011111
         * messageContent : httpurl
         * messageType : 2
         */

        private Integer duration;
        private String messageContent;
        private int messageType;
        private String localVoicePath; //本地语音路径

        public LeaveMessagesBean(Integer duration, String messageContent, int messageType) {
            this.duration = duration;
            this.messageContent = messageContent;
            this.messageType = messageType;
        }


        public String getLocalVoicePath() {
            return localVoicePath;
        }

        public void setLocalVoicePath(String localVoicePath) {
            this.localVoicePath = localVoicePath;
        }


        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        public String getMessageContent() {
            return messageContent;
        }

        public void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }

        public int getMessageType() {
            return messageType;
        }

        public void setMessageType(int messageType) {
            this.messageType = messageType;
        }
    }
}
