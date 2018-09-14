package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.CoinDtl;
import com.qyy.jyshop.supple.MyMapper;

public interface CoinDtlDao extends MyMapper<CoinDtl> {

	Map<String, Object> queryMemberCoinInfoByMemberId(Long memberId);

	List<Map<String, Object>> queryCoinDtlByParam(Long memberId);

	List<Map<String, Object>> selectSignListById(@Param("memberId")Long memberId,@Param("curYear")String curYear);
	/**
	 * 查询该用户是否已经签到
	 * @author Tonny
	 * @param coinType 
	 * @date 2018年3月27日
	 */
	List<Map<String, Object>> selectCoinDtlByMemberId(@Param("memberId")Long memberId, @Param("coinType")short coinType);

}
