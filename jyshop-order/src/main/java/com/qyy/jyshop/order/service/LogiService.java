package com.qyy.jyshop.order.service;

import java.util.Map;

public interface LogiService {

	Map<String, Object> queryLogiDistributionInfo(Long orderId,Integer orderType, String token);

}
