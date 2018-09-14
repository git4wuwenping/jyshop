package com.qyy.jyshop.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.ProductStore;
import com.qyy.jyshop.supple.MyMapper;

public interface ProductStoreDao extends MyMapper<ProductStore>{
	
	/**
	 * 查找货品库存id和可用库存
	 * @author Tonny
	 * @date 2018年3月24日
	 */
	List<ProductStore> selectProductStoreAndStoreId(@Param("goodsId")Long goodsId);
	
	/**
	 * 根据goodsId查询商品库存回显编辑
	 * @author Tonny
	 * @date 2018年3月22日
	 */
	List<Map<String, Object>> selectProductStoreByGoodsId(@Param("goodsId")Long goodsId);
    
    /**
     * 查询大于库存的货品根据供应商排序
     * @author hwc
     * @created 2017年12月16日 上午9:57:21
     * @param productId
     * @param usableStore
     * @param comIdOrder
     * @return
     */
    public List<ProductStore> selectByUsableStore(@Param("productId")Long productId,
            @Param("usableStore")Integer usableStore,
            @Param("comIdOrder")String comIdOrder);

    /**
     * 修改货品库存
     * @author Tonny
     * @param timestamp 
     * @date 2018年3月24日
     */
	void updateProductStoreByStoreId(@Param("storeId")Long storeId, 
									@Param("userableStore")int userableStore, 
									@Param("creationTime")Timestamp creationTime);
	/**
	 * 减少货品库存
	 * @author hwc
	 * @created 2018年4月3日 上午10:45:29
	 * @param productId
	 * @param store
	 */
	void updateStoreOfCut(@Param("productId")Long productId,@Param("store")Integer store);
	
	 /**
     * 根据货品IDS货品库存
     * @author hwc
     * @created 2018年1月25日 下午5:13:28
     * @param productIdList
     */
    void deleteByProductIds(List<Long> productIdList);

}
