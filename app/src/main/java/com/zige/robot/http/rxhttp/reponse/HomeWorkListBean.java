package com.zige.robot.http.rxhttp.reponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */

public class HomeWorkListBean {


    /**
     * code : 200
     * data : {"hasNextPage":false,"list":[{"assigner":"爸爸","content":"homework","endTime":12334,"homeworkId":123,"isDone":false,"leaveMessages":[{"duration":2,"isRead":false,"messageContent":"http:llll","messageType":2,"owner":1}],"startTime":1233,"subjectId":123,"subjectName":"http:llll"}]}
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

    public static class DataBean implements Serializable{
        /**
         * hasNextPage : false
         * list : [{"assigner":"爸爸","content":"homework","endTime":12334,"homeworkId":123,"isDone":false,"leaveMessages":[{"duration":2,"isRead":false,"messageContent":"http:llll","messageType":2,"owner":1}],"startTime":1233,"subjectId":123,"subjectName":"http:llll"}]
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

        public static class ListBean implements Serializable{
            /**
             * assigner : 爸爸
             * content : homework
             * endTime : 12334
             * homeworkId : 123
             * isDone : false
             * leaveMessages : [{"duration":2,"isRead":false,"messageContent":"http:llll","messageType":2,"owner":1}]
             * startTime : 1233
             * subjectId : 123
             * subjectName : http:llll
             */

            private String assigner;
            private String content;
            private long endTime;
            private long homeworkId;
            private boolean isDone;
            private long startTime;
            private int unReadCount;


            public int getUnReadCount() {
                return unReadCount;
            }

            public void setUnReadCount(int unReadCount) {
                this.unReadCount = unReadCount;
            }

            public int getLastChildMessageType() {
                return lastChildMessageType;
            }

            public void setLastChildMessageType(int lastChildMessageType) {
                this.lastChildMessageType = lastChildMessageType;
            }

            public String getLastChildMessageContent() {
                return lastChildMessageContent;
            }

            public void setLastChildMessageContent(String lastChildMessageContent) {
                this.lastChildMessageContent = lastChildMessageContent;
            }

            private int lastChildMessageType;
            private String lastChildMessageContent;


            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            private long createTime;
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

            public static class LeaveMessagesBean implements Serializable{
                /**
                 * duration : 2
                 * isRead : false
                 * messageContent : http:llll
                 * messageType : 2
                 * owner : 1
                 */

                private Integer duration;
                private boolean isRead;
                private String messageContent;
                private int messageType;
                private int owner; //1家长  2 小孩

                public Long getLeaveMessageId() {
                    return leaveMessageId;
                }

                public void setLeaveMessageId(Long leaveMessageId) {
                    this.leaveMessageId = leaveMessageId;
                }

                private Long leaveMessageId;

                public Integer getDuration() {
                    return duration;
                }

                public void setDuration(Integer duration) {
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
}
