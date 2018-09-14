package com.qyy.jyshop.admin.giftorder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.admin.giftorder.service.GiftDeliveryService;
import com.qyy.jyshop.dao.GiftDeliveryDao;
import com.qyy.jyshop.model.GiftDelivery;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class GiftDeliveryServiceImpl  extends AbstratService<GiftDelivery> implements GiftDeliveryService{

    @Autowired
    private GiftDeliveryDao giftDeliveryDao;
    
    @Override
    public GiftDelivery queryByOrderId(Long orderId) {
        return this.giftDeliveryDao.selectLogiByOrderId(orderId);
    }

}
