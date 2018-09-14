package com.qyy.jyshop.shopstore.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.ShopStoreDao;
import com.qyy.jyshop.model.ShopStore;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.shopstore.service.ShopStoreService;
import com.qyy.jyshop.supple.AbstratService;
@Service
public class ShopStoreServiceImpl extends AbstratService<ShopStore> implements ShopStoreService{

	@Autowired
	private ShopStoreDao shopStoreDao;
	/*@Override
	public ShopStore getShopStoreByGoodsId(int shopStoreId) {
		//ParamData params = this.getParamData(pageNo,pageSize);
		//PageAjax<Map<String,Object>> pageQuery = this.pageQuery("ShopStoreDao.selectGoodListByShopId", params);
		List<Map<String, Object>> ShopStoreGoodsList = shopStoreDao.selectGoodListByShopId(shopStoreId);
		PageAjax<Map<String,Object>> pageQuery = new PageAjax<Map<String, Object>>(ShopStoreGoodsList);
		ShopStore shopStore = shopStoreDao.selectShopStoreById(shopStoreId);
		return shopStore;
	}
*/
	@Override
	public ShopStore shopStoreInfo(Integer shopStoreId){
		return shopStoreDao.shopStoreInfo(shopStoreId);
	}

	@Override
	public ShopStore findShopStoreById(Map<String,Object> goods) {
		//商户
		int comId= (int) goods.get("comId");
		if(comId!=0){	
			int shopStoreId = 0;
			List<ShopStore> list = shopStoreDao.findStoreByComId(comId,shopStoreId);
			return list.get(0);
		}else{	//自营
			int shopStoreId = (int) goods.get("shopStoreId");
			List<ShopStore> list = shopStoreDao.findStoreByComId(comId,shopStoreId);
			return list.get(0);
		}
		/*ParamData params = new ParamData();
		int comId= (int) goods.get("comId");
		int shopStoreId = (int) goods.get("shopStoreId");
		if(shopStoreId!=0){	
			params.put("comId", comId);
			List<ShopStore> shopStoreList = shopStoreDao.findStoreByComId(comId,(int) goods.get("shopStoreId"));
			return shopStoreList.get(0);
		}else{//商户
			//params.put("shopStoreId", goods.get("shopStoreId"));
			List<ShopStore> list = shopStoreDao.findStoreByComId(comId);
			return list.get(0);
		}*/
		//PageAjax<Map<String,Object>> pageAjax = this.pageQuery("ShopStoreDao.findStoreByComId", params);
		//List<Map<String,Object>> rows = pageAjax.getRows();
		//return rows.get(0);
		//return shopStoreList.get(0);
	}

}
