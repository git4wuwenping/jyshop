package com.qyy.jyshop.member.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.MemberPointExt;
import com.qyy.jyshop.pojo.PageAjax;

public interface RedPointService {

	PageAjax<Map<String, Object>> getMemberRedPointDrawList(String token,int status, Integer pageNo, Integer pageSize);

	List<Map<String, Object>> getMemberRedPointGainList(String token);
	
	List<Map<String, Object>> getMemberRedPointGainUnconfirmedList(String token);

	Map<String, Object> redPointDraw(String token, Integer drawPoint, Integer style, MemberPointExt memberPointExt);

	Map<String, Object> getMemberRedPointInfo(String token);

}
