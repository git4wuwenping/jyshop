package com.qyy.jyshop.log.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.PayLogDao;
import com.qyy.jyshop.log.service.PayLogService;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.PayLog;
import com.qyy.jyshop.model.PayOrder;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;

@Service
public class PayLogServiceImpl extends AbstratService<PayLog> implements PayLogService{

    @Autowired
    private PayLogDao payLogDao;
    
    @Override
    public void doAddOrderPayLog(Order order) {
        PayLog payLog=new PayLog();
        payLog.setOrderId(order.getOrderId());
        payLog.setOrderSn(order.getOrderSn());
//        payLog.setOrderType(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "order")));
        payLog.setOrderType(0);
        payLog.setPayMoney(order.getPayMoney());
        payLog.setPayTime(order.getPaymentTime());
        payLog.setRemark("商品订单支付");
        payLog.setMemberId(order.getMemberId());
        this.payLogDao.insertSelective(payLog);  
    }

    @Override
    public void doAddPayLog(PayOrder payOrder) {
        PayLog payLog=new PayLog();
        payLog.setOrderId(payOrder.getPayId());
        payLog.setOrderSn(payOrder.getPaySn());
//        payLog.setOrderType(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "pay_order")));
        payLog.setOrderType(1);
        payLog.setPayMoney(payOrder.getPayAmount());
        payLog.setPayTime(payOrder.getPayTime());
        payLog.setRemark("支付订单支付");
        payLog.setMemberId(payOrder.getMemberId());
        this.payLogDao.insertSelective(payLog); 
    }

}
