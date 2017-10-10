package com.zige.robot.bean;

import java.io.Serializable;

/**
 * 功能：
 * <p/>
 * Created by zhanghuan on 2016/12/15.
 */
public class AnswerCode implements Serializable {
    public int rc;
    public String operation;
    public String service;
    public AnswerBean answer;
    public String text;
}
