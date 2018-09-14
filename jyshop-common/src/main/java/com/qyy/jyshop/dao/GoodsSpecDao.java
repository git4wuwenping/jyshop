package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.GoodsSpec;
import com.qyy.jyshop.supple.MyMapper;
import com.qyy.jyshop.vo.SpecVo;

public interface GoodsSpecDao extends MyMapper<GoodsSpec>{

    List<SpecVo> selectSpecsByGoodsId(@Param("goodsId")Long goodsId);
    
    /**
     * 根据商品ID删除货品类型规格
     * @author hwc
     * @created 2018年1月4日 下午7:52:34
     * @param goodsId
     */
    void deleteByGoodsId(@Param("goodsId") Long goodsId);
    
    /**
     * 根据货品IDS删除货品类型规格
     * @author hwc
     * @created 2018年1月4日 下午7:50:15
     * @param productIdList
     */
    void deleteByProductIds(List<Long> productIdList);
    
    List<Map<String,Object>> querySepcValueByGoodsId(@Param("goodsId")Long goodsId);
}
