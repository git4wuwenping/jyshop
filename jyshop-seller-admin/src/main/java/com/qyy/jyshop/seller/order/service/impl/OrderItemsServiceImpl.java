package com.qyy.jyshop.seller.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.seller.order.service.OrderItemsService;
import com.qyy.jyshop.dao.OrderItemsDao;
import com.qyy.jyshop.model.OrderItems;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

@Service
public class OrderItemsServiceImpl extends AbstratService<OrderItems> implements OrderItemsService{

    @Autowired
    private OrderItemsDao orderItemsDao;
    
    @Override
    public List<OrderItems> queryByOrderId(Long orderId) {
        
        if(StringUtil.isEmpty(orderId))
            return null;
        
        Example example = new Example(OrderItems.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        
        return this.orderItemsDao.selectByExample(example);
    }
}
