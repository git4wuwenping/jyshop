package com.qyy.jyshop.pay;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 待整理_www
 */
public interface PayServiceApi {
    
    /**
     * 预存款支付
     * @author hwc
     * @created 2017年12月23日 上午11:06:30
     * @param payAmount
     * @param orderId
     * @param token
     * @return
     */
    @RequestMapping("/pay")
    public Boolean pay(@RequestParam("payAmount")BigDecimal payAmount,
            @RequestParam("orderId")Long orderId,
            @RequestParam("orderType")Integer orderType,
            @RequestParam("payPwd")String payPwd,
            @RequestParam("token")String token) ; 
    
    
    /**
     * app请求支付信息
     * @author hwc
     * @created 2018年1月11日 上午11:14:45
     * @param payAmount
     * @param orderSn
     * @param orderType
     * @param createIp
     * @param token
     * @return
     */
    @RequestMapping(value ="/getAppPayInfo")
    public Map<String,Object> getAppPayInfo(@RequestParam("payAmount")BigDecimal payAmount,
            @RequestParam("orderSn")String orderSn,
            @RequestParam("orderType")Integer orderType,
            @RequestParam("createIp")String createIp,
            @RequestParam("token")String token);
    
    /**
     * h5请求支付信息
     * @author hwc
     * @created 2018年1月22日 上午11:33:39
     * @param payAmount
     * @param orderSn
     * @param orderType
     * @param createIp
     * @param token
     */
    @RequestMapping(value ="/getH5PayInfo")
    public Map<String,Object> getH5PayInfo(@RequestParam("payAmount")BigDecimal payAmount,
            @RequestParam("orderSn")String orderSn,
            @RequestParam("orderType")Integer orderType,
            @RequestParam("createIp")String createIp,
            @RequestParam("token")String token);
    
    /**
     * 普通订单支付异步回调
     * @author hwc
     * @created 2018年1月11日 下午3:42:09
     * @param resString 回调参数
     * @param reqType 回调类型 0:H5 1:支付宝
     */
    @RequestMapping(value ="/onOrderPayReturn")
    public void onOrderPayReturn(@RequestParam("resString")String resString,
            @RequestParam("reqType")Integer reqType);

    /**
     * 支付订单支付异步回调
     * @author hwc
     * @created 2018年1月9日 上午11:43:30
     * @param resString 回调参数
     * @param reqType 回调类型 0:H5 1:支付宝
     */
    @RequestMapping(value ="/onPayReturn")
    public void onPayReturn(@RequestParam("resString")String resString,
            @RequestParam("reqType")Integer reqType);
    
    
    /**
     * 礼包订单支付异步回调
     * @author hwc
     * @created 2018年1月11日 下午3:42:09
     * @param resString 回调参数
     * @param reqType 回调类型 0:H5 1:支付宝
     */
    @RequestMapping(value ="/onGiftOrderPayReturn")
    public void onGiftOrderPayReturn(@RequestParam("resString")String resString,
            @RequestParam("reqType")Integer reqType);
    
    /**
     * 砍价订单
     * @author hwc
     * @created 2018年1月11日 下午3:42:09
     * @param resString 回调参数
     * @param reqType 回调类型 0:H5 1:支付宝
     */
    @RequestMapping(value ="/onBargainOrderReturn")
    public void onBargainOrderReturn(@RequestParam("resString")String resString,
            @RequestParam("reqType")Integer reqType);
    
    /**
     * 支付宝异步回调
     * @author hwc
     * @created 2018年2月7日 下午2:50:37
     * @param returnMap
     */
    @RequestMapping(value ="/onAlipayReturn")
    public void onAlipayReturn(@RequestParam Map<String,Object> returnMap);

    /**
     * 拼团订单支付回调
     * @param resString
     * @param reqType
     */
    @RequestMapping(value ="/onSpellOrderReturn")
    public void onSpellOrderReturn(@RequestParam("resString")String resString,
                                     @RequestParam("reqType")Integer reqType);

    /**
     * 申请退款App
     * @return
     */
    @RequestMapping(value ="/refundApp")
    Map<String, String> refundApp(@RequestParam("outRefundNo")String outRefundNo,
            @RequestParam("outTradeNo")String outTradeNo,
            @RequestParam("refundFee")Integer refundFee,
            @RequestParam("totalFee")Integer totalFee);

    /**
     * 申请退款H5
     * @return
     */
    @RequestMapping(value ="/refundH5")
    Map<String, String> refundH5(@RequestParam("outRefundNo")String outRefundNo,
            @RequestParam("outTradeNo")String outTradeNo,
            @RequestParam("refundFee")Integer refundFee,
            @RequestParam("totalFee")Integer totalFee);

    /**
     * 订单申请退款回调
     * @param resString
     * @param reqType
     */
    @RequestMapping(value ="/onRefundReturn")
    void onRefundReturn(@RequestParam("resString")String resString,
            @RequestParam("reqType")Integer reqType);
}
