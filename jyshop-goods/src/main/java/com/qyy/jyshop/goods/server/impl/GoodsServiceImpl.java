package com.qyy.jyshop.goods.server.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.conf.ShopBaseConf;
import com.qyy.jyshop.dao.GoodsDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.goods.server.GoodsService;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.Product;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

@Service 
public class GoodsServiceImpl extends AbstratService<Goods> implements GoodsService {
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private ShopBaseConf shopBaseConf;
	
	@Override
	public PageAjax<Map<String,Object>> homeGoodsList(Integer pageNo,Integer pageSize) {
		ParamData params = this.getParamData(pageNo,pageSize);
		ProfitParam profitParam = shopBaseConf.getParamConfig(ProfitParam.class, "shopParam_profitParam");
		if(profitParam.getProfitMemberSecReal().compareTo(BigDecimal.ZERO) >0){
		    params.put("redPointRatio", profitParam.getProfitMemberSecReal());
		}else{
		    params.put("redPointRatio", 0);
		}
		return this.pageQuery("GoodsDao.selectGoodsList",params);
		
	}

	@Override
	public PageAjax<Map<String, Object>> hotGoods(Integer pageNo, Integer pageSize) {
		
		if(StringUtil.isEmpty(pageNo))
			throw new AppErrorException("页码不能为空");
		
		if( pageNo>3)
			return null;
		
		ParamData params = this.getParamData(pageNo,pageSize);
		ProfitParam profitParam = shopBaseConf.getParamConfig(ProfitParam.class, "shopParam_profitParam");
        if(profitParam.getProfitMemberSecReal().compareTo(BigDecimal.ZERO) >0){
            params.put("redPointRatio", profitParam.getProfitMemberSecReal());
        }else{
            params.put("redPointRatio", 0);
        }
		return this.pageQuery("GoodsDao.selectGoodListOrderByCounts", params);
	}

	@Override
	public Map<String,Object> getGoodsByGoodsId(Long goodsId) {
	    ProfitParam profitParam = shopBaseConf.getParamConfig(ProfitParam.class, "shopParam_profitParam");
	    BigDecimal redPointRatio =profitParam.getProfitMemberSecReal().compareTo(BigDecimal.ZERO) > 0 ? profitParam.getProfitMemberSecReal() : BigDecimal.ZERO;
	    Map<String, Object> goodsMap = goodsDao.findGoodDetailedById(goodsId,redPointRatio);
	    	//保障标签
	  		String[] ensureTagNameArr =null;
	  		if(!StringUtil.isEmpty(goodsMap.get("ensureTagName"))){
	  			ensureTagNameArr = goodsMap.get("ensureTagName").toString().split("\\|");
	  		}
	  		//商品标签
	  		String[] tagNameArr =null;
	  		if(!StringUtil.isEmpty(goodsMap.get("tagName"))){
	  			tagNameArr = goodsMap.get("tagName").toString().split("\\|");
	  		}
	  		
	  		goodsMap.put("tagName", tagNameArr);
	  		goodsMap.put("ensureTagName", ensureTagNameArr);
	    return goodsMap;
	}

	@Override
	public PageAjax<Map<String, Object>> shopStoreGoodsList(Integer pageNo, Integer pageSize,Integer shopStoreId,Integer comId) {
		ParamData params = this.getParamData(pageNo,pageSize);
		params.put("shopStoreId", shopStoreId);
		params.put("comId", comId);
		ProfitParam profitParam = shopBaseConf.getParamConfig(ProfitParam.class, "shopParam_profitParam");
        if(profitParam.getProfitMemberSecReal().compareTo(BigDecimal.ZERO) >0){
            params.put("redPointRatio", profitParam.getProfitMemberSecReal());
        }else{
            params.put("redPointRatio", 0);
        }
		PageAjax<Map<String,Object>> pageQuery = this.pageQuery("GoodsDao.selectGoodListByShopId", params);
		/*List<Map<String,Object>> getShopStoreGoodsList = goodsDao.selectGoodListByShopId(shopStoreId);
		PageAjax<Map<String,Object>> pageQuery = new PageAjax<Map<String,Object>>(getShopStoreGoodsList);
		pageQuery.setPageNo(pageNo);
		pageQuery.setPageSize(pageSize);*/
		//pageQuery.setRows(getShopStoreGoodsList);
		return pageQuery;
	}

	@Override
	public int findBondByComId(Integer comId) {
		int bond = goodsDao.findBondByComId(comId);
			return bond;
	}

	@Override
	public Product getProductByGoodsId(Long goodsId) {
		
		return goodsDao.getProductByGoodsId(goodsId);
	}

	@Override
	public PageAjax<Map<String, Object>> selectShopownerGoods(Integer pageNo, Integer pageSize) {
		//List<Map<String,Object>> shopownerGoodsList = goodsDao.selectShopownerGoods();
		ParamData params = this.getParamData(pageNo,pageSize);
		ProfitParam profitParam = shopBaseConf.getParamConfig(ProfitParam.class, "shopParam_profitParam");
        if(profitParam.getProfitMemberSecReal().compareTo(BigDecimal.ZERO) >0){
            params.put("redPointRatio", profitParam.getProfitMemberSecReal());
        }else{
            params.put("redPointRatio", 0);
        }
		return this.pageQuery("GoodsDao.selectShopownerGoods", params);
	}

	@Override
	public List<Map<String,Object>> selectGoodsGalleryByGoodsId(Long goodsId) {
		return goodsDao.selectGoodsGalleryByGoodsId(goodsId);
	}

	@Override
	public PageAjax<Map<String, Object>> searchGoodsByParams(String name,Integer pageNo, Integer pageSize,Integer shopStoreId) {
		ParamData params = this.getParamData(pageNo,pageSize);
		params.put("name", name);
		params.put("shopStoreId", shopStoreId);
		ProfitParam profitParam = shopBaseConf.getParamConfig(ProfitParam.class, "shopParam_profitParam");
        if(profitParam.getProfitMemberSecReal().compareTo(BigDecimal.ZERO) >0){
            params.put("redPointRatio", profitParam.getProfitMemberSecReal());
        }else{
            params.put("redPointRatio", 0);
        }
		return this.pageQuery("GoodsDao.searchGoodsByParams", params);
	}


}
