/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.coupon.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.admin.coupon.service.CouponRelService;
import com.qyy.jyshop.dao.CouponRelDao;
import com.qyy.jyshop.model.CouponRel;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;

/**
 * 描述
 * @author jiangbin
 * @created 2018年4月14日 上午10:11:20
 */
@Service
public class CouponRelServiceImpl extends AbstratService<CouponRel> implements CouponRelService {

    @Autowired
    private CouponRelDao couponRelDao;

    @Override
    public PageAjax<Map<String, Object>> pageRel(String cpnSn, Integer relType, PageAjax<Map<String, Object>> page){
        ParamData params = this.getParamData(page.getPageNo(), page.getPageSize());
        params.put("cpnSn", cpnSn);
        if(relType == 0){
            return this.pageQuery("CouponRelDao.selectCatRel", params); 
        }else if(relType == 1){
            return this.pageQuery("CouponRelDao.selectGoodsRel", params);
        }else if(relType == 2){
            return this.pageQuery("CouponRelDao.selectComRel", params);
        }
        return null;
    }
    
    @Override
    @Transactional
    public String delRel(Long id) {
        this.deleteByID(id);
        return null;
    }

    @Override
    @Transactional
    public String delAllRel(String cpnSn) {
        ParamData params = new ParamData();
        params.put("cpnSn", cpnSn);
        int delBy = this.delBy("CouponRelDao.delRel", params);
        return null;
    }

    @Override
    @Transactional
    public String addRel(Long[] relIdArr, String cpnSn, Integer relType) {
        //新增
        List<CouponRel> couponRelList = new ArrayList<CouponRel>();
        for(Long relId : relIdArr){
            CouponRel couponRel = new CouponRel();
            couponRel.setCpnSn(cpnSn);
            couponRel.setRelId(relId);
            couponRel.setRelType(relType);// 关联类型：0-分类 1-商品 2-供应商
            couponRelList.add(couponRel);
        }
        if(CollectionUtils.isNotEmpty(couponRelList)){
            this.couponRelDao.batchInsert(couponRelList);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> queryGoodsCatRel(String cpnSn) {
        return couponRelDao.queryGoodsCatRel(cpnSn);
    }

    @Override
    public List<Map<String, Object>> queryGoodsCatList() {
        return couponRelDao.queryGoodsCatList();
    }

    

}
