package com.zige.robot.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

public class RolesNameBean {


    /**
     * code : 0000
     * message : 成功
     * roleList : ["爸爸","妈妈","爷爷","奶奶","哥哥","姐姐","弟弟","妹妹","其他"]
     */

    private String code;
    private String message;
    private List<String> roleList;

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

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }
}
