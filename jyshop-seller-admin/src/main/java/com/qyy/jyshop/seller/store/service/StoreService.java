package com.qyy.jyshop.seller.store.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.qyy.jyshop.model.ProductStore;

public interface StoreService {
	/**
	    * 根据goodsId查询商品库存回显编辑
	    * @author Tonny
	    * @date 2018年3月23日
	    */
	   public List<Map<String, Object>> selectProductStoreByGoodsId(Long goodsId);
	/**
	 * 修改货品库存
	 * @author Tonny
	 * @date 2018年3月23日
	 */
	public String editStore(String dataJson);
	
	
}
