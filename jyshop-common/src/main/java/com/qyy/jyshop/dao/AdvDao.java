/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.Adv;
import com.qyy.jyshop.supple.MyMapper;

/**
 * 广告位
 * 
 * @author jiangbin
 * @created 2018年3月15日 下午2:53:16
 */
public interface AdvDao extends MyMapper<Adv> {
    
    List<Map<String,Object>> queryAdvByAdvSpaceId(Long advSpaceId);

}
