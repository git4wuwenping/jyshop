package com.qyy.jyshop.basics.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.basics.feign.OrderLogFeign;
import com.qyy.jyshop.basics.service.TestService;
import com.qyy.jyshop.dao.TestDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Test;

@Service
public class TestServiceImpl implements TestService{

    @Autowired
    private TestDao testDao;
    @Autowired
    private OrderLogFeign orderLogFeign;
    
    @Override
    @Transactional
    public void Test() {
        System.out.println("aa:"+orderLogFeign.queryOrderLogList());
//        throw new AppErrorException("jyshop-log服务请求失败...");
//        Test test=new Test();
//        test.setValue("123456");
//        testDao.insert(test);
//        
//        throw new AppErrorException("出错了");
    }
    
    
}
