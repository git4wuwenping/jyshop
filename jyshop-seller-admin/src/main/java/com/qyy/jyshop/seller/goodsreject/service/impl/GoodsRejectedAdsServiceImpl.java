/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.seller.goodsreject.service.impl;

import com.qyy.jyshop.model.GoodsRejectedAds;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.seller.common.annotation.ServiceLog;
import com.qyy.jyshop.supple.AbstratService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author wu
 * @created 2018/3/28 17:07
 */
@Service
public class GoodsRejectedAdsServiceImpl extends AbstratService<GoodsRejectedAds>

{
    @ServiceLog("获取退货地址列表(分页_ajax)")
    public PageAjax<Map<String, Object>> pageList(PageAjax<GoodsRejectedAds> page, Map map)
    {
        ParamData params = getParamData(Integer.valueOf(page.getPageNo()), Integer.valueOf(page.getPageSize()));
        int comId = getLoginUserComId().intValue();
        map.put("comId", Integer.valueOf(comId));
        params.putAll(map);
        return pageQuery("GoodsRejectedAdsDao.pageList", params);
    }

    @ServiceLog("保留一个默认")
    @Transactional
    public AjaxResult saveOrUpdate(GoodsRejectedAds entity) {
        GoodsRejectedAds ads = queryByID(entity);
        String used = entity.getUsed();
        if ("1".equals(used))
        {
            Example example = new Example(GoodsRejectedAds.class);
            Criteria createCriteria = example.createCriteria();
            createCriteria.andEqualTo("used", used);
            createCriteria.andEqualTo("comId", getLoginUserComId());
            List<GoodsRejectedAds> lists = queryByExample(example);
            List list = new ArrayList();
            for (GoodsRejectedAds gra : lists) {
                Map user = new HashMap();
                user.put("id", gra.getId());
                list.add(user);
            }
            batUpdate("GoodsRejectedAdsDao.updateForUsed", list);
        }
        if (ads != null) {
            return update(entity);
        }
        return save(entity);
    }
}
