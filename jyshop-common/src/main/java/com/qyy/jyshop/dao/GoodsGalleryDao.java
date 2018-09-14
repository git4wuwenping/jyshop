package com.qyy.jyshop.dao;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.GoodsGallery;
import com.qyy.jyshop.supple.MyMapper;

public interface GoodsGalleryDao extends MyMapper<GoodsGallery> {
    
    
    void deleteByGoodsId(@Param("goodsId")Long goodsId);
}