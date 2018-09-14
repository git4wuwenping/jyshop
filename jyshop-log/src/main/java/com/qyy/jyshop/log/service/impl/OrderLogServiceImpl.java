package com.qyy.jyshop.log.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.dao.OrderLogDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.log.service.OrderLogService;
import com.qyy.jyshop.model.OrderLog;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class OrderLogServiceImpl extends AbstratService<OrderLog> implements OrderLogService{

    @Autowired
    private OrderLogDao orderLogDao;
    
    @Override
    @Transactional
    public List<OrderLog> queryOrderLog() {

        throw new AppErrorException("出错了111");
    }

}
