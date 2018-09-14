package com.qyy.jyshop.admin.spell.service.impl;

import com.qyy.jyshop.admin.spell.service.SpellOrderItemsService;
import com.qyy.jyshop.dao.SpellOrderItemsDao;
import com.qyy.jyshop.model.SpellOrderItems;
import com.qyy.jyshop.supple.AbstratService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpellOrderItemsServiceImpl extends AbstratService<SpellOrderItems> implements SpellOrderItemsService {

    @Autowired
    private SpellOrderItemsDao spellOrderItemsDao;

    @Override
    public SpellOrderItems getItemsByOrderId(Long orderId) {
        return spellOrderItemsDao.getItemsByOrderId(orderId);
    }
}
