package com.qyy.jyshop.admin.giftorder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.giftorder.service.GiftOrderLogService;
import com.qyy.jyshop.dao.GiftOrderLogDao;
import com.qyy.jyshop.model.GiftOrderLog;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

@Service
public class GiftOrderLogServiceImpl extends AbstratService<GiftOrderLog> implements GiftOrderLogService{

    @Autowired
    private GiftOrderLogDao giftOrderLogDao;
    
    @Override
    public List<GiftOrderLog> queryByOrderId(Long orderId) {
        
        if(StringUtil.isEmpty(orderId))
            return null;
        
        Example example = new Example(GiftOrderLog.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        
        return this.giftOrderLogDao.selectByExample(example);
    }

}
