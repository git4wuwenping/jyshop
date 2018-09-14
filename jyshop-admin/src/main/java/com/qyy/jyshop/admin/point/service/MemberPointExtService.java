package com.qyy.jyshop.admin.point.service;

import java.util.Map;

import com.qyy.jyshop.model.MemberPointExt;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.vo.MemberPointExtVo;

public interface MemberPointExtService {

	PageAjax<Map<String, Object>> pageRedPointDrawApplyAjax(PageAjax<Map<String, Object>> page,
			MemberPointExtVo memberPointExtVo);

	MemberPointExt getMemberPointExtById(Long id);

	String remitRedPointDrawApply(Long id);

	String auditRedPointDrawApply(MemberPointExt memberPointExt);

}
