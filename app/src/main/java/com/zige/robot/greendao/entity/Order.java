package com.zige.robot.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Administrator on 2017/5/3.
 */
@Entity
public class Order {
    @Id
    private Long id;
    @Unique
    private String orderContent;

    @Generated(hash = 437962777)
    public Order(Long id, String orderContent) {
        this.id = id;
        this.orderContent = orderContent;
    }

    @Generated(hash = 1105174599)
    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }
}
