package com.qyy.jyshop.admin.giftorder.service;

import com.qyy.jyshop.model.GiftDelivery;


public interface GiftDeliveryService {

    
    public GiftDelivery queryByOrderId(Long orderId);
}
