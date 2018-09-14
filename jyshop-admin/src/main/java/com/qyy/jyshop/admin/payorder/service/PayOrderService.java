package com.qyy.jyshop.admin.payorder.service;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;

public interface PayOrderService {

	PageAjax<Map<String, Object>> pageAjaxPayOrder(PageAjax<Map<String, Object>> page, String nickname,
			Integer payType);

}
