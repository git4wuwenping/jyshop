package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.MemberPointExt;
import com.qyy.jyshop.supple.MyMapper;
import com.qyy.jyshop.vo.MemberPointExtVo;

public interface MemberPointExtDao extends MyMapper<MemberPointExt>{
	
	public List<Map<String,Object>> selectRedPointDrawApplyList(MemberPointExtVo memberPointExtVo);
	
	public List<Map<String,Object>> getMemberRedPointDrawList(Long memberId);
	
	

}
