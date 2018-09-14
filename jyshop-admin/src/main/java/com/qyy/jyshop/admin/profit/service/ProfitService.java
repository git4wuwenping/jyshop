package com.qyy.jyshop.admin.profit.service;

import java.util.Map;

import com.qyy.jyshop.model.ProfitParam;

public interface ProfitService {

    /**
     * 订单首次分润
     * @author hwc
     * @created 2018年1月15日 下午3:46:11
     * @param orderId
     */
    public void doOrderFirstProfit(Map<String,Object> orderMap,ProfitParam profitParam,Integer orderType);

    /**
     * 订单分润
     * @author hwc
     * @created 2018年1月16日 下午4:00:43
     * @param orderMap
     */
    public void doOrderProfit(Map<String,Object> orderMap,ProfitParam profitParam,Integer orderType);

    /**
     * 店家下级分润
     * @param orderMap
     * @param profitParam
     */
	public void doShopownerSubProfitOrder(Map<String, Object> orderMap, ProfitParam profitParam,Integer orderType);
}
