package com.qyy.jyshop.admin.giftorder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.giftorder.service.GiftOrderItemsService;
import com.qyy.jyshop.dao.GiftOrderItemsDao;
import com.qyy.jyshop.model.GiftOrderItems;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

@Service
public class GiftOrderItemsServiceImpl extends AbstratService<GiftOrderItems> implements GiftOrderItemsService{

    @Autowired
    private GiftOrderItemsDao giftOrderItemsDao;
    
    @Override
    public List<GiftOrderItems> queryByOrderId(Long orderId) {
        
        if(StringUtil.isEmpty(orderId))
            return null;
        
        Example example = new Example(GiftOrderItems.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        
        return this.giftOrderItemsDao.selectByExample(example);
    }
}
