package com.qyy.jyshop.admin.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.order.service.OrderLogService;
import com.qyy.jyshop.dao.OrderLogDao;
import com.qyy.jyshop.model.OrderLog;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

@Service
public class OrderLogServiceImpl extends AbstratService<OrderLog> implements OrderLogService{

    @Autowired
    private OrderLogDao orderLogDao;
    
    @Override
    public List<OrderLog> queryByOrderId(Long orderId) {
        
        if(StringUtil.isEmpty(orderId))
            return null;
        
        Example example = new Example(OrderLog.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        
        return this.orderLogDao.selectByExample(example);
    }

}
