package com.qyy.jyshop.alipay.controller;

import com.qyy.jyshop.alipay.service.AliPayService;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.pay.PayServiceApi;
import com.qyy.jyshop.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;



@RestController
public class AliPayController implements PayServiceApi{
  
    @Autowired
    private AliPayService aliPayService;
    
    @Override
    public Boolean pay(BigDecimal payAmount, Long orderId, Integer orderType,String payPwd,String token) {
        return false;
    }
    
    @Override
    public Map<String, Object> getAppPayInfo(BigDecimal payAmount,String orderSn,Integer orderType,String createIp, String token) {
        
        try{             
            String strPayData=this.aliPayService.getAppPayParams(payAmount, orderSn,orderType, createIp, token);          
            if(StringUtil.isEmpty(strPayData))
                return null;
            else{
                Map<String,Object> returnMap=new HashMap<String, Object>();
                returnMap.put("payData", strPayData);
                return returnMap;
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new AppErrorException("获取支付参数失败...");
        }
    }
    
    @Override
    public Map<String,Object> getH5PayInfo(BigDecimal payAmount,String orderSn,Integer orderType,String createIp, String token) {
        return null;
    }
    
    @Override
    public void onOrderPayReturn(String resString,Integer reqType){
        System.out.println("bbbb:"+resString);
    }
        
    @Override
    public void onPayReturn(String resString,Integer reqType){
       System.out.println("aaaa:"+resString);
    }
    
    @Override
    public void onGiftOrderPayReturn(String resString,Integer reqType){
        //resString="<xml><appid><![CDATA[wxf3c95148add7878f]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1436779802]]></mch_id><nonce_str><![CDATA[cd62792712cf456aa61ccc18602a3702]]></nonce_str><openid><![CDATA[o0nqgwJEJC6tG1gOQnME8ZnI8vfc]]></openid><out_trade_no><![CDATA[1516931200424]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[C669DCE08B571EE150FC026599C7E7D5]]></sign><time_end><![CDATA[20180126094645]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4200000076201801260972240102]]></transaction_id></xml>";
        System.out.println("cccc:"+resString);
    }

    @Override
    public void onBargainOrderReturn(String resString, Integer reqType) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSpellOrderReturn(String resString, Integer reqType) {
        // TODO Auto-generated method stub
    }

    @Override public Map<String, String> refundApp(String outRefundNo, String outTradeNo,
            Integer refundFee, Integer totalFee) {
        return null;
    }

    @Override public Map<String, String> refundH5(String outRefundNo, String outTradeNo,
            Integer refundFee, Integer totalFee) {
        return null;
    }

    @Override
    public void onRefundReturn(String resString, Integer reqType) {

    }

    @Override
    public void onAlipayReturn(@RequestParam Map<String, Object> returnMap) {
        this.aliPayService.doPayReturn(returnMap);
    }

}
