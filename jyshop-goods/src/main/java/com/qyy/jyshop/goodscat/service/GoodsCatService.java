package com.qyy.jyshop.goodscat.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.pojo.PageAjax;

public interface GoodsCatService {

	List<Map> getGoodsCatData();

	PageAjax<Map<String,Object>> getGoodsByCatId(Integer catId,Integer pageNo,Integer pageSize);
	
	/**
	 * 商品排序
	 * @param orderType
	 * @param sort
	 * @param catId
	 * @param shopStoreId 
	 * @return
	 */
	PageAjax<Map<String,Object>> orderGoods(String orderType, Integer sort, Integer catId, Integer shopStoreId,Integer pageNo,Integer pageSize);

	/**
	 * 查询一级分类下的所有商品
	 * @param pageNo
	 * @param pageSize
	 * @param catId
	 * @return
	 */
	PageAjax<Map<String, Object>> selectGoodsByFirCatId(Integer pageNo, Integer pageSize, Integer catId);


}
