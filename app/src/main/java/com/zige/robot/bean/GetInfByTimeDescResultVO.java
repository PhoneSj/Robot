package com.zige.robot.bean;

import java.util.List;

/**
 * @author Feel on 2017/4/7 09:49
 */

public class GetInfByTimeDescResultVO {

    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String message;

    private List<GetInfoByTimeDescVO> data;

    public List<GetInfoByTimeDescVO> getData() {
        return data;
    }

    public void setData(List<GetInfoByTimeDescVO> data) {
        this.data = data;
    }

    public static class  GetInfoByTimeDescVO{
        private String id; // 记录id,
        private String username; //用户手机号,
        private String deviceid; // 设备id,
        private String rewardtype; //奖励类型: 0:未完成 1:已完成,
        private String deal; // 处理状态: 0:未处理 1:已处理,
        private String wordtext; //养成描述,
        private String image; //图片地址,
        private String worktime; //创建时间,时间戳; 使用new Date(worktime)

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWorktime() {
            return worktime;
        }

        public void setWorktime(String worktime) {
            this.worktime = worktime;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getWordtext() {
            return wordtext;
        }

        public void setWordtext(String wordtext) {
            this.wordtext = wordtext;
        }

        public String getDeal() {
            return deal;
        }

        public void setDeal(String deal) {
            this.deal = deal;
        }

        public String getRewardtype() {
            return rewardtype;
        }

        public void setRewardtype(String rewardtype) {
            this.rewardtype = rewardtype;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
