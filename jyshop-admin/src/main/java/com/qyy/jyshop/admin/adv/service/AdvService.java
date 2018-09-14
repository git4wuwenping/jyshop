/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.adv.service;

import java.util.Map;

import com.qyy.jyshop.model.Adv;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;

/**
 * 描述
 * @author jiangbin
 * @created 2018年3月15日 下午2:57:10
 */
public interface AdvService {


    AjaxResult updateAdvUsed(Long id);

    String doEditAdv(Adv adv);

    String delAdById(Long id);

    PageAjax<Map<String,Object>> pageAdv(PageAjax<Map<String,Object>> page, Long advSpaceId);

    Adv queryAdvById(Long id);

    String doAddAdv(Adv adv);

}
