package com.qyy.jyshop.dao;


import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.BargainGoods;
import com.qyy.jyshop.pojo.PageAjax;

import tk.mybatis.mapper.common.Mapper;

public interface BargainGoodsDao extends Mapper<BargainGoods>{
	/**
	 * 查询砍价商品列表
	 * @author Tonny
	 * @date 2018年4月11日
	 */
	PageAjax<Map<String,Object>> selectBargainGoodsList();

    /**
     * 根据砍价活动ID获取可用的砍价活动
     * @author hwc
     * @created 2018年4月12日 上午11:27:24
     * @param bargainId
     * @return
     */
    public BargainGoods  selectByBargainId(@Param("bargainId")Long bargainId);

	/**
	 * 砍价商品详情
	 * @author Tonny
	 * @param bargainId 
	 * @date 2018年4月12日
	 */
	Map<String, Object> selectBargainGoodsDetails(@Param("goodsId")Long goodsId, @Param("bargainId")Long bargainId);

}
