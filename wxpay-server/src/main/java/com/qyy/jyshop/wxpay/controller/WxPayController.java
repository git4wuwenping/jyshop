package com.qyy.jyshop.wxpay.controller;

import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.RightOrder;
import com.qyy.jyshop.pay.PayServiceApi;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.util.XmlUtil;
import com.qyy.jyshop.wxpay.service.WxPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;



@RestController
public class WxPayController implements PayServiceApi{
  
    @Autowired
    private WxPayService wxPayService;
    
    @Override
    public Boolean pay(BigDecimal payAmount, Long orderId, Integer orderType,String payPwd, String token) {
        return false;
    }
    
    @Override
    public Map<String, Object> getAppPayInfo(BigDecimal payAmount,String orderSn,Integer orderType,String createIp, String token) {
        
        try{             
            return this.wxPayService.getAppPayParams(payAmount, orderSn,orderType, createIp, token);          
        }catch(Exception e){
            e.printStackTrace();
            throw new AppErrorException("获取支付参数失败...");
        }
    }
    
    @Override
    public Map<String,Object> getH5PayInfo(BigDecimal payAmount,String orderSn,Integer orderType,String createIp, String token) {
        
        try{          
            return  this.wxPayService.getH5PayParams(payAmount, orderSn,orderType, createIp, token);
        }catch(Exception e){
            e.printStackTrace();
            throw new AppErrorException("获取支付参数失败...");
        }
    }
    
    @Override
    public void onOrderPayReturn(String resString,Integer reqType){
        //resString="<xml><appid><![CDATA[wxf3c95148add7878f]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1436779802]]></mch_id><nonce_str><![CDATA[cd62792712cf456aa61ccc18602a3702]]></nonce_str><openid><![CDATA[o0nqgwJEJC6tG1gOQnME8ZnI8vfc]]></openid><out_trade_no><![CDATA[1516931200424]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[C669DCE08B571EE150FC026599C7E7D5]]></sign><time_end><![CDATA[20180126094645]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4200000076201801260972240102]]></transaction_id></xml>";
        System.out.println("bbbb:"+resString);
        this.wxPayService.doOrderPayReturn(resString,reqType);
    }
        
    @Override
    public void onPayReturn(String resString,Integer reqType){
        
        //resString="<xml><appid><![CDATA[wx7ed0b984275c0214]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1480304862]]></mch_id><nonce_str><![CDATA[b1b001f0307348b8b9d414617c2558f3]]></nonce_str><openid><![CDATA[ocQ5KwSPC_WtMPRF7LFbvwk-BIkI]]></openid><out_trade_no><![CDATA[1516955446349]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[D119351B54A7F377315C3BD5EC40A734]]></sign><time_end><![CDATA[20180126163051]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[APP]]></trade_type><transaction_id><![CDATA[4200000059201801261199518442]]></transaction_id></xml>";
        System.out.println("aaaa:"+resString);
        this.wxPayService.doPayReturn(resString,reqType);
    }
    
    @Override
    public void onGiftOrderPayReturn(String resString,Integer reqType){
        //resString="<xml><appid><![CDATA[wxf3c95148add7878f]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1436779802]]></mch_id><nonce_str><![CDATA[cd62792712cf456aa61ccc18602a3702]]></nonce_str><openid><![CDATA[o0nqgwJEJC6tG1gOQnME8ZnI8vfc]]></openid><out_trade_no><![CDATA[1516931200424]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[C669DCE08B571EE150FC026599C7E7D5]]></sign><time_end><![CDATA[20180126094645]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4200000076201801260972240102]]></transaction_id></xml>";
        System.out.println("cccc:"+resString);
        this.wxPayService.doGiftOrderPayReturn(resString,reqType);
    }

    @Override
    public void onBargainOrderReturn(String resString,Integer reqType){
        //resString="<xml><appid><![CDATA[wxf3c95148add7878f]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1436779802]]></mch_id><nonce_str><![CDATA[cd62792712cf456aa61ccc18602a3702]]></nonce_str><openid><![CDATA[o0nqgwJEJC6tG1gOQnME8ZnI8vfc]]></openid><out_trade_no><![CDATA[1516931200424]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[C669DCE08B571EE150FC026599C7E7D5]]></sign><time_end><![CDATA[20180126094645]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4200000076201801260972240102]]></transaction_id></xml>";
        System.out.println("dddd:"+resString);
        this.wxPayService.doBargainOrderReturn(resString,reqType);
    }

    @Override
    public void onSpellOrderReturn(String resString,Integer reqType){
        //resString="<xml><appid><![CDATA[wxf3c95148add7878f]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1436779802]]></mch_id><nonce_str><![CDATA[cd62792712cf456aa61ccc18602a3702]]></nonce_str><openid><![CDATA[o0nqgwJEJC6tG1gOQnME8ZnI8vfc]]></openid><out_trade_no><![CDATA[1516931200424]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[C669DCE08B571EE150FC026599C7E7D5]]></sign><time_end><![CDATA[20180126094645]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4200000076201801260972240102]]></transaction_id></xml>";
        System.out.println("eeee:"+resString);
        this.wxPayService.doSpellOrderReturn(resString,reqType);
    }

    @Override
    public Map<String, String> refundApp(String outRefundNo, String outTradeNo,
            Integer refundFee, Integer totalFee) {
        try {
            HashMap<String, String> data = new HashMap<>();
            data.put("out_refund_no",outRefundNo);
            data.put("out_trade_no",outTradeNo);
            data.put("refund_fee",refundFee.toString());
            data.put("total_fee",totalFee.toString());
            return wxPayService.refundApp(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> refundH5(String outRefundNo,String outTradeNo,
            Integer refundFee, Integer totalFee) {
        try {
            HashMap<String, String> data = new HashMap<>();
            data.put("out_refund_no",outRefundNo);
            data.put("out_trade_no",outTradeNo);
            data.put("refund_fee",refundFee.toString());
            data.put("total_fee",totalFee.toString());
            return wxPayService.refundH5(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRefundReturn(String resString,Integer reqType){
//        resString="<xml> <return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg> <appid><![CDATA[wx2421b1c4370ec43b]]></appid> <mch_id><![CDATA[10000100]]></mch_id> <nonce_str><![CDATA[NfsMFbUFpdbEhPXP]]></nonce_str> <sign><![CDATA[B7274EB9F8925EB93100DD2085FA56C0]]></sign> <result_code><![CDATA[SUCCESS]]></result_code> <transaction_id><![CDATA[1008450740201411110005820873]]></transaction_id> <out_trade_no><![CDATA[1415757673]]></out_trade_no> <out_refund_no><![CDATA[2018042300001]]></out_refund_no> <refund_id><![CDATA[2008450740201411110000174436]]></refund_id> <refund_channel><![CDATA[]]></refund_channel> <refund_fee>1</refund_fee> </xml>";
        System.out.println("ffff:"+resString);
        wxPayService.onRefundReturn(resString,reqType);
    }

    @Override
    public void onAlipayReturn(Map<String, Object> returnMap) {
        // TODO Auto-generated method stub
        
    }



}
