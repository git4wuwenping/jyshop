package com.qyy.jyshop.goods.server;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.Product;
import com.qyy.jyshop.pojo.PageAjax;

public interface GoodsService {
	/**
	 * 查询店长礼包
	 * @return
	 */
	PageAjax<Map<String, Object>> selectShopownerGoods(Integer pageNo, Integer pageSize);

	PageAjax<Map<String,Object>> homeGoodsList(Integer pageNo,Integer pageSize);

	PageAjax<Map<String, Object>> hotGoods(Integer pageNo, Integer pageSize);

	Map<String,Object> getGoodsByGoodsId(Long goodsId);

	PageAjax<Map<String, Object>> shopStoreGoodsList(Integer pageNo, Integer pageSize, Integer shopStoreId, Integer comId);

	int findBondByComId(Integer comId);

	Product getProductByGoodsId(Long goodsId);

	List<Map<String,Object>> selectGoodsGalleryByGoodsId(Long goodsId);
	/**
	 * 根据条件查询商品
	 * @param goodName
	 * @param shopStoreId 
	 * @return
	 */
	PageAjax<Map<String, Object>> searchGoodsByParams(String goodName,Integer pageNo, Integer pageSize, Integer shopStoreId);

	






}
