package com.qyy.jyshop.admin.bargain.service.impl;

import com.qyy.jyshop.admin.bargain.service.BargainDeliveryService;
import com.qyy.jyshop.admin.order.service.DeliveryService;
import com.qyy.jyshop.dao.BargainDeliveryDao;
import com.qyy.jyshop.dao.DeliveryDao;
import com.qyy.jyshop.model.BargainDelivery;
import com.qyy.jyshop.model.Delivery;
import com.qyy.jyshop.supple.AbstratService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BargainDeliveryServiceImpl extends AbstratService<BargainDelivery> implements BargainDeliveryService {

    @Autowired
    private BargainDeliveryDao deliveryDao;
    
    @Override
    public BargainDelivery queryByOrderId(Long orderId) {
        return this.deliveryDao.selectLogiByOrderId(orderId);
    }

}
