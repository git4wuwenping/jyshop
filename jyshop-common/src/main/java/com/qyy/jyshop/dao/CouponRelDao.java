/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.CouponRel;
import com.qyy.jyshop.supple.MyMapper;

/**
 * 
 * @author jiangbin
 * @created 2018年4月11日 上午8:41:32
 */
public interface CouponRelDao extends MyMapper<CouponRel> {
    
    void batchInsert(List<CouponRel> list);
    
    List<Map<String, Object>> selectGoodsRel(@Param("cpnSn") String cpnSn);

    List<Map<String, Object>> selectCatRel(@Param("cpnSn") String cpnSn);

    List<Map<String, Object>> selectComRel(@Param("cpnSn") String cpnSn);

    void delRel(Map<String, Object> params);

    List<Map<String, Object>> queryGoodsCatRel(String cpnSn);

    List<Map<String, Object>> queryGoodsCatList();

}
