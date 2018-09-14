/*
 * Copyright (c) 2005, 2017, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.Address;
import com.qyy.jyshop.supple.MyMapper;



/**
 * 地区
 * @author hwc
 * @created 2017年11月24日 下午3:33:30
 */
public interface AddressDao extends MyMapper<Address>{
    
    /**
     * 根据地区Id查询地区名称
     * @author hwc
     * @created 2017年11月25日 上午10:07:53
     * @param addressId
     * @return
     */
    String selectAddressName(@Param("addressId")String addressId);
    
    /**
     * 根据父ID查询子地区 
     * @author hwc
     * @created 2017年11月25日 下午3:25:05
     * @param parentId
     * @return
     */
    List<Address> selectByParentId(@Param("parentId")String parentId);
    
    /**
     * 查询所有的省
     * @author hwc
     * @created 2017年11月24日 下午3:54:29
     * @return
     */
    List<Address> selectProvince();
    
    /**
     * 查询所有的市
     * @author hwc
     * @created 2017年11月24日 下午4:46:23
     * @return
     */
    List<Address> selectCity();
    
    /**
     * 查询所有的区
     * @author hwc
     * @created 2017年11月24日 下午4:46:26
     * @return
     */
    List<Address> selectDistrict();
}
