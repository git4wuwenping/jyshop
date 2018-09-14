package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.GiftPackageGallery;
import com.qyy.jyshop.supple.MyMapper;

public interface GiftPackageGalleryDao extends MyMapper<GiftPackageGallery> {

	List<Map<String, Object>> selectGiftPackageGalleryByGpId(@Param("gpId")Long gpId);

}