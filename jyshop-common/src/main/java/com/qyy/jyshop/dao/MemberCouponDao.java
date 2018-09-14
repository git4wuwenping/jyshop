/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.MemberCoupon;
import com.qyy.jyshop.supple.MyMapper;

/**
 * 
 * @author jiangbin
 * @created 2018年4月11日 上午8:41:32
 */
public interface MemberCouponDao extends MyMapper<MemberCoupon> {

    /**以下为后台管理方法调用---------------------------------------------------------------------------------------**/
    void batchInsert(List<MemberCoupon> list);
    
    List<Map<String,Object>> selectMemberCouponList(Long cpnId);
    
    /**以下为接口方法调用---------------------------------------------------------------------------------**/

    List<Map<String, Object>> getUsableCouponRecently(Long memberId);
    
    List<Map<String, Object>> getMemberCouponList(Long memberId,Integer getFlag);
    
}
