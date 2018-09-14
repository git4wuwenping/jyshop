package com.qyy.jyshop.dao;

import com.qyy.jyshop.model.SpellOrderItems;
import com.qyy.jyshop.supple.MyMapper;

public interface SpellOrderItemsDao extends MyMapper<SpellOrderItems> {

    public SpellOrderItems getItemsByOrderId(Long orderId);
}