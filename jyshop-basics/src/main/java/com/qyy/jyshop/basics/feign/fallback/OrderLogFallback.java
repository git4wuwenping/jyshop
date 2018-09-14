package com.qyy.jyshop.basics.feign.fallback;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.qyy.jyshop.basics.feign.OrderLogFeign;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.OrderLog;
import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
@Component
public class OrderLogFallback implements OrderLogFeign{

    @Override
    public List<OrderLog> queryOrderLogList() {
//        throw new AppErrorException("aaaa");
        return new ArrayList<OrderLog>();
    }

}
