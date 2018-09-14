package com.qyy.jyshop.basics.feign.fallback;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qyy.jyshop.basics.feign.OrderLogFeign;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.OrderLog;

import feign.hystrix.FallbackFactory;

@Component
public class OrderLogFeignFallbackFactory implements FallbackFactory<OrderLogFeign>{

    private static final Logger log = LoggerFactory.getLogger(OrderLogFeignFallbackFactory.class);
    
    @Override
    public OrderLogFeign create(Throwable throwable) {
        return new OrderLogFeign() {
            @Override
            public List<OrderLog> queryOrderLogList() {
                log.error("qyy异常处理={}", throwable.getMessage());
                throw new AppErrorException("jyshop-log服务请求失败...");
//                return null;
            }
        };
    }
}
