package com.qyy.jyshop.seller.statistics.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.seller.common.annotation.Authority;
import com.qyy.jyshop.seller.common.annotation.ControllerLog;
import com.qyy.jyshop.seller.common.controller.BaseController;
import com.qyy.jyshop.seller.statistics.service.StatisticsService;

@Controller
@RequestMapping("/admin/statistics")
public class StatisticsController extends BaseController{
	@Autowired
	private StatisticsService statisticsServiceImpl;

	@Authority(opCode = "010101", opName = "待发货订单主页")
    @RequestMapping("consignmentOrdersMain")
    public String consignmentOrdersMain(Map<String, Object> map) {    
        return "statistics/consignmentOrders_main";
    }
    
    @ControllerLog("查询待发货订单")
    @RequestMapping("consignmentOrders")
    @ResponseBody
    @Authority(opCode = "010102", opName = "查询待发货订单")
    public PageAjax<Map<String,Object>> consignmentOrders(PageAjax<Map<String,Object>> page) {
        return statisticsServiceImpl.consignmentOrders(page);
    }
    
	@Authority(opCode = "010201", opName = "昨日订单主页")
    @RequestMapping("yesterdayOrdersMain")
    public String yesterdayOrdersMain(Map<String, Object> map) {    
        return "statistics/yesterdayOrders_main";
    }
    
	@ControllerLog("查询昨日订单")
    @RequestMapping("yesterdayOrders")
    @ResponseBody
    @Authority(opCode = "010202", opName = "查询昨日订单")
    public PageAjax<Map<String,Object>> yesterdayOrders(PageAjax<Map<String,Object>> page) {
        return statisticsServiceImpl.yesterdayOrders(page);
    }
}
