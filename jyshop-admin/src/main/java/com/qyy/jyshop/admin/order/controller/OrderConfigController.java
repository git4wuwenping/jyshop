package com.qyy.jyshop.admin.order.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.order.service.OrderAutocommentConfigService;
import com.qyy.jyshop.admin.order.service.OrderParamConfigService;
import com.qyy.jyshop.model.OrderAutocommentConfig;
import com.qyy.jyshop.model.OrderParamConfig;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/orderConfig")
public class OrderConfigController  extends BaseController{
    
	@Autowired
	private OrderAutocommentConfigService orderAutocommentService;
	@Autowired
	private OrderParamConfigService orderParamConfigService;
      
    @Authority(opCode = "030201", opName = "订单设置")
    @RequestMapping("orderConfigMain")
    public String orderConfigMain(Map<String, Object> map) {  
        map.put("config", orderParamConfigService.queryOrderParamConfig());
        return "order/config/order_config_main";
    }
    
    @ControllerLog("修改订单设置")
    @RequestMapping("editOrderConfig")
    @ResponseBody
    @Authority(opCode = "03020101", opName = "修改订单设置")
    public AjaxResult editOrderConfig(OrderParamConfig config) {
        try{
            return AppUtil.returnObj(orderParamConfigService.edit(config));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("查询自动评论列表")
    @RequestMapping("pageContentAjax")
    @ResponseBody
    @Authority(opCode = "03020102", opName = "查询自动评论列表")
    public PageAjax<OrderAutocommentConfig> pageContentAjax(PageAjax<OrderAutocommentConfig> page) {
        return orderAutocommentService.pageConfig(page);
    }
    
    @ControllerLog("删除自动评论")
    @RequestMapping("delContent/{id}")
    @ResponseBody
    @Authority(opCode = "03020103", opName = "删除自动评论")
    public AjaxResult delContent(@PathVariable Long id) {
        try{
            return AppUtil.returnObj(orderAutocommentService.deleteById(id));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    
    @Authority(opCode = "03020104", opName = "新增自动评论页面")
    @RequestMapping("preAddContent")
    public String preAddContent() {  
        return "order/config/add_content";
    }
    
    @ControllerLog("新增自动评论")
    @RequestMapping("addContent")
    @ResponseBody
    @Authority(opCode = "03020105", opName = "新增自动评论")
    public AjaxResult addContent(OrderAutocommentConfig config) {
        try{
            return AppUtil.returnObj(orderAutocommentService.add(config));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @Authority(opCode = "03020106", opName = "编辑自动评论页面")
    @RequestMapping("preEditContent/{id}")
    public String preEditContent(@PathVariable Long id,Map<String,Object> map) {
    	map.put("comment", orderAutocommentService.queryConfigById(id));
        return "order/config/edit_content";
    }
    
    @ControllerLog("编辑保存自动评论")
    @RequestMapping("editContent")
    @ResponseBody
    @Authority(opCode = "03020107", opName = "编辑保存自动评论")
    public AjaxResult editContent(OrderAutocommentConfig config) {
        try{
            return AppUtil.returnObj(orderAutocommentService.edit(config));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
}
