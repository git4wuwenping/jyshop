package com.qyy.jyshop.dao;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.Delivery;
import com.qyy.jyshop.supple.MyMapper;

public interface DeliveryDao  extends MyMapper<Delivery>{

	/***
	 * 根据订单号查询物流信息
	 * @param orderId
	 * @return
	 */
	Delivery selectLogiByOrderId(@Param("orderId")Long orderId);

}
