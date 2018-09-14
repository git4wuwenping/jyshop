package com.qyy.jyshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.DlyTypeArea;
import com.qyy.jyshop.supple.MyMapper;

public interface DlyTypeAreaDao extends MyMapper<DlyTypeArea> {
    
    /**
     * 根据方式Id获取地区配置
     * @author hwc
     * @created 2018年2月3日 下午3:34:39
     * @param typeId
     * @return
     */
    List<DlyTypeArea> selectByTypeId(@Param("typeId")Long typeId);
}