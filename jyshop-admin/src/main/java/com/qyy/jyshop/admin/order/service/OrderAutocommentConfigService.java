package com.qyy.jyshop.admin.order.service;

import com.qyy.jyshop.model.OrderAutocommentConfig;
import com.qyy.jyshop.pojo.PageAjax;

public interface OrderAutocommentConfigService {

	PageAjax<OrderAutocommentConfig> pageConfig(PageAjax<OrderAutocommentConfig> page);

	String deleteById(Long id);

	String add(OrderAutocommentConfig config);

	OrderAutocommentConfig queryConfigById(Long id);

	String edit(OrderAutocommentConfig config);

}
