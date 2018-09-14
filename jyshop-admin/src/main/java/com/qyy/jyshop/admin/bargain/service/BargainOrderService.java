/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.bargain.service;

import com.qyy.jyshop.model.BargainOrder;
import com.qyy.jyshop.pojo.PageAjax;

import java.util.Map;

/**
 * 描述
 *
 * @author wu
 * @created 2018/4/17 10:23
 */
public interface BargainOrderService {

    PageAjax<Map<String, Object>> page(PageAjax<BargainOrder> page);

    BargainOrder queryByID(Long id);

    String deliverySave(Map<String, Object> params);

    String doCancleOrder(Long orderId);
}
