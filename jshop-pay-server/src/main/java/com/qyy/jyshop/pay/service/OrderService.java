package com.qyy.jyshop.pay.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public interface OrderService {

    
    /**
     * 订单支付
     * @author hwc
     * @created 2017年12月23日 上午10:33:03
     * @param orderId
     * @param payType
     * @param token
     */
    public Map<String,Object> doOrderPay(Long orderId,Integer payType,Integer reqType,String payPwd,String token);
    
    /**
     * 礼包订单支付
     * @author hwc
     * @created 2017年12月23日 上午10:33:03
     * @param orderId
     * @param payType
     * @param token
     */
    public Map<String,Object> doGiftOrderPay(Long giftOrderId,Integer payType,Integer reqType,String payPwd,String token);
    
    /**
     * 砍价订单支付
     * @author hwc
     * @created 2018年4月14日 下午3:20:56
     * @param orderId
     * @param payType
     * @param reqType
     * @param payPwd
     * @param token
     * @return
     */
    public Map<String,Object> doBargainOrderPay(Long orderId,Integer payType,Integer reqType,String payPwd,String token);

    /**
     * 拼团订单支付
     * @param orderId
     * @param payType
     * @param reqType
     * @param payPwd
     * @param token
     * @return
     */
    public Map<String,Object> doSpellOrderPay(Long orderId,Integer payType,Integer reqType,String payPwd,String token);

    /**
     * 订单退款
     * @param id
     * @param type
     * @param outRefundNo
     * @param refundFee
     * @param totalFee
     * @return
     */
    Map<String,String> refund(Long id, String type, String outRefundNo, Integer refundFee, Integer totalFee);
}
