package com.qyy.jyshop.goods.server.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.dao.OrderItemsDao;
import com.qyy.jyshop.dao.ProductDao;
import com.qyy.jyshop.dao.ProductStoreDao;
import com.qyy.jyshop.goods.server.ProductStoreService;
import com.qyy.jyshop.model.OrderItems;
import com.qyy.jyshop.model.ProductStore;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class ProductStoreServiceImpl extends AbstratService<ProductStore> implements ProductStoreService{

    @Autowired
    private OrderItemsDao orderItemsDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductStoreDao productStoreDao;
    
    @Override
    @Transactional
    public void doEditStoreByOrderId(Long orderId) {
       
        List<OrderItems> orderItemsList=this.orderItemsDao.selectByOrderId(orderId);
        
        if(orderItemsList!=null && orderItemsList.size()>0){
            
            for (OrderItems orderItems : orderItemsList) {
                this.productDao.updateStoreOfCut(orderItems.getProductId(), orderItems.getBuyCount());
                this.productStoreDao.updateStoreOfCut(orderItems.getProductId(), orderItems.getBuyCount());
            }
        }
    }

}
