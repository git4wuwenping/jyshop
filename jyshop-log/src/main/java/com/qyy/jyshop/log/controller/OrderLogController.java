package com.qyy.jyshop.log.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.log.service.OrderLogService;
import com.qyy.jyshop.model.OrderLog;

@RestController
public class OrderLogController {

    @Autowired
    private OrderLogService orderLogService;
    
    @RequestMapping("queryOrderLogList")
    public List<OrderLog> logTest(Map<String, Object> map) {
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
       
       return this.orderLogService.queryOrderLog();
    }
}
