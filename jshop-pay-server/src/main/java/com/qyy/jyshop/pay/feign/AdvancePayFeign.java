package com.qyy.jyshop.pay.feign;

import java.math.BigDecimal;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "advancePay-server"
//fallback = AdvancePayFallback.class
)
public interface AdvancePayFeign {

    @RequestMapping("pay")
    public Boolean pay(@RequestParam("payAmount")BigDecimal payAmount,
            @RequestParam("orderId")Long orderId,
            @RequestParam("orderType")Integer orderType,
            @RequestParam("payPwd")String payPwd,
            @RequestParam("token")String token) ; 
}
