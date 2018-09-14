/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.bargain.controller;

import com.qyy.jyshop.admin.bargain.service.BargainDeliveryService;
import com.qyy.jyshop.admin.bargain.service.BargainLogService;
import com.qyy.jyshop.admin.bargain.service.BargainOrderItemsService;
import com.qyy.jyshop.admin.bargain.service.BargainOrderService;
import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.delivery.service.LogiService;
import com.qyy.jyshop.admin.goods.service.GoodsService;
import com.qyy.jyshop.admin.member.service.MemberService;
import com.qyy.jyshop.admin.order.service.OrderItemsService;
import com.qyy.jyshop.dao.BargainLogDao;
import com.qyy.jyshop.model.*;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 描述
 * 砍价活动订单
 * @author wu
 * @created 2018/4/17 10:12
 */
@Controller
@RequestMapping("/admin/bargain/order")
public class BargainOrderController extends BaseController{

    @Autowired
    private BargainOrderService service;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BargainOrderItemsService itemsService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private LogiService logiService;
    @Autowired
    private BargainLogService logService;
    @Autowired
    private BargainDeliveryService deliveryService;

    @ControllerLog("砍价活动订单")
    @RequestMapping("main")
    @Authority(opCode = "110303", opName = "砍价活动订单")
    public String main(){
        return "bargain/order/main";
    }

    @ControllerLog("砍价活动订单分页")
    @Authority(opCode = "11030301", opName = "砍价活动订单分页")
    @RequestMapping("page")
    @ResponseBody
    public PageAjax<Map<String, Object>> page(PageAjax<BargainOrder> page) {
        PageAjax<Map<String, Object>> data = service.page(page);
        return data;
    }

    @ControllerLog("砍价订单详情")
    @RequestMapping("detail/{orderId}")
    @Authority(opCode = "11030302", opName = "砍价订单详情")
    public ModelAndView queryPayOrderDetail(@PathVariable("orderId")Long orderId,ModelMap model) {
        BargainOrder order = service.queryByID(orderId);
        model.put("order",order);
        if(order!=null){
            //用户信息
            Member member = memberService.preDetaileCustomer(order.getMemberId());
            model.put("member",member);
            //砍价商品
            BargainOrderItems items = itemsService.queryByOrderId(orderId);
            Long goodsId = items.getGoodsId();
            model.put("orderLogList",logService.queryByOrderId(orderId));
            Goods goods = goodsService.queryByGoodsId(goodsId);
            model.put("items",items);
            model.put("goods",goods);
            BargainDelivery delivery = deliveryService.queryByOrderId(orderId);
            if(delivery!=null){
                //物流信息
                model.put("logisticMap", logiService.queryLogiDistributionInfo(delivery.getLogiName(), delivery.getLogiCode(), delivery.getLogiNo()));
            }else{
                model.put("logisticMap", null);
            }
        }

        return new ModelAndView("bargain/order/detail",model);
    }

    @ControllerLog("砍价订单发货页面")
    @Authority(opCode = "11030303", opName = "砍价订单发货页面")
    @RequestMapping("delivery/{orderId}")
    public String preOrderDelivery(@PathVariable("orderId")Long orderId,ModelMap model) {
        model.put("logiList",logiService.queryLogi());
        model.put("order",service.queryByID(orderId));
        return "bargain/order/delivery";
    }

    @ControllerLog("砍价订单发货")
    @RequestMapping("deliverySave")
    @ResponseBody
    @Authority(opCode = "11030304", opName = "砍价订单发货")
    public AjaxResult deliverySave(@RequestParam Map<String,Object> params) {
        try{
            return AppUtil.returnObj(service.deliverySave(params));
        }catch (Exception e) {
            e.printStackTrace();
            return AppUtil.returnObj(2,e.getMessage());
        }
    }

    @ControllerLog("砍价订单作废")
    @RequestMapping("cancleOrder/{orderId}")
    @ResponseBody
    @Authority(opCode = "11030305", opName = "砍价订单作废")
    public AjaxResult cancleOrder(@PathVariable("orderId")Long orderId) {
        try{
            return AppUtil.returnObj(service.doCancleOrder(orderId));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
}
