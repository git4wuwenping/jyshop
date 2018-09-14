package com.qyy.jyshop.admin.order.service.impl;

import org.springframework.stereotype.Service;

import com.qyy.jyshop.admin.order.service.OrderAutocommentConfigService;
import com.qyy.jyshop.model.OrderAutocommentConfig;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class OrderAutocommentServiceImpl extends AbstratService<OrderAutocommentConfig> implements OrderAutocommentConfigService{

	@Override
	public PageAjax<OrderAutocommentConfig> pageConfig(PageAjax<OrderAutocommentConfig> page) {
		return this.queryPage(page, null);
	}

	@Override
	public String deleteById(Long id) {
		this.delById(id);
		return null;
	}

	@Override
	public String add(OrderAutocommentConfig config) {
		this.insert(config);
		return null;
	}

	@Override
	public OrderAutocommentConfig queryConfigById(Long id) {
		
		return this.queryByID(id);
	}

	@Override
	public String edit(OrderAutocommentConfig config) {
		this.updateByID(config);
		return null;
	}


}
