package com.qyy.jyshop.admin.point.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.point.service.MemberPointExtService;
import com.qyy.jyshop.admin.point.service.RedPointService;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.pojo.PageAjax;

@Controller
@RequestMapping("/admin/point")
public class RedPointController extends BaseController {

    @Autowired
    private RedPointService redPointService;

    @Autowired
    private MemberPointExtService memberPointExtService;

    @Autowired
    private MemberDao memberDao;

    /**
     * 红积分列表
     * 
     * @return
     */
    @Authority(opCode = "080101", opName = "红积分列表")
    @RequestMapping("redPointMain")
    public String redPointMain() {
        return "point/redPoint/redPoint_main";
    }

    @RequestMapping("pageRedPointAjax")
    @ResponseBody
    @Authority(opCode = "08010101", opName = "查询会员红积分列表")
    public PageAjax<Map<String, Object>> pageRedPointAjax(PageAjax<Map<String, Object>> page, Integer conditionType,
            String conditionVal, Integer memberType) {
        return redPointService.pageRedPointAjax(page, conditionType, conditionVal, memberType);
    }

    @RequestMapping("preDetaileRedPoint/{memberId}")
    @Authority(opCode = "08010102", opName = "红积分详情页面")
    public String preDetaileRedPoint(@PathVariable("memberId") Long memberId, Map<String, Object> map) {
        Map<String, Object> memberInfo = redPointService.queryMemberInfoByMemberId(memberId);
        map.put("memberInfo", memberInfo);
        Map<String, Object> memberPointInfo = redPointService.queryMemberPointByMemberId(memberId);
        map.put("memberPointInfo", memberPointInfo);
        return "point/redPoint/redPoint_detail";
    }

}
