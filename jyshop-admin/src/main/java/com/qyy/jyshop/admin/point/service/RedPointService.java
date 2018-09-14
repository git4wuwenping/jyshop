package com.qyy.jyshop.admin.point.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.MemberPoint;
import com.qyy.jyshop.model.PointDrawLog;
import com.qyy.jyshop.pojo.PageAjax;

public interface RedPointService {

	PageAjax<Map<String, Object>> pageRedPointAjax(PageAjax<Map<String, Object>> page, Integer conditionType,
			String conditionVal, Integer memberType);

	Map<String,Object> queryMemberPointByMemberId(Long memberId);

	Map<String, Object> queryMemberInfoByMemberId(Long memberId);

	MemberPoint getMemberPointById(Long mpId);

    List<PointDrawLog> queryPointLogListByBillNo(String billNo);

}
