package com.qyy.jyshop.seller.statistics.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.OrderDao;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.seller.statistics.service.StatisticsService;
import com.qyy.jyshop.supple.AbstratService;
@Service
public class StatisticsServiceImpl extends AbstratService<Order> implements StatisticsService {
    @Autowired
    private OrderDao orderDao;
	@Override
	public PageAjax<Map<String, Object>> consignmentOrders(PageAjax<Map<String, Object>> page) {
		ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
    	AuthUser currentUser = this.getLoginUser();
    	Integer comId = currentUser.getComId();
    	pd.put("comId", comId);
        return this.pageQuery("OrderDao.selectConsignmentOrders", pd);
	}

	@Override
	public PageAjax<Map<String, Object>> yesterdayOrders(PageAjax<Map<String, Object>> page) {
		ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
		AuthUser currentUser = this.getLoginUser();
		Integer comId = currentUser.getComId();
		pd.put("comId", comId);
		return this.pageQuery("OrderDao.selectYesterdayOrders", pd);
	}

	@Override
	public List<Map<String,Object>> statisticsCount() {
		AuthUser currentUser = this.getLoginUser();
		 Integer comIds = currentUser.getComId();
		 Map<String, Object> map = new HashMap<>();
		 map.put("comIds", comIds);
		return orderDao.selectStatisticsCount(map);
	}

	@Override
	public PageAjax<Map<String, Object>> newUserYesterday(PageAjax<Map<String, Object>> page) {
		ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
		return this.pageQuery("CompanyDao.selectNewUserYesterday", pd);
	}

	@Override
	public PageAjax<Map<String, Object>> newShopkeeperYesterday(PageAjax<Map<String, Object>> page) {
		ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
		return this.pageQuery("MemberDao.selectNewShopkeeperYesterday", pd);
	}
}
