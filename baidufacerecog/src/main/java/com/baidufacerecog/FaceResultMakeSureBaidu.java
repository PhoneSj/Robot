package com.baidufacerecog;

import java.util.List;

/**
 * 功能：
 * <p>
 * Created by zhanghuan on 2017/6/15.
 */

public class FaceResultMakeSureBaidu {
    /**
     * results : [93.86580657959,92.237548828125]
     * result_num : 2
     * log_id : 1629483134
     */

    private int result_num;
    private long log_id;
    private List<Double> result;
    /**
     * error_code : 216611
     * error_msg : user not exist
     */

    private int error_code = 0;
    private String error_msg;

    public int getResult_num() {
        return result_num;
    }

    public void setResult_num(int result_num) {
        this.result_num = result_num;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public List<Double> getResults() {
        return result;
    }

    public void setResults(List<Double> results) {
        this.result = results;
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
}
