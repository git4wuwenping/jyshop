package com.qyy.jyshop.shopstore.service;


import java.util.Map;

import com.qyy.jyshop.model.ShopStore;

public interface ShopStoreService {

	//ShopStore getShopStoreByGoodsId(int shopStoreId);

	ShopStore shopStoreInfo(Integer shopStoreId);

	ShopStore findShopStoreById(Map<String,Object> goods);

}
