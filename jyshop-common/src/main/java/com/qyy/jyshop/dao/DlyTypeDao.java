package com.qyy.jyshop.dao;

import com.qyy.jyshop.model.DlyType;
import com.qyy.jyshop.supple.MyMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DlyTypeDao extends MyMapper<DlyType> {
    

    /**
     * 根据供应商Id获取配送模板列表
     * @param map
     * @return
     */
    Map selectDlyTypeByParams(Map map);
    
    /**
     * 根据模板Id获取配送模板
     * @author hwc
     * @created 2018年3月29日 下午4:06:14
     * @param typeId
     * @return
     */
    DlyType selectByTypeId(@Param("typeId")Long typeId);
    
    /**
     * 根据供应商Id获取配送模板
     * @author hwc
     * @created 2018年2月3日 下午3:18:18
     * @param comId
     * @return
     */
    List<DlyType> selectByComId(@Param("comId")Integer comId);
}