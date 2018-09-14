package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.GiftPackageSpec;
import com.qyy.jyshop.supple.MyMapper;

public interface GiftPackageSpecDao extends MyMapper<GiftPackageSpec> {

	List<Map<String, Object>> querySepcValueByGpId(@Param("gpId")Long gpId);
	
	void deleteByGpId(@Param("gpId")Long gpId);
}