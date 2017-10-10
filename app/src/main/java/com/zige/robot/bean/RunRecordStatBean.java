package com.zige.robot.bean;

/**
 * Created by lidingwei on 2017/5/17 0017.
 * 运行记录数据统计bean
 */
public class RunRecordStatBean {

    /**
     * code :
     * message :
     * robotRunStat : {"id":0,"robotDeviceId":"","curAppName":"","battery":0,"rateMusic":0,
     * "rateStory":0,"rateIdle":0,"rateLearn":0,"rateOther":0,"gameTotalTime":0,"gamePerTime":0,
     * "gameIntervalTime":0,"learnStartTime":"","learnEndTime":""}
     */

    private String code;
    private String message;
    private RobotRunStatBean robotRunStat;

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

    public RobotRunStatBean getRobotRunStat() {
        return robotRunStat;
    }

    public void setRobotRunStat(RobotRunStatBean robotRunStat) {
        this.robotRunStat = robotRunStat;
    }

    public static class RobotRunStatBean {
        /**
         * id : 0
         * robotDeviceId :
         * curAppName :
         * battery : 0
         * rateMusic : 0
         * rateStory : 0
         * rateIdle : 0
         * rateLearn : 0
         * rateOther : 0
         * gameTotalTime : 0
         * gamePerTime : 0
         * gameIntervalTime : 0
         * learnStartTime :
         * learnEndTime :
         */

        private int id;
        private String robotDeviceId;
        private String curAppName;
        private int battery;
        private int rateMusic;
        private int rateStory;
        private int rateIdle;
        private int rateLearn;
        private int rateOther;
        private int gameTotalTime;
        private int gamePerTime;
        private int gameIntervalTime;
        private String learnStartTime;
        private String learnEndTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRobotDeviceId() {
            return robotDeviceId;
        }

        public void setRobotDeviceId(String robotDeviceId) {
            this.robotDeviceId = robotDeviceId;
        }

        public String getCurAppName() {
            return curAppName;
        }

        public void setCurAppName(String curAppName) {
            this.curAppName = curAppName;
        }

        public int getBattery() {
            return battery;
        }

        public void setBattery(int battery) {
            this.battery = battery;
        }

        public int getRateMusic() {
            return rateMusic;
        }

        public void setRateMusic(int rateMusic) {
            this.rateMusic = rateMusic;
        }

        public int getRateStory() {
            return rateStory;
        }

        public void setRateStory(int rateStory) {
            this.rateStory = rateStory;
        }

        public int getRateIdle() {
            return rateIdle;
        }

        public void setRateIdle(int rateIdle) {
            this.rateIdle = rateIdle;
        }

        public int getRateLearn() {
            return rateLearn;
        }

        public void setRateLearn(int rateLearn) {
            this.rateLearn = rateLearn;
        }

        public int getRateOther() {
            return rateOther;
        }

        public void setRateOther(int rateOther) {
            this.rateOther = rateOther;
        }

        public int getGameTotalTime() {
            return gameTotalTime;
        }

        public void setGameTotalTime(int gameTotalTime) {
            this.gameTotalTime = gameTotalTime;
        }

        public int getGamePerTime() {
            return gamePerTime;
        }

        public void setGamePerTime(int gamePerTime) {
            this.gamePerTime = gamePerTime;
        }

        public int getGameIntervalTime() {
            return gameIntervalTime;
        }

        public void setGameIntervalTime(int gameIntervalTime) {
            this.gameIntervalTime = gameIntervalTime;
        }

        public String getLearnStartTime() {
            return learnStartTime;
        }

        public void setLearnStartTime(String learnStartTime) {
            this.learnStartTime = learnStartTime;
        }

        public String getLearnEndTime() {
            return learnEndTime;
        }

        public void setLearnEndTime(String learnEndTime) {
            this.learnEndTime = learnEndTime;
        }
    }
}