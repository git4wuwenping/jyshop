package com.qyy.jyshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.SpecValues;
import com.qyy.jyshop.supple.MyMapper;

public interface SpecValuesDao extends MyMapper<SpecValues>{
    
    /**
     * 根据规格ID获取规格值Id
     * @author hwc
     * @created 2018年1月5日 下午4:04:08
     * @param specId
     * @return
     */
    List<Long> selectOfSpecValuesId(@Param("specId")Long specId);
    
    /**
     * 根据规格ID获取规格值
     * @author hwc
     * @created 2018年1月3日 上午9:16:57
     * @param specId
     * @return
     */
    List<SpecValues> selectBySpecId(@Param("specId")Long specId);
    
    /**
     * 批量添加规格值
     * @author hwc
     * @created 2018年1月2日 下午4:22:08
     * @param specValuesList
     */
    void batchInsert(List<SpecValues> specValuesList);
    
    /**
     * 根据规格Id删除规格值
     * @author hwc
     * @created 2018年1月3日 上午11:11:38
     * @param specId
     */
    void delBySpecId(@Param("specId")Long specId);
    
    /**
     * 根据规格值IDS删除规格值
     * @author hwc
     * @created 2018年1月5日 下午4:22:06
     * @param specValuesId
     */
    void deleteBySpecValuesIds(List<Long> specValuesId);
}
