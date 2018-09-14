package com.qyy.jyshop.wxpay.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.PayOrder;

@FeignClient(name = "jyshop-log")
public interface PayLogFeign {
   
    
    @RequestMapping("addOrderPayLog")
    public void addOrderPayLog(Order order); 
    
    @RequestMapping("addPayLog")
    public void addPayLog(PayOrder payOrder);
}
