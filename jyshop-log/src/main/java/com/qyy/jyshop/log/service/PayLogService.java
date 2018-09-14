package com.qyy.jyshop.log.service;

import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.PayOrder;

public interface PayLogService {
    
    /**
     * 添加普通订单支付日志
     * @author hwc
     * @created 2018年1月11日 下午4:13:13
     * @param payOrder
     */
    public void doAddOrderPayLog(Order order);
    
    /**
     * 添加支付订单支付日志
     * @author hwc
     * @created 2018年1月11日 下午4:12:51
     * @param payOrder
     */
    public void doAddPayLog(PayOrder payOrder);
}
