package com.qyy.jyshop.admin.member.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.member.service.MemberAddressService;
import com.qyy.jyshop.admin.member.service.MemberService;
import com.qyy.jyshop.admin.order.service.OrderService;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.DataTablePage;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/member")
public class MemberController extends BaseController{
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private MemberAddressService memberAddressService;
	/**
	 * 客户列表
	 * @return
	 */
	@Authority(opCode = "050101", opName = "客户列表")
	@RequestMapping("memMain")
	public String cusMain(){
		return "member/member_main";
	}
	
	@ControllerLog("查询会员列表")
	@RequestMapping("pageMemberAjax")
	@ResponseBody
	@Authority(opCode = "05010101", opName = "查询会员列表")
	public PageAjax<Map<String, Object>> pageMemberAjax(PageAjax<Member> page) {
		return memberService.selectMemberByParams(page);
	}
	
	@ControllerLog("会员详情页面")
	@RequestMapping("preDetaileMember/{memberId}")
	@Authority(opCode = "05010102", opName = "会员详情页面")
	public String preDetaileMember(@PathVariable("memberId")Long memberId,Map<String, Object> map) {
		Member member = memberService.preDetaileCustomer(memberId);
		map.put("member", member);
		/*Integer identityId = member.getIdentityId();
		if(identityId == 1){	//店长
			List<Order> orders = memberService.PageIntegral(memberId);
	        map.put("orders",orders);
	        if(orders != null){
	        	//下级信息
	        	List<Member> members = memberService.pagelowerMemAjax(memberId);
	        	map.put("members",members);
	        }
			return "member/detaile_shopowner_member";	//店长详情界面
		}else if(identityId == 2){	//普通用户
			//累计交易金额、支付订单数、 订单均价、交易商品件数
			map.put("orderCount", orderService.getOrderCountByMemberId(memberId));
			//收货地址
			map.put("addressList", memberAddressService.getAddressListByMemberId(memberId));
			//交易记录-订单记录
			map.put("orderList", memberService.PageIntegral(memberId));
			//积分记录
			map.put("pointList", memberService.getPointListByMemberId(memberId));
			//下级信息
			map.put("sonMemberList", memberService.getSonMemberList(memberId));
			return "member/detaile_ordinary_member";	//普通用户详情页面
		}else {	//其他
			return null;
		}*/
		//累计交易金额、支付订单数、 订单均价、交易商品件数
		map.put("orderCount", orderService.getOrderCountByMemberId(memberId));
		//收货地址
		map.put("addressList", memberAddressService.getAddressListByMemberId(memberId));
		//交易记录-订单记录
		map.put("orderList", memberService.getOrderListByMemberId(memberId));
		//积分记录
		map.put("pointList", memberService.getPointListByMemberId(memberId));
		//待确认积分记录
		map.put("pointUnconfirmedList", memberService.getPointUnconfirmedListByMemberId(memberId));
		//下级信息
		map.put("sonMemberList", memberService.getSonMemberList(memberId));
		//提现方式
		map.put("drawTypeMap", memberService.getDrawTypeListByMemberId(memberId));
		return "member/member_detail";	//普通用户详情页面
	}
	
	
    @RequestMapping("modifyMemberState/{memberId}")
    @ResponseBody
    @Authority(opCode = "05010103", opName = "修改会员冻结状态")
    public AjaxResult modifyMemberState(@PathVariable("memberId") Long memberId,Integer state) {
        try {
            return AppUtil.returnObj(memberService.modifyMemberState(memberId,state));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    
    @ControllerLog("获取dataTable会员数据")
    @RequestMapping("pageDataTableMember")
    @ResponseBody
    @Authority(opCode = "05010104", opName = "获取dataTable会员数据")
    public DataTablePage<Map<String,Object>> pageDataTableMember(@RequestParam Map<String, Object> map) {
        DataTablePage<Map<String,Object>> page = memberService.pageDataTableMember(map);
        return page;
    }
}
