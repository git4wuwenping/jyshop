package com.qyy.jyshop.dao;

import com.qyy.jyshop.model.GiftPackage;
import com.qyy.jyshop.supple.MyMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface GiftPackageDao extends MyMapper<GiftPackage> {
	
    /**
     * 根据条件查询礼包列表
     * @param params
     * @return
     */
    List<Map<String,Object>> selectGiftPackageByParams(Map<String,Object> params);
    /**
     * 礼包商品上下架
     * @author Tonny
     * @date 2018年3月21日
     */
	void doGiftPackageSalesExamine(@Param("gpId")Long gpId, @Param("marketEnable")Integer marketEnable);
}