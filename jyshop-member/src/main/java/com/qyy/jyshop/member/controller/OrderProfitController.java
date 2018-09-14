package com.qyy.jyshop.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.member.service.OrderProfitService;
import com.qyy.jyshop.model.Order;
@RestController
public class OrderProfitController extends AppBaseController{

	@Autowired
	private OrderProfitService orderProfitService;
	
	/**
	 * 订单支付完成调用，增加会员分润
	 */
	@RequestMapping(value = "orderPayProfit")
    public String orderPayProfit(Order order){
		String resultStr = orderProfitService.orderPayProfit(order);
        return resultStr;
    }
	
	
}
