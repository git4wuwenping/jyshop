package com.qyy.jyshop.dao;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.GiftDelivery;
import com.qyy.jyshop.supple.MyMapper;

public interface GiftDeliveryDao  extends MyMapper<GiftDelivery>{

	/***
	 * 根据订单号查询物流信息
	 * @param orderId
	 * @return
	 */
	GiftDelivery selectLogiByOrderId(@Param("orderId")Long orderId);

}
