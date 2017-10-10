package com.baidufacerecog;

import java.util.List;

/**
 * 功能：
 * <p>
 * Created by zhanghuan on 2017/6/15.
 */

public class FaceResultBaidu {

    /**
     * log_id : 73473737
     * result_num : 1
     * result : [{"group_id":"test1","uid":"u333333","user_info":"Test User","scores":[99.3,83.4]}]
     */

    private long log_id;
    private int result_num;
    private List<ResultBean> result;
    /**
     * error_code : 216618
     * log_id : 2167499623061519
     * error_msg : no user in group
     */

    private int error_code = 0;
    private String error_msg;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getResult_num() {
        return result_num;
    }

    public void setResult_num(int result_num) {
        this.result_num = result_num;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public static class ResultBean {
        /**
         * group_id : test1
         * uid : u333333
         * user_info : Test User
         * scores : [99.3,83.4]
         */

        private String group_id;
        private String uid;
        private String user_info;
        private List<Double> scores;

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUser_info() {
            return user_info;
        }

        public void setUser_info(String user_info) {
            this.user_info = user_info;
        }

        public List<Double> getScores() {
            return scores;
        }

        public void setScores(List<Double> scores) {
            this.scores = scores;
        }
    }
}
