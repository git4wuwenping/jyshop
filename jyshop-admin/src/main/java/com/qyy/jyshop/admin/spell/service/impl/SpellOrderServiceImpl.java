package com.qyy.jyshop.admin.spell.service.impl;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.spell.service.SpellOrderService;
import com.qyy.jyshop.model.SpellOrder;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class SpellOrderServiceImpl extends AbstratService<SpellOrder> implements SpellOrderService {

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @ServiceLog("查询拼团订单分页列表")
    @Override
    public PageAjax<Map<String,Object>> pageSpellOrder(PageAjax<Map<String,Object>> page) {
        ParamData params = this.getParamData(page.getPageNo(),page.getPageSize());
        return this.pageQuery("SpellOrderDao.getSpellOrderByParams", params);
    }

    @Override
    public SpellOrder queryByID(Long orderId) {
        return super.queryByID(orderId);
    }
}
