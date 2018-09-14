package com.qyy.jyshop.dao;

import com.qyy.jyshop.model.GiftPackageProduct;
import com.qyy.jyshop.supple.MyMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface GiftPackageProductDao extends MyMapper<GiftPackageProduct>{

    Map<String,Object> selectByGpProductId(@Param("productId")Long productId,@Param("marketEnable")Integer marketEnable);

    List<Long> selectOfGpProductId(@Param("gpId")Long gpId);
    
    List<Map> selectByGpId(@Param("gpId")Long gpId);
    
	List<Map<String, Object>> selectGiftPackageProductByGpProductId(@Param("gpId")Long gpId);
	
	void deleteByGpProductIds(List<Long> productIdList);
}
