package com.qyy.jyshop.bargain.service;

import java.math.BigDecimal;
import java.util.Map;

public interface BargainOrderService {
    
    /**
     * 用户帮忙砍价
     * @author hwc
     * @created 2018年4月13日 上午11:42:29
     * @param orderId
     * @param token
     * @return
     */
    public BigDecimal doMemberBargain(Long orderId,String token);
    
    /**
     * 设置收货地址
     * @author hwc
     * @created 2018年4月14日 上午11:39:30
     * @param token
     * @param addrId
     * @param orderId
     * @return
     */
    public BigDecimal doEditOrderAddress(String token,Long addrId,Long orderId);
    
    /**
     * 创建砍价订单
     * @author hwc
     * @created 2018年4月12日 上午10:44:22
     * @param bargainId
     * @param token
     * @return 
     */
    public Map<String,Object> doCreateBargainOrder(Long bargainId,String token);
    
    /**
     * 获取砍价详情
     * @author hwc
     * @created 2018年4月13日 上午8:51:30
     * @param orderId
     * @param token
     * @return
     */
    public Map<String,Object> queryBargainDetail(Long orderId,String token);
    
    /**
     * 获取砍价结算页信息
     * @author hwc
     * @created 2018年4月14日 上午10:03:12
     * @param orderId
     * @param token
     * @return
     */
    public Map<String,Object> doBargainOrderCheckout(Long orderId,Long productId,String token);
    
	/**
	 * 查询发起砍价列表
	 * @author Tonny
	 * @date 2018年4月11日
	 */
	public Map<String, Object> selectBargainOrderList(String token, Integer pageNo, Integer pageSize);
	
	/**
	 * 订单收货
	 * @author hwc
	 * @created 2018年4月19日 上午10:15:02
	 * @param token
	 * @param orderId
	 */
	public void doBargainOrderRog(String token,Long orderId);
	/**
	 * 查询参与砍价列表
	 * @author Tonny
	 * @date 2018年4月12日
	 */
	//public PageAjax<Map<String, Object>> selectParticipateBargainOrderList(String token, Integer pageNo, Integer pageSize);

}
