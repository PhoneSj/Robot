package com.zige.robot.fsj.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */

public class CallComsumeRecordBean {

    /**
     * page : 1
     * total : 10
     * rows : [{"id":1,"callId":"74095751","caller":"爸爸","callerId":"123","called":"馒头","calledId":"123456","consumeTime":124,"createTime":123566779}]
     */

    private int page;
    private int total;
    private List<RowsBean> rows;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * id : 1
         * callId : 74095751
         * caller : 爸爸
         * callerId : 123
         * called : 馒头
         * calledId : 123456
         * consumeTime : 124
         * createTime : 123566779
         */

        private int id;
        private String callId;
        private String caller;
        private String callerId;
        private String called;
        private String calledId;
        private int consumeTime;
        private int createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCallId() {
            return callId;
        }

        public void setCallId(String callId) {
            this.callId = callId;
        }

        public String getCaller() {
            return caller;
        }

        public void setCaller(String caller) {
            this.caller = caller;
        }

        public String getCallerId() {
            return callerId;
        }

        public void setCallerId(String callerId) {
            this.callerId = callerId;
        }

        public String getCalled() {
            return called;
        }

        public void setCalled(String called) {
            this.called = called;
        }

        public String getCalledId() {
            return calledId;
        }

        public void setCalledId(String calledId) {
            this.calledId = calledId;
        }

        public int getConsumeTime() {
            return consumeTime;
        }

        public void setConsumeTime(int consumeTime) {
            this.consumeTime = consumeTime;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }
    }
}
