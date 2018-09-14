package com.qyy.jyshop.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.BargainOrder;
import com.qyy.jyshop.supple.MyMapper;

public interface BargainOrderDao  extends MyMapper<BargainOrder>{

    /**
     * 查询免单砍价支付总金额 
     * @author hwc
     * @created 2018年4月18日 下午4:35:33
     * @param parentId
     * @return
     */
    BigDecimal selectPayAmount(@Param("parentId")Long parentId);
    
    /**
     * 砍价订单详情
     * @author hwc
     * @created 2018年4月13日 上午9:50:35
     * @param orderId
     * @return
     */
    Map<String,Object> selectBargainDetail(@Param("orderId")Long orderId);
    
    /**
     * 根据订单ID获取砍价订单
     * @author hwc
     * @created 2018年4月13日 下午3:48:45
     * @param orderId
     * @return
     */
    BargainOrder selectByOrderId(@Param("orderId")Long orderId);
    
    /**
     * 获取用户砍价订单
     * @author hwc
     * @created 2018年4月14日 上午10:18:36
     * @param orderId
     * @param memberId
     * @return
     */
    BargainOrder selectMemberOrder(@Param("orderId")Long orderId,@Param("memberId")Long memberId);
    
    /**
     * 根据订单父ID获取用户 砍价订单
     * @author hwc
     * @created 2018年4月17日 下午3:26:44
     * @param parentId
     * @param memberId
     * @return
     */
    BargainOrder selectByParentIdAndMemberId(@Param("parentId")Long parentId,@Param("memberId")Long memberId);
    
    /**
     * 根据订单ID与订单状态获取订单
     * @author hwc
     * @created 2018年4月18日 下午3:49:51
     * @param orderId
     * @param orderStatus
     * @return
     */
    BargainOrder selectByOrderSnAndStatus(@Param("orderSn")String orderSn,@Param("orderStatus")Integer orderStatus);
    
    /**
     * 修改订单状态
     * @author hwc
     * @created 2018年4月13日 下午5:00:59
     * @param orderId
     * @param memberId
     * @param orderStatus
     */
    int updateOfOrderStatus(@Param("orderId")Long orderId,
            @Param("memberId")Long memberId,
            @Param("orderStatus")Integer orderStatus,
            @Param("orderAmount")BigDecimal orderAmount);

    /**
     * 批量删除
     * @param list
     */
    void batchDel(List<BargainOrder> list);
}
