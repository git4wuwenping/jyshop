/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.order.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.conf.ShopBaseConf;
import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.exception.AppLoginException;
import com.qyy.jyshop.model.Delivery;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.OrderParamConfig;
import com.qyy.jyshop.model.RightOrder;
import com.qyy.jyshop.order.service.LogiService;
import com.qyy.jyshop.order.service.RightService;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;

/**
 * 维权申请控制类
 * @author wu
 * @created 2018/3/19 16:47
 */
@RestController
public class RightController extends AppBaseController {

    @Autowired
    private LogiService logiService;
    @Autowired
    private RightService service;
    @Autowired
    private ShopBaseConf shopBaseConf;

    @ModelAttribute
    public void check(String token){
        if(StringUtils.isBlank(token)){
            throw new AppLoginException("token不能为空!");
        }
        service.getMember(token);
    }

    /**
     * 申请维权页面数据
     * @param orderId
     * @return
     */
//    @RequestMapping("right/input")
//    public Map<String,Object> inputItem(Long orderId){
//        Map<String, Object> map = new HashedMap();
//        Order order = orderService.queryByID(orderId);
//        OrderItems items = new OrderItems();
//        items.setOrderId(orderId);
//        List<OrderItems> itemsList = orderItemsService.queryList(items);
//        map.put("order", order);
//        map.put("itemsList", itemsList);
//        return this.outMessage(0, "维权页面",map);
//    }

    /**
     * 提交维权申请单
     * @param entity
     * @return
     */
//    @RequestMapping(value = "right/submit")
    @RequestMapping(value = "right/submit",method = {RequestMethod.POST})
    public Map<String,Object> submit(@ModelAttribute RightOrder entity,@RequestParam(value = "itemsIds") Long[] itemsIds){
        Map<String, Object> map = new HashedMap();
        RightOrder rightOrder = service.submit(entity,itemsIds);
        map.put("rightOrder",rightOrder);
        return this.outMessage(0, "提交成功", map);
    }

    /**
     * 提交进入维权详情
     * @param itemId 子订单id
     * @return
     */
//    @RequestMapping("right/detailItem")
//    public Map<String,Object> detailByItemId(Long itemId){
//        Map<String,Object> returnMap = service.queryDetailByItemId(itemId);
//        return this.outMessage(0, "获取成功", returnMap);
//    }

    /**
     * 维权详情
     * @param id 维权申请id
     * @return
     */
    @RequestMapping("right/detail")
    public Map<String,Object> detailById(Long id){
        Map<String,Object> returnMap = service.queryDetailById(id);
        return this.outMessage(0, "获取成功", returnMap);
    }

    /**
     * 取消维权订单
     * @param id 维权申请id
     * @return
     */
    @RequestMapping("right/cancel")
    public Map<String,Object> cancelById(Long id){
        Map<String, Object> map = service.cancelById(id);
        return this.outMessage(0, "取消成功", map);
    }

    /**
     * 维权订单列表
     * @param page
     * @return
     */
    @RequestMapping("right/page")
    public Map<String, Object> page(PageAjax<RightOrder> page, String token) {
        Map<String,Object> returnMap = new HashMap<String, Object>();
        
        OrderParamConfig orderParamConfig = shopBaseConf.getParamConfig(OrderParamConfig.class, "shopParam_orderParamConfig");
        BigDecimal secondsRefundAutoAgree = orderParamConfig.getDayRefundAutoAgree().multiply(new BigDecimal(3600*24));
        BigDecimal secondsBackAutoAgree = orderParamConfig.getDayBackAutoAgree().multiply(new BigDecimal(3600*24));

        PageAjax<Map<String,Object>> data = service.page(page,token,secondsRefundAutoAgree,secondsBackAutoAgree);
        List list = data.getRows();
        //替换文本
        for(Object rightOrder :list){
            RightOrder right = (RightOrder)rightOrder;
            right.setType(DictionaryUtil.getDataLabelByValue("right_type", right.getType()));
            right.setStatus(DictionaryUtil.getDataLabelByValue("order_items_status", right.getStatus()));
        }
        Map<String, Object> dataList = this.getPageMap(data);
        returnMap.put("dataList",dataList);
        
        return this.outMessage(0, "获取成功", returnMap );
    }

    /**
     * 填写退货物流
     * @param rightOrderId
     * @return
     */
    @RequestMapping("right/inputDelivery")
    public Map<String,Object> inputDelivery(Long rightOrderId){
        Map<String,Object> data = service.queryDelivery(rightOrderId);
        return this.outMessage(0, "获取成功", data);
    }

    /**
     * 提交退货物流
     * @param delivery 需要商品itemId
     * @return
     */
    @RequestMapping(value = "right/submitDelivery",method = {RequestMethod.POST})
    public Map<String,Object> submitDelivery(Delivery delivery,String token,
            @RequestParam(value = "rightOrderId")Long rightOrderId){
//        Map<String,Object> returnMap = logiService.queryLogiDistributionInfo(rightOrderId,
//                Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "right_order")),token);
//        Object obj = returnMap.get("resp");
//        if(!StringUtil.isEmpty(obj)){
//            JSONObject goodsJson= JSONObject.fromObject(obj);
//            try {
//                goodsJson.getString("returnCode");
//                //验证输入的运单号是否正确
//                return this.outMessage(1, "获取失败", null);
//            }catch(Exception e){
//            }
//        }
        delivery.setOrderId(rightOrderId);
        Member member = service.getMember(token);
        delivery.setOpId(Long.valueOf(member.getMemberId()));
        delivery.setOpUser(member.getUname());
        Map<String,Object> data = service.submitDelivery(delivery);
        return this.outMessage(0, "提交成功", data);
    }

    /**
     * 退货物流信息查询
     * @param rightOrderId
     * @param token
     * @return
     */
    @RequestMapping(value = "right/logiInfo")
    public Map<String,Object> LogisticsInfo(Long rightOrderId,String token){
        Map<String,Object> returnMap = logiService.queryLogiDistributionInfo(rightOrderId,
                Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "right_order")),token);
        Object obj = returnMap.get("resp");
        if(!StringUtil.isEmpty(obj)){
            JSONObject goodsJson= JSONObject.fromObject(obj);
            if(goodsJson.getInt("status")==200){
                returnMap.put("logiInfo", goodsJson.getJSONArray("data"));
                return this.outMessage(0, "获取成功", returnMap);
            }
        }
        return this.outMessage(1, "获取失败", null);
    }
}
