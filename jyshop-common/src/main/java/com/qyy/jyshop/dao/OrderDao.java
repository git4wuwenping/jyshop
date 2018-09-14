package com.qyy.jyshop.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.supple.MyMapper;

public interface OrderDao extends MyMapper<Order> {

    List<Map<String, Object>> orderCount(Long memberId);

    Map<String, Object> getOrderCountByMemberId(Long memberId);

    /**
     * 获取用户订单详情
     * 
     * @author hwc
     * @created 2017年12月16日 下午3:02:40
     * @param orderId
     * @param memberId
     * @return
     */
    Order selectMemberOrder(@Param("orderId") Long orderId, @Param("memberId") Long memberId);

    /**
     * 根据订单Id查询没拆过单的订单
     * 
     * @author hwc
     * @created 2017年12月21日 下午3:06:13
     * @param orderId
     * @return
     */
    Order selectByOrderIdNoSplit(@Param("orderId") Long orderId);

    /**
     * 根据订单号与订单状态查询订单
     * 
     * @author hwc
     * @created 2018年1月9日 下午7:38:38
     * @param orderSn
     * @param orderStatus
     * @param payStatus
     * @param shipStatus
     * @return
     */
    Order selectByOrderSnAndStatus(@Param("orderSn") String orderSn, @Param("orderStatus") Integer orderStatus,
            @Param("payStatus") Integer payStatus, @Param("shipStatus") Integer shipStatus);

    /**
     * 查询已支付待分配订单
     * 
     * @author hwc
     * @created 2017年12月19日 上午11:44:46
     * @return
     */
    List<Long> selectPayOrderId();

    /**
     * 根据条件查询用户订单列表
     * 
     * @author hwc
     * @created 2017年12月20日 上午11:10:35
     * @param params
     * @return
     */
    List<Map<String, Object>> selectMemberOrderList(Map<String, Object> params);

    /**
     * 根据条件查询订单列表
     * 
     * @author hwc
     * @created 2017年12月23日 下午3:31:43
     * @param params
     * @return
     */
    List<Map<String, Object>> selectOrderByParams(Map<String, Object> params);

    /**
     * 查询首次分润订单
     * 
     * @author hwc
     * @created 2018年1月15日 上午10:55:48
     * @param orderStatus
     * @param profitStatus
     * @param orderAmount
     * @param isProfit
     * @return
     */
    List<Map<String, Object>> selectProfitOrder(@Param("orderStatus") Integer orderStatus,
            @Param("profitStatus") Integer profitStatus, @Param("orderAmount") BigDecimal orderAmount,
            @Param("isProfit") Integer isProfit);

    /***
     * 查询待发货订单
     */
    List<Map<String, Object>> selectConsignmentOrders(Map<String, Object> params);

    /**
     * 查询昨日订单
     * 
     * @param params
     * @return
     */
    List<Map<String, Object>> selectYesterdayOrders(Map<String, Object> params);

    /**
     * 首页统计
     */
    List<Map<String, Object>> selectStatisticsCount(Map<String, Object> params);

    List<Order> findOrderByMemId(@Param("memberId") Long memberId);

    /**
     * 修改订单所属供应商
     * @author hwc
     * @created 2017年12月16日 下午3:02:57
     * @param orderId
     * @param comId
     */
    void updateComIdByOrderId(@Param("orderId") Long orderId, @Param("comId") Integer comId,
            @Param("orderStatus") Integer orderStatus);
    
    /**
     * 修改订单支付成功后的状态
     * @author hwc
     * @created 2018年3月27日 上午11:06:37
     * @param orderId
     * @param orderStatus
     * @param payStatus
     * @param shipStatus
     * @param paymentId
     * @param paymentName
     * @param paymentType
     * @param payMoney
     * @param paymentTime
     */
    void updateOrderPayStatus(@Param("orderId") Long orderId,
            @Param("orderStatus") Integer orderStatus,
            @Param("payStatus") Integer payStatus,
            @Param("shipStatus") Integer shipStatus,
            @Param("paymentId") Integer paymentId,
            @Param("paymentName") String paymentName,
            @Param("paymentType") String paymentType,
            @Param("payMoney") BigDecimal payMoney,
            @Param("paymentTime") Timestamp paymentTime);
    
    /**
     * 修改订单状态(null值不修改)
     * 
     * @author hwc
     * @created 2017年12月20日 下午5:02:28
     * @param orderId
     * @param orderStatus
     * @param orderStatus
     * @param orderStatus
     */
    void updateOrderStatus(@Param("orderId") Long orderId, @Param("orderStatus") Integer orderStatus,
            @Param("payStatus") Integer payStatus, @Param("shipStatus") Integer shipStatus);

    /**
     * 根据订单号修改订单状态(null值不修改)
     * 
     * @author hwc
     * @created 2018年1月9日 下午7:10:07
     * @param orderSn
     * @param orderStatus
     * @param payStatus
     * @param shipStatus
     */
    void updateOrderStatusByOrderSn(@Param("orderSn") String orderSn, @Param("orderStatus") Integer orderStatus,
            @Param("payStatus") Integer payStatus, @Param("shipStatus") Integer shipStatus);

    /**
     * 订单伪删除
     * 
     * @author hwc
     * @created 2017年12月20日 下午5:07:16
     * @param orderId
     */
    void updateDeleteFlag(@Param("orderId") Long orderId);

    /**
     * 修改订单分润状态
     * 
     * @author hwc
     * @created 2018年1月15日 下午7:04:49
     * @param orderId
     * @param profitStatus
     * @return
     */
    int updateOfProfitStatus(@Param("orderId") Long orderId, @Param("profitStatus") Integer profitStatus);

    /**
     * 自动收货
     * 
     * @author hwc
     * @created 2018年1月30日 下午4:06:10
     * @param orderStatus
     * @param day
     * @param newOrderStatus
     */
    void updateOfReceipt(@Param("orderStatus") Integer orderStatus, @Param("day") Integer day,
            @Param("newOrderStatus") Integer newOrderStatus);

    /**
     * 自动完成
     * 
     * @author hwc
     * @created 2018年1月30日 下午4:06:29
     * @param orderStatus
     * @param day
     * @param newOrderStatus
     */
    void updateOfComplete(@Param("orderStatus") Integer orderStatus, @Param("day") Integer day,
            @Param("newOrderStatus") Integer newOrderStatus);

    /**
     * 修改订单上级分润状态
     * 
     * @param valueOf
     * @param valueOf2
     */
    void updateSubProfitStatus(@Param("orderId") Long orderId, @Param("profitStatus") Integer profitStatus);

    /**
     * 查询未分润的上级分润订单
     * 
     * @param orderStatus
     * @param profitStatus
     * @return
     */
    List<Map<String, Object>> selectShopownerSubProfitOrder(@Param("orderStatus") Integer orderStatus,
            @Param("profitStatus") Integer profitStatus);

    /**
     * 查询导出订单数据
     * 
     * @param map
     * @return
     */
    List<Map<String, Object>> queryExportOrderData(Map<String, Object> map);

}
