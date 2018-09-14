/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.right.controller;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.delivery.service.LogiService;
import com.qyy.jyshop.admin.right.service.RightService;
import com.qyy.jyshop.model.Delivery;
import com.qyy.jyshop.model.RightOrder;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.EntityReflectUtil;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.util.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 描述
 *
 * @author wu
 * @created 2018/3/29 11:28
 */
@Controller
@RequestMapping({"/admin/right"})
public class RightController extends BaseController {

    @Autowired
    private RightService service;
    @Autowired
    private LogiService logiService;

    @RequestMapping("main")
    @Authority(opCode = "030401", opName = "维权订单")
    public String main()
    {
        return "right/main";
    }

    @ControllerLog("查询维权单列表")
    @RequestMapping("page")
    @ResponseBody
    @Authority(opCode = "03040101", opName = "查询维权列表")
    public PageAjax<Map<String, Object>> page(PageAjax<RightOrder> page)
    {
        PageAjax<Map<String, Object>> data = service.pageList(page);
        List list = data.getRows();
        //替换文本
        for(Object rightOrder :list){
            RightOrder right = (RightOrder)rightOrder;
            right.setType(DictionaryUtil.getDataLabelByValue("right_type", right.getType()));
            right.setSellerStatus(DictionaryUtil.getDataLabelByValue("order_items_status", right.getSellerStatus()));
        }
        return data;
    }

    @ControllerLog("维权订单详情")
    @RequestMapping("detail/{id}")
    @Authority(opCode = "03040102", opName = "维权订单详情")
    public String edit(@PathVariable("id") Long id, Map<String, Object> map)
    {
        Map<String, Object> dataMap = service.queryDetailById(id);
        Delivery delivery = (Delivery) dataMap.get("delivery");
        if(delivery!=null){
            //物流信息
            map.put("logisticMap", logiService.queryLogiDistributionInfo(delivery.getLogiName(), delivery.getLogiCode(), delivery.getLogiNo()));
        }else{
            map.put("logisticMap", null);
        }
        map.putAll(dataMap);
        return "right/tab";
    }

    @ControllerLog("审核维权订单详情")
    @RequestMapping("shenhe/{id}")
    @Authority(opCode = "03040103", opName = "审核维权订单详情")
    public String shenhe(@PathVariable("id") Long id, Map<String, Object> map)
    {
        Map<String, Object> dataMap = service.queryDetailById(id);
        map.putAll(dataMap);
        return "right/shenhe";
    }

    @ControllerLog("确认收货")
    @RequestMapping("shouhuo/{id}")
    @ResponseBody
    public AjaxResult shouhuo(@PathVariable("id") Long id)
    {
        String msg = service.revGoods(id);
        return AppUtil.returnObj(msg);
    }

    @ControllerLog("确认退款")
    @RequestMapping("tuikuan/{id}")
    @ResponseBody
    public AjaxResult tuikuan(@PathVariable("id") Long id) {
        String msg = service.back(id);
        return AppUtil.returnObj(msg);
    }

    @ControllerLog("维权订单详情处理")
    @RequestMapping(value = {"deal/{id}"},method = {RequestMethod.POST})
    @Authority(opCode = "03040104", opName = "维权订单详情处理")
    @ResponseBody
    public AjaxResult deal(@PathVariable("id") Long id, boolean agree, String rejectReason)
    {
        String msg = service.deal(id, agree, rejectReason);
        return AppUtil.returnObj(msg);
    }
}