package com.qyy.jyshop.admin.bargain.service;

import com.qyy.jyshop.model.BargainDelivery;
import com.qyy.jyshop.model.Delivery;

public interface BargainDeliveryService {

    
    public BargainDelivery queryByOrderId(Long orderId);
}
