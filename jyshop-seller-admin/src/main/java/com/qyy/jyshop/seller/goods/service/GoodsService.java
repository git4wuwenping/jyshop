package com.qyy.jyshop.seller.goods.service;


import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.pojo.DataTablePage;
import com.qyy.jyshop.pojo.PageAjax;

import java.util.Map;

public interface GoodsService {

	/**
	 * 查询库存列表
	 * @author Tonny
	 * @date 2018年3月6日
	 */
	public PageAjax<Map<String, Object>> pageStoreAjax(PageAjax<Map<String, Object>> page);
    
    /**
     * 根据商品Id获取商品信息
     * @author hwc
     * @created 2017年12月27日 下午3:22:47
     * @param goodsId
     * @return
     */
    public Goods queryByGoodsId(Long goodsId);
    
    /**
     * 查询商品列表(分页)
     * @author hwc
     * @created 2017年12月26日 下午2:57:47
     * @param page
     * @return
     */
    public PageAjax<Map<String,Object>> pageGoods(PageAjax<Map<String,Object>> page);

    /**
     * 添加商品
     * @param map
     * @return
     */
    String doAddGoods(Map<String, Object> map);
   
    /**
     * 编辑商品
     * @param map
     * @return
     */
    String editGoods(Map<String, Object> map);
    
    /**
     * 上下架
     * @param map
     * @return
     */
    String doGoodsSalesExamine(Long goodsId,Integer marketEnable);

	public DataTablePage<Goods> pageDataTableGoods(Map<String, Object> map);
}
