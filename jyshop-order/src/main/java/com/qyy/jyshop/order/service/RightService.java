/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.order.service;

import com.qyy.jyshop.model.Delivery;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.RightOrder;
import com.qyy.jyshop.pojo.PageAjax;

import java.math.BigDecimal;
import java.util.Map;
/**
 * 描述
 *
 * @author wu
 * @created 2018/3/28 17:34
 */
public interface RightService {

    Member getMember(String paramString);

    RightOrder submit(RightOrder paramRightOrder, Long[] paramArrayOfLong);

    Map<String, Object> queryDetailById(Long paramLong);

    Map<String, Object> cancelById(Long paramLong);

    PageAjax<Map<String, Object>> page(PageAjax<RightOrder> paramPageAjax, String paramString, BigDecimal secondsRefundAutoAgree, BigDecimal secondsBackAutoAgree);

    Map<String, Object> queryDelivery(Long paramLong);

    Map<String, Object> submitDelivery(Delivery paramDelivery);
}
