/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.bargain.controller;

import com.qyy.jyshop.admin.bargain.service.BargainOrderItemsService;
import com.qyy.jyshop.admin.bargain.service.BargainOrderService;
import com.qyy.jyshop.admin.bargain.service.impl.BargainOrderItemsServiceImpl;
import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.model.BargainOrder;
import com.qyy.jyshop.model.BargainOrderItems;
import com.qyy.jyshop.model.BargainRecomGoods;
import com.qyy.jyshop.pojo.DataTablePage;
import com.qyy.jyshop.pojo.PageAjax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 描述
 * 砍价记录控制类
 * @author wu
 * @created 2018/4/16 15:07
 */
@Controller
@RequestMapping("/admin/bargain/record")
public class BargainOrderItemsController extends BaseController{

    @Autowired
    private BargainOrderItemsService service;
    @Autowired
    private BargainOrderService bargainOrderService;

    @ControllerLog("砍价发起记录")
    @RequestMapping("main")
    @Authority(opCode = "11030107", opName = "砍价发起记录")
    public String bargainCreate(Long bargainId, Map<String, Object> map){
        map.put("bargainId",bargainId);
        return "bargain/record/main";
    }

    @ControllerLog("指定砍价活动分页")
    @Authority(opCode = "11030108", opName = "指定砍价活动分页")
    @RequestMapping("page")
    @ResponseBody
    public PageAjax<Map<String, Object>> page(PageAjax<BargainOrderItems> page) {
        PageAjax<Map<String, Object>> data = service.page(page);
        return data;
    }

    @ControllerLog("砍价记录详情")
    @RequestMapping("join/{id}")
    @Authority(opCode = "11030109", opName = "砍价记录详情")
    public String join(@PathVariable("id") Long orderId, String status, Map<String, Object> map){
        BargainOrderItems items = service.queryByOrderId(orderId);
        BargainOrder bargainOrder = bargainOrderService.queryByID(orderId);
        Integer sts = bargainOrder.getOrderStatus();
        boolean flag = true;
        BigDecimal a = items.getBargainPrices().add(items.getTargetPrice());
        BigDecimal b = items.getGoodsPrice().subtract(a);
        if(b.compareTo(a)==0){
            flag = false;
        }
        //达到标靶价格也算结束
        map.put("status",status);
        map.put("items",items);
        long taskDate = items.getTakeDate().getTime();
        long now = new Date().getTime();
        long time = taskDate - now;
        if(time>0&&sts==0&&flag){
            map.put("time",time/1000);
        }else {
            map.put("time",0L);
        }
        return "bargain/record/select";
    }

    @ControllerLog("获取砍价详情数据")
    @RequestMapping("detail")
    @ResponseBody
    @Authority(opCode = "11030110", opName = "获取砍价详情数据")
    public DataTablePage<Map<String,Object>> pageDataTableGoods(@RequestParam Map<String, Object> map) {
        DataTablePage<Map<String,Object>> page = service.pageDataTable(map);
        return page;
    }
}
