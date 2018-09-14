package com.qyy.jyshop.admin.spell.service;

import com.qyy.jyshop.model.SpellOrder;
import com.qyy.jyshop.pojo.PageAjax;

import java.util.Map;

public interface SpellOrderService {

    public PageAjax<Map<String,Object>> pageSpellOrder(PageAjax<Map<String,Object>> page);

    public SpellOrder queryByID(Long orderId);
}
