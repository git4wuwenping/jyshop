package com.qyy.jyshop.bargain.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.qyy.jyshop.bargain.service.BargainService;
import com.qyy.jyshop.model.BargainGoods;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class BargainServiceImpl extends AbstratService<BargainGoods> implements BargainService {


	@Override
	public PageAjax<Map<String, Object>> selectBargainGoodsList(Integer pageNo, Integer pageSize) {
		ParamData params = this.getParamData(pageNo,pageSize);
		PageAjax<Map<String,Object>> bargainGoodsList = this.pageQuery("BargainGoodsDao.selectBargainGoodsList", params);
		return bargainGoodsList;
	}

	@Override
	public PageAjax<Map<String, Object>> selectBargainCarouselMessage(Integer pageNo, Integer pageSize) {
		ParamData params = this.getParamData(pageNo,pageSize);
		PageAjax<Map<String,Object>> bargainCarouselMessageList = this.pageQuery("BargainGoodsDao.selectBargainCarouselMessage", params);
		return bargainCarouselMessageList;
	}

}
