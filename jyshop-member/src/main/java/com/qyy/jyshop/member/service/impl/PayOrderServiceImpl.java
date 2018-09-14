package com.qyy.jyshop.member.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.PayOrderDao;
import com.qyy.jyshop.member.service.PayOrderService;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.PayOrder;
import com.qyy.jyshop.supple.AbstratService;
@Service
public class PayOrderServiceImpl extends AbstratService<PayOrder> implements PayOrderService {
	@Autowired
	private PayOrderDao payOrderDao;
	@Override
	public BigDecimal selectPayAmount(String token) {
		Member member = this.getMember(token);
		List<Map<String, Object>> payOrderList = this.payOrderDao.selectPayOrderListByMemberId(member.getMemberId());
		Map<String, Object> map = null;
		BigDecimal payAmount = null;
		if(payOrderList!=null && payOrderList.size()>0){
			map = payOrderList.get(0);
			payAmount =  (BigDecimal) map.get("pay_amount");
		}else{
			payAmount = new BigDecimal(0);
		}
		return payAmount;
	}

}
