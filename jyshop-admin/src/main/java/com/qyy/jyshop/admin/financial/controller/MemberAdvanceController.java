package com.qyy.jyshop.admin.financial.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.annotation.FinancialMgrAuth;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.financial.service.MemberAdvanceService;
import com.qyy.jyshop.admin.member.service.MemberService;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;

@Controller
@RequestMapping("/admin/financialMgr/memberAdvance")
public class MemberAdvanceController extends BaseController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberAdvanceService memberAdvanceService;
    /**
     * 客户列表
     * @return
     */
    @FinancialMgrAuth(validate = true)
    @Authority(opCode = "090401", opName = "客户列表")
    @RequestMapping("memerMain")
    public String memerMain(){
        return "financial/memberAdvance/member_main";
    }
    
    @FinancialMgrAuth(validate = true)
    @RequestMapping("pageMemberAjax")
    @ResponseBody
    @Authority(opCode = "09040101", opName = "查询会员列表")
    public PageAjax<Map<String, Object>> pageMemberAjax(PageAjax<Member> page) {
        return memberService.selectMemberByParams(page);
    }

    @FinancialMgrAuth(validate = true)
    @Authority(opCode = "09040102", opName = "会员预存款充值页面")
    @RequestMapping("preRecharge/{memberId}")
    public String preRecharge(@PathVariable Long memberId,Map<String,Object> map){
        
        map.put("member", memberService.preDetaileCustomer(memberId));
        map.put("payLogList", memberAdvanceService.queryPayLogListByMemberId(memberId));
        
        return "financial/memberAdvance/member_recharge";
    }
    
    @FinancialMgrAuth(validate = true)
    @ControllerLog("会员预存款充值")
    @RequestMapping("recharge")
    @ResponseBody
    @Authority(opCode = "09040103", opName = "会员预存款充值")
    public AjaxResult recharge(String memberId,String rmb,String adminRemark) {
        return memberAdvanceService.recharge(memberId,rmb,adminRemark);
    }
    
}
