package com.qyy.jyshop.pay.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@FeignClient(name = "wxpay-server"
)
public interface WxPayFeign {

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
     * 获取H5支付参数
     * @author hwc
     * @created 2018年1月22日 下午4:32:05
     * @param payAmount
     * @param orderSn
     * @param orderType
     * @param createIp
     * @param token
     * @return
     */
    @RequestMapping("getH5PayInfo")
    public Map<String,Object> getH5PayInfo(@RequestParam("payAmount")BigDecimal payAmount,
            @RequestParam("orderSn")String orderSn,
            @RequestParam("orderType")Integer orderType,
            @RequestParam("createIp")String createIp,
            @RequestParam("token")String token);
    
    /**
     * 普通订单支付回调
     * @author hwc
     * @created 2018年1月9日 下午7:24:30
     * @param resString
     */
    @RequestMapping(value ="/onOrderPayReturn")
    public void onOrderPayReturn(@RequestParam("resString")String resString,
            @RequestParam("reqType")Integer reqType);
    
    /**
     * 支付订单支付回调
     * @author hwc
     * @created 2018年1月9日 下午7:24:30
     * @param resString
     */
    @RequestMapping(value ="/onPayReturn")
    public void onPayReturn(@RequestParam("resString")String resString,
            @RequestParam("reqType")Integer reqType);
    
    /**
     * 礼包订单支付回调
     * @author hwc
     * @created 2018年1月9日 下午7:24:30
     * @param resString
     */
    @RequestMapping(value ="/onGiftOrderPayReturn")
    public void onGiftOrderPayReturn(@RequestParam("resString")String resString,
            @RequestParam("reqType")Integer reqType);
    
    /**
     * 砍价订单
     * @author hwc
     * @created 2018年4月18日 下午3:43:05
     * @param resString
     * @param reqType
     */
    @RequestMapping(value ="/onBargainOrderReturn")
    public void onBargainOrderReturn(@RequestParam("resString")String resString,
            @RequestParam("reqType")Integer reqType);

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
