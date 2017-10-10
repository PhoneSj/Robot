package com.zige.robot.bean;

import java.util.List;

/**
 * Created by lidingwei on 2017/5/17 0017.
 */
public class RunRecordListBean {

    /**
     * code :
     * message :
     * recordList : [{"appName":"","startTime":"","endTime":""}]
     */

    private String code;
    private String message;
    private List<RecordListBean> recordList;

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

    public List<RecordListBean> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<RecordListBean> recordList) {
        this.recordList = recordList;
    }

    public static class RecordListBean {
        /**
         * appName :
         * startTime :
         * endTime :
         */

        private String appName;
        private String startTime;
        private String endTime;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}