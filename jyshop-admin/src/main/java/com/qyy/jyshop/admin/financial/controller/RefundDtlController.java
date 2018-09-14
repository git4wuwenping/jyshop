package com.qyy.jyshop.admin.financial.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.FinancialMgrAuth;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.financial.service.RefundDtlService;
import com.qyy.jyshop.pojo.PageAjax;

@Controller
@RequestMapping("/admin/financialMgr/refundDtl")
public class RefundDtlController extends BaseController {

    @Autowired
    private RefundDtlService refundDtlService;

    @FinancialMgrAuth(validate = true)
    @Authority(opCode = "090202", opName = "退款明细")
    @RequestMapping("refundDtlMain")
    public String refundDtlMain() {
        return "financial/refundDtl/refund_dtl_main";
    }

    @FinancialMgrAuth(validate = true)
    @RequestMapping("pageAjaxRefundDtl")
    @ResponseBody
    @Authority(opCode = "09020201", opName = "退款明细列表")
    public PageAjax<Map<String, Object>> pageAjaxRefundDtl(PageAjax<Map<String, Object>> page) {
        return refundDtlService.pageAjaxRefundDtl(page);
    }

}
