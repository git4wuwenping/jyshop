package com.qyy.jyshop.admin.giftorder.controllelr;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.delivery.service.LogiService;
import com.qyy.jyshop.admin.giftorder.service.GiftDeliveryService;
import com.qyy.jyshop.admin.giftorder.service.GiftOrderItemsService;
import com.qyy.jyshop.admin.giftorder.service.GiftOrderLogService;
import com.qyy.jyshop.admin.giftorder.service.GiftOrderService;
import com.qyy.jyshop.admin.member.service.MemberService;
import com.qyy.jyshop.model.GiftDelivery;
import com.qyy.jyshop.model.GiftOrder;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/giftorder")
public class GiftOrderController  extends BaseController{
    
    @Autowired
    private MemberService memberService;
    @Autowired
    private GiftOrderService giftOrderService;
    @Autowired
    private GiftOrderItemsService giftOrderItemsService;
    @Autowired
    private GiftOrderLogService giftOrderLogService;
    @Autowired
    private LogiService logiService;
    @Autowired
    private GiftDeliveryService giftDeliveryService;
    
    @Authority(opCode = "030105", opName = "礼包订单")
    @RequestMapping("orderMain")
    public String orderMain(Map<String, Object> map) { 
        return "giftorder/order_main";
    }
    

    @ControllerLog("查询礼包订单列表")
    @RequestMapping("pageOrderAjax")
    @ResponseBody
    @Authority(opCode = "03010501", opName = "查询礼包订单列表")
    public PageAjax<Map<String,Object>> pageOrderAjax(PageAjax<Map<String,Object>> page) {
        return giftOrderService.pageOrder(page);
    }
    
    @ControllerLog("礼包订单详情")
    @RequestMapping("queryOrderDetail/{orderId}")
    @Authority(opCode = "03010502", opName = "礼包订单详情")
    public ModelAndView queryPayOrderDetail(@PathVariable("orderId")Long orderId,ModelMap model) {
        GiftOrder order=this.giftOrderService.queryOrder(orderId);
        model.put("order",order);
        if(order!=null){
            //用户信息
        	Member member = this.memberService.preDetaileCustomer(order.getMemberId());
            model.put("member",member);
            //商品列表
            model.put("goodsList",this.giftOrderItemsService.queryByOrderId(orderId));
            //订单日志
            model.put("orderLogList",this.giftOrderLogService.queryByOrderId(orderId));
            //物流信息
            GiftDelivery giftDelivery = this.giftDeliveryService.queryByOrderId(orderId);
            if(giftDelivery!=null){
                //物流信息
                model.put("logisticMap", 
                        this.logiService.queryLogiDistributionInfo(giftDelivery.getLogiName(),
                                giftDelivery.getLogiCode(),
                                giftDelivery.getLogiNo()));
            }else{
                model.put("logisticMap", null);
            }
            model.put("profitList",this.giftOrderService.queryGiftOrderProfit(order,member));
        }
        return new ModelAndView("giftorder/order_detail",model);
    }
    
    @ControllerLog("发货页面")
    @Authority(opCode = "0301050203", opName = "发货页面")
    @RequestMapping("preOrderDelivery/{orderId}")
    public String preOrderDelivery(@PathVariable("orderId")Long orderId,ModelMap model) {
        model.put("logiList",logiService.queryLogi());
        model.put("order",this.giftOrderService.queryOrder(orderId));
        return "giftorder/order_delivery";
    }
    
    @ControllerLog("订单发货")
    @RequestMapping("orderDelivery")
    @ResponseBody
    @Authority(opCode = "0301050201", opName = "订单发货")
    public AjaxResult orderDelivery(@RequestParam Map<String,Object> params) {
        try{
            return AppUtil.returnObj(giftOrderService.doOrderDelivery(params));
        }catch (Exception e) {
            e.printStackTrace();
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("订单作废")
    @RequestMapping("cancleOrder/{orderId}")
    @ResponseBody
    @Authority(opCode = "0301050202", opName = "订单作废")
    public AjaxResult cancleOrder(@PathVariable("orderId")Long orderId) {
        try{
            return AppUtil.returnObj(giftOrderService.doCancleOrder(orderId));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
}
