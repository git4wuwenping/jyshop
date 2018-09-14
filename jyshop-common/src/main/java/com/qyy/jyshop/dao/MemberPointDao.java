package com.qyy.jyshop.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.MemberPoint;
import com.qyy.jyshop.supple.MyMapper;

public interface MemberPointDao extends MyMapper<MemberPoint>{

    /**
     * 批量添加
     * @author hwc
     * @created 2018年1月15日 下午5:19:23
     * @param memberPointList 
     */
    public void batchInsert(List<MemberPoint> memberPointList);
    
    public List<Map<String,Object>> selectMemberRedPointList(@Param("memberId")String memberId,@Param("nickname")String nickname,@Param("memberType")Integer memberType);

	public Map<String, Object> queryMemberInfoByMemberId(Long memberId);

	public List<Map<String, Object>> getMemberRedPointInfo(Long memberId);

	public List<Map<String, Object>> selectRedPointUnconfirmedList(@Param("memberId")Long memberId);
	
	public List<Map<String, Object>> getMemberRedPointGainList(@Param("memberId")Long memberId);

	public List<Map<String, Object>> getOrderProfit(Long orderId);
	
}
