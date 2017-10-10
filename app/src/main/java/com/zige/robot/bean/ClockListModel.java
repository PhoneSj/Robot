package com.zige.robot.bean;

import java.util.List;

/**
 * Created by ldw on 2017/8/29.
 */

public class ClockListModel {

    /**
     * code : 0000
     * message : 成功
     * clockList : [{"id":13,"clockTimeHour":2,"repeat":"2","clockTimeMin":2,"content":"哈哈","member":"爸爸","status":0},{"id":15,"clockTimeHour":2,"repeat":"2","clockTimeMin":2,"content":"??????","member":"爸爸","status":0},{"id":16,"clockTimeHour":2,"repeat":"2","clockTimeMin":2,"content":"哈哈","member":"爸爸","status":0},{"id":17,"clockTimeHour":2,"repeat":"2","clockTimeMin":2,"content":"哈哈","member":"爸爸","status":0},{"id":18,"clockTimeHour":2,"repeat":"2","clockTimeMin":2,"content":"哈哈","member":"爸爸","status":0},{"id":19,"clockTimeHour":2,"repeat":"2","clockTimeMin":2,"content":"哈哈","member":"爸爸","status":0},{"id":39,"clockTimeHour":13,"repeat":"3","clockTimeMin":50,"content":"你好啊","member":"爸爸","status":0},{"id":40,"clockTimeHour":14,"repeat":"3","clockTimeMin":0,"content":"礼拜三","member":"爸爸","status":0},{"id":41,"clockTimeHour":15,"repeat":"3,7","clockTimeMin":0,"content":"礼拜三日","member":"爸爸","status":0}]
     */

    private String code;
    private String message;
    private List<ClockListBean> clockList;

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

    public List<ClockListBean> getClockList() {
        return clockList;
    }

    public void setClockList(List<ClockListBean> clockList) {
        this.clockList = clockList;
    }

    public static class ClockListBean {
        /**
         * id : 13
         * clockTimeHour : 2
         * repeat : 2
         * clockTimeMin : 2
         * content : 哈哈
         * member : 爸爸
         * status : 0
         */

        private int id;
        private int clockTimeHour;
        private String repeat;
        private int clockTimeMin;
        private String content;
        private String member;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getClockTimeHour() {
            return clockTimeHour;
        }

        public void setClockTimeHour(int clockTimeHour) {
            this.clockTimeHour = clockTimeHour;
        }

        public String getRepeat() {
            return repeat;
        }

        public void setRepeat(String repeat) {
            this.repeat = repeat;
        }

        public int getClockTimeMin() {
            return clockTimeMin;
        }

        public void setClockTimeMin(int clockTimeMin) {
            this.clockTimeMin = clockTimeMin;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMember() {
            return member;
        }

        public void setMember(String member) {
            this.member = member;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
