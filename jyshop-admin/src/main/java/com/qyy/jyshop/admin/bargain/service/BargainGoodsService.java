/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.bargain.service;

import com.qyy.jyshop.model.BargainGoods;
import com.qyy.jyshop.model.BargainRecomGoods;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 描述
 *
 * @author wu
 * @created 2018/4/11 17:00
 */
public interface BargainGoodsService {

    PageAjax<Map<String, Object>> pageList(PageAjax<BargainGoods> page);

    PageAjax<Map<String, Object>> recommendPage(PageAjax<BargainRecomGoods> page);

    AjaxResult saveEntity(BargainGoods entity, String endDateStr, BigDecimal priceMinx);

    AjaxResult deleteByID(Long id);

    BargainGoods queryByID(Long id);

    AjaxResult updateEntity(BargainGoods entity, String endDateStr, BigDecimal priceMinx);

    /**
     * 操作砍价活动状态
     * @param id
     * @param open
     * @return
     */
    AjaxResult deal(Long id, Integer open);

    String recommendAdd(Map<String, String> map);

    /**
     * 删除砍价推荐商品
     * @param id 商品id
     * @return
     */
    AjaxResult delRecommendByID(Long id);
}
