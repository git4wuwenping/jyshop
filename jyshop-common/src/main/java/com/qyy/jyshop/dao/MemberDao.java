package com.qyy.jyshop.dao;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.supple.MyMapper;

public interface MemberDao extends MyMapper<Member>{
	
	/**
	 * 查询会员是否购买店长礼包
	 * @author Tonny
	 * @date 2018年3月21日
	 */
	Integer selectGiftOrderCount(Long memberId);

    /**
     * 根据用户名和密码获取用户信息
     * @author hwc
     * @created 2017年12月9日 下午3:44:39
     * @param uname
     * @param password
     * @return
     */
    Member queryByUnameAndPassword(@Param("uname")String uname,@Param("password")String password);
    
    /**
     * 根据openId获取用户
     * @author hwc
     * @created 2018年1月19日 上午10:32:53
     * @param openId
     * @return
     */
    Member selectByOpenId(@Param("openId")String openId);
    
    /**
     * 根据unionId获取用户
     * @author hwc
     * @created 2018年1月19日 下午2:35:25
     * @param unionId
     * @return
     */
    Member selectByUnionId(@Param("unionId")String unionId);
    
    /**
     * 查询昨日新增用户
     * @param parentId
     * @param comId
     * @return
     */
    List<Map<String, Object>> selectNewUserYesterday(Map<String,Object> params);
    
    /**
     * 查询昨日新增店长
     * @param memberId
     * @return
     */
    List<Map<String, Object>>  selectNewShopkeeperYesterday(Map<String, Object> param);
    
	List<Member> findLowerMem(@Param("memberId") Long memberId);
	/**
	 * 根据手机号查询会员
	 * @author Tonny
	 * @date 2018年3月14日
	 */
	int seletMemberByMobile(@Param("memberId")Long memberId,@Param("mobile")String mobile);
	/**
	 * 查询是否存在重复推广的商品
	 * @author Tonny
	 * @date 2018年3月14日
	 */
	int selectMemberGoodsById(@Param("goodsId")Long goodsId, @Param("memberId")Long memberId);
	
	/***
	 * 查询手机号
	 * @param memberId
	 * @return
	 */
	String selcetMobile(@Param("memberId")Long memberId);
	/***
	 * 查询是否消费满足200
	 * @param memberId
	 * @return
	 */
	Integer seletConsumptionSituation(@Param("memberId")Long memberId);
	
	/**
	 * 查询会员id
	 * @return
	 */
	List<Long> findShopownerMember();
	
	/**
	 * 根据商品id查询店长推荐商品
	 * @param goodsId
	 * @return
	 */
	List<Member>  selectMemberByGoodsId(@Param("goodsId")Long goodsId);
	/**
	 * 查询交易总金额 前台接口
	 * @param memberId
	 * @return
	 */
	Long selectSumAmountByMemberId(@Param("memberId")Long memberId);
	/**
	 * 根据id查询会员
	 * @param memberId
	 * @return
	 */
	Member findMemerById(@Param("memberId")Long memberId);
	
	/**
     * 会员分页<Datatable>
     */
    List<Map<String,Object>> selectDataTableMember(Map<String,Object> params);
	/**
	 * 扣除用户预存款
	 * @author hwc
	 * @created 2018年1月6日 上午11:09:27
	 * @param memberId
	 * @param advance
	 */
	int updateMemberAdvanceOfCut(@Param("memberId") Long memberId,@Param("advance")BigDecimal advance);
	
	/**
	 * 添加用户预存款
	 * @author hwc
	 * @created 2018年1月12日 上午10:20:19
	 * @param memberId
	 * @param advance
	 * @return
	 */
	int updateMemberAdvanceOfAdd(@Param("memberId") Long memberId,@Param("advance")BigDecimal advance);
	
	/**
	 * 修改用户为分润用户
	 * @author hwc
	 * @created 2018年1月15日 下午6:40:06
	 * @param memberId
	 * @param advance
	 * @return
	 */
	int updateProfitMember(@Param("memberId") Long memberId,@Param("profitTime")Timestamp profitTime);
	
	/**
	 * 修改普通用户为店长
	 * @author hwc
	 * @created 2018年2月1日 下午2:06:31
	 * @param memberId
	 * @param profitTime
	 * @return
	 */
	int updateMemberOfShopOwner(@Param("memberId") Long memberId,@Param("profitTime")Timestamp profitTime);
	
	/**
	 * 增加用户积分
	 * @author hwc
	 * @created 2018年1月31日 上午10:01:35
	 * @param memberId
	 * @param cloudPoint
	 * @param yellowPoint
	 * @param redPoint
	 */
	void updateMemberPointOfAdd(@Param("memberId") Long memberId,
	        @Param("cloudPoint")BigDecimal cloudPoint,
	        @Param("yellowPoint")BigDecimal yellowPoint,
	        @Param("redPoint")BigDecimal redPoint);
	/**
	 * 将商品添加到我的商品
	 * @author Tonny
	 * @date 2018年3月14日
	 */
	void addGoodsToMember(@Param("goodsId")Long goodsId, @Param("memberId")Long memberId);

	void updateMemberPointById(@Param("memberId")Long memberId, @Param("point")BigDecimal point);

	void applyForShopowner(@Param("memberId")Long memberId);

	/**
	 * 绑定手机号
	 * @author Tonny
	 * @param password 
	 * @date 2018年3月14日
	 */
	void updateMemberMobile(@Param("memberId")Long memberId, @Param("mobile")String mobile, @Param("password")String password);

	/**
	 * 删除我的商品
	 * @author Tonny
	 * @date 2018年3月14日
	 */
	void removeMemberGoods(@Param("memberId")Long memberId, @Param("goodsId")Long goodsId);
	/**
	 * 查询手机号是否已经被注册
	 * @author Tonny
	 * @date 2018年3月14日
	 */
	int checkMemberUserName(@Param("mobile")String mobile);
	/**
	 * 查询原支付密码是否正确
	 * @author Tonny
	 * @date 2018年3月6日
	 */
	int checkPayPasswordByMemberId(@Param("memberId")Long memberId, @Param("payPassword")String payPassword);

	Long insertMemberReturnId(Member member);

}
