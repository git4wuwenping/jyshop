package com.qyy.jyshop.dao;

import com.qyy.jyshop.model.SpellRecomGoods;
import com.qyy.jyshop.supple.MyMapper;

import java.util.List;
import java.util.Map;

public interface SpellRecomGoodsDao extends MyMapper<SpellRecomGoods> {

    /**
     * 获取拼团推荐商品
     * @return
     */
    public List<Map<String,Object>> getGoodsList();

    /**
     * 批量添加推荐商品
     * @param list
     */
    public void batchInsert(List<SpellRecomGoods> list);
}
