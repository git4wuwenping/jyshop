package com.qyy.jyshop.dao;





import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.ShopStore;
import com.qyy.jyshop.supple.MyMapper;

public interface ShopStoreDao extends MyMapper<ShopStore>{

	List<ShopStore> findStoreByComId(@Param("comId")int comId,@Param("shopStoreId")int shopStoreId);


	List<Map<String, Object>> selectGoodListByShopId(@Param("shopStoreId")int shopStoreId);

	ShopStore shopStoreInfo(@Param("shopStoreId")Integer shopStoreId);

	//ShopStore selectShopStoreById(int shopStoreId);
	/**
	 * 查询所有直营店铺
	 * @author Tonny
	 * @date 2018年3月14日
	 */
	List<ShopStore> findAllDirShopStores(@Param("comId")Integer comId);

	/**
	 * 校验店铺名
	 * @author Tonny
	 * @date 2018年3月14日
	 */
	int selectShopStoreByName(@Param("shopStoreName")String shopStoreName);
	
	//List<ShopStore> findStoreByComId(@Param("comId")Integer comId);
	/**
	 * 查询店铺下商品和订单数量
	 * @author Tonny
	 * @date 2018年3月7日
	 */
	//List<Map<String, Object>> selectGoodsAndOrderCountsById(Map<String, Object> map);
	List<Map<String, Object>> selectGoodsAndOrderCountsById(@Param("shopStoreId")int shopStoreId);

	/*List<ShopStore> pageStoresAjax();*/

	void updateStoreById(ShopStore store);
	/**
	 * 停用/启用
	 * @author Tonny
	 * @date 2018年3月14日
	 */
	void isDisabledExamine(@Param("shopStoreId")int shopStoreId, @Param("state")Integer state);

	




}
