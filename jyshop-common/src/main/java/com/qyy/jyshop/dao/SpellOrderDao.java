package com.qyy.jyshop.dao;

import com.qyy.jyshop.model.SpellOrder;
import com.qyy.jyshop.supple.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SpellOrderDao extends MyMapper<SpellOrder> {

    List<Map<String,Object>> getSpellOrderByParams(Map<String,Object> params);

    public Map<String, Object> getOrderDetail(@Param("spellId")Long spellId, @Param("memberId")Long memberId);

    public SpellOrder getOrderByOrderSn(@Param("orderSn")String orderSn);
}
