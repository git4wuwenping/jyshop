package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.Product;
import com.qyy.jyshop.supple.MyMapper;

import org.apache.ibatis.annotations.Param;


public interface ProductDao extends MyMapper<Product>{

    /**
     * 获取某个货品信息
     * @author hwc
     * @created 2017年11月22日 下午2:24:09
     * @param productId 货品Id
     * @param marketEnable 上架状态
     * @return 货品信息
     */
    Map<String,Object> selectByProductId(@Param("productId")Long productId,@Param("marketEnable")Integer marketEnable);

    /**
     * 根据商品Id获取货品ID列表
     * @author hwc
     * @created 2018年1月4日 下午6:50:00
     * @param goodsId
     * @return
     */
    List<Long> selectOfProductId(@Param("goodsId")Long goodsId);
    
    /**
     * 根据商品Id获取货品
     * @author hwc
     * @created 2017年12月26日 下午7:10:38
     * @param goodsId
     * @return
     */
    List<Map<String,Object>> selectByGoodsId(@Param("goodsId")Long goodsId);
    
    /**
     * 减少货品库存
     * @author hwc
     * @created 2018年4月3日 上午10:44:47
     * @param productId
     * @param store
     */
    void updateStoreOfCut(@Param("productId")Long productId,@Param("store")Integer store);
    
    /**
     * 根据商品Id删除货品 
     * @author hwc
     * @created 2018年1月4日 下午6:57:49
     * @param goodsId
     */
    void deleteByGoodsId(@Param("goodsId")Long goodsId);
    
    /**
     * 根据货品IDS获取货品
     * @author hwc
     * @created 2018年1月4日 下午6:57:45
     * @param productIdList
     */
    void deleteByProductIds(List<Long> productIdList);
}
