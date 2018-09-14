package com.qyy.jyshop.pay.service;

import java.math.BigDecimal;
import java.util.Map;

public interface PayOrderService {

    /**
     * 订单充值
     * @author hwc
     * @created 2018年1月11日 上午10:15:32
     * @param orderAmount
     * @param payType
     * @param reqType
     * @param token
     */
    public Map<String,Object> doPayRecharge(BigDecimal orderAmount,Integer payType,Integer reqType,String token);
}
