package com.qyy.jyshop.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.Product;
import com.qyy.jyshop.supple.MyMapper;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.pojo.PageAjax;

public interface GoodsDao extends MyMapper<Goods>{
    
    
    /**
     * 根据分类Id查询最近的商品号
     * @author hwc
     * @created 2018年1月23日 下午5:33:18
     * @param catId
     * @return
     */
    String selectOfGoodsSn(@Param("comId")Integer comId,@Param("catId")Integer catId);
    
    /**
     * 根据货号查询货品
     * @author hwc
     * @created 2018年1月23日 上午10:50:29
     * @param goodsSn
     * @return
     */
    Goods selectByGoodsSn(@Param("goodsSn")String goodsSn);

	/**
	 * 根据商品ID查询商品
	 * @param goodsId
	 * @return
	 */
	Goods selectByGoodsId(@Param("goodsId")Long goodsId);
    
	/*根据商品id获取总销售量 */
	List<Long> getTotalSales(@Param("goodsId") Long goodsId);
    
	
	/**
	 * 查询首页商品列表
	 */
	PageAjax<Map<String,Object>> selectGoodsList();
	
	/**
	 * 查询热门商品
	 */
	PageAjax<Map<String, Object>> selectGoodListOrderByCounts();
	
	/**
     * 根据商品类别查询商品
     * @param catId
     * @return
     */
	PageAjax<Map<String,Object>> selectGoodsById(@Param("catId")Integer catId);

	/**
	 * 根据店铺id查询商品
	 */
	List<Map<String,Object>> selectGoodListByShopId(Map<String,Object> params);
	
	
	/**
	 * 商品分页
	 */
	List<Map<String,Object>> selectDataTableGoods(Map<String,Object> params);

	/**
	 * 根据商品id查询商品信息和店铺信息
	 * @param redPointRatio 
	 * @param goodId
	 * @return
	 */
	Map<String,Object> findGoodDetailedById(@Param("goodsId")Long goodsId, @Param("redPointRatio")BigDecimal redPointRatio);
	
	/**
	 * 查找商品对应供应商的保证金
	 * @param comId
	 * @return
	 */
	int findBondByComId(@Param("comId")Integer comId);


    /**
     * 根据条件查询商品列表
     * @author hwc
     * @created 2017年12月26日 下午2:55:36
     * @param params
     * @return
     */
    List<Map<String,Object>> selectGoodsByParams(Map<String,Object> params);
    /**
     * 前台首页搜索商品接口
     * @author Tonny
     * @created 2018年3月7日 下午4:48:36
     * @param params
     * @return
     */
    List<Map<String,Object>> searchGoodsByParams(Map<String,Object> params);
    
    /**
     * 根据条件查询商品库存列表
     * @author Tonny
     * @created 2018年3月6日 下午5:26:36
     * @param params
     * @return
     */
    List<Map<String,Object>> selectGoodsStoreByParams(Map<String,Object> params);
    
    /**
     * 查询店长礼包
     * @return
     */
    List<Map<String, Object>> selectShopownerGoods();
    /**
     * 商品排序
     * @param shopStoreId 
     * @param redPointRatio 
     * @param goodsId
     * @param marketEnable
     */
    List<Map<String, Object>> orderGoods(@Param("orderType")String orderType, @Param("sort")Integer sort, @Param("catId")Integer catId, @Param("shopStoreId")Integer shopStoreId,@Param("redPointRatio")BigDecimal redPointRatio);
    
    List<Map<String, Object>> getMemberGoods(@Param("memberId")Long memberId);
    
    Product getProductByGoodsId(@Param("goodsId")Long goodsId);
   
	/**
	 * 查询商品轮播图
	 * @param goodsId
	 * @return
	 */
	List<Map<String,Object>> selectGoodsGalleryByGoodsId(@Param("goodsId")Long goodsId);

	/**
	 * 生成商品二维码
	 * @author Tonny
	 * @date 2018年3月22日
	 */
	void updateGoodsQRCodePathById(@Param("goodsId")Long goodsId, @Param("qRCodePath")String qRCodePath);
	
	void updateOfMarketEnable(@Param("goodsId")Long goodsId,@Param("marketEnable")Integer marketEnable);

	void updateAa(@Param("goodsId") Long goodsId,@Param("goodsSn")String goodsSn);
	//Long insertGoods(Goods goods);

	void doAddGoods(Goods goods);
	
	
}
