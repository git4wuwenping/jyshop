package com.qyy.jyshop.dao;

import com.qyy.jyshop.model.Delivery;
import com.qyy.jyshop.model.SpellDelivery;
import com.qyy.jyshop.supple.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface SpellDeliveryDao extends MyMapper<SpellDelivery> {

    public SpellDelivery selectLogiByOrderId(@Param("orderId")Long orderId);
}
