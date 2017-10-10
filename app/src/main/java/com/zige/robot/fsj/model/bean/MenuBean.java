package com.zige.robot.fsj.model.bean;

/**
 * Created by Administrator on 2017/9/11.
 */

public class MenuBean {

    private int icon;
    private String title;

    public MenuBean(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
