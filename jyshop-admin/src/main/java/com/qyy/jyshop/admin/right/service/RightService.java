/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.right.service;

import com.qyy.jyshop.model.RightOrder;
import com.qyy.jyshop.pojo.PageAjax;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 维权业务接口
 * @author wu
 * @created 2018/3/29 11:29
 */
public interface RightService
{
    /**
     * 获取维权单列表
     * @param paramPageAjax
     * @return
     */
    PageAjax<Map<String, Object>> pageList(PageAjax<RightOrder> paramPageAjax);

    /**
     * 维权订单详情
     * @param paramString
     * @return
     */
    Map<String, Object> queryDetailById(Long paramString);

    /**
     * 退款确认退款
     * @param id
     * @return
     */
    String back(Long id);

    /**
     * 退货退款确认收货确认付款
     * @param id
     * @return
     */
    String revGoods(Long id);

    /**
     * 维权订单详情处理
     * @param paramString1
     * @param paramBoolean
     * @param paramString2
     * @return
     */
    String deal(Long paramString1, boolean paramBoolean, String paramString2);

    /**
     * 买家发起退款申请超过天数未处理
     * @param time
     * @return
     */
    void autoTuikuan(BigDecimal time);

    /**
     * 根据维权单号查找
     * @param serial
     * @return
     */
    RightOrder findBySerial(String serial);
}