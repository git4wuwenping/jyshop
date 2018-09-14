package com.qyy.jyshop.admin.order.controller;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.delivery.service.LogiService;
import com.qyy.jyshop.admin.member.service.MemberService;
import com.qyy.jyshop.admin.order.service.DeliveryService;
import com.qyy.jyshop.admin.order.service.OrderItemsService;
import com.qyy.jyshop.admin.order.service.OrderLogService;
import com.qyy.jyshop.admin.order.service.OrderService;
import com.qyy.jyshop.model.Delivery;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.ExcelData;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

import java.util.Map;

@Controller
@RequestMapping("/admin/order")
public class OrderController  extends BaseController{
    
    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemsService orderItemsService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private LogiService logiService;
    @Autowired
    private DeliveryService deliveryService;
    
    @Authority(opCode = "030101", opName = "自营订单")
    @RequestMapping("orderMain")
    public String orderMain(Map<String, Object> map) {  
        map.put("comId", 0);
        map.put("orderType", 0);
        return "order/order_main";
    }
    
    @Authority(opCode = "030102", opName = "商户订单")
    @RequestMapping("comOrderMain")  
    public String comOrderMain(Map<String, Object> map) {
        map.put("comIds", 0);
        return "order/com_order_main";
    }
    
    @Authority(opCode = "030106", opName = "海外订单")
    @RequestMapping("overseasOrderMain")  
    public String overseasOrderMain(Map<String, Object> map) {
        map.put("comId", 0);
        map.put("orderType", 1);
        return "order/overseas_order_main";
    }
    
    @ControllerLog("查询订单列表")
    @RequestMapping("pageOrderAjax")
    @ResponseBody
    @Authority(opCode = "03010101", opName = "查询订单列表")
    public PageAjax<Map<String,Object>> pageOrderAjax(PageAjax<Map<String,Object>> page) {
        return this.orderService.pageOrder(page);
    }
    
    @ControllerLog("查询订单详情")
    @RequestMapping("queryOrderDetail/{orderId}")
    @Authority(opCode = "03010102", opName = "查询订单详情")
    public ModelAndView queryPayOrderDetail(@PathVariable("orderId")Long orderId,ModelMap model) {
        Order order=this.orderService.queryOrder(orderId);
        model.put("order",order);
        if(order!=null){
            //用户信息
        	Member member = this.memberService.preDetaileCustomer(order.getMemberId());
            model.put("member",member);
            //商品列表
            model.put("goodsList",this.orderItemsService.queryByOrderId(orderId));
            //订单日志
            model.put("orderLogList",this.orderLogService.queryByOrderId(orderId));
            Delivery delivery = this.deliveryService.queryByOrderId(orderId);
            if(delivery!=null){
                //物流信息
                model.put("logisticMap", 
                        this.logiService.queryLogiDistributionInfo(delivery.getLogiName(),
                                delivery.getLogiCode(),
                                delivery.getLogiNo()));
            }else{
                model.put("logisticMap", null);
            }
            
            model.put("profitList",this.orderService.queryOrderProfit(order,member));
        }
        return new ModelAndView("order/order_detail",model);
    }
    
    @ControllerLog("发货页面")
    @Authority(opCode = "0301010206", opName = "发货页面")
    @RequestMapping("preOrderDelivery/{orderId}")
    public String preOrderDelivery(@PathVariable("orderId")Long orderId,ModelMap model) {
        model.put("logiList",logiService.queryLogi());
        model.put("order",this.orderService.queryOrder(orderId));
        return "order/order_delivery";
    }
    
    @ControllerLog("订单发货")
    @RequestMapping("orderDelivery")
    @ResponseBody
    @Authority(opCode = "0301010202", opName = "订单发货")
    public AjaxResult orderDelivery(@RequestParam Map<String,Object> params) {
        try{
            return AppUtil.returnObj(orderService.doOrderDelivery(params));
        }catch (Exception e) {
            e.printStackTrace();
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("批量发货页面")
    @Authority(opCode = "03010105", opName = "批量发货页面")
    @RequestMapping("preBatchDelivery")
    public String preBatchDelivery(@RequestParam Map<String,Object> params,ModelMap model) {
        model.put("logiList",logiService.queryLogi());
        params.put("orderStatus", DictionaryUtil.getDataValueByName("order_status", "order_allocation"));
        model.put("orderList",this.orderService.queryOrderList(params));
        return "order/batch_delivery";
    }
    
    @ControllerLog("批量发货")
    @RequestMapping("batchDelivery")
    @ResponseBody
    @Authority(opCode = "03010106", opName = "批量发货")
    public AjaxResult batchDelivery() {
        try{
            return AppUtil.returnObj(orderService.doBatchDelivery());
        }catch (Exception e) {
            e.printStackTrace();
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("订单作废")
    @RequestMapping("cancleOrder/{orderId}")
    @ResponseBody
    @Authority(opCode = "0301010205", opName = "订单作废")
    public AjaxResult cancleOrder(@PathVariable("orderId")Long orderId) {
        try{
            return AppUtil.returnObj(orderService.doCancleOrder(orderId));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }

    @ControllerLog("导出订单数据到excel")
    @RequestMapping("exportOrderToExcel")
    @Authority(opCode = "03010104", opName = "导出订单数据到excel")
    public void exportGoodsToExcel(HttpServletResponse response, @RequestParam Map<String, Object> map) throws Exception{
        ExcelData data = orderService.getExportData(map,response);
        //ExportExcelUtils.exportExcel(response,"订单数据" + DateUtil.dateToDateFullString(new Date())+ ".xlsx",data);
        logger.info("controller结束------" + TimestampUtil.getNowTime());
    }
}
