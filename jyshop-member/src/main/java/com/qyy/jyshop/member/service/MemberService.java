package com.qyy.jyshop.member.service;

import com.qyy.jyshop.model.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
	/**
	 * 查询手机号
	 * @param token
	 * @return
	 */
	String selcetMobile(String token);

	/***
	 * 查询是否满足消费200
	 * @param token
	 * @return
	 */
	Integer seletConsumptionSituation(String token);
	
	/**
	 * 查询是否购买店长礼包
	 * @author Tonny
	 * @date 2018年3月21日
	 */
	Integer selectGiftOrderCount(String token);

	List<Map<String, Object>> getMemberGoods(String token, String docBase, String path);
	
	/**
	 * 查询交易总金额
	 * @param memberId
	 * @return
	 */
	Long selectSumAmountByMemberId(Long memberId);

	Member jifenyuetongji(String token);
	
	/**
	 * 已签到日期、金币数量返回
	 * @author Tonny
	 * @date 2018年3月19日
	 */
	Map<String, Object> returnSignAndCoin(String token, Integer month);
	
	/**
	 * 我的下级会员
	 * @param token
	 * @return
	 */
    List<Member> queryMyChildMemberList(String token);
	
    /**
	 * 根据id查询会员信息
	 * @author Tonny
	 * @date 2018年3月6日
	 */
	Member selectMemberBYMemberId(Long memberId);
	
	/**
	 * 查询手机号是否已经被注册
	 * @author Tonny
	 * @date 2018年3月5日
	 */
	boolean checkMemberUserName(String mobile);
	
	String applyForShopowner(String token);

	boolean updateMemberMobile(String token, String mobile);

	/**
	 * 更新个人信息接口
	 * @author Tonny
	 * @date 2018年3月5日
	 */
	void updateMemberInfo(Map<String, Object> map);
	
	/**
	 * 忘记支付密码接口
	 * @author Tonny
	 * @param mobile 
	 * @date 2018年3月6日
	 */
	void forgetPayPassword(String token, String mobile, String code, String newPayPassword);
	
	/**
	 * 删除我的商品
	 * @param goodsId
	 * @param token
	 */
	void removeMemberGoods(Long goodsId, String token);
	
	/**
	 * 签到
	 * @author Tonny
	 * @param coinType 
	 * @date 2018年3月16日
	 */
	Map<String, Object>  memberSign(String token, short coinType);
	
	String MyTwoDimensionCode(Long memberId, String docBase,String path, String logoPath) throws Exception;
	
    /**
     * 我要推广
     * @param goodsId
     * @param token
     */
	void memberPromote(Long goodsId,String token);
	
	/**
	 * 我的商品排序
	 * @param token
	 * @param docBase
	 * @param path
	 * @param orderType 
	 * @return
	 */
	List<Map<String, Object>> orderMemberGoods(String token, String docBase, String path, String orderType);
	/**
	 * 修改支付密码
	 * @author Tonny
	 * @param password 
	 * @date 2018年3月28日
	 */
	void setPassword(String token, String payPassword, String password);
	
}
