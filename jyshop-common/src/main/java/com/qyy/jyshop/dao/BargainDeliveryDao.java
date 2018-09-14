package com.qyy.jyshop.dao;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.BargainDelivery;
import com.qyy.jyshop.supple.MyMapper;

public interface BargainDeliveryDao extends MyMapper<BargainDelivery>{
	/**
	 * 根据订单号查询物流信息
	 * @author Tonny
	 * @date 2018年4月18日
	 */
	BargainDelivery selectLogiByOrderId(@Param("orderId")Long orderId);

}
