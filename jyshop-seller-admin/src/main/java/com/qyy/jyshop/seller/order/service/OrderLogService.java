package com.qyy.jyshop.seller.order.service;

import java.util.List;

import com.qyy.jyshop.model.OrderLog;

public interface OrderLogService {

    public List<OrderLog> queryByOrderId(Long orderId);
}
