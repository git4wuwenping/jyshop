package com.qyy.jyshop.admin.point.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.admin.point.service.PointParamService;
import com.qyy.jyshop.model.PointParam;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class PointParamServiceImpl extends AbstratService<PointParam> implements PointParamService {

	@Override
	public PointParam getPointParam() {
		List<PointParam> queryAll = this.queryAll();
		if (CollectionUtils.isNotEmpty(queryAll)) {
			return queryAll.get(0);
		}
		return null;
	}

	@Override
	@Transactional
	public String editPointParam(PointParam pointParam) {
		if (pointParam == null)
			return "获取积分设置失败";
		this.updateByID(pointParam);
		return null;
	}

}
