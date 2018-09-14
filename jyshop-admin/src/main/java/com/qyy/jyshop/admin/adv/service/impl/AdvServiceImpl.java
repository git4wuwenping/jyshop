/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.adv.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.admin.adv.service.AdvService;
import com.qyy.jyshop.model.Adv;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;

/**
 * 描述
 * 
 * @author jiangbin
 * @created 2018年3月15日 下午3:30:23
 */
@Service
public class AdvServiceImpl extends AbstratService<Adv> implements AdvService {

    @Override
    public PageAjax<Map<String, Object>> pageAdv(PageAjax<Map<String, Object>> page, Long advSpaceId) {
        ParamData params = getParamData(page.getPageNo(), page.getPageSize());
        params.put("advSpaceId", advSpaceId);
        return this.pageQuery("AdvDao.queryAdvByAdvSpaceId", params);
    }

    @Override
    public Adv queryAdvById(Long id) {
        return this.queryByID(id);
    }

    @Override
    @Transactional
    public String doAddAdv(Adv adv) {
        adv.setUsed((short)0);
        this.insert(adv);
        return null;
    }

    @Override
    @Transactional
    public AjaxResult updateAdvUsed(Long id) {
        Adv adv = this.queryByID(id);
        adv.setUsed(adv.getUsed().intValue() == 0 ? (short) 1 : (short) 0);
        return this.update(adv);
    }

    @Override
    @Transactional
    public String doEditAdv(Adv adv) {
        this.update(adv);
        return null;
    }

    @Override
    @Transactional
    public String delAdById(Long id) {
        this.delById(id);
        return null;
    }

}
