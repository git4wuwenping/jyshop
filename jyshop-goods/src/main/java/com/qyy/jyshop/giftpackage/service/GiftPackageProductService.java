package com.qyy.jyshop.giftpackage.service;

import java.util.List;
import java.util.Map;

public interface GiftPackageProductService {
	/**
     * 根据店长礼包id查询货品
     * @param gpId
     * @return
     */
	public List<Map<String,Object>> queryByGpId(Long gpId);
}
