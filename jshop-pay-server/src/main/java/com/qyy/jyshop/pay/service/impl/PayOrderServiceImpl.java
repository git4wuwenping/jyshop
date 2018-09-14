package com.qyy.jyshop.pay.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.dao.PayOrderDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.PayOrder;
import com.qyy.jyshop.pay.feign.AliPayFeign;
import com.qyy.jyshop.pay.feign.WxPayFeign;
import com.qyy.jyshop.pay.service.PayOrderService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;

@Service
public class PayOrderServiceImpl extends AbstratService<PayOrder> implements PayOrderService{

    @Autowired
    private WxPayFeign wxPayFeign;
    @Autowired
    private AliPayFeign aliPayFeign;
    @Autowired
    private PayOrderDao payOrderDao;
    
    @Override
    @Transactional
    public Map<String,Object> doPayRecharge(BigDecimal orderAmount, Integer payType, Integer reqType, String token) {
        
        synchronized (token) {
            if(StringUtil.isEmpty(orderAmount))
                throw new AppErrorException("支付金额不能为空...");
            if(StringUtil.isEmpty(reqType))
                throw new AppErrorException("请求类型...");
            if(orderAmount.compareTo(new BigDecimal(10))<0)
                throw new AppErrorException("充值金额必须大于10元...");
            
            orderAmount=orderAmount.setScale(0, BigDecimal.ROUND_DOWN);
            Long nowDate=System.currentTimeMillis();
            PayOrder payOrder=new PayOrder();
            payOrder.setPaySn(String.valueOf(nowDate));
            payOrder.setMemberId(this.getMemberId(token));
            payOrder.setOrderAmount(orderAmount);
            payOrder.setCreateTime(new Timestamp(nowDate));
            payOrder.setType(0);
            
            int addCount=this.payOrderDao.insertSelective(payOrder);
            
            if(addCount==1){
                
                //IOS APP 支付
                if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                    
                    //微信支付
                    if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay")))){
                        return this.wxPayFeign.getAppPayInfo(orderAmount, payOrder.getPaySn(),
                                Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "pay_order")),
                                null, 
                                token);
                        
                    }else if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "alipay")))){

                        return this.aliPayFeign.getAppPayInfo(payOrder.getOrderAmount(), payOrder.getPaySn(),
                                Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "pay_order")),
                                null, 
                                token);
                    }else{
                        throw new AppErrorException("错误的支付类型...");
                    }
                //H5 微信支付
                }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                    return this.wxPayFeign.getH5PayInfo(orderAmount, payOrder.getPaySn(),
                            Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "pay_order")),
                            null, 
                            token);
                }else{
                    throw new AppErrorException("错误的请求类型...");
                }
            }else{
                throw new AppErrorException("创建支付订单失败...");
            }
        }

    }
}
