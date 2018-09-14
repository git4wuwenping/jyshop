package com.qyy.jyshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.OrderItems;
import com.qyy.jyshop.supple.MyMapper;

public interface OrderItemsDao extends MyMapper<OrderItems>{
    
    /**
     * 根据订单ID与货品Id查询订单货品信息
     * @author hwc
     * @created 2017年12月16日 下午3:44:48
     * @param orderId
     * @param productId
     * @return
     */
    OrderItems selectByOrderIdAndProductId(@Param("orderId")Long orderId,@Param("productId")Long productId);
    
    /**
     * 根据订单Id获取订单货品列表
     * @author hwc
     * @created 2017年12月20日 下午2:42:15
     * @param orderId
     * @return
     */
    List<OrderItems> selectByOrderId(@Param("orderId")Long orderId);
    /**
     * 根据订单Id获取订单货品列表及店铺名称
     * @author Tonny
     * @date 2018年4月21日
     */
    List<OrderItems> selectProductListByOrderId(@Param("orderId")Long orderId);
    
    /**
     * 批量添加
     * @param orderItemsList
     */
    void batchInsert(List<OrderItems> orderItemsList);
}
