/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.dao;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;

import com.qyy.jyshop.model.Floor;

/**
 * 描述
 * @author jiangbin
 * @created 2018年3月13日 下午4:30:58
 */
public interface FloorDao extends Mapper<Floor>{

    void batchInsert(List<Floor> floorList);

    void deleteByFloorIds(List<Long> oldFloorIdList);
}
