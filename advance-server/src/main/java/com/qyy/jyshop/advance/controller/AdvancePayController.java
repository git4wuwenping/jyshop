package com.qyy.jyshop.advance.controller;

import com.qyy.jyshop.advance.service.MemberAdvanceService;
import com.qyy.jyshop.pay.PayServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AdvancePayController implements PayServiceApi{
    
    @Autowired
    private MemberAdvanceService memberAdvanceService;
    
    @Override
    public Boolean pay(BigDecimal payAmount, Long orderId,Integer orderType,String payPwd,String token) {  
        return this.memberAdvanceService.doMemberAdvancePay(payAmount, orderId, orderType,payPwd,token);
    }

    @Override
    public Map<String, Object> getAppPayInfo(BigDecimal payAmount, String orderSn, Integer orderType, String createIp,
            String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getH5PayInfo(BigDecimal payAmount, String orderSn, Integer orderType, String createIp,
            String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onOrderPayReturn(String resString, Integer reqType) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPayReturn(String resString, Integer reqType) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onGiftOrderPayReturn(String resString, Integer reqType) {
        // TODO Auto-generated method stub
        
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
    public void onAlipayReturn(Map<String, Object> returnMap) {
        // TODO Auto-generated method stub
        
    }

}
