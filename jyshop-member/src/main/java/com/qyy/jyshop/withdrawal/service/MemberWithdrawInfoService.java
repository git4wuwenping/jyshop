package com.qyy.jyshop.withdrawal.service;

import java.util.List;
import java.util.Map;

public interface MemberWithdrawInfoService {

	Map<String, Object> getWithdrawalDetial(String token);

	/**
	 * 查询银行卡列表
	 * @author Tonny
	 * @date 2018年4月25日
	 */
	List<Map<String, Object>> selectBankCardList(String token);

	Map<String, Object> getDrawSill(String token);
	/**
	 * 银行卡认证
	 * @author Tonny
	 * @return 
	 * @date 2018年4月24日
	 */
	String bankCardCertificate(String token, String cardNum, String cardholder, String mobile, String cardType);
	/**
	 * 银行卡解除绑定
	 * @author Tonny
	 * @date 2018年4月25日
	 */
	void updateBankCardUnbind(String token, String cardNum);
	
	Map<String, Object> saveWithdrawal(String token, String accountName, String accountNo, int drawType);
	/**
	 * 添加银行卡
	 * @author Tonny
	 * @param path 
	 * @param token 
	 * @date 2018年4月26日
	 */
	void insertMemberWithdrawInfo(String apistore_GET,Long memberId, String path);
	/**
	 * 设置默认银行卡
	 * @author Tonny
	 * @date 2018年4月28日
	 */
	void setDefaultBankCard(String token, String cardNum);
}
