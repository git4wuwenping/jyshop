package com.qyy.jyshop.giftpackage.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.GiftPackage;
import com.qyy.jyshop.pojo.PageAjax;

public interface GiftPackageService {
	/**
	 * 查询店长礼包
	 * @param pageNo
	 * @param pageSize
	 * @param memberId
	 * @return
	 */
	PageAjax<Map<String, Object>> getGiftPackageByMemberId(Integer pageNo, Integer pageSize);

	Map<String, Object> getGiftPackageByGpId(Long gpId);

	List<Map<String, Object>> selectGiftPackageGalleryByGpId(Long gpId);

}
