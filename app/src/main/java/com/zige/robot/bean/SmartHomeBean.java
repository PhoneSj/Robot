package com.zige.robot.bean;

/**
 * Created by Administrator on 2017/7/19.
 */

public class SmartHomeBean {


    private int resId=-1;
    private String docText;
    private String path="";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getDocText() {
        return docText;
    }

    public void setDocText(String docText) {
        this.docText = docText;
    }


    public SmartHomeBean(int resId, String docText) {
        this.resId = resId;
        this.docText = docText;
    }

    public SmartHomeBean(String path, String docText) {
        this.path = path;
        this.docText = docText;
    }


}
