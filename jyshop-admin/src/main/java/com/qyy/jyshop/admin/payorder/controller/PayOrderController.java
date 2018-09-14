package com.qyy.jyshop.admin.payorder.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.FinancialMgrAuth;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.payorder.service.PayOrderService;
import com.qyy.jyshop.pojo.PageAjax;

@Controller
@RequestMapping("/admin/financialMgr/payOrder")
public class PayOrderController extends BaseController {

    @Autowired
    private PayOrderService payOrderService;

    @FinancialMgrAuth(validate = true)
    @Authority(opCode = "090201", opName = "充值明细")
    @RequestMapping("payOrderMain")
    public String payOrderMain() {
        return "payorder/payorder_main";
    }

    @FinancialMgrAuth(validate = true)
    @RequestMapping("pageAjaxPayOrder")
    @ResponseBody
    @Authority(opCode = "09020101", opName = "充值明细列表")
    public PageAjax<Map<String, Object>> pageAjaxPayOrder(PageAjax<Map<String, Object>> page, String nickname,
            Integer payType) {
        return payOrderService.pageAjaxPayOrder(page, nickname, payType);
    }

}
