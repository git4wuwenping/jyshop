package com.qyy.jyshop.admin.member.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberPoint;
import com.qyy.jyshop.model.MemberWithdrawInfo;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.pojo.DataTablePage;
import com.qyy.jyshop.pojo.ExcelData;
import com.qyy.jyshop.pojo.PageAjax;

public interface MemberService {

	/**
	 * 查询会员列表
	 * @param page
	 * @param customer
	 * @return
	 */
	//PageAjax<Member> pageMemberAjax(PageAjax<Member> page, Member member);
	PageAjax<Map<String, Object>> selectMemberByParams(PageAjax<Member> page);
	/**
	 * 查询会员详情到用户详情页
	 * @param memberId
	 * @return
	 */
	Member preDetaileCustomer(Long memberId);

	PageAjax<Order> PageIntegral(PageAjax<Order> page, Order order, Long memberId);

	List<Order> PageIntegral(Long memberId);

	List<Member> pagelowerMemAjax(Long memberId);

	void updateMemberPointById(Long memberId, BigDecimal redPoint);

	List<MemberPoint> getPointListByMemberId(Long memberId);

	List<Member> getSonMemberList(Long memberId);

	List<Order> getOrderListByMemberId(Long memberId);

	Map<String,Object> getDrawTypeListByMemberId(Long memberId);

	String modifyMemberState(Long memberId, Integer state);

	List<MemberPoint> getPointUnconfirmedListByMemberId(Long memberId);
	
    DataTablePage<Map<String, Object>> pageDataTableMember(Map<String, Object> map);
}
