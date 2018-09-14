package com.qyy.jyshop.admin.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.admin.order.service.DeliveryService;
import com.qyy.jyshop.dao.DeliveryDao;
import com.qyy.jyshop.model.Delivery;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class DeliveryServiceImpl  extends AbstratService<Delivery> implements DeliveryService{

    @Autowired
    private DeliveryDao deliveryDao;
    
    @Override
    public Delivery queryByOrderId(Long orderId) {
        return this.deliveryDao.selectLogiByOrderId(orderId);
    }

}
