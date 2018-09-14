package com.qyy.jyshop.admin.order.service;

import com.qyy.jyshop.model.Delivery;

public interface DeliveryService {

    
    public Delivery queryByOrderId(Long orderId);
}
