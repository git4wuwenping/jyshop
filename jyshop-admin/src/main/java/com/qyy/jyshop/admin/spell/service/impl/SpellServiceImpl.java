package com.qyy.jyshop.admin.spell.service.impl;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.spell.service.SpellService;
import com.qyy.jyshop.dao.SpellDao;
import com.qyy.jyshop.model.Spell;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SpellServiceImpl extends AbstratService<Spell> implements SpellService {

    @Autowired
    private SpellDao spellDao;

    @ServiceLog("查询拼团分页列表")
    @Override
    public PageAjax<Map<String,Object>> pageSpell(PageAjax<Map<String,Object>> page) {
        ParamData params = this.getParamData(page.getPageNo(),page.getPageSize());
        return this.pageQuery("SpellDao.getSpellByParams", params);
    }

    @ServiceLog("获取拼团详情")
    @Override
    public Spell getSpellDetail(Long spellId){
        return spellDao.selectByPrimaryKey(spellId);
    }
}
