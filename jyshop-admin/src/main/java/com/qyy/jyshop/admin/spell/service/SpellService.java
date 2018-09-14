package com.qyy.jyshop.admin.spell.service;

import com.qyy.jyshop.model.Spell;
import com.qyy.jyshop.pojo.PageAjax;

import java.util.Map;

public interface SpellService {

    /**
     * 分页查询拼团列表
     * @param page
     * @return
     */
    public PageAjax<Map<String,Object>> pageSpell(PageAjax<Map<String,Object>> page);

    /**
     * 获取拼团详情
     * @param spellId
     * @return
     */
    public Spell getSpellDetail(Long spellId);
}
