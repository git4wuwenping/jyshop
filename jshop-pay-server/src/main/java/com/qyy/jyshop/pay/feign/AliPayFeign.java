package com.qyy.jyshop.pay.feign;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "alipay-server"
)
public interface AliPayFeign {

    /**
     * 获取App支付参数
     * @author hwc
     * @created 2018年1月8日 下午3:58:04
     * @param payAmount
     * @param orderSn
     * @param createIp
     * @param token
     * @return
     */
    @RequestMapping("getAppPayInfo")
    public Map<String,Object> getAppPayInfo(@RequestParam("payAmount")BigDecimal payAmount,
            @RequestParam("orderSn")String orderSn,
            @RequestParam("orderType")Integer orderType,
            @RequestParam("createIp")String createIp,
            @RequestParam("token")String token);
    
    /**
     * 支付宝异步回调
     * @author hwc
     * @created 2018年2月7日 下午2:50:37
     * @param returnMap
     */
    @RequestMapping(value ="onAlipayReturn")
    public void onAlipayReturn(@RequestParam Map<String,Object> returnMap);
}
