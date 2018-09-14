package com.qyy.jyshop.alipay.service;

import java.math.BigDecimal;
import java.util.Map;

public interface AliPayService {

    /**
     * 获取APP支付请求数据
     * @author hwc
     * @created 2018年1月6日 下午5:21:50
     * @param payAmount
     * @param orderSn
     * @param createIp
     * @param token
     * @return
     */
    public String getAppPayParams(BigDecimal payAmount,String orderSn,Integer orderType,String createIp, String token)throws Exception;
    
    /**
     * 支付回调
     * @author hwc
     * @created 2018年2月7日 下午2:30:03
     * @param returnMap
     */
    public void doPayReturn(Map<String,Object> returnMap);
}
