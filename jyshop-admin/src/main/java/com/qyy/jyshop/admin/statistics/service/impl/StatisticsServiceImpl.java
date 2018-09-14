package com.qyy.jyshop.admin.statistics.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.admin.statistics.service.StatisticsService;
import com.qyy.jyshop.dao.AuthUserDao;
import com.qyy.jyshop.dao.CompanyDao;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.dao.OrderDao;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
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
		 Integer comId = currentUser.getComId();
		 /*//查询待发货订单数量
		 map.put("consignmentOrdersCount", orderDao.selectConsignmentOrdersCount(comId));	
		 //查询昨日订单数量
		 map.put("yesterdayOrdersCount", orderDao.selectYesterdayOrdersCount(comId));
		 //查询昨日新增用户数量
		 map.put("newUserYesterdayCount",userDao.selectNewUserYesterday());
		 //查询昨日新增店长数量
		 //map.put("newShopkeeperYesterdayCount", memberDao.selectNewShopkeeperYesterday());
		 //查询昨日交易总额
		 map.put("yesterdayTradingVolumeCount", orderDao.selectYesterdayTradingVolumeCount());
		 //昨日充值总额
		 map.put("yesterdayRechargeCount", "");*/
		 //查询统计数据
		 Map<String, Object> map = new HashMap<String, Object>();
		 map.put("comId", comId);
		return orderDao.selectStatisticsCount(map);
	}

	@Override
	public PageAjax<Map<String, Object>> newUserYesterday(PageAjax<Map<String, Object>> page) {
		ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
		return this.pageQuery("MemberDao.selectNewUserYesterday", pd);
	}

	@Override
	public PageAjax<Map<String, Object>> newShopkeeperYesterday(PageAjax<Map<String, Object>> page) {
		ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
		return this.pageQuery("MemberDao.selectNewShopkeeperYesterday", pd);
	}
}
