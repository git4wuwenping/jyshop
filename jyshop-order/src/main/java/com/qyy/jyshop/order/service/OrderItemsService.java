package com.qyy.jyshop.order.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.qyy.jyshop.model.Order;

public interface OrderItemsService {

    /**
     * 添加订单货品信息
     * @author hwc
     * @created 2018年3月23日 上午10:24:29
     * @param goodsJsonArray
     * @param order 父订单
     * @param childOrderList 子订单列表
     */
    public void doAddOrderItems(JSONArray goodsJsonArray,Order order,List<Order> childOrderList);
}
