/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.bargain.service;

import com.qyy.jyshop.model.BargainOrderItems;
import com.qyy.jyshop.model.BargainRecomGoods;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.DataTablePage;
import com.qyy.jyshop.pojo.PageAjax;

import java.util.Map;

/**
 * 描述
 *
 * @author wu
 * @created 2018/4/11 17:00
 */
public interface BargainOrderItemsService {

    PageAjax<Map<String, Object>> page(PageAjax<BargainOrderItems> page);

    BargainOrderItems queryByOrderId(Long orderId);

    DataTablePage<Map<String,Object>> pageDataTable(Map<String, Object> map);
}
