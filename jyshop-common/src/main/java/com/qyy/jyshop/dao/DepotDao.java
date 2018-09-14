package com.qyy.jyshop.dao;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.Depot;
import com.qyy.jyshop.supple.MyMapper;

public interface DepotDao extends MyMapper<Depot>{

    /**
     * 根据仓库ID获取供应商Id
     * @author hwc
     * @created 2017年12月16日 上午10:52:42
     * @param id
     * @return
     */
    Integer selectComIdById(@Param("id")Long id);
    
    /**
     * 根据供应商ID获取仓库信息
     * @author hwc
     * @created 2018年1月25日 下午4:58:35
     * @param comId
     * @return
     */
    Depot selectByComId(@Param("comId")Integer comId);
}
