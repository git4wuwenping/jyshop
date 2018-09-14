package com.qyy.jyshop.admin.order.service;

import com.qyy.jyshop.model.OrderParamConfig;

public interface OrderParamConfigService {

	OrderParamConfig queryOrderParamConfig();

	String edit(OrderParamConfig config);

}
