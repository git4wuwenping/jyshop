package com.qyy.jyshop.admin.payorder.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.qyy.jyshop.admin.payorder.service.PayOrderService;
import com.qyy.jyshop.model.PayOrder;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class PayOrderServiceImpl extends AbstratService<PayOrder> implements PayOrderService {

	@Override
	public PageAjax<Map<String, Object>> pageAjaxPayOrder(PageAjax<Map<String, Object>> page, String nickname,
			Integer payType) {
		ParamData params = this.getParamData(page.getPageNo(), page.getPageSize());
		params.put("nickname", nickname);
		params.put("payType", payType);
		return this.pageQuery("PayOrderDao.selectPayOrderList", params);
	}

}
