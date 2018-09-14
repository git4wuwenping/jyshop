package com.qyy.jyshop.admin.delivery.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.delivery.service.LogiService;
import com.qyy.jyshop.admin.order.service.OrderService;
import com.qyy.jyshop.model.Logi;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/logi")
public class LogiController extends BaseController{

    @Autowired
    private LogiService logiService;
    @Autowired
    private OrderService orderService;
    
    @Authority(opCode = "070101", opName = "物流公司")
    @RequestMapping("logiMain")
    public String logiMain(){
        this.orderService.doAutomaticReceipt();
        this.orderService.doAutomaticComplete();
        return "logi/logi_main";
    }

    @ControllerLog("查询物流公司列表")
    @RequestMapping("pageLogiAjax")
    @ResponseBody
    @Authority(opCode = "07010101", opName = "查询物流公司列表")
    public PageAjax<Logi> pageLogiAjax(PageAjax<Logi> page, Logi Logi) {
        return logiService.pageLogi(page, Logi);
    }
    
    @ControllerLog("添加物流公司页面")
    @Authority(opCode = "07010102", opName = "添加物流公司页面")
    @RequestMapping("preAddLogi")
    public String preAddLogi() {
        return "logi/add_logi";
    }

    @ControllerLog("添加物流公司")
    @RequestMapping("addLogi")
    @ResponseBody
    @Authority(opCode = "07010103", opName = "添加物流公司")
    public AjaxResult addLogi(Logi logi) {
        try{
            return AppUtil.returnObj(logiService.doAddLogi(logi));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }

    @ControllerLog("更新物流公司页面")
    @Authority(opCode = "07010102", opName = "查询物流公司详情")
    @RequestMapping("preEditLogi/{logiId}")
    public String preEditLogi(@PathVariable("logiId") Integer logiId, Map<String, Object> map) {
        Logi logi = logiService.queryByLogiId(logiId);
        map.put("logi", logi);
        return "logi/edit_logi";
    }

    @ControllerLog("修改物流公司")
    @RequestMapping("editLogi")
    @ResponseBody
    @Authority(opCode = "07010105", opName = "修改物流公司")
    public AjaxResult editLogi(Logi logi) {
        try{
            return AppUtil.returnObj(logiService.doEditLogi(logi));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }

    @ControllerLog("删除物流公司")
    @RequestMapping("delLogi/{logiId}")
    @ResponseBody
    @Authority(opCode = "07010106", opName = "删除物流公司")
    public AjaxResult delLogi(@PathVariable("logiId") int logiId) {
        try{
            return AppUtil.returnObj(logiService.doDelLogi(logiId));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
}
