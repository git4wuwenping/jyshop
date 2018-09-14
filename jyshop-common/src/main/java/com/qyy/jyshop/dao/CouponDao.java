/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.Coupon;
import com.qyy.jyshop.supple.MyMapper;

/**
 * 
 * @author jiangbin
 * @created 2018年4月11日 上午8:41:32
 */
public interface CouponDao extends MyMapper<Coupon> {
    
    /**以下为后台管理方法调用---------------------------------------------------------------------------------------**/
    
    List<Map<String,Object>> selectCouponList(Map<String,Object> params);

    Map<String, Object> selectCouponInfoByCpnId(Long cpnId);

    void updateGrantNumByCpnId(@Param("cpnId") Long cpnId, @Param("grantCount") Integer grantCount);
    
    /**以下为接口方法调用---------------------------------------------------------------------------------**/
    
    List<Map<String,Object>> getCouponList(Map<String,Object> params);
    
    List<Map<String,Object>> getCouponGoodsListByCpnId(Map<String,Object> params);
}
