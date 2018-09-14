package com.qyy.jyshop.admin.spell.service;

import com.qyy.jyshop.model.SpellOrderItems;

public interface SpellOrderItemsService {

    public SpellOrderItems getItemsByOrderId(Long orderId);
}
