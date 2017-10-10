package com.zige.robot.http.rxhttp.reponse;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 *
 */

public class HomeWorkDetails {

    /**
     * code : 200
     * data : {"assigner":"爸爸","content":"好好读书","endTime":11111111111111,"homeworkId":1111111111111,"isDone":false,"leaveMessages":[{"duration":180,"isRead":false,"messageContent":"httpurl","messageType":2,"owner":1}],"startTime":1111111111111111111,"subjectId":11111111111111,"subjectName":"语文"}
     * message : SUCCESS
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * assigner : 爸爸
         * content : 好好读书
         * endTime : 11111111111111
         * homeworkId : 1111111111111
         * isDone : false
         * leaveMessages : [{"duration":180,"isRead":false,"messageContent":"httpurl","messageType":2,"owner":1}]
         * startTime : 1111111111111111111
         * subjectId : 11111111111111
         * subjectName : 语文
         */

        private String assigner;
        private String content;
        private long endTime;
        private long homeworkId;
        private boolean isDone;
        private long startTime;
        private long subjectId;
        private String subjectName;
        private List<LeaveMessagesBean> leaveMessages;

        public String getAssigner() {
            return assigner;
        }

        public void setAssigner(String assigner) {
            this.assigner = assigner;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getHomeworkId() {
            return homeworkId;
        }

        public void setHomeworkId(long homeworkId) {
            this.homeworkId = homeworkId;
        }

        public boolean isIsDone() {
            return isDone;
        }

        public void setIsDone(boolean isDone) {
            this.isDone = isDone;
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

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public List<LeaveMessagesBean> getLeaveMessages() {
            return leaveMessages;
        }

        public void setLeaveMessages(List<LeaveMessagesBean> leaveMessages) {
            this.leaveMessages = leaveMessages;
        }

        public static class LeaveMessagesBean {
            /**
             * duration : 180
             * isRead : false
             * messageContent : httpurl
             * messageType : 2
             * owner : 1
             */

            private Long duration;
            private boolean isRead;
            private String messageContent;
            private int messageType;
            private int owner;
            private Long leaveMessageId;
            private String localVoicePath;

            public String getLocalVoicePath() {
                return localVoicePath;
            }

            public void setLocalVoicePath(String localVoicePath) {
                this.localVoicePath = localVoicePath;
            }

            public Long getLeaveMessageId() {
                return leaveMessageId;
            }

            public void setLeaveMessageId(Long leaveMessageId) {
                this.leaveMessageId = leaveMessageId;
            }



            public LeaveMessagesBean(Long duration, boolean isRead, String messageContent, int messageType, int owner) {
                this.duration = duration;
                this.isRead = isRead;
                this.messageContent = messageContent;
                this.messageType = messageType;
                this.owner = owner;
            }

            public Long getDuration() {
                return duration;
            }

            public void setDuration(Long duration) {
                this.duration = duration;
            }

            public boolean isIsRead() {
                return isRead;
            }

            public void setIsRead(boolean isRead) {
                this.isRead = isRead;
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

            public int getOwner() {
                return owner;
            }

            public void setOwner(int owner) {
                this.owner = owner;
            }
        }
    }
}
