package com.qyy.jyshop.admin.order.service;

import java.util.List;

import com.qyy.jyshop.model.OrderLog;

public interface OrderLogService {

    public List<OrderLog> queryByOrderId(Long orderId);
}
