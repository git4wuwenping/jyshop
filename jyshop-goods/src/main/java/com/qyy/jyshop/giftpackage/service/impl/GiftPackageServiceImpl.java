package com.qyy.jyshop.giftpackage.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.conf.ShopBaseConf;
import com.qyy.jyshop.dao.GiftPackageGalleryDao;
import com.qyy.jyshop.giftpackage.service.GiftPackageService;
import com.qyy.jyshop.model.GiftPackage;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.EntityReflectUtil;
@Service
public class GiftPackageServiceImpl extends AbstratService<GiftPackage> implements GiftPackageService {
	@Autowired
	private GiftPackageGalleryDao giftPackageGalleryDao;
	@Autowired
	private ShopBaseConf shopBaseConf;
	
	@Override
	public PageAjax<Map<String, Object>> getGiftPackageByMemberId(Integer pageNo, Integer pageSize) {
		ParamData params = this.getParamData(pageNo,pageSize);
		ProfitParam profitParam = shopBaseConf.getParamConfig(ProfitParam.class, "shopParam_profitParam");
        BigDecimal redPointRatio =profitParam.getProfitMemberSecReal().compareTo(BigDecimal.ZERO) > 0 ? profitParam.getProfitMemberSecReal() : BigDecimal.ZERO;
        params.put("redPointRatio", redPointRatio);
		return this.pageQuery("GiftPackageDao.selectGiftPackageByParams", params);
	}

	@Override
	public Map<String, Object> getGiftPackageByGpId(Long gpId) {
	    ProfitParam profitParam = shopBaseConf.getParamConfig(ProfitParam.class, "shopParam_profitParam");
        BigDecimal redPointRatio =profitParam.getProfitMemberSecReal().compareTo(BigDecimal.ZERO) > 0 ? profitParam.getProfitMemberSecReal() : BigDecimal.ZERO;
		GiftPackage giftPackage = this.queryByID(gpId);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap = EntityReflectUtil.toMap(giftPackage);
		if(redPointRatio.compareTo(BigDecimal.ZERO) == 0){
		    returnMap.put("redPoint", 0);
		}else{
		    returnMap.put("redPoint",giftPackage.getPrice().subtract(giftPackage.getCost()).multiply(redPointRatio));
		}
		return returnMap;
	}

	@Override
	public List<Map<String, Object>> selectGiftPackageGalleryByGpId(Long gpId) {
		
		return giftPackageGalleryDao.selectGiftPackageGalleryByGpId(gpId);
	}

}
