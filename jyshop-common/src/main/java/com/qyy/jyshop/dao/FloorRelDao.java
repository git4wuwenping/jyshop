/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import tk.mybatis.mapper.common.Mapper;

import com.qyy.jyshop.model.FloorRel;

/**
 * 描述
 * 
 * @author jiangbin
 * @created 2018年3月13日 下午4:30:58
 */
public interface FloorRelDao extends Mapper<FloorRel> {

    List<Map<String, Object>> queryFloorGoodscatListByFloorId(Long floorId);

    List<Map<String, Object>> queryFloorGoodsListByFloorId(Long floorId);

    void batchInsert(List<FloorRel> floorRelList);

    void deleteByFloorId(Long floorId);

    List<Map<String, Object>> queryGoodsCatListByParentId(Long floorId);

    List<Map<String, Object>> queryGoodsListByParentIds(Long floorId);

    List<Map<String, Object>> queryFloorGoodsCatRel(Long floorId);

    List<Map<String, Object>> queryGoodsCatList();

    void deleteByFloorIds(List<Long> oldFloorIdList);

    List<Map<String, Object>> queryFloorGoodsRel(Long floorId);

}
