package com.qyy.jyshop.seller.order.controller;

import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.ExcelData;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.seller.common.annotation.Authority;
import com.qyy.jyshop.seller.common.annotation.ControllerLog;
import com.qyy.jyshop.seller.common.controller.BaseController;
import com.qyy.jyshop.seller.delivery.service.LogiService;
import com.qyy.jyshop.seller.member.service.MemberService;
import com.qyy.jyshop.seller.order.service.OrderItemsService;
import com.qyy.jyshop.seller.order.service.OrderLogService;
import com.qyy.jyshop.seller.order.service.OrderService;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.util.DateUtil;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.ExportExcelUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

import java.util.Date;
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
    
    
    @Authority(opCode = "030102", opName = "商户订单")
    @RequestMapping("comOrderMain")  
    public String comOrderMain(Map<String, Object> map) {
        map.put("comIds", 0);
        return "order/com_order_main";
    }

    @ControllerLog("查询订单列表")
    @RequestMapping("pageOrderAjax")
    @ResponseBody
    @Authority(opCode = "03010101", opName = "查询订单列表")
    public PageAjax<Map<String,Object>> pageOrderAjax(PageAjax<Map<String,Object>> page) {
        return orderService.pageOrder(page);
    }
    
    @ControllerLog("查询订单详情")
    @RequestMapping("queryOrderDetail/{orderId}")
    @Authority(opCode = "03010102", opName = "查询订单详情")
    public ModelAndView queryPayOrderDetail(@PathVariable("orderId")Long orderId,ModelMap model) {
        Order order=this.orderService.queryOrder(orderId);
        model.put("order",order);
        if(order!=null){
            //用户信息
            model.put("member",this.memberService.queryByMemberId(order.getMemberId()));
            //商品列表
            model.put("goodsList",this.orderItemsService.queryByOrderId(orderId));
            //订单日志
            model.put("orderLogList",this.orderLogService.queryByOrderId(orderId));
            //物流信息
            model.put("logisticMap", this.logiService.queryLogiDistributionInfo(orderId,
                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "order"))));
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
        ExcelData data = orderService.getExportData(map);
        ExportExcelUtils.exportExcel(response,"订单数据" + DateUtil.dateToDateFullString(new Date())+ ".xlsx",data);
    }
}
