package com.qyy.jyshop.wxpay.service;


import java.math.BigDecimal;
import java.util.Map;

public interface WxPayService {

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
    public Map<String,Object> getAppPayParams(BigDecimal payAmount,String orderSn,Integer orderType,String createIp, String token)throws Exception;
    
    /**
     * 获取H5支付请求数据
     * @author hwc
     * @created 2018年1月22日 上午11:36:33
     * @param payAmount
     * @param orderSn
     * @param orderType
     * @param createIp
     * @param token
     * @return
     * @throws Exception
     */
    public Map<String,Object> getH5PayParams(BigDecimal payAmount,String orderSn,Integer orderType,String createIp, String token);
    
    /**
     * 普通订单支付回调处理
     * @author hwc
     * @created 2018年1月11日 下午3:43:22
     * @param strXml
     * @param reqType
     */
    public void doOrderPayReturn(String strXml,Integer reqType);
    
    /**
     * 支付订单支付回调处理
     * @author hwc
     * @created 2018年1月9日 下午2:27:03
     * @param strXml
     * @param reqType
     */
    public void doPayReturn(String strXml,Integer reqType);
    
    /**
     * 礼包订单支付回调处理
     * @author hwc
     * @created 2018年1月11日 下午3:43:22
     * @param strXml
     * @param reqType
     */
    public void doGiftOrderPayReturn(String strXml,Integer reqType);
    
    /**
     * 砍价订单支付回调处理
     * @author hwc
     * @created 2018年4月18日 下午3:46:08
     * @param strXml
     * @param reqType
     */
    public void doBargainOrderReturn(String strXml,Integer reqType);

    /**
     * 拼团订单支付回调处理
     * @param strXml
     * @param reqType
     */
    public void doSpellOrderReturn(String strXml,Integer reqType);

    /**
     * 作用：申请退款App
     * 其他：需要证书
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     */
    Map<String, String> refundApp(Map<String, String> reqData)throws Exception;

    /**
     * 作用：申请退款H5
     * 其他：需要证书
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     */
    Map<String, String> refundH5(Map<String, String> reqData)throws Exception;

    /**
     * 订单申请退款回调
     * @param resString
     * @param reqType
     */
    void onRefundReturn(String resString,Integer reqType);

}
