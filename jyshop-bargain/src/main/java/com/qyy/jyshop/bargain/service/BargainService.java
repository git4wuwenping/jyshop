package com.qyy.jyshop.bargain.service;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;

public interface BargainService {
	/**
	 * 分页查询砍价商品列表
	 * @author Tonny
	 * @date 2018年4月11日
	 */
	PageAjax<Map<String, Object>> selectBargainGoodsList(Integer pageNo, Integer pageSize);
	/**
	 * 查询砍价商品列表轮播消息
	 * @author Tonny
	 * @date 2018年4月17日
	 */
	PageAjax<Map<String, Object>> selectBargainCarouselMessage(Integer pageNo, Integer pageSize);

}
