package com.zige.robot.greendao.util;

import android.util.Log;

import com.zige.robot.greendao.GreenDaoManager;
import com.zige.robot.greendao.entity.Order;
import com.zige.robot.greendao.gen.OrderDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */

public class OrderDaoUtil {

    private static OrderDaoUtil mInstance;
    private OrderDao mOrderDao;
    private static final String TAG ="OrderDaoUtil";

    public OrderDaoUtil(){
        mOrderDao = GreenDaoManager.getInstance().getSession().getOrderDao();
    }

    public static OrderDaoUtil getInstance() {
        if (mInstance == null) {
            mInstance = new OrderDaoUtil();
        }
        return mInstance;
    }

    /**
     * 查询所有
     * @return
     */
    public List<Order> queryAllOrder(){
        List<Order> orderList = new ArrayList<>();
        orderList = mOrderDao.queryBuilder().build().list();
        Log.i(TAG,  "查询到：" + orderList.size() + "条结果");
        return  orderList;
    }

    //查询
    public void queryOrder(String content) {
        List result = mOrderDao.queryBuilder().where(OrderDao.Properties.OrderContent.eq(content)).build().list();
        Log.i(TAG,  "查询到：" + result.size() + "条结果");
    }

    //更新
    public void updateOrder(String prevContent, String newContent) {
        Order findOrder = mOrderDao.queryBuilder().where(OrderDao.Properties.OrderContent.eq(prevContent)).build().unique();
        if (findOrder != null) {
            findOrder.setOrderContent(newContent);
            mOrderDao.update(findOrder);
            Log.i(TAG,  "修改成功");
        } else {
            Log.i(TAG,  "数据不存在");
        }

    }

    //删除
    public void deleteOrder(Long id) {
        Order findOrder = mOrderDao.queryBuilder().where(OrderDao.Properties.Id.eq(id)).build().unique();
        if(findOrder != null) {
            mOrderDao.delete(findOrder);
            Log.i(TAG,  "删除成功");
        } else {
            Log.i(TAG,  "数据不存在");
        }
    }

    //插入
    public void insertOrder(Long id, String content) {
        Order order = new Order(id, content);
        mOrderDao.insertOrReplace(order);
        Log.i(TAG,  "数据添加成功：" + content);
    }

    //删除所有的order
    public void deleteAllOrder(){
        List<Order> orderList = mOrderDao.queryBuilder().build().list();
        if(orderList!=null && orderList.size() >0){
            for (Order order : orderList) {
                deleteOrder(order.getId());
            }
        }
    }



}
