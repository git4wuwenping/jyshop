package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.MemberWithdrawInfo;
import com.qyy.jyshop.supple.MyMapper;

public interface MemberWithdrawInfoDao extends MyMapper<MemberWithdrawInfo>{

	/**
	 * 根据memberid查询银行卡列表
	 * @author Tonny
	 * @date 2018年4月25日
	 */
	List<Map<String, Object>> selectWithDrawalsByMEmberId(@Param("memberId")Long memberId);
	/**
	 * 银行卡解除绑定
	 * @author Tonny
	 * @date 2018年4月25日
	 */
	void updateBankCardUnbind(@Param("memberId")Long memberId, @Param("accountNo")String cardNum);
	/**
	 * 设置默认银行卡
	 * @author Tonny
	 * @date 2018年4月28日
	 */
	void updateDefaultBankCard(@Param("memberId")Long memberId, @Param("accountNo")String cardNum);

	//List<MemberWithdrawInfo> selectWithDrawalsByMEmberId(@Param("memberId")Long memberId);
	
}
