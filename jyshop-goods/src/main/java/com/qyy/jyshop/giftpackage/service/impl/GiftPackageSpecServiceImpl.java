package com.qyy.jyshop.giftpackage.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.GiftPackageSpecDao;
import com.qyy.jyshop.giftpackage.service.GiftPackageSpecService;
import com.qyy.jyshop.model.GiftPackageSpec;
import com.qyy.jyshop.supple.AbstratService;
@Service
public class GiftPackageSpecServiceImpl extends AbstratService<GiftPackageSpec> implements GiftPackageSpecService {
	
	@Autowired
	private GiftPackageSpecDao giftPackageSpecDao;
	@Override
	public List<Map<String, Object>> queryByGpId(Long gpId) {
		List<Map<String, Object>> mapList = giftPackageSpecDao.querySepcValueByGpId(gpId);
		if(CollectionUtils.isEmpty(mapList)) {
			return null;
		}
		String specId = null;
		List<Map<String, Object>> resultList1 = null;
		List<Map<String, Object>> resultList = new ArrayList<>();
		Map map2 = new HashedMap();
		Map map1 = null;
		for (Map map : mapList) {
			String newSpecId = map.get("specId").toString();
			if (MapUtils.isEmpty(map2) || !newSpecId.equals(map2.get("specId").toString())) {
				if (CollectionUtils.isNotEmpty(resultList1) && MapUtils.isNotEmpty(map2)) {
					map2.put("detail", resultList1);
					resultList.add(map2);
					 map2 = new HashedMap();
				}
				map2.put("specId", map.get("specId"));
				map2.put("specName", map.get("specName"));
				resultList1 = new ArrayList<>();
			}
			map1 = new HashedMap();
			map1.put("specValueId", map.get("specValueId"));
			map1.put("specValue", map.get("specValue"));
			resultList1.add(map1);
		}

		map2.put("detail", resultList1);
		resultList.add(map2);
		return resultList;
	}

}
