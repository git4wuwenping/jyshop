package com.qyy.jyshop.seller.statistics.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;

public interface StatisticsService {
	 /**
     * 查询代发货订单
     * @param page
     * @return
     */
	public PageAjax<Map<String, Object>> consignmentOrders(PageAjax<Map<String, Object>> page);

	/**
	 * 查询待发货订单数量
	 * @return
	 */
	//public Long consignmentOrdersCount();
	/**
	 * 查询昨日订单数量
	 * @return
	 */
	//public Long yesterdayOrdersCount();

	/**
	 * 查询昨日订单
	 * @param page
	 * @return
	 */
	public PageAjax<Map<String, Object>> yesterdayOrders(PageAjax<Map<String, Object>> page);

	public List<Map<String,Object>> statisticsCount();
	/**
	 * 查询昨日新增用户
	 * @param page
	 * @return
	 */
	public PageAjax<Map<String, Object>> newUserYesterday(PageAjax<Map<String, Object>> page);
	/**
	 * 查询昨日新增店长
	 * @param page
	 * @return
	 */
	public PageAjax<Map<String, Object>> newShopkeeperYesterday(PageAjax<Map<String, Object>> page);
}
