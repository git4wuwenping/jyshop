package com.qyy.jyshop.basics.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qyy.jyshop.basics.feign.fallback.OrderLogFeignFallbackFactory;
import com.qyy.jyshop.basics.feign.fallback.OrderLogFallback;
import com.qyy.jyshop.model.OrderLog;


@FeignClient(name = "jyshop-log",
//fallback = OrderLogFallback.class,
fallbackFactory  = OrderLogFeignFallbackFactory.class 
//primary = false
)
public interface OrderLogFeign {

    @RequestMapping(value = "queryOrderLogList")
    public List<OrderLog> queryOrderLogList();
}
