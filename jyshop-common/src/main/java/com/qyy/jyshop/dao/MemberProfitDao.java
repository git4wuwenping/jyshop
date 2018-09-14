package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.MemberProfit;
import com.qyy.jyshop.supple.MyMapper;

public interface MemberProfitDao extends MyMapper<MemberProfit>{
	List<Map<String, Object>> getMemberRedPointGainList (Map<String,Object> params);
}
