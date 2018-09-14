package com.qyy.jyshop.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.OrderCheckout;
import com.qyy.jyshop.supple.MyMapper;

public interface OrderCheckoutDao extends MyMapper<OrderCheckout>{
    
    /**
     * 获取支付金额
     * @author hwc
     * @created 2018年2月5日 下午4:03:16
     * @param orderId
     * @param comId
     * @param type
     * @return
     */
    BigDecimal selectOfPayMoney(@Param("orderId")Long orderId,
            @Param("comId")Integer comId,
            @Param("type")Integer type);
    
    /**
     * 根据订单Id获取结算信息
     * @author hwc
     * @created 2017年12月16日 下午3:27:11
     * @param orderId
     * @return
     */
    List<OrderCheckout> selectByOrderId(@Param("orderId")Long orderId);
    
    /**
     * 批量添加结算信息
     * @author hwc
     * @created 2017年12月16日 下午3:27:33
     * @param orderCheckoutList
     */
    void batchInsert(List<OrderCheckout> orderCheckoutList);
}
