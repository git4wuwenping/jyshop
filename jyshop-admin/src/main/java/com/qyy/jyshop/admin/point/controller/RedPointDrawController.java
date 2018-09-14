package com.qyy.jyshop.admin.point.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.FinancialMgrAuth;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.point.service.MemberPointExtService;
import com.qyy.jyshop.admin.point.service.RedPointService;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.model.MemberPointExt;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.vo.MemberPointExtVo;

@Controller
@RequestMapping("/admin/financialMgr/point")
public class RedPointDrawController extends BaseController {

    @Autowired
    private RedPointService redPointService;

    @Autowired
    private MemberPointExtService memberPointExtService;

    @Autowired
    private MemberDao memberDao;

    /**
     * 红积分提现申请
     * 
     * @return
     */
    @FinancialMgrAuth(validate = true)
    @Authority(opCode = "090101", opName = "红积分提现申请")
    @RequestMapping("redPointDrawApplyMain")
    public String redPointDrawApplyMain() {
        return "point/redPointDrawApply/drawApply_main";
    }

    @FinancialMgrAuth(validate = true)
    @RequestMapping("pageRedPointDrawApplyAjax")
    @ResponseBody
    @Authority(opCode = "09010101", opName = "查询会员红积分提现列表")
    public PageAjax<Map<String, Object>> pageRedPointDrawApplyAjax(PageAjax<Map<String, Object>> page,
            MemberPointExtVo memberPointExtVo) {
        return memberPointExtService.pageRedPointDrawApplyAjax(page, memberPointExtVo);
    }

    @FinancialMgrAuth(validate = true)
    @Authority(opCode = "09010102", opName = "红积分提现申请审核界面")
    @RequestMapping("preAuditRedPointDrawApply/{id}")
    public String preAuditRedPointDrawApply(@PathVariable("id") Long id, Map<String, Object> map) {
        MemberPointExt memberPointExt = memberPointExtService.getMemberPointExtById(id);
        map.put("memberPointExt", memberPointExt);
        map.put("memberPoint", redPointService.getMemberPointById(memberPointExt.getMpId()));
        map.put("member", memberDao.selectByPrimaryKey(memberPointExt.getMemberId()));
        return "point/redPointDrawApply/drawApply_audit";
    }

    @FinancialMgrAuth(validate = true)
    @Authority(opCode = "09010103", opName = "红积分提现申请详情")
    @RequestMapping("preDetailRedPointDrawApply/{id}")
    public String preDetailRedPointDrawApply(@PathVariable("id") Long id, Map<String, Object> map) {
        MemberPointExt memberPointExt = memberPointExtService.getMemberPointExtById(id);
        map.put("memberPointExt", memberPointExt);
        map.put("memberPoint", redPointService.getMemberPointById(memberPointExt.getMpId()));
        map.put("logList", redPointService.queryPointLogListByBillNo(memberPointExt.getBillNo()));
        map.put("member", memberDao.selectByPrimaryKey(memberPointExt.getMemberId()));
        return "point/redPointDrawApply/drawApply_detail";
    }

    @FinancialMgrAuth(validate = true)
    @RequestMapping("remitRedPointDrawApply/{id}")
    @ResponseBody
    @Authority(opCode = "09010104", opName = "红积分提现申请打款")
    public AjaxResult remitRedPointDrawApply(@PathVariable("id") Long id) {
        try {
            return AppUtil.returnObj(memberPointExtService.remitRedPointDrawApply(id));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

    @FinancialMgrAuth(validate = true)
    @RequestMapping("auditRedPointDrawApply")
    @ResponseBody
    @Authority(opCode = "09010105", opName = "红积分提现申请审核")
    public AjaxResult auditRedPointDrawApply(MemberPointExt memberPointExt) {
        try {
            return AppUtil.returnObj(memberPointExtService.auditRedPointDrawApply(memberPointExt));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }

}
