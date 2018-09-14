package com.qyy.jyshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.GoodsType;
import com.qyy.jyshop.supple.MyMapper;

public interface GoodsTypeDao extends MyMapper<GoodsType>{
    
    /**
     * 根据供应商Id获取类型
     * @author hwc
     * @created 2018年1月5日 上午10:01:59
     * @param comId
     * @return
     */
    List<GoodsType> selectByComId(@Param("comId")Integer comId);
    
    /**
     * 修改类型规格
     * @author hwc
     * @created 2018年1月3日 下午4:46:35
     * @param typeId
     * @param comId
     * @param specIds
     */
    int updateOfSpecIds(@Param("typeId")Long typeId,@Param("comId")Integer comId,@Param("specIds")String specIds);
}
