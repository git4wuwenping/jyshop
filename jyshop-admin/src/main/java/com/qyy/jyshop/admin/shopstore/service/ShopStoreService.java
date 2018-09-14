package com.qyy.jyshop.admin.shopstore.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.ShopStore;
import com.qyy.jyshop.pojo.PageAjax;

public interface ShopStoreService {
	
	
	/**
	 * 根据供应商id查找店铺
	 * @param integer 
	 * @return
	 */
	List<ShopStore> findAllStores();


	/**
	 * 根据店铺id查找店铺
	 * @param goodsId
	 * @return
	 */
	ShopStore findStoreByGoodId(int shopStoreId);

	/**
	 * 查询直营店铺列表
	 * @author Tonny
	 * @date 2018年3月5日
	 */
	PageAjax<Map<String, Object>> selectShopStoreByParams(PageAjax<ShopStore> page);
	
	/**
	 * 根据id查询店铺
	 * @author Tonny
	 * @date 2018年3月5日
	 */
	ShopStore selectShopStoreById(int shopStoreId);

	/**
	 * 
	 * @author Tonny
	 * @param comId 
	 * @date 2018年3月14日
	 */
	List<ShopStore> findAllDirShopStores(Integer comId);

	/**
	 * 校验店铺名
	 * @author Tonny
	 * @date 2018年3月14日
	 */
	String selectShopStoreByName(String shopStoreName);
	
	/**
	 * 添加店铺
	 * @author Tonny
	 * @date 2018年3月5日
	 */
	String addShopStore(Map<String, Object> map);
	
	/**
	 * 修改店铺
	 * @author Tonny
	 * @date 2018年3月5日
	 */
	String editShopStore(Map<String, Object> map);

	/**
	 * 删除店铺
	 * @author Tonny
	 * @date 2018年3月5日
	 */
	String deleteShopStore(int shopStoreId);

	/**
	 * 停用/启用
	 * @author Tonny
	 * @date 2018年3月14日
	 */
	String isDisabledExamine(int shopStoreId, Integer valueOf);
	
	/**
	 * 校验店铺信息
	 * @author Tonny
	 * @date 2018年4月2日
	 */
	String checkShopStoreInformation(String shopStoreInformation);



	/**
	 * 分页查询店铺及店铺下的商户商品列表
	 * @param page
	 * @param store
	 * @return
	 */
	/*PageAjax<ShopStore> pageStoresAjax(PageAjax<ShopStore> page);*/

}
