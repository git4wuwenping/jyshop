package com.qyy.jyshop.admin.spell.service;

import com.qyy.jyshop.model.SpellRecomGoods;
import com.qyy.jyshop.pojo.PageAjax;

import java.util.Map;

public interface SpellRecomGoodsService {

    public PageAjax<Map<String, Object>> recommendPage(PageAjax<SpellRecomGoods> page);

    public String recommendAdd(Map<String, String> map);
}
