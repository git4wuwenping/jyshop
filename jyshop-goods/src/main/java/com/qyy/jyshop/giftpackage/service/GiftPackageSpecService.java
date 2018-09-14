package com.qyy.jyshop.giftpackage.service;

import java.util.List;
import java.util.Map;

public interface GiftPackageSpecService {

	List<Map<String, Object>> queryByGpId(Long gpId);

}
