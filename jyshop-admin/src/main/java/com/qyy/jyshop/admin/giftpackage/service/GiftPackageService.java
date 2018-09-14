package com.qyy.jyshop.admin.giftpackage.service;

import com.qyy.jyshop.model.GiftPackage;
import com.qyy.jyshop.pojo.PageAjax;

import java.util.Map;

public interface GiftPackageService {

	PageAjax<Map<String, Object>> pageGiftPackage(PageAjax<Map<String, Object>> page);

    String doAddGiftPackage(Map<String, Object> map);

    GiftPackage queryByGpId(Long gpId);

    String editGiftPackage(Map<String, Object> map);

    String doDelGiftPackage(Long gpId);

	String doGiftPackageSalesExamine(Long gpId, Integer valueOf);
}
