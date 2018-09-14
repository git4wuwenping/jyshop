/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.adv.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.adv.service.AdvSpaceService;
import com.qyy.jyshop.dao.AdvDao;
import com.qyy.jyshop.model.Adv;
import com.qyy.jyshop.model.AdvSpace;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.supple.AbstratService;

/**
 * 描述
 * @author jiangbin
 * @created 2018年3月15日 下午3:31:20
 */
@Service
public class AdvSpaceServiceImpl extends AbstratService<AdvSpace> implements AdvSpaceService{
    
    @Autowired
    private AdvDao advDao;

    @Override
    public PageAjax<AdvSpace> pageAdvSpace(PageAjax<AdvSpace> page, AdvSpace advSpace) {
        Example example = new Example(AdvSpace.class);
        Criteria createCriteria = example.createCriteria();
        if (advSpace.getType() != null) {
            createCriteria.andEqualTo("type", advSpace.getType());
        }
        if (advSpace.getTitle() != null) {
            createCriteria.andLike("title", "%"+advSpace.getTitle()+"%");
        }
        return this.queryPageListByExample(page.getPageNo(), page.getPageSize(), example);
    }

    @Override
    @Transactional
    public String addAdvSpace(AdvSpace advSpace) {
        advSpace.setUsed((short)0);
        this.insert(advSpace);
        return null;
    }

    @Override
    public AdvSpace queryAdvSpaceById(Long id) {
        return this.queryByID(id);
    }

    @Override
    @Transactional
    public String doEditAdvSpace(AdvSpace advSpace) {
        this.update(advSpace);
        
        return null;
    }

    @Override
    @Transactional
    public String doDelAdvSpace(Long id) {
        this.delById(id);
        Example example = new Example(Adv.class);
        example.createCriteria().andEqualTo("advSpaceId", id);
        advDao.deleteByExample(example);
        return null;
    }

    @Override
    @Transactional
    public AjaxResult updateAdvSpaceUsed(Long id) {
        AdvSpace advSpace = this.queryByID(id);
        advSpace.setUsed(advSpace.getUsed().intValue()==0?(short)1:(short)0);
        return this.update(advSpace);
    }

}
