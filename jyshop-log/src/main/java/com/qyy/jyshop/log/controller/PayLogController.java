package com.qyy.jyshop.log.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.log.service.PayLogService;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.PayOrder;

@RestController
public class PayLogController {

    @Autowired
    private PayLogService payLogService;
    
    @RequestMapping("addOrderPayLog")
    public void addOrderPayLog(@RequestBody Order order) {
        this.payLogService.doAddOrderPayLog(order);
    }    
    
    @RequestMapping("addPayLog")
    public void addPayLog(@RequestBody PayOrder payOrder) {
        this.payLogService.doAddPayLog(payOrder);
    }
}
